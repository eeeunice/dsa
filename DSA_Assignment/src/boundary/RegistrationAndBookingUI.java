package boundary;

import adt.QueueInterface;
import adt.ListInterface;
import control.RegistrationAndBookingDataController;
import entity.Guest;
import utility.ClearScreen;
import utility.Header;

import java.util.Scanner;

public class RegistrationAndBookingUI {
    private QueueInterface<Guest> guestQueue = RegistrationAndBookingDataController.getGuestQueue();
    private Scanner scanner = new Scanner(System.in);

    // Standard Room Rates per night (in RM)
    private static final double RATE_SINGLE = 100.00;
    private static final double RATE_DOUBLE = 180.00;
    private static final double RATE_SUITE = 350.00;
    private static final double RATE_PRESIDENTIAL = 800.00;

    private double getRoomRate(String roomType) {
        switch (roomType) {
            case "Single": return RATE_SINGLE;
            case "Double": return RATE_DOUBLE;
            case "Suite": return RATE_SUITE;
            case "Presidential Suite": return RATE_PRESIDENTIAL;
            default: return 0.0;
        }
    }

    private String selectRoomTypeMenu() {
        System.out.println("Select Room Type:");
        System.out.printf("1. Single (RM%.2f/night)%n", RATE_SINGLE);
        System.out.printf("2. Double (RM%.2f/night)%n", RATE_DOUBLE);
        System.out.printf("3. Suite (RM%.2f/night)%n", RATE_SUITE);
        System.out.printf("4. Presidential Suite (RM%.2f/night)%n", RATE_PRESIDENTIAL);
        
        int roomChoice = 0;
        while (true) {
            System.out.print("Please choose room type (1-4): ");
            if (scanner.hasNextInt()) {
                roomChoice = scanner.nextInt();
                scanner.nextLine();
                if (roomChoice >= 1 && roomChoice <= 4) {
                    break;
                }
            } else {
                scanner.next();
            }
            System.out.println("Error: Invalid choice! Please enter a number between 1 and 4.");
        }

        switch (roomChoice) {
            case 1: return "Single";
            case 2: return "Double";
            case 3: return "Suite";
            case 4: return "Presidential Suite";
            default: return "Single";
        }
    }

