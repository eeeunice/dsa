package boundary;

import control.HouseKeepingManager;
import entity.Room;
import entity.CleaningTask;
import adt.ListInterface;
import adt.QueueInterface;
import utility.Header;
import java.util.Scanner;

public class HouseKeepingUI {
    
    private HouseKeepingManager manager;
    private Scanner scanner;

    public HouseKeepingUI() {
        this.manager = new HouseKeepingManager();
        this.scanner = new Scanner(System.in);
    }

    public void houseKeepingMenu() {
        int choice = 0;
        
        do {
            Header.printHeader();
            System.out.println(Header.PURPLE + "  =================== HOUSEKEEPING MANAGEMENT & TASK LOG ===================" + Header.RESET);
            System.out.println("  1. [Read]   View All Rooms (Master List)");
            System.out.println("  2. [Update] Change Room Status & Assign Staff");
            System.out.println("  3. [Queue]  Cleaning Task Queue Management (Dispatch & View)");
            System.out.println("  4. [Search] Filter Rooms by Status");
            System.out.println("  5. [Stack]  Rollback (Undo) / Redo Last Status Change");
            System.out.println("  6. [Sync]   Sync Dirty Rooms from Front Desk Check-Outs");
            System.out.println("  7. Exit to Main Menu");
            System.out.println(Header.PURPLE + "  ==========================================================================" + Header.RESET);
            System.out.print("  Select an option (1-7): ");
            
            choice = readIntInput(1, 6);
            
            switch (choice) {
                case 1:
                    displayAllRooms(manager.getRoomList());
                    break;
                    
                case 2:
                    handleUpdateRoomStatus();
                    break;
                    
                case 3:
                    handleTaskQueueMenu();
                    break;
                    
                case 4:
                    handleFilterRooms();
                    break;
                    
                case 5:
                    handleUndoRedoMenu();
                    break;
                
                case 6:
                    handleSyncFromFD();
                    break;
                    
                case 7:
                    System.out.println(Header.GREEN + "\n  Returning to Main Menu..." + Header.RESET);
                    break;
            }
            
            if (choice != 7) {
                System.out.print("\n  Press Enter to continue...");
                scanner.nextLine();
            }

        } while (choice != 7);
    }

    // --- DISPLAY TABLE ---
    private void displayAllRooms(ListInterface<Room> list) {
        System.out.println("\n" + Header.DARK_BLUE + "+----------+------------+---------------+-----------------+------------------+------------------------------+" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "| Room ID  | Room Type  | Status        | Assigned Staff  | Last Cleaned     | Remarks                      |" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "+----------+------------+---------------+-----------------+------------------+------------------------------+" + Header.RESET);
        
        if (list.isEmpty()) {
            System.out.println("|                            No rooms available in the system.                                      |");
        } else {
            for (int i = 1; i <= list.getNumberOfEntries(); i++) {
                Room r = list.get(i);
                String statusColor = getStatusColor(r.getStatus());
                
                System.out.printf("| %-8s | %-10s | %s%-13s%s | %-15s | %-16s | %-28s |\n",
                        r.getRoomId(),
                        r.getRoomType(),
                        statusColor, r.getStatus(), Header.RESET,
                        r.getAssignedStaff(),
                        r.getLastCleanedTime(),
                        r.getRemarks());
            }
        }
        System.out.println(Header.DARK_BLUE + "+----------+------------+---------------+-----------------+------------------+------------------------------+" + Header.RESET);
    }

