package control;

import adt.LinkedList;
import adt.ListInterface;
import adt.ArrayQueue;
import adt.QueueInterface;
import adt.ArrayStack;
import adt.StackInterface;
import entity.Room;
import entity.CleaningTask;
import entity.HouseKeepingRecord;
import entity.Guest;
import dao.HouseKeepingData;

public class HouseKeepingController {
    private static final HouseKeepingController INSTANCE = new HouseKeepingController();

    private ListInterface<Room> roomList;
    private QueueInterface<CleaningTask> cleaningQueue;
    private StackInterface<HouseKeepingRecord> historyStack;
    private StackInterface<HouseKeepingRecord> redoStack;
    private final HouseKeepingData housekeepingDao;

    private HouseKeepingController() {
        this.roomList = new LinkedList<>();
        this.cleaningQueue = new ArrayQueue<>();
        this.historyStack = new ArrayStack<>();
        this.redoStack = new ArrayStack<>();
        this.housekeepingDao = new HouseKeepingData();

        loadInitialData();
    }

    public static HouseKeepingController getInstance() {
        return INSTANCE;
    }

    private void loadInitialData() {
        Room[] initialRooms = housekeepingDao.initRoomData();
        if (initialRooms != null) {
            for (Room r : initialRooms) {
                if (r != null) {
                    this.roomList.add(r);
                }
            }
        }

        CleaningTask[] initialTasks = housekeepingDao.initCleaningTaskData();
        if (initialTasks != null) {
            for (CleaningTask t : initialTasks) {
                if (t != null) {
                    cleaningQueue.enqueue(t);
                }
            }
        }

        HouseKeepingRecord[] initialRecords = housekeepingDao.initHousekeepingRecordData();
        if (initialRecords != null) {
            for (HouseKeepingRecord r : initialRecords) {
                if (r != null) {
                    historyStack.push(r);
                }
            }
        }
    }

