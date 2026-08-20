package boundary;

import control.BookingDataController;
import control.HouseKeepingController;
import entity.Room;
import entity.CleaningTask;
import utility.Header;
import utility.ClearScreen;
import java.util.Scanner;

public class HouseKeepingUI {
    
    private HouseKeepingController manager;
    private Scanner scanner;

    public HouseKeepingUI() {
        this.manager = HouseKeepingController.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void houseKeepingMenu() {
        int choice = 0;
        
        do {
            ClearScreen.clear();
            Header.printHeader();
            System.out.println(Header.PURPLE + "  =================== HOUSEKEEPING MANAGEMENT & TASK LOG ===================" + Header.RESET);
            System.out.println("  1. View All Rooms (Master List)");
            System.out.println("  2. Change Room Status & Assign Staff");
            System.out.println("  3. Cleaning Task Queue Management (Dispatch & View)");
            System.out.println("  4. Filter Rooms by Status");
            System.out.println("  5. Rollback (Undo) / Redo Last Status Change");
            System.out.println("  6. Sync Dirty Rooms from Front Desk Check-Outs");
            System.out.println("  7. Exit to Main Menu");
            System.out.println(Header.PURPLE + "  ==========================================================================" + Header.RESET);
            System.out.print("  Select an option (1-7): ");
            
            choice = readIntInput(1, 7);
            
            switch (choice) {
                case 1:
                    ClearScreen.clear();
                    displayAllRooms(manager.getAllRooms());
                    break;
                    
                case 2:
                    ClearScreen.clear();
                    handleUpdateRoomStatus();
                    break;
                    
                case 3:
                    ClearScreen.clear();
                    handleTaskQueueMenu();
                    break;
                    
                case 4:
                    ClearScreen.clear();
                    handleFilterRooms();
                    break;
                    
                case 5:
                    ClearScreen.clear();
                    handleUndoRedoMenu();
                    break;
                
                case 6:
                    ClearScreen.clear();
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

    private void displayAllRooms(Room[] rooms) {
        System.out.println("\n" + Header.DARK_BLUE + "+----------+---------------+-----------------+------------------+-----------------------------------+" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "| Room ID  | Status        | Assigned Staff  | Last Cleaned     | Remarks                           |" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "+----------+---------------+-----------------+------------------+-----------------------------------+" + Header.RESET);
        
        if (rooms == null || rooms.length == 0) {
            System.out.println("|                            No rooms available in the system.                                      |");
        } else {
            for (Room r : rooms) {
                if (r == null) {
                    continue;
                }
                String statusColor = getStatusColor(r.getStatus());
                
                System.out.printf("| %-8s | %s%-13s%s | %-15s | %-16s | %-33s |\n",
                        r.getRoomId(),
                        statusColor, r.getStatus(), Header.RESET,
                        r.getAssignedStaff(),
                        r.getLastCleanedTime(),
                        r.getRemarks());
            }
        }
        System.out.println(Header.DARK_BLUE + "+----------+---------------+-----------------+------------------+-----------------------------------+" + Header.RESET);
    }

    // --- UPDATE STATUS WITH NUMERIC MENU VALIDATION & ROOM DISPLAY ---
    private void handleUpdateRoomStatus() {
        System.out.println("\n" + Header.PURPLE + "--- UPDATE ROOM STATUS ---" + Header.RESET);
        displayAllRooms(manager.getAllRooms());

        if (manager.getAllRooms().length == 0) {
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
        System.out.println("  4. Maintenance (Out Of Order / Repair)");
        System.out.println("  5. Occupied (Guest Checked-In)");
        System.out.print("  Choice (1-5): ");
        int statusChoice = readIntInput(1, 5);

        String newStatus = Room.STATUS_CLEAN;
        switch (statusChoice) {
            case 1: newStatus = Room.STATUS_CLEAN; break;
            case 2: newStatus = Room.STATUS_DIRTY; break;
            case 3: newStatus = Room.STATUS_IN_PROGRESS; break;
            case 4: newStatus = Room.STATUS_MAINTENANCE; break;
            case 5: newStatus = Room.STATUS_OCCUPIED; break;
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
            System.out.println("\n" + Header.PURPLE + "=== CLEANING TASK QUEUE ===" + Header.RESET);
            System.out.println("  1. View Current Cleaning Task Queue & Dispatch Task");
            System.out.println("  2. Manually Add Room to Cleaning Queue");
            System.out.println("  3. Return to Housekeeping Main Menu");
            System.out.print("  Select option (1-3): ");
            
            queueChoice = readIntInput(1, 3);
            
            switch (queueChoice) {
                case 1:
                    displayTaskQueue();
                    
                    CleaningTask[] tasks = manager.getCleaningTasks();
                    if (tasks != null && tasks.length > 0) {
                        System.out.print("\n  Do you want to dispatch the next task to a housekeeper? (Y/N): ");
                        String choice = scanner.nextLine().trim();
                        
                        if (choice.equalsIgnoreCase("Y")) {
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
                        } else {
                            System.out.println("  Returning to Task Queue Menu...");
                        }
                    }
                    break;
                    
                case 2:
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
        } while (queueChoice != 3);
    }

    // --- DISPLAY TASK QUEUE ---
    private void displayTaskQueue() {
        CleaningTask[] tasks = manager.getCleaningTasks();
        System.out.println("\n" + Header.DARK_BLUE + "+----------+----------+---------------+------------+----------------+-----------------+" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "| Task ID  | Room ID  | Priority      | Req. Time  | Assigned Staff | Task Status     |" + Header.RESET);
        System.out.println(Header.DARK_BLUE + "+----------+----------+---------------+------------+----------------+-----------------+" + Header.RESET);
        
        if (tasks == null || tasks.length == 0) {
            System.out.println("|                           No pending cleaning tasks in queue.                        |");
        } else {
            for (CleaningTask t : tasks) {
                if (t == null) {
                    continue;
                }
                String pColor = t.getPriority().contains("VIP") ? Header.RED : Header.RESET;
                System.out.printf("| %-8s | %-8s | %s%-13s%s | %-10s | %-14s | %-15s |\n",
                        t.getTaskId(), t.getRoomId(),
                        pColor, t.getPriority(), Header.RESET,
                        t.getRequestedTime(), t.getAssignedStaff(), t.getTaskStatus());
            }
        }
        System.out.println(Header.DARK_BLUE + "+----------+----------+---------------+------------+----------------+-----------------+" + Header.RESET);
    }

    // --- SEARCH / FILTER ROOMS ---
    private void handleFilterRooms() {
        System.out.println("\n" + Header.PURPLE + "--- FILTER ROOMS BY STATUS ---" + Header.RESET);
        System.out.println("  Select status to filter:");
        System.out.println("  1. Clean");
        System.out.println("  2. Dirty");
        System.out.println("  3. In Progress");
        System.out.println("  4. Maintenance");
        System.out.println("  5. Occupied");
        System.out.print("  Choice (1-5): ");
        
        int filterChoice = readIntInput(1, 5);
        String targetStatus = Room.STATUS_CLEAN;
        switch (filterChoice) {
            case 1: targetStatus = Room.STATUS_CLEAN; break;
            case 2: targetStatus = Room.STATUS_DIRTY; break;
            case 3: targetStatus = Room.STATUS_IN_PROGRESS; break;
            case 4: targetStatus = Room.STATUS_MAINTENANCE; break;
            case 5: targetStatus = Room.STATUS_OCCUPIED; break;
        }

        Room[] filtered = manager.getRoomsByStatus(targetStatus);
        System.out.println("\n  Filter Results for [" + targetStatus + "] (" + filtered.length + " rooms found):");
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
        displayAllRooms(manager.getAllRooms());
    }

    // --- UTILITY: INTEGER INPUT VALIDATION ---
    private int readIntInput(int min, int max) {
        int value = -1;
        while (true) {
            try {
                value = Integer.parseInt(scanner.nextLine().trim());
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
            case Room.STATUS_CLEAN:
                return Header.GREEN;
            case Room.STATUS_DIRTY:
                return Header.RED;
            case Room.STATUS_IN_PROGRESS: 
                return Header.YELLOW;
            case Room.STATUS_MAINTENANCE: 
                return Header.PURPLE;
            case Room.STATUS_OCCUPIED:   
                return Header.DARK_BLUE;
            default:                      
                return Header.RESET;
        }
    }
}