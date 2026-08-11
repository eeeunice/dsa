package boundary;

import control.HouseKeepingManager;
import java.util.Scanner;
import adt.LinkedList;

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
            System.out.println("\n=== HOUSEKEEPING MANAGEMENT ===");
            System.out.println("1. [Create] Add New Room");
            System.out.println("2. [Read]   View All Rooms");
            System.out.println("3. [Update] Change Room Status");
            System.out.println("4. [Undo]   Rollback Last Status Update");
            System.out.println("5. [Delete] Remove a Room");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            
            choice = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (choice) {
                case 1:
                    System.out.print("Enter new Room ID: ");
                    String newId = scanner.nextLine();
                    if (manager.addRoom(newId)) {
                        System.out.println("Success! Room added.");
                    } else {
                        System.out.println("Error: Room already exists.");
                    }
                    break;
                    
                case 2:
                    System.out.println("\n--- Room Database ---");
                    System.out.println(manager.getAllRoomsAsString());
                    break;
                    
                case 3:
                    System.out.print("Enter Room ID to update: ");
                    String updateId = scanner.nextLine();
                    System.out.print("Enter new status (Clean/Dirty/In Progress/Maintenance): ");
                    String newStatus = scanner.nextLine();
                    
                    if (manager.updateRoomStatus(updateId, newStatus)) {
                        System.out.println("Success! Status updated.");
                    } else {
                        System.out.println("Error: Room not found.");
                    }
                    break;
                    
                case 4:
                    if (manager.undoLastStatusUpdate()) {
                        System.out.println("Success! The last status change was rolled back.");
                    } else {
                        System.out.println("Error: No recent changes to undo.");
                    }
                    break;
                    
                case 5:
                    System.out.print("Enter Room ID to delete: ");
                    String deleteId = scanner.nextLine();
                    if (manager.deleteRoom(deleteId)) {
                        System.out.println("Success! Room deleted.");
                    } else {
                        System.out.println("Error: Room not found.");
                    }
                    break;
                    
                case 6:
                    System.out.println("Exiting Housekeeping Menu...");
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }
}