    // --- CREATE ---
    public String addRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return "Error: Room ID cannot be empty.";
        }
        String cleanId = roomId.trim();
        if (findRoom(cleanId) != null) {
            return "Error: Room ID '" + cleanId + "' already exists in the system.";
        }
        Room newRoom = new Room(cleanId);
        roomList.add(newRoom);
        return "SUCCESS: Room " + cleanId + " added successfully.";
    }

    // --- READ ---
    public ListInterface<Room> getRoomList() {
        return roomList;
    }

    public Room[] getAllRooms() {
        Room[] rooms = new Room[roomList.getNumberOfEntries()];
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            rooms[i - 1] = roomList.get(i);
        }
        return rooms;
    }

    public Room[] getRoomsByStatus(String status) {
        ListInterface<Room> filtered = filterRoomsByStatus(status);
        Room[] rooms = new Room[filtered.getNumberOfEntries()];
        for (int i = 1; i <= filtered.getNumberOfEntries(); i++) {
            rooms[i - 1] = filtered.get(i);
        }
        return rooms;
    }

    public CleaningTask[] getCleaningTasks() {
        ArrayQueue<CleaningTask> tempQueue = new ArrayQueue<>();
        CleaningTask[] tasks = new CleaningTask[cleaningQueue.getNumberOfEntries()];
        int index = 0;

        while (!cleaningQueue.isEmpty()) {
            CleaningTask task = cleaningQueue.dequeue();
            tasks[index++] = task;
            tempQueue.enqueue(task);
        }
        while (!tempQueue.isEmpty()) {
            cleaningQueue.enqueue(tempQueue.dequeue());
        }
        return tasks;
    }

    // --- UPDATE ---
    public String updateRoomStatus(String roomId, String newStatus, String staffName, String remarks) {
        Room room = findRoom(roomId);
        if (room == null) return "Error: Room ID '" + roomId + "' not found.";
        if (!isValidStatus(newStatus)) return "Error: Invalid room status specified.";

        HouseKeepingRecord log = new HouseKeepingRecord(
                room.getRoomId(), room.getStatus(), newStatus,
                room.getAssignedStaff(), staffName,
                room.getRemarks(), remarks
        );
        historyStack.push(log);
        redoStack.clear();

        room.setStatus(newStatus);
        room.setAssignedStaff(staffName);
        if (remarks != null && !remarks.trim().isEmpty()) {
            room.setRemarks(remarks);
        }

        // ====== SYNC (QUEUE) ======
        if (Room.STATUS_DIRTY.equalsIgnoreCase(newStatus)) {
            if (!isRoomInCleaningQueue(room.getRoomId())) {
                CleaningTask task = new CleaningTask(room.getRoomId(), "Normal");
                cleaningQueue.enqueue(task);
            } else {
                updateTaskInQueue(room.getRoomId(), "Pending", "Unassigned");
            }
        } else if (Room.STATUS_IN_PROGRESS.equalsIgnoreCase(newStatus)) {
            if (!isRoomInCleaningQueue(room.getRoomId())) {
                CleaningTask task = new CleaningTask(room.getRoomId(), "Normal");
                task.setTaskStatus("In Progress");
                task.setAssignedStaff(staffName);
                cleaningQueue.enqueue(task);
            } else {
                updateTaskInQueue(room.getRoomId(), "In Progress", staffName);
            }
        } else {
            removeCleaningTaskByRoomId(room.getRoomId());
        }

        if (Room.STATUS_CLEAN.equalsIgnoreCase(newStatus)) {
            FrontDeskController.getInstance().notifyRoomCleaned(room.getRoomId());
        }

        return "SUCCESS: Room " + room.getRoomId() + " status updated to '" + newStatus + "'.";
    }

    // --- Cleaning Task Management ---
    public String enqueueCleaningTask(String roomId, String priority) {
        Room room = findRoom(roomId);
        if (room == null) return "Error: Room ID '" + roomId + "' not found.";
        if (isRoomInCleaningQueue(roomId)) return "Warning: Room " + room.getRoomId() + " is already in the cleaning queue.";

        CleaningTask task = new CleaningTask(room.getRoomId(), priority);
        cleaningQueue.enqueue(task);

        if (!Room.STATUS_DIRTY.equalsIgnoreCase(room.getStatus()) && !Room.STATUS_IN_PROGRESS.equalsIgnoreCase(room.getStatus())) {
            updateRoomStatus(room.getRoomId(), Room.STATUS_DIRTY, "Unassigned", "Added to cleaning queue manually.");
        }
        return "SUCCESS: Cleaning Task " + task.getTaskId() + " created for Room " + room.getRoomId() + " [" + priority + "].";
    }

    public CleaningTask getNextCleaningTask() {
        return cleaningQueue.getFront();
    }

    // Modified
    public String dispatchNextCleaningTask(String staffName) {
        if (cleaningQueue.isEmpty()) return "Notice: No pending cleaning tasks in the queue.";
        if (staffName == null || staffName.trim().isEmpty()) return "Error: Staff name is required for task assignment.";

        CleaningTask dispatchedTask = null;
        ArrayQueue<CleaningTask> tempQueue = new ArrayQueue<>();

        while (!cleaningQueue.isEmpty()) {
            CleaningTask t = cleaningQueue.dequeue();
            if (dispatchedTask == null && "Pending".equalsIgnoreCase(t.getTaskStatus())) {
                dispatchedTask = t;
                t.setAssignedStaff(staffName.trim());
                t.setTaskStatus("In Progress");
            }
            tempQueue.enqueue(t);
        }

        while (!tempQueue.isEmpty()) {
            cleaningQueue.enqueue(tempQueue.dequeue());
        }

        if (dispatchedTask == null) {
            return "Notice: No 'Pending' tasks available to dispatch. All tasks are currently in progress.";
        }

        Room room = findRoom(dispatchedTask.getRoomId());
        if (room != null) {
            updateRoomStatus(room.getRoomId(), Room.STATUS_IN_PROGRESS, staffName.trim(), "Assigned from Task Queue (" + dispatchedTask.getTaskId() + ")");
        }

        return "SUCCESS: Task " + dispatchedTask.getTaskId() + " for Room " + dispatchedTask.getRoomId() + " dispatched to " + staffName + ".";
    }

    public QueueInterface<CleaningTask> getCleaningQueue() {
        return cleaningQueue;
    }

    // --- Undo Redo ---
    public String undoLastAction() {
        if (historyStack.isEmpty()) return "Error: No actions available to undo.";
        HouseKeepingRecord lastLog = historyStack.pop();
        if (lastLog == null) return "Error: No actions available to undo.";

        Room room = findRoom(lastLog.getRoomId());
        if (room != null) {
            HouseKeepingRecord redoRecord = new HouseKeepingRecord(
                    room.getRoomId(), room.getStatus(), lastLog.getPreviousStatus(),
                    room.getAssignedStaff(), lastLog.getPreviousStaff(),
                    room.getRemarks(), lastLog.getPreviousRemarks()
            );
            redoStack.push(redoRecord);

            updateRoomStatus(room.getRoomId(), lastLog.getPreviousStatus(), lastLog.getPreviousStaff(), lastLog.getPreviousRemarks());
            historyStack.pop(); 

            return "SUCCESS (Undo): Room " + room.getRoomId() + " reverted back to status '"
                    + lastLog.getPreviousStatus() + "' (Staff: " + lastLog.getPreviousStaff() + ").";
        }
        return "Error: Target room for undo no longer exists.";
    }

    public String redoLastAction() {
        if (redoStack.isEmpty()) return "Error: No actions available to redo.";
        HouseKeepingRecord redoLog = redoStack.pop();
        if (redoLog == null) return "Error: No actions available to redo.";

        Room room = findRoom(redoLog.getRoomId());
        if (room != null) {
            HouseKeepingRecord undoLog = new HouseKeepingRecord(
                    room.getRoomId(), room.getStatus(), redoLog.getNewStatus(),
                    room.getAssignedStaff(), redoLog.getNewStaff(),
                    room.getRemarks(), redoLog.getNewRemarks()
            );
            historyStack.push(undoLog);

            updateRoomStatus(room.getRoomId(), redoLog.getNewStatus(), redoLog.getNewStaff(), redoLog.getNewRemarks());
            historyStack.pop();

            return "SUCCESS (Redo): Room " + room.getRoomId() + " re-applied status '"
                    + redoLog.getNewStatus() + "' (Staff: " + redoLog.getNewStaff() + ").";
        }
        return "Error: Target room for redo no longer exists.";
    }

    public StackInterface<HouseKeepingRecord> getHistoryStack() {
        return historyStack;
    }

    public ListInterface<Room> filterRoomsByStatus(String status) {
        ListInterface<Room> filtered = new LinkedList<>();
        if (status == null) return filtered;

        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room r = roomList.get(i);
            if (r != null && r.getStatus().equalsIgnoreCase(status.trim())) {
                filtered.add(r);
            }
        }
        return filtered;
    }

    public String deleteRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) return "Error: Room ID cannot be empty.";
        String targetId = roomId.trim();
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room room = roomList.get(i);
            if (room != null && room.getRoomId().equalsIgnoreCase(targetId)) {
                removeCleaningTaskByRoomId(targetId);
                roomList.remove(i);
                return "SUCCESS: Room " + room.getRoomId() + " removed from the system.";
            }
        }
        return "Error: Room ID '" + targetId + "' not found.";
    }

    public String generateSummaryReport() {
        int total = roomList.getNumberOfEntries();
        if (total == 0) return "No rooms available for report generation.";

        int cleanCount = 0, dirtyCount = 0, inProgressCount = 0, maintenanceCount = 0, occupiedCount = 0;

        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room r = roomList.get(i);
            if (r != null) {
                if (Room.STATUS_CLEAN.equalsIgnoreCase(r.getStatus())) cleanCount++;
                else if (Room.STATUS_DIRTY.equalsIgnoreCase(r.getStatus())) dirtyCount++;
                else if (Room.STATUS_IN_PROGRESS.equalsIgnoreCase(r.getStatus())) inProgressCount++;
                else if (Room.STATUS_MAINTENANCE.equalsIgnoreCase(r.getStatus())) maintenanceCount++;
                else if (Room.STATUS_OCCUPIED.equalsIgnoreCase(r.getStatus())) occupiedCount++;
            }
        }

        double cleanPercentage = ((double) cleanCount / total) * 100.0;
        int pendingTasksCount = cleaningQueue.getNumberOfEntries();

        StringBuilder report = new StringBuilder();
        report.append("============== HOUSEKEEPING SUMMARY REPORT ==============\n");
        report.append(String.format(" Total Registered Rooms  : %d\n", total));
        report.append(String.format(" Clean Rooms (Ready)     : %d (%.1f%%)\n", cleanCount, cleanPercentage));
        report.append(String.format(" Dirty Rooms             : %d\n", dirtyCount));
        report.append(String.format(" In Progress (Cleaning)  : %d\n", inProgressCount));
        report.append(String.format(" Occupied Rooms          : %d\n", occupiedCount));
        report.append(String.format(" Under Maintenance       : %d\n", maintenanceCount));
        report.append(" --------------------------------------------------------\n");
        report.append(String.format(" Pending Cleaning Tasks  : %d task(s) in Queue\n", pendingTasksCount));
        report.append(String.format(" Undo History Size       : %d action(s) recorded\n", historyStack.getNumberOfEntries()));
        report.append("=========================================================");

        return report.toString();
    }

    public String assignCleanRoom() {
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room r = roomList.get(i);
            if (r != null && Room.STATUS_CLEAN.equalsIgnoreCase(r.getStatus())) {
                return r.getRoomId();
            }
        }
        return null;
    }

    public String markRoomOccupied(String roomId, String guestName, String remarks) {
        return updateRoomStatus(roomId, Room.STATUS_OCCUPIED, guestName, remarks);
    }

    public String notifyCheckOut(String roomId, String staffName, String remarks) {
        return updateRoomStatus(roomId, Room.STATUS_DIRTY,
                (staffName != null ? staffName : "Unassigned"), remarks);
    }

    public String syncFromFrontDesk() {
        Guest[] guestList = FrontDeskController.getInstance().getCheckedOutGuests();
        if (guestList == null || guestList.length == 0) return "Sync failed: Front Desk guest list is empty or unavailable.";

        int synced = 0;
        for (Guest g : guestList) {
            if (g == null) continue;

            if ("Checked-Out".equalsIgnoreCase(g.getStatus()) && g.getRoomId() != null) {
                Room room = findRoom(g.getRoomId());
                if (room != null && !Room.STATUS_DIRTY.equalsIgnoreCase(room.getStatus())
                        && !Room.STATUS_IN_PROGRESS.equalsIgnoreCase(room.getStatus())) {
                    notifyCheckOut(g.getRoomId(), "Unassigned", "Auto-sync: Guest " + g.getFullName() + " checked out");
                    synced++;
                }
            }
        }
        return synced == 0 ? "Sync complete. No new dirty rooms detected from Front Desk." : "Sync complete. " + synced + " room(s) marked Dirty from Front Desk check-outs.";
    }

    public Room findRoom(String roomId) {
        if (roomId == null) return null;
        String search = roomId.trim();
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room room = roomList.get(i);
            if (room != null && room.getRoomId() != null && room.getRoomId().equalsIgnoreCase(search)) {
                return room;
            }
        }
        return null;
    }

    private boolean isRoomInCleaningQueue(String roomId) {
        if (cleaningQueue.isEmpty() || roomId == null) return false;

        ArrayQueue<CleaningTask> tempQueue = new ArrayQueue<>();
        boolean found = false;
        String searchId = roomId.trim();

        while (!cleaningQueue.isEmpty()) {
            CleaningTask t = cleaningQueue.dequeue();
            if (t != null) {
                if (t.getRoomId() != null && t.getRoomId().equalsIgnoreCase(searchId)) {
                    found = true;
                }
                tempQueue.enqueue(t);
            }
        }
        while (!tempQueue.isEmpty()) {
            cleaningQueue.enqueue(tempQueue.dequeue());
        }
        return found;
    }

    private void updateTaskInQueue(String roomId, String taskStatus, String staffName) {
        if (roomId == null || cleaningQueue.isEmpty()) return;

        ArrayQueue<CleaningTask> tempQueue = new ArrayQueue<>();
        String searchId = roomId.trim();

        while (!cleaningQueue.isEmpty()) {
            CleaningTask task = cleaningQueue.dequeue();
            if (task != null && task.getRoomId() != null && task.getRoomId().equalsIgnoreCase(searchId)) {
                task.setTaskStatus(taskStatus);
                task.setAssignedStaff(staffName != null && !staffName.isEmpty() ? staffName : "Unassigned");
            }
            tempQueue.enqueue(task);
        }
        while (!tempQueue.isEmpty()) {
            cleaningQueue.enqueue(tempQueue.dequeue());
        }
    }

    private void removeCleaningTaskByRoomId(String roomId) {
        if (roomId == null || cleaningQueue.isEmpty()) return;

        ArrayQueue<CleaningTask> tempQueue = new ArrayQueue<>();
        String searchId = roomId.trim();

        while (!cleaningQueue.isEmpty()) {
            CleaningTask task = cleaningQueue.dequeue();
            if (task != null && task.getRoomId() != null && !task.getRoomId().equalsIgnoreCase(searchId)) {
                tempQueue.enqueue(task);
            }
        }
        while (!tempQueue.isEmpty()) {
            cleaningQueue.enqueue(tempQueue.dequeue());
        }
    }

    private boolean isValidStatus(String status) {
        if (status == null) return false;
        return Room.STATUS_CLEAN.equalsIgnoreCase(status) ||
                Room.STATUS_DIRTY.equalsIgnoreCase(status) ||
                Room.STATUS_IN_PROGRESS.equalsIgnoreCase(status) ||
                Room.STATUS_MAINTENANCE.equalsIgnoreCase(status) ||
                Room.STATUS_OCCUPIED.equalsIgnoreCase(status);
    }
}