    public void bookingModule() {
        int choice;
        do {
            ClearScreen.clear();
            Header.printHeader();
            
            System.out.println("=== Walk-In Registration & Standard Booking ===");
            System.out.println("1. Register & Booking");
            System.out.println("2. Serve & Remove Next Guest");
            System.out.println("3. View Next Guest & Queue Status");        
            System.out.println("4. Display All Guests");    
            System.out.println("5. Cancel Guest Reservation");                
            System.out.println("6. Update / Edit Guest Details");                
<<<<<<< HEAD
            System.out.println("7. Main Menu");                                                       
=======
            System.out.println("7. Main Menu");                                    
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
            System.out.print("Please choose an option (1-7): ");
            
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input! Please enter a number between 1 and 7: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1: // Register & Enqueue New Guest
                    char addAnother;
                    do {
                        String name;
                        while (true) {
                            System.out.print("Enter Your Full Name (Max 26 characters): ");
                            name = scanner.nextLine().trim();
                            if (!name.isEmpty() && name.length() <= 26 && name.matches("[a-zA-Z\\s]+")) {
                                break;
                            }
                            System.out.println("Error: Name must be alphabetic letters/spaces only and maximum 26 characters long.");
                        }
                        
                        char gender;
                        while (true) {
                            System.out.print("Enter Gender (M/F): ");
                            String genderInput = scanner.nextLine().trim().toUpperCase();
                            if (genderInput.length() == 1 && (genderInput.charAt(0) == 'M' || genderInput.charAt(0) == 'F')) {
                                gender = genderInput.charAt(0);
                                break;
                            }
                            System.out.println("Error: Invalid gender! Please enter 'M' or 'F'.");
                        }
                        
                        String contact;
                        while (true) {
                            System.out.print("Enter Contact Number (e.g., 011-12345678): ");
                            contact = scanner.nextLine().trim();
                            if (contact.matches("^01[0-14-9]-[0-9]{7,8}$")) {
                                break;
                            }
                            System.out.println("Error: Invalid format! Must include correct prefix, a hyphen (-), and 7 to 8 digits.");
                        }
                        
<<<<<<< HEAD
                        // Numbered Room Type Selection
                        String room;
                        while (true) {
                            System.out.println("Select Room Type:");
                            System.out.println("  1. Single (RM 150.00 / night)");
                            System.out.println("  2. Double (RM 250.00 / night)");
                            System.out.println("  3. Suite (RM 500.00 / night)");
                            System.out.println("  4. Presidential Suite (RM 1200.00 / night)");
                            System.out.print("Enter choice (1-4): ");
                            
                            if (scanner.hasNextInt()) {
                                int roomChoice = scanner.nextInt();
                                scanner.nextLine();
                                if (roomChoice == 1) {
                                    room = "Single";
                                    break;
                                } else if (roomChoice == 2) {
                                    room = "Double";
                                    break;
                                } else if (roomChoice == 3) {
                                    room = "Suite";
                                    break;
                                } else if (roomChoice == 4) {
                                    room = "Presidential Suite";
                                    break;
                                }
                            } else {
                                scanner.next();
                            }
                            System.out.println("Error: Invalid choice! Please select a number between 1 and 4.");
                        }

                        // Number of Rooms Needed
                        int numberOfRooms = 1;
                        while (true) {
                            System.out.print("Enter Number of Rooms Needed (1 to 5): ");
                            if (scanner.hasNextInt()) {
                                numberOfRooms = scanner.nextInt();
                                scanner.nextLine();
                                if (numberOfRooms >= 1 && numberOfRooms <= 5) {
                                    break;
                                }
                                System.out.println("Error: Number of rooms must be between 1 and 5.");
                            } else {
                                System.out.println("Error: Please enter a valid number.");
                                scanner.next();
                            }
                        }
=======
                        // Select Room Type via menu
                        String room = selectRoomTypeMenu();
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
                        
                        int duration = 0;
                        while (true) {
                            System.out.print("Enter Stay Duration (1 to 30 Nights): ");
                            if (scanner.hasNextInt()) {
                                duration = scanner.nextInt();
                                scanner.nextLine(); 
                                if (duration >= 1 && duration <= 30) {
                                    break;
                                }
                                System.out.println("Error: Stay duration must be between 1 and 30 days only.");
                            } else {
                                System.out.println("Error: Please enter a valid number.");
                                scanner.next();
                            }
                        }

                        // Ticket Generation Logic
<<<<<<< HEAD
                        ListInterface<Guest> currentList = HotelDataController.getSharedGuestList();
                        int maxId = 10000000; 
=======
                        ListInterface<Guest> currentList = RegistrationAndBookingDataController.getSharedGuestList();
                        int maxId = 10000000;
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
                        
                        for (int i = 1; i <= currentList.getNumberOfEntries(); i++) {
                            int tNum = currentList.get(i).getTicketNumber();
                            if (tNum > maxId) {
                                maxId = tNum;
                            }
                        }
                        
                        int ticket = maxId + 1;
                        
<<<<<<< HEAD
                        Guest newGuest = new Guest(ticket, name, gender, contact, room, numberOfRooms, duration);
                        
                        HotelDataController.addGuest(newGuest);
                        
                        System.out.println("\nSuccessfully added! Ticket Assigned: " + ticket + " | Total Price: RM " + String.format("%.2f", newGuest.calculateTotalPrice()));
=======
                        Guest newGuest = new Guest(ticket, name, gender, contact, room, duration);
                        RegistrationAndBookingDataController.addGuest(newGuest);
                        
                        double totalPrice = getRoomRate(room) * duration;
                        System.out.println("\nSuccessfully added! Ticket Assigned: " + ticket);
                        System.out.printf("Total Estimated Cost: RM%.2f (RM%.2f x %d nights)%n", totalPrice, getRoomRate(room), duration);
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc

                        while (true) {
                            System.out.print("\nDo you want to add another guest? (Y/N): ");
                            String response = scanner.nextLine().trim().toUpperCase();
                            
                            if (response.length() == 1 && (response.charAt(0) == 'Y' || response.charAt(0) == 'N')) {
                                addAnother = response.charAt(0);
                                break;
                            }
                            System.out.println("Error: Invalid input! Please type 'Y' for Yes or 'N' for No.");
                        }

                    } while (addAnother == 'Y');
                    break;

                case 2: // Serve & Remove Next Guest
                    if (guestQueue.isEmpty()) {
                        System.out.println("The queue is currently empty.");
                    } else {
                        Guest processed = guestQueue.dequeue();
                        
                        ListInterface<Guest> masterListForServe = RegistrationAndBookingDataController.getSharedGuestList();
                        for (int i = 1; i <= masterListForServe.getNumberOfEntries(); i++) {
                            Guest g = masterListForServe.get(i);
                            if (g.getTicketNumber() == processed.getTicketNumber()) {
                                g.setStatus("Served");
                                break;
                            }
                        }
                        
                        guestQueue.clear();
                        for (int j = 1; j <= masterListForServe.getNumberOfEntries(); j++) {
                            Guest g = masterListForServe.get(j);
                            if ("Waiting".equalsIgnoreCase(g.getStatus())) {
                                guestQueue.enqueue(g);
                            }
                        }

                        System.out.println("Successfully processed and updated guest: " + processed.getFullName() + " (Ticket: " + processed.getTicketNumber() + ")");
                    }
                    break;

                case 3: // View Next Guest & Queue Status
                    System.out.println("\n===============================================================");
                    System.out.println("            QUEUE STATUS & NEXT GUEST INFO          ");
                    System.out.println("=================================================================");
                    System.out.println("Total Guests Waiting in Queue : " + guestQueue.getNumberOfEntries());
                    System.out.println("-----------------------------------------------------------------");
                    if (guestQueue.isEmpty()) {
                        System.out.println("Status: The queue is currently empty.");
                    } else {
                        Guest nextGuest = guestQueue.getFront();
                        double total = getRoomRate(nextGuest.getRoomType()) * nextGuest.getStayDuration();
                        System.out.println("Next Guest in Line:");
                        System.out.println("  > Ticket Number : " + nextGuest.getTicketNumber());
                        System.out.println("  > Full Name     : " + nextGuest.getFullName());
                        System.out.println("  > Room Type     : " + nextGuest.getRoomType());
                        System.out.println("  > Rooms Count   : " + nextGuest.getNumberOfRooms());
                        System.out.println("  > Stay Duration : " + nextGuest.getStayDuration() + " Nights");
<<<<<<< HEAD
                        System.out.println("  > Total Price   : RM " + String.format("%.2f", nextGuest.calculateTotalPrice()));
=======
                        System.out.printf("  > Total Price   : RM%.2f%n", total);
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
                        System.out.println("  > Contact No.   : " + nextGuest.getContactNumber());
                    }
                    System.out.println("===================================================================");
                    break;

                case 4: // Display All Guests
<<<<<<< HEAD
                    System.out.println("\n=================================================================================================================================");
                    System.out.println("                                                            ALL REGISTERED GUESTS                                                                ");
                    System.out.println("=================================================================================================================================");
=======
                    System.out.println("\n========================================================================================================================");
                    System.out.println("                                               ALL REGISTERED GUESTS                                                    ");
                    System.out.println("========================================================================================================================");
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
                    
                    ListInterface<Guest> allGuests = RegistrationAndBookingDataController.getSharedGuestList();
                    
                    if (allGuests.isEmpty()) {
                        System.out.println("No guest records found in the system or file.");
                    } else {
<<<<<<< HEAD
                        System.out.printf("%-10s | %-24s | %-6s | %-12s | %-18s | %-6s | %-8s | %-12s | %-10s%n", 
                                "Ticket", "Full Name", "Gender", "Contact", "Room", "Rooms", "Nights", "Total (RM)", "Status");
                        System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
                        for (int i = 1; i <= allGuests.getNumberOfEntries(); i++) {
                            Guest g = allGuests.get(i);
                            System.out.printf("%-10d | %-24s | %-6s | %-12s | %-18s | %-6d | %-8d | RM %-9.2f | %-10s%n",
                                    g.getTicketNumber(), g.getFullName(), g.getGender(), 
                                    g.getContactNumber(), g.getRoomType(), g.getNumberOfRooms(), g.getStayDuration(), g.calculateTotalPrice(), g.getStatus());
                        }
                    }
                    System.out.println("=================================================================================================================================");
=======
                        System.out.printf("%-12s | %-24s | %-6s | %-13s | %-20s | %-6s | %-13s | %-10s%n", 
                                          "Ticket", "Full Name", "Gender", "Contact", "Room", "Nights", "Total Price", "Status");
                        System.out.println("------------------------------------------------------------------------------------------------------------------------");
                        for (int i = 1; i <= allGuests.getNumberOfEntries(); i++) {
                            Guest g = allGuests.get(i);
                            double totalPrice = getRoomRate(g.getRoomType()) * g.getStayDuration();
                            System.out.printf("%-12d | %-24s | %-6s | %-13s | %-20s | %-6d | RM%-11.2f | %-10s%n",
                                              g.getTicketNumber(), g.getFullName(), g.getGender(), 
                                              g.getContactNumber(), g.getRoomType(), g.getStayDuration(), totalPrice, g.getStatus());
                        }
                    }
                    System.out.println("========================================================================================================================");
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
                    break;

                case 5: // Cancel Guest Reservation
                    System.out.println("\n--- Cancel Guest Reservation ---");
                    ListInterface<Guest> masterList = RegistrationAndBookingDataController.getSharedGuestList();
                    
                    if (masterList.isEmpty()) {
                        System.out.println("No guest records available to cancel.");
                        break;
                    }

                    System.out.print("Enter Ticket Number to cancel (e.g., 10000001): ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid input! Please enter a valid integer Ticket Number: ");
                        scanner.next();
                    }
                    int cancelTicket = scanner.nextInt();
                    scanner.nextLine();
                    
