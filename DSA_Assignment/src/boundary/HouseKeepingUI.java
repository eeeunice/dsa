package boundary;

import control.HouseKeepingController;
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
            utility.ClearScreen.clear();
            utility.Header.printHeader();
            System.out.println(utility.Header.PURPLE + "  =================== HOUSEKEEPING MANAGEMENT & TASK LOG ===================" + utility.Header.RESET);
            System.out.println("  1. View All Rooms (Master List)");
            System.out.println("  2. Change Room Status & Assign Staff");
            System.out.println("  3. Cleaning Task Queue Management (Dispatch & View)");
            System.out.println("  4. Filter Rooms by Status");
            System.out.println("  5. Undo/Redo Last Status Change");
            System.out.println("  6. Sync Dirty Rooms(Checked Out)");
            System.out.println("  7. Lost & Found Management");
            System.out.println("  8. Exit to Main Menu");
            System.out.println(utility.Header.PURPLE + "  ==========================================================================" + utility.Header.RESET);
            System.out.print("  Select an option (1-8): ");

            choice = readIntInput(1, 8);

            switch (choice) {
                case 1:
                    utility.ClearScreen.clear();
                    displayAllRooms(manager.getAllRoomsData());
                    break;

                case 2:
                    utility.ClearScreen.clear();
                    handleUpdateRoomStatus();
                    break;

                case 3:
                    utility.ClearScreen.clear();
                    handleTaskQueueMenu();
                    break;

                case 4:
                    utility.ClearScreen.clear();
                    handleFilterRooms();
                    break;

                case 5:
                    utility.ClearScreen.clear();
                    handleUndoRedoMenu();
                    break;

                case 6:
                    utility.ClearScreen.clear();
                    handleSyncDirtyRooms();
                    break;

                case 7:
                    utility.ClearScreen.clear();
                    handleLostAndFoundMenu();
                    break;

                case 8:
                    System.out.println(utility.Header.GREEN + "\n  Returning to Main Menu..." + utility.Header.RESET);
                    break;
                    
                default:
                System.out.println("\nInvalid choice. Please choose between 1 to 8.");
            }

            if (choice != 8) {
                System.out.print("\n  Press Enter to continue...");
                scanner.nextLine();
            }

        } while (choice != 8);
    }

    private void displayAllRooms(String[][] roomsData) {
        System.out.println("\n" + utility.Header.DARK_BLUE + "+----------+---------------+-----------------+------------------+-----------------------------------+" + utility.Header.RESET);
        System.out.println(utility.Header.DARK_BLUE + "| Room ID  | Status        | Assigned Staff  | Last Cleaned     | Remarks                           |" + utility.Header.RESET);
        System.out.println(utility.Header.DARK_BLUE + "+----------+---------------+-----------------+------------------+-----------------------------------+" + utility.Header.RESET);

        if (roomsData == null || roomsData.length == 0) {
            System.out.println("|                            No rooms available in the system.                                     |");
        } else {
            for (String[] r : roomsData) {
                if (r == null) continue;
                String statusColor = getStatusColor(r[1]);

                System.out.printf("| %-8s | %s%-13s%s | %-15s | %-16s | %-33s |\n",
                        r[0],
                        statusColor, r[1], utility.Header.RESET,
                        r[2],
                        r[3],
                        r[4]);
            }
        }
        System.out.println(utility.Header.DARK_BLUE + "+----------+---------------+-----------------+------------------+-----------------------------------+" + utility.Header.RESET);
    }

    // --- UPDATE STATUS WITH NUMERIC MENU VALIDATION & ROOM DISPLAY ---
    private void handleUpdateRoomStatus() {
        System.out.println("\n" + utility.Header.PURPLE + "--- UPDATE ROOM STATUS ---" + utility.Header.RESET);
        displayAllRooms(manager.getAllRoomsData());

        if (manager.getAllRoomsData().length == 0) {
            return;
        }

        String currentStatus = null;
        String roomId = "";
        while (currentStatus == null) {
            System.out.print("\n  Enter Room ID to update (or type '0' to cancel): ");
            roomId = scanner.nextLine().trim();

            if (roomId.equalsIgnoreCase("0")) {
                System.out.println("  [!] Update canceled.");
                return;
            }

            if (roomId.isEmpty()) {
                System.out.println(utility.Header.RED + "  [!] Room ID cannot be empty." + utility.Header.RESET);
                continue;
            }

            currentStatus = manager.getRoomStatus(roomId);
            if (currentStatus == null) {
                System.out.println(utility.Header.RED + "  [!] Room ID '" + roomId + "' does not exist. Please try again." + utility.Header.RESET);
            }
        }

        System.out.println("  Current Status for Room " + roomId + ": " + getStatusColor(currentStatus) + currentStatus + utility.Header.RESET);
        System.out.println("\n  Select New Status:");
        System.out.println("  1. Clean (Available)");
        System.out.println("  2. Dirty (Needs Cleaning)");
        System.out.println("  3. In Progress (Currently Being Cleaned)");
        System.out.println("  4. Maintenance (Out Of Order / Repair)");
        System.out.println("  5. Occupied (Guest Checked-In)");
        System.out.print("  Choice (1-5): ");
        int statusChoice = readIntInput(1, 5);

        String newStatus = "Clean";
        switch (statusChoice) {
            case 1: newStatus = "Clean"; break;
            case 2: newStatus = "Dirty"; break;
            case 3: newStatus = "In Progress"; break;
            case 4: newStatus = "Maintenance"; break;
            case 5: newStatus = "Occupied"; break;
        }

        System.out.print("  Enter Assigned Staff Name (Leave blank if unassigned): ");
        String staff = scanner.nextLine().trim();
        if (staff.isEmpty()) {
            staff = "Unassigned";
        }

        System.out.print("  Enter Remarks / Notes (Optional): ");
        String remarks = scanner.nextLine().trim();

        String result = manager.updateRoomStatus(roomId, newStatus, staff, remarks);
        if (result.startsWith("SUCCESS")) {
            System.out.println(utility.Header.GREEN + "  [✓] " + result + utility.Header.RESET);
        } else {
            System.out.println(utility.Header.RED + "  [!] " + result + utility.Header.RESET);
        }
    }

    // --- TASK QUEUE MANAGEMENT SUB-MENU ---
    private void handleTaskQueueMenu() {
        int queueChoice = 0;
        do {
            System.out.println("\n" + utility.Header.PURPLE + "=== CLEANING TASK QUEUE ===" + utility.Header.RESET);
            System.out.println("  1. View Current Cleaning Task Queue & Dispatch Task");
            System.out.println("  2. Manually Add Room to Cleaning Queue");
            System.out.println("  3. Return to Housekeeping Main Menu");
            System.out.print("  Select option (1-3): ");

            queueChoice = readIntInput(1, 3);

            switch (queueChoice) {
                case 1:
                    displayTaskQueue();

                    String[][] tasksData = manager.getCleaningTasksData();
                    if (tasksData != null && tasksData.length > 0) {
                        System.out.print("\n  Do you want to dispatch the next task to a housekeeper? (Y/N): ");
                        String choice = scanner.nextLine().trim();

                        if (choice.equalsIgnoreCase("Y")) {
                            System.out.print("  Enter Housekeeper Name: ");
                            String staff = scanner.nextLine().trim();

                            while (staff.isEmpty()) {
                                System.out.println(utility.Header.RED + "  [!] Housekeeper name cannot be empty." + utility.Header.RESET);
                                System.out.print("  Enter Housekeeper Name: ");
                                staff = scanner.nextLine().trim();
                            }

                            String dispatchResult = manager.dispatchNextCleaningTask(staff);
                            if (dispatchResult.startsWith("SUCCESS")) {
                                System.out.println(utility.Header.GREEN + "  [✓] " + dispatchResult + utility.Header.RESET);
                            } else {
                                System.out.println(utility.Header.YELLOW + "  " + dispatchResult + utility.Header.RESET);
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
                        System.out.println(utility.Header.GREEN + "  [✓] " + enqueueResult + utility.Header.RESET);
                    } else {
                        System.out.println(utility.Header.RED + "  [!] " + enqueueResult + utility.Header.RESET);
                    }
                    break;
            }
        } while (queueChoice != 3);
    }

    // --- DISPLAY TASK QUEUE ---
    private void displayTaskQueue() {
        String[][] tasksData = manager.getCleaningTasksData();
        System.out.println("\n" + utility.Header.DARK_BLUE + "+----------+----------+---------------+------------+----------------+-----------------+" + utility.Header.RESET);
        System.out.println(utility.Header.DARK_BLUE + "| Task ID  | Room ID  | Priority      | Req. Time  | Assigned Staff | Task Status     |" + utility.Header.RESET);
        System.out.println(utility.Header.DARK_BLUE + "+----------+----------+---------------+------------+----------------+-----------------+" + utility.Header.RESET);

        if (tasksData == null || tasksData.length == 0) {
            System.out.println("|                            No pending cleaning tasks in queue.                          |");
        } else {
            for (String[] t : tasksData) {
                if (t == null) continue;
                String pColor = t[2].contains("VIP") ? utility.Header.RED : utility.Header.RESET;
                System.out.printf("| %-8s | %-8s | %s%-13s%s | %-10s | %-14s | %-15s |\n",
                        t[0], t[1],
                        pColor, t[2], utility.Header.RESET,
                        t[3], t[4], t[5]);
            }
        }
        System.out.println(utility.Header.DARK_BLUE + "+----------+----------+---------------+------------+----------------+-----------------+" + utility.Header.RESET);
    }

    // --- SEARCH / FILTER ROOMS ---
    private void handleFilterRooms() {
        System.out.println("\n" + utility.Header.PURPLE + "--- FILTER ROOMS BY STATUS ---" + utility.Header.RESET);
        System.out.println("  Select status to filter:");
        System.out.println("  1. Clean");
        System.out.println("  2. Dirty");
        System.out.println("  3. In Progress");
        System.out.println("  4. Maintenance");
        System.out.println("  5. Occupied");
        System.out.print("  Choice (1-5): ");

        int filterChoice = readIntInput(1, 5);
        String targetStatus = "Clean";
        switch (filterChoice) {
            case 1: targetStatus = "Clean"; break;
            case 2: targetStatus = "Dirty"; break;
            case 3: targetStatus = "In Progress"; break;
            case 4: targetStatus = "Maintenance"; break;
            case 5: targetStatus = "Occupied"; break;
        }

        String[][] filteredData = manager.getRoomsDataByStatus(targetStatus);
        System.out.println("\n  Filter Results for [" + targetStatus + "] (" + filteredData.length + " rooms found):");
        displayAllRooms(filteredData);
    }

    // --- UNDO / REDO SUB-MENU ---
    private void handleUndoRedoMenu() {
        System.out.println("\n" + utility.Header.PURPLE + "--- UNDO / REDO (ArrayStack ADT) ---" + utility.Header.RESET);
        System.out.println("  1. Undo Last Status Update");
        System.out.println("  2. Redo Last Status Update");
        System.out.print("  Choice (1-2): ");
        int choice = readIntInput(1, 2);

        if (choice == 1) {
            String res = manager.undoLastAction();
            if (res.startsWith("SUCCESS")) {
                System.out.println(utility.Header.GREEN + "  [✓] " + res + utility.Header.RESET);
            } else {
                System.out.println(utility.Header.RED + "  [!] " + res + utility.Header.RESET);
            }
        } else {
            String res = manager.redoLastAction();
            if (res.startsWith("SUCCESS")) {
                System.out.println(utility.Header.GREEN + "  [✓] " + res + utility.Header.RESET);
            } else {
                System.out.println(utility.Header.RED + "  [!] " + res + utility.Header.RESET);
            }
        }
    }

    private void handleSyncDirtyRooms() {
        String[][] dirtyRoomsData = HouseKeepingController.getInstance().syncAndGetDirtyRoomsData();

        System.out.println("\n==========================================================================================");
        System.out.println("                        DIRTY ROOMS LIST (PENDING CLEANING)                               ");
        System.out.println("==========================================================================================");
        System.out.printf("%-10s | %-12s | %-15s | %-20s | %-20s\n", "Room ID", "Status", "Assigned Staff", "Last Cleaned", "Remarks");
        System.out.println("------------------------------------------------------------------------------------------");

        if (dirtyRoomsData == null || dirtyRoomsData.length == 0) {
            System.out.println("  [!] No dirty rooms found.");
        } else {
            for (String[] row : dirtyRoomsData) {
                System.out.printf("%-10s | %-12s | %-15s | %-20s | %-20s\n",
                        row[0], row[1], row[2], row[3], row[4]);
            }
        }
        System.out.println("==========================================================================================");

        System.out.println(utility.Header.GREEN + " [!] Synced successfully! Displaying " + dirtyRoomsData.length + " Dirty Room(s)." + utility.Header.RESET);
    }

    // ==========================================
    // --- LOST & FOUND SUB-MENU & FUNCTIONS ---
    // ==========================================
    private void handleLostAndFoundMenu() {
        int choice = 0;
        do {
            System.out.println("\n" + utility.Header.PURPLE + "=== LOST & FOUND MANAGEMENT ===" + utility.Header.RESET);
            System.out.println("  1. View All Lost & Found Items");
            System.out.println("  2. Report New Lost Item");
            System.out.println("  3. Claim Lost Item");
            System.out.println("  4. Return to Housekeeping Main Menu");
            System.out.print("  Select option (1-4): ");

            choice = readIntInput(1, 4);

            switch (choice) {
                case 1:
                    displayLostItems();
                    break;
                case 2:
                    handleReportLostItem();
                    break;
                case 3:
                    handleClaimLostItem();
                    break;
            }
        } while (choice != 4);
    }

    private void displayLostItems() {
        String[][] lostItems = manager.getLostItemsData();

        System.out.println("\n" + utility.Header.DARK_BLUE + "+----------+----------+---------------------------+-----------------+------------+" + utility.Header.RESET);
        System.out.println(utility.Header.DARK_BLUE + "| Item ID  | Room ID  | Item Description          | Date Found      | Status     |" + utility.Header.RESET);
        System.out.println(utility.Header.DARK_BLUE + "+----------+----------+---------------------------+-----------------+------------+" + utility.Header.RESET);

        if (lostItems == null || lostItems.length == 0) {
            System.out.println("|                      No lost & found items recorded.                             |");
        } else {
            for (String[] row : lostItems) {
                if (row == null) continue;
                String statusColor = "Claimed".equalsIgnoreCase(row[4]) ? utility.Header.GREEN : utility.Header.RED;

                System.out.printf("| %-8s | %-8s | %-25s | %-15s | %s%-10s%s |\n",
                        row[0], row[1], row[2], row[3],
                        statusColor, row[4], utility.Header.RESET);
            }
        }
        System.out.println(utility.Header.DARK_BLUE + "+----------+----------+---------------------------+-----------------+------------+" + utility.Header.RESET);
    }

    private void handleReportLostItem() {
        System.out.println("\n" + utility.Header.PURPLE + "--- REPORT NEW LOST ITEM ---" + utility.Header.RESET);
        
        System.out.print("  Enter Room ID where item was found: ");
        String roomId = scanner.nextLine().trim();
        while (roomId.isEmpty()) {
            System.out.println(utility.Header.RED + "  [!] Room ID cannot be empty." + utility.Header.RESET);
            System.out.print("  Enter Room ID: ");
            roomId = scanner.nextLine().trim();
        }

        System.out.print("  Enter Item Description / Name: ");
        String itemName = scanner.nextLine().trim();
        while (itemName.isEmpty()) {
            System.out.println(utility.Header.RED + "  [!] Item name cannot be empty." + utility.Header.RESET);
            System.out.print("  Enter Item Description: ");
            itemName = scanner.nextLine().trim();
        }

        System.out.print("  Enter Date Found (e.g. YYYY-MM-DD): ");
        String dateFound = scanner.nextLine().trim();
        if (dateFound.isEmpty()) {
            dateFound = "Today";
        }

        String res = manager.reportLostItem(roomId, itemName, dateFound);
        System.out.println(utility.Header.GREEN + "  [✓] Lost Item Reported: " + res + utility.Header.RESET);
    }

    private void handleClaimLostItem() {
        System.out.println("\n" + utility.Header.PURPLE + "--- CLAIM LOST ITEM ---" + utility.Header.RESET);
        displayLostItems();

        System.out.print("\n  Enter Item ID to claim (or '0' to cancel): ");
        String itemId = scanner.nextLine().trim();

        if (itemId.equalsIgnoreCase("0") || itemId.isEmpty()) {
            System.out.println("  [!] Claim action canceled.");
            return;
        }

        String res = manager.claimLostItem(itemId);
        if (res.startsWith("SUCCESS")) {
            System.out.println(utility.Header.GREEN + "  [✓] " + res + utility.Header.RESET);
        } else {
            System.out.println(utility.Header.RED + "  [!] " + res + utility.Header.RESET);
        }
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
                    System.out.print(utility.Header.RED + "  [!] Out of range. Enter number between " + min + " and " + max + ": " + utility.Header.RESET);
                }
            } catch (NumberFormatException e) {
                System.out.print(utility.Header.RED + "  [!] Invalid input. Please enter a valid number (" + min + "-" + max + "): " + utility.Header.RESET);
            }
        }
    }

    private String getStatusColor(String status) {
        switch (status) {
            case "Clean":
                return utility.Header.GREEN;
            case "Dirty":
                return utility.Header.RED;
            case "In Progress":
                return utility.Header.YELLOW;
            case "Maintenance":
                return utility.Header.PURPLE;
            case "Occupied":
                return utility.Header.DARK_BLUE;
            default:
                return utility.Header.RESET;
        }
    }
}