    // --- UPDATE STATUS WITH NUMERIC MENU VALIDATION & ROOM DISPLAY ---
    private void handleUpdateRoomStatus() {
        System.out.println("\n" + Header.PURPLE + "--- UPDATE ROOM STATUS ---" + Header.RESET);
        displayAllRooms(manager.getRoomList());

        if (manager.getRoomList().isEmpty()) {
            return;
        }

        Room room = null;
        String roomId = "";
        while (room == null) {
            System.out.print("\n  Enter Room ID to update (or type '0' to cancel): ");
            roomId = scanner.nextLine().trim();

            if (roomId.equalsIgnoreCase("0")) {
                System.out.println("  [!] Update canceled.");
                return;
            }

            if (roomId.isEmpty()) {
                System.out.println(Header.RED + "  [!] Room ID cannot be empty." + Header.RESET);
                continue;
            }

            room = manager.findRoom(roomId);
            if (room == null) {
                System.out.println(Header.RED + "  [!] Room ID '" + roomId + "' does not exist. Please try again." + Header.RESET);
            }
        }

        System.out.println("  Current Status for Room " + room.getRoomId() + ": " + getStatusColor(room.getStatus()) + room.getStatus() + Header.RESET);
        System.out.println("\n  Select New Status:");
        System.out.println("  1. Clean (Available)");
        System.out.println("  2. Dirty (Needs Cleaning)");
        System.out.println("  3. In Progress (Currently Being Cleaned)");
        System.out.println("  4. Maintenance (OutOf Order / Repair)");
        System.out.print("  Choice (1-4): ");
        int statusChoice = readIntInput(1, 4);

        String newStatus = Room.STATUS_CLEAN;
        switch (statusChoice) {
            case 1: newStatus = Room.STATUS_CLEAN; break;
            case 2: newStatus = Room.STATUS_DIRTY; break;
            case 3: newStatus = Room.STATUS_IN_PROGRESS; break;
            case 4: newStatus = Room.STATUS_MAINTENANCE; break;
        }

        System.out.print("  Enter Assigned Staff Name (Leave blank if unassigned): ");
        String staff = scanner.nextLine().trim();
        if (staff.isEmpty()) {
            staff = "Unassigned";
        }

        System.out.print("  Enter Remarks / Notes (Optional): ");
        String remarks = scanner.nextLine().trim();

        String result = manager.updateRoomStatus(room.getRoomId(), newStatus, staff, remarks);
        if (result.startsWith("SUCCESS")) {
            System.out.println(Header.GREEN + "  [✓] " + result + Header.RESET);
        } else {
            System.out.println(Header.RED + "  [!] " + result + Header.RESET);
        }
    }

    // --- TASK QUEUE MANAGEMENT SUB-MENU ---
    private void handleTaskQueueMenu() {
        int queueChoice = 0;
        do {
            System.out.println("\n" + Header.PURPLE + "=== CLEANING TASK QUEUE (ArrayQueue ADT) ===" + Header.RESET);
            System.out.println("  1. View Current Cleaning Task Queue");
            System.out.println("  2. Dispatch Next Task to Housekeeper (Dequeue FIFO)");
            System.out.println("  3. Manually Add Room to Cleaning Queue (Enqueue)");
            System.out.println("  4. Return to Housekeeping Main Menu");
            System.out.print("  Select option (1-4): ");
            
            queueChoice = readIntInput(1, 4);
            
            switch (queueChoice) {
                case 1:
                    displayTaskQueue();
                    break;
                    
                case 2:
                    System.out.print("  Enter Housekeeper Name: ");
                    String staff = scanner.nextLine().trim();
                    while (staff.isEmpty()) {
                        System.out.println(Header.RED + "  [!] Housekeeper name cannot be empty." + Header.RESET);
                        System.out.print("  Enter Housekeeper Name: ");
                        staff = scanner.nextLine().trim();
                    }
                    String dispatchResult = manager.dispatchNextCleaningTask(staff);
                    if (dispatchResult.startsWith("SUCCESS")) {
                        System.out.println(Header.GREEN + "  [✓] " + dispatchResult + Header.RESET);
                    } else {
                        System.out.println(Header.YELLOW + "  " + dispatchResult + Header.RESET);
                    }
                    break;
                    
                case 3:
                    System.out.print("  Enter Room ID to enqueue: ");
                    String rId = scanner.nextLine().trim();
                    System.out.println("  Select Priority Level:");
                    System.out.println("  1. Normal");
                    System.out.println("  2. High (VIP)");
                    System.out.print("  Choice (1-2): ");
                    int pChoice = readIntInput(1, 2);
                    String priority = (pChoice == 2) ? "High (VIP)" : "Normal";
                    
                    String enqueueResult = manager.enqueueCleaningTask(rId, priority);
                    if (enqueueResult.startsWith("SUCCESS")) {
                        System.out.println(Header.GREEN + "  [✓] " + enqueueResult + Header.RESET);
                    } else {
                        System.out.println(Header.RED + "  [!] " + enqueueResult + Header.RESET);
                    }
                    break;
            }
        } while (queueChoice != 4);
    }

