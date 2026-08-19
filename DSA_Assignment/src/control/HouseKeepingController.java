package control;

import adt.LinkedList;
import adt.ListInterface;
import adt.ArrayQueue;
import adt.QueueInterface;
import adt.ArrayStack;
import adt.StackInterface;
import entity.Room;
import entity.CleaningTask;
import entity.HousekeepingRecord;
import entity.Guest;
import dao.HouseKeepingData;
import control.FrontDeskController; 

public class HouseKeepingController {

    // List ADT (LinkedList) storing room master list
    private ListInterface<Room> roomList;

    // Queue ADT (ArrayQueue) for cleaning task dispatching (FIFO)
    private QueueInterface<CleaningTask> cleaningQueue;

    // Stack ADT (ArrayStack) for Undo / Rollback
    private StackInterface<HousekeepingRecord> historyStack;

    // Stack ADT (ArrayStack) for Redo
    private StackInterface<HousekeepingRecord> redoStack;

    private HouseKeepingData housekeepingDao;

    // 跨模块控制，控制器内部实例化 FrontDeskController
    private FrontDeskController frontDeskController;

    public HouseKeepingController() {
        this.roomList = new LinkedList<>();
        this.cleaningQueue = new ArrayQueue<>();
        this.historyStack = new ArrayStack<>();
        this.redoStack = new ArrayStack<>();
        this.housekeepingDao = new HouseKeepingData();

        // 实例化依赖的 Front Desk Controller
        this.frontDeskController = new FrontDeskController();

        loadInitialData();
    }

    private void loadInitialData() {
        // 1. 从 DAO 初始化房间列表
        this.roomList = housekeepingDao.initHousekeepingRecordData();

        // 预设房间状态与清洁任务
        updateRoomStatusWithoutLogging("101", Room.STATUS_CLEAN, "Alice", "Regular cleaning completed");
        updateRoomStatusWithoutLogging("102", Room.STATUS_DIRTY, "Unassigned", "Guest checked out");
        updateRoomStatusWithoutLogging("201", Room.STATUS_IN_PROGRESS, "Bob", "Deep cleaning in progress");
        updateRoomStatusWithoutLogging("202", Room.STATUS_DIRTY, "Unassigned", "VIP arriving soon");
        updateRoomStatusWithoutLogging("301", Room.STATUS_MAINTENANCE, "Charlie", "AC Repair required");

        // 入队脏房任务
        enqueueCleaningTask("102", "Normal");
        enqueueCleaningTask("202", "High (VIP)");
    }

    // --- CREATE ---
    public String addRoom(String roomId, String roomType) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return "Error: Room ID cannot be empty.";
        }

        String cleanId = roomId.trim();
        if (findRoom(cleanId) != null) {
            return "Error: Room ID '" + cleanId + "' already exists in the system.";
        }

        if (roomType == null || roomType.trim().isEmpty()) {
            roomType = "Single"; // 默认设置为 Single 对应 Front Desk 房型
        }

        Room newRoom = new Room(cleanId, roomType.trim());
        roomList.add(newRoom);
        return "SUCCESS: Room " + cleanId + " (" + roomType + ") added successfully.";
    }

    // --- READ ALL ---
    public ListInterface<Room> getRoomList() {
        return roomList;
    }

    // --- UPDATE STATUS ---
    public String updateRoomStatus(String roomId, String newStatus, String staffName, String remarks) {
        Room room = findRoom(roomId);
        if (room == null) {
            return "Error: Room ID '" + roomId + "' not found.";
        }

        if (!isValidStatus(newStatus)) {
            return "Error: Invalid room status specified.";
        }

        // Push current state onto Undo historyStack before modification
        HousekeepingRecord log = new HousekeepingRecord(
                room.getRoomId(),
                room.getStatus(),
                newStatus,
                room.getAssignedStaff(),
                staffName,
                room.getRemarks(),
                remarks
        );
        historyStack.push(log);
        redoStack.clear(); // Clear Redo stack whenever a new change is made

        // Apply state change
        room.setStatus(newStatus);
        room.setAssignedStaff(staffName);
        if (remarks != null && !remarks.trim().isEmpty()) {
            room.setRemarks(remarks);
        }

        // Auto-enqueue to Cleaning Queue if marked as Dirty
        if (Room.STATUS_DIRTY.equalsIgnoreCase(newStatus)) {
            if (!isRoomInCleaningQueue(room.getRoomId())) {
                CleaningTask task = new CleaningTask(room.getRoomId(), room.getRoomType(), "Normal");
                cleaningQueue.enqueue(task);
            }
        }

        // 如果房间被打扫干净 (STATUS_CLEAN)，主动跨模块通知 FrontDesk 更新状态
        if (Room.STATUS_CLEAN.equalsIgnoreCase(newStatus)) {
            if (frontDeskController != null) {
                frontDeskController.notifyRoomCleaned(room.getRoomId());
            }
        }

        return "SUCCESS: Room " + room.getRoomId() + " status updated to '" + newStatus + "'.";
    }

    private void updateRoomStatusWithoutLogging(String roomId, String status, String staff, String remarks) {
        Room room = findRoom(roomId);
        if (room != null) {
            room.setStatus(status);
            room.setAssignedStaff(staff);
            room.setRemarks(remarks);
        }
    }

    // --- QUEUE ADT: Cleaning Task Management ---
    public String enqueueCleaningTask(String roomId, String priority) {
        Room room = findRoom(roomId);
        if (room == null) {
            return "Error: Room ID '" + roomId + "' not found.";
        }

        if (isRoomInCleaningQueue(roomId)) {
            return "Warning: Room " + room.getRoomId() + " is already in the cleaning queue.";
        }

        CleaningTask task = new CleaningTask(room.getRoomId(), room.getRoomType(), priority);
        cleaningQueue.enqueue(task);

        if (!Room.STATUS_DIRTY.equalsIgnoreCase(room.getStatus()) && !Room.STATUS_IN_PROGRESS.equalsIgnoreCase(room.getStatus())) {
            room.setStatus(Room.STATUS_DIRTY);
        }

        return "SUCCESS: Cleaning Task " + task.getTaskId() + " created for Room " + room.getRoomId() + " [" + priority + "].";
    }

    public CleaningTask getNextCleaningTask() {
        return cleaningQueue.getFront();
    }

    public String dispatchNextCleaningTask(String staffName) {
        if (cleaningQueue.isEmpty()) {
            return "Notice: No pending cleaning tasks in the queue.";
        }

        if (staffName == null || staffName.trim().isEmpty()) {
            return "Error: Staff name is required for task assignment.";
        }

        CleaningTask task = cleaningQueue.dequeue();
        if (task == null) {
            return "Notice: No pending cleaning tasks in the queue.";
        }

        task.setAssignedStaff(staffName.trim());
        task.setTaskStatus("In Progress");

        Room room = findRoom(task.getRoomId());
        if (room != null) {
            updateRoomStatus(room.getRoomId(), Room.STATUS_IN_PROGRESS, staffName.trim(), "Assigned from Task Queue (" + task.getTaskId() + ")");
        }

        return "SUCCESS: Task " + task.getTaskId() + " for Room " + task.getRoomId() + " dispatched to " + staffName + ".";
    }

    public QueueInterface<CleaningTask> getCleaningQueue() {
        return cleaningQueue;
    }

    // --- STACK ADT: Undo & Redo ---
    public String undoLastAction() {
        if (historyStack.isEmpty()) {
            return "Error: No actions available to undo.";
        }

        HousekeepingRecord lastLog = historyStack.pop();
        if (lastLog == null) {
            return "Error: No actions available to undo.";
        }

        Room room = findRoom(lastLog.getRoomId());
        if (room != null) {
            // Save current state to Redo stack before restoring old state
            HousekeepingRecord redoRecord = new HousekeepingRecord(
                    room.getRoomId(),
                    room.getStatus(),
                    lastLog.getPreviousStatus(),
                    room.getAssignedStaff(),
                    lastLog.getPreviousStaff(),
                    room.getRemarks(),
                    lastLog.getPreviousRemarks()
            );
            redoStack.push(redoRecord);

            // Revert room properties
            room.setStatus(lastLog.getPreviousStatus());
            room.setAssignedStaff(lastLog.getPreviousStaff());
            room.setRemarks(lastLog.getPreviousRemarks());

            return "SUCCESS (Undo): Room " + room.getRoomId() + " reverted back to status '"
                    + lastLog.getPreviousStatus() + "' (Staff: " + lastLog.getPreviousStaff() + ").";
        }

        return "Error: Target room for undo no longer exists.";
    }

    public String redoLastAction() {
        if (redoStack.isEmpty()) {
            return "Error: No actions available to redo.";
        }

        HousekeepingRecord redoLog = redoStack.pop();
        if (redoLog == null) {
            return "Error: No actions available to redo.";
        }

        Room room = findRoom(redoLog.getRoomId());
        if (room != null) {
            // Push current state back to Undo historyStack
            HousekeepingRecord undoLog = new HousekeepingRecord(
                    room.getRoomId(),
                    room.getStatus(),
                    redoLog.getNewStatus(),
                    room.getAssignedStaff(),
                    redoLog.getNewStaff(),
                    room.getRemarks(),
                    redoLog.getNewRemarks()
            );
            historyStack.push(undoLog);

            // Reapply redo state
            room.setStatus(redoLog.getNewStatus());
            room.setAssignedStaff(redoLog.getNewStaff());
            room.setRemarks(redoLog.getNewRemarks());

            return "SUCCESS (Redo): Room " + room.getRoomId() + " re-applied status '"
                    + redoLog.getNewStatus() + "' (Staff: " + redoLog.getNewStaff() + ").";
        }

        return "Error: Target room for redo no longer exists.";
    }

    public StackInterface<HousekeepingRecord> getHistoryStack() {
        return historyStack;
    }

    // --- SEARCH / FILTERING ---
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

    // --- DELETE ---
    public String deleteRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return "Error: Room ID cannot be empty.";
        }

        String targetId = roomId.trim();
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room room = roomList.get(i);
            if (room != null && room.getRoomId().equalsIgnoreCase(targetId)) {
                roomList.remove(i);
                return "SUCCESS: Room " + room.getRoomId() + " removed from the system.";
            }
        }
        return "Error: Room ID '" + targetId + "' not found.";
    }

    // --- ANALYTICS / SUMMARY REPORT ---
    public String generateSummaryReport() {
        int total = roomList.getNumberOfEntries();
        if (total == 0) {
            return "No rooms available for report generation.";
        }

        int cleanCount = 0;
        int dirtyCount = 0;
        int inProgressCount = 0;
        int maintenanceCount = 0;

        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room r = roomList.get(i);
            if (r != null) {
                if (Room.STATUS_CLEAN.equalsIgnoreCase(r.getStatus())) cleanCount++;
                else if (Room.STATUS_DIRTY.equalsIgnoreCase(r.getStatus())) dirtyCount++;
                else if (Room.STATUS_IN_PROGRESS.equalsIgnoreCase(r.getStatus())) inProgressCount++;
                else if (Room.STATUS_MAINTENANCE.equalsIgnoreCase(r.getStatus())) maintenanceCount++;
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
        report.append(String.format(" Under Maintenance        : %d\n", maintenanceCount));
        report.append(" --------------------------------------------------------\n");
        report.append(String.format(" Pending Cleaning Tasks  : %d task(s) in Queue\n", pendingTasksCount));
        report.append(String.format(" Undo History Size       : %d action(s) recorded\n", historyStack.getNumberOfEntries()));
        report.append("=========================================================");

        return report.toString();
    }

    // Assign clean room
    public String assignCleanRoom(String roomType) {
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room r = roomList.get(i);
            if (r != null
                    && Room.STATUS_CLEAN.equalsIgnoreCase(r.getStatus())
                    && r.getRoomType().equalsIgnoreCase(roomType)) {
                return r.getRoomId();
            }
        }
        return null;
    }

    public String notifyCheckOut(String roomId, String staffName, String remarks) {
        return updateRoomStatus(roomId, Room.STATUS_DIRTY,
                (staffName != null ? staffName : "Unassigned"), remarks);
    }

    // 同步前台 Check-Out 记录
    public String syncFromFrontDesk() {
        if (frontDeskController == null) {
            return "Sync failed: Front Desk Controller unavailable.";
        }

        // 1. 调用 FrontDeskController 获取 Checked-Out 列表 (修正方法名拼写: getCheckOutGuest)
        ListInterface<Guest> guestList = frontDeskController.getCheckOutGuest(); 
        if (guestList == null || guestList.isEmpty()) {
            return "Sync failed: Front Desk guest list is empty or unavailable.";
        }

        int synced = 0;
        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest g = guestList.get(i);
            if (g == null) continue;

            // 2. 修正方法名拼写: g.getRoomId()
            if ("Checked-Out".equalsIgnoreCase(g.getStatus()) && g.getRoomId() != null) {
                Room room = findRoom(g.getRoomId());
                if (room != null && !Room.STATUS_DIRTY.equalsIgnoreCase(room.getStatus())
                        && !Room.STATUS_IN_PROGRESS.equalsIgnoreCase(room.getStatus())) {
                    notifyCheckOut(g.getRoomId(), "Unassigned",
                            "Auto-sync: Guest " + g.getFullName() + " checked out");
                    synced++;
                }
            }
        }

        if (synced == 0) {
            return "Sync complete. No new dirty rooms detected from Front Desk.";
        }
        return "Sync complete. " + synced + " room(s) marked Dirty from Front Desk check-outs.";
    }

    // --- SAFE HELPER METHODS ---
    public Room findRoom(String roomId) {
        if (roomId == null) return null;
        String search = roomId.trim();
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room room = roomList.get(i);
            if (room != null && room.getRoomId() != null) {
                if (room.getRoomId().equalsIgnoreCase(search)) {
                    return room;
                }
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

    private boolean isValidStatus(String status) {
        if (status == null) return false;
        return Room.STATUS_CLEAN.equalsIgnoreCase(status) ||
                Room.STATUS_DIRTY.equalsIgnoreCase(status) ||
                Room.STATUS_IN_PROGRESS.equalsIgnoreCase(status) ||
                Room.STATUS_MAINTENANCE.equalsIgnoreCase(status);
    }
}