                    boolean found = false;
                    for (int i = 1; i <= masterList.getNumberOfEntries(); i++) {
                        Guest g = masterList.get(i);
                        if (g.getTicketNumber() == cancelTicket) {
                            found = true;
                            System.out.println("\nFound Guest: " + g.getFullName() + " | Room: " + g.getRoomType());
                            
                            boolean confirmed = false;
                            while (true) {
                                System.out.print("Are you sure you want to delete/cancel this reservation? (Y/N): ");
                                String confirm = scanner.nextLine().trim().toUpperCase();
                                
                                if (confirm.length() == 1 && (confirm.charAt(0) == 'Y' || confirm.charAt(0) == 'N')) {
                                    confirmed = (confirm.charAt(0) == 'Y');
                                    break;
                                }
                                System.out.println("Error: Invalid input! Please type 'Y' for Yes or 'N' for No.");
                            }
                            
                            if (confirmed) {
                                masterList.remove(i); 
                                
                                guestQueue.clear();
                                for (int j = 1; j <= masterList.getNumberOfEntries(); j++) {
                                    guestQueue.enqueue(masterList.get(j));
                                }
                                
                                System.out.println("Success: Reservation " + cancelTicket + " for " + g.getFullName() + " has been cancelled.");
                            } else {
                                System.out.println("Deletion cancelled.");
                            }
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Error: Ticket number '" + cancelTicket + "' not found.");
                    }
                    break;

              case 6: // Update / Edit Guest Details
                    System.out.println("\n--- Update Guest Details ---");
                    ListInterface<Guest> editList = RegistrationAndBookingDataController.getSharedGuestList();
                    
                    if (editList.isEmpty()) {
                        System.out.println("No guest records available to update.");
                        break;
                    }

                    System.out.print("Enter Ticket Number of the guest to update (e.g., 10000001): ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid input! Please enter a valid integer Ticket Number: ");
                        scanner.next();
                    }
                    int editTicket = scanner.nextInt();
                    scanner.nextLine();
                    