    private void displayTaskQueue() {
        QueueInterface<CleaningTask> queue = manager.getCleaningQueue();
        System.out.println("\n" + Header.DARK_BLUE + "+----------+----------+------------+---------------+------------+----------------+--------------+" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "| Task ID  | Room ID  | Room Type  | Priority      | Req. Time  | Assigned Staff | Task Status  |" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "+----------+----------+------------+---------------+------------+----------------+--------------+" + Header.RESET);
        
        if (queue.isEmpty()) {
            System.out.println("|                               No pending cleaning tasks in queue.                            |");
        } else {
            // Traverse queue temporarily
            adt.ArrayQueue<CleaningTask> temp = new adt.ArrayQueue<>();
            while (!queue.isEmpty()) {
                CleaningTask t = queue.dequeue();
                String pColor = t.getPriority().contains("VIP") ? Header.RED : Header.RESET;
                System.out.printf("| %-8s | %-8s | %-10s | %s%-13s%s | %-10s | %-14s | %-12s |\n",
                        t.getTaskId(), t.getRoomId(), t.getRoomType(),
                        pColor, t.getPriority(), Header.RESET,
                        t.getRequestedTime(), t.getAssignedStaff(), t.getTaskStatus());
                temp.enqueue(t);
            }
            while (!temp.isEmpty()) {
                queue.enqueue(temp.dequeue());
            }
        }
        System.out.println(Header.DARK_BLUE + "+----------+----------+------------+---------------+------------+----------------+--------------+" + Header.RESET);
    }

    // --- SEARCH / FILTER ROOMS ---
    private void handleFilterRooms() {
        System.out.println("\n" + Header.PURPLE + "--- FILTER ROOMS BY STATUS ---" + Header.RESET);
        System.out.println("  Select status to filter:");
        System.out.println("  1. Clean");
        System.out.println("  2. Dirty");
        System.out.println("  3. In Progress");
        System.out.println("  4. Maintenance");
        System.out.print("  Choice (1-4): ");
        
        int filterChoice = readIntInput(1, 4);
        String targetStatus = Room.STATUS_CLEAN;
        switch (filterChoice) {
            case 1: targetStatus = Room.STATUS_CLEAN; break;
            case 2: targetStatus = Room.STATUS_DIRTY; break;
            case 3: targetStatus = Room.STATUS_IN_PROGRESS; break;
            case 4: targetStatus = Room.STATUS_MAINTENANCE; break;
        }

        ListInterface<Room> filtered = manager.filterRoomsByStatus(targetStatus);
        System.out.println("\n  Filter Results for [" + targetStatus + "] (" + filtered.getNumberOfEntries() + " rooms found):");
        displayAllRooms(filtered);
    }

    // --- UNDO / REDO SUB-MENU ---
    private void handleUndoRedoMenu() {
        System.out.println("\n" + Header.PURPLE + "--- UNDO / REDO (ArrayStack ADT) ---" + Header.RESET);
        System.out.println("  1. Undo Last Status Update");
        System.out.println("  2. Redo Last Status Update");
        System.out.print("  Choice (1-2): ");
        int choice = readIntInput(1, 2);

        if (choice == 1) {
            String res = manager.undoLastAction();
            if (res.startsWith("SUCCESS")) {
                System.out.println(Header.GREEN + "  [✓] " + res + Header.RESET);
            } else {
                System.out.println(Header.RED + "  [!] " + res + Header.RESET);
            }
        } else {
            String res = manager.redoLastAction();
            if (res.startsWith("SUCCESS")) {
                System.out.println(Header.GREEN + "  [✓] " + res + Header.RESET);
            } else {
                System.out.println(Header.RED + "  [!] " + res + Header.RESET);
            }
        }
    }
    
    private void handleSyncFromFD() {
        System.out.println("\n" + Header.PURPLE + "--- SYNC DIRTY ROOMS FROM FRONT DESK ---" + Header.RESET);
        System.out.println("  Scanning Front Desk check-out records...");
        String result = manager.syncFromFrontDesk();
        System.out.println(Header.GREEN + "  [✓] " + result + Header.RESET);
        System.out.println("\n  Updated Room List:");
        displayAllRooms(manager.getRoomList());
    }

    // --- UTILITY: INTEGER INPUT VALIDATION ---
    private int readIntInput(int min, int max) {
        int value = -1;
        while (true) {
            try {
                String line = scanner.nextLine().trim();
                value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.print(Header.RED + "  [!] Out of range. Enter number between " + min + " and " + max + ": " + Header.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.print(Header.RED + "  [!] Invalid input. Please enter a valid number (" + min + "-" + max + "): " + Header.RESET);
            }
        }
    }

    private String getStatusColor(String status) {
        switch (status) {
            case Room.STATUS_CLEAN:       return Header.GREEN;
            case Room.STATUS_DIRTY:       return Header.RED;
            case Room.STATUS_IN_PROGRESS: return Header.YELLOW;
            case Room.STATUS_MAINTENANCE: return Header.PURPLE;
            default:                      return Header.RESET;
        }
    }
}