                    boolean editFound = false;
                    for (int i = 1; i <= editList.getNumberOfEntries(); i++) {
                        Guest g = editList.get(i);
                        if (g.getTicketNumber() == editTicket) {
                            if (!"Waiting".equalsIgnoreCase(g.getStatus())) {
                                System.out.println("Error: Guest with ticket '" + editTicket + "' has already been served and cannot be edited.");
                                editFound = true;
                                break;
                            }
                            
                            editFound = true;
                            System.out.println("\nFound Guest: " + g.getFullName() + " | Current Room: " + g.getRoomType());
                            System.out.println("Note: Guest Full Name cannot be changed.");
                            System.out.println("What would you like to update?");
                            System.out.println("1. Update Contact Number");
                            System.out.println("2. Update Room Type");
                            System.out.println("3. Update Number of Rooms");
                            System.out.println("4. Update Stay Duration");
                            System.out.print("Choose option (1-4): ");
                            
                            while (!scanner.hasNextInt()) {
                                System.out.print("Invalid input! Please enter a number between 1 and 4: ");
                                scanner.next();
                            }
                            int updateChoice = scanner.nextInt();
                            scanner.nextLine(); 
                            
                            if (updateChoice == 1) {
                                System.out.print("Enter new contact number: ");
                                String newContact = scanner.nextLine().trim();
                                if (newContact.matches("^01[0-14-9]-[0-9]{7,8}$")) {
                                    g.setContactNumber(newContact);
                                    System.out.println("Contact number updated successfully!");
                                } else {
                                    System.out.println("Invalid format! Update cancelled.");
                                    break;
                                }
                            } else if (updateChoice == 2) {
<<<<<<< HEAD
                                System.out.println("Select New Room Type:");
                                System.out.println("  1. Single (RM 150.00 / night)");
                                System.out.println("  2. Double (RM 250.00 / night)");
                                System.out.println("  3. Suite (RM 500.00 / night)");
                                System.out.println("  4. Presidential Suite (RM 1200.00 / night)");
                                System.out.print("Enter choice (1-4): ");
                                
                                if (scanner.hasNextInt()) {
                                    int roomChoice = scanner.nextInt();
                                    scanner.nextLine();
                                    if (roomChoice == 1) g.setRoomType("Single");
                                    else if (roomChoice == 2) g.setRoomType("Double");
                                    else if (roomChoice == 3) g.setRoomType("Suite");
                                    else if (roomChoice == 4) g.setRoomType("Presidential Suite");
                                    else {
                                        System.out.println("Invalid room choice! Update cancelled.");
                                        break;
                                    }
                                    System.out.println("Room type updated successfully!");
                                } else {
                                    scanner.next();
                                    System.out.println("Invalid input! Update cancelled.");
                                    break;
                                }
                            } else if (updateChoice == 3) {
                                int newRooms = 0;
                                while (true) {
                                    System.out.print("Enter new number of rooms (1 to 5): ");
                                    if (scanner.hasNextInt()) {
                                        newRooms = scanner.nextInt();
                                        scanner.nextLine();
                                        if (newRooms >= 1 && newRooms <= 5) {
                                            break;
                                        }
                                        System.out.println("Error: Number of rooms must be between 1 and 5.");
                                    } else {
                                        System.out.println("Error: Please enter a valid number.");
                                        scanner.next();
                                    }
=======
                                String newRoom = selectRoomTypeMenu();
                                g.setRoomType(newRoom);
                                double newTotal = getRoomRate(newRoom) * g.getStayDuration();
                                System.out.println("Room type updated successfully!");
                                System.out.printf("New Total Price: RM%.2f%n", newTotal);
                            } else if (updateChoice == 3) {
                                System.out.print("Enter new stay duration (1 to 30): ");
                                int newDuration = scanner.nextInt();
                                scanner.nextLine();
                                if (newDuration >= 1 && newDuration <= 30) {
                                    g.setStayDuration(newDuration);
                                    double newTotal = getRoomRate(g.getRoomType()) * newDuration;
                                    System.out.println("Stay duration updated successfully!");
                                    System.out.printf("New Total Price: RM%.2f%n", newTotal);
                                } else {
                                    System.out.println("Invalid duration! Update cancelled.");
                                    break;
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
                                }
                                g.setNumberOfRooms(newRooms);
                                System.out.println("Number of rooms updated successfully!");
                            } else if (updateChoice == 4) {
                                int newDuration = 0;
                                while (true) {
                                    System.out.print("Enter new stay duration (1 to 30 Nights): ");
                                    if (scanner.hasNextInt()) {
                                        newDuration = scanner.nextInt();
                                        scanner.nextLine();
                                        if (newDuration >= 1 && newDuration <= 30) {
                                            break;
                                        }
                                        System.out.println("Error: Stay duration must be between 1 and 30 days only.");
                                    } else {
                                        System.out.println("Error: Please enter a valid number.");
                                        scanner.next();
                                    }
                                }
                                g.setStayDuration(newDuration);
                                System.out.println("Stay duration updated successfully!");
                            } else {
                                System.out.println("Invalid option choice.");
                                break;
                            }
                            
                            guestQueue.clear();
                            for (int j = 1; j <= editList.getNumberOfEntries(); j++) {
                                Guest tempG = editList.get(j);
                                if ("Waiting".equalsIgnoreCase(tempG.getStatus())) {
                                    guestQueue.enqueue(tempG);
                                }
                            }
                            break;
                        }
                    }
                    
                    if (!editFound) {
                        System.out.println("Error: Ticket number '" + editTicket + "' not found.");
                    }
                    break;
              case 7:
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please choose between 1 and 7.");
            }
            
            if (choice != 7) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } while (choice != 7);
    }
}