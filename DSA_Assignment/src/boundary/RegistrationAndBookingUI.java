package boundary;

import control.BookingDataController;
import entity.Guest;
import utility.ClearScreen;
import utility.Header;

import java.util.Scanner;

public class RegistrationAndBookingUI {
    private Scanner scanner = new Scanner(System.in);
    
    public void bookingModule() {
        int choice;
        do {
            ClearScreen.clear();
            Header.printHeader();
            
            System.out.println("=== Walk-In Registration & Standard Booking ===");
            System.out.println("1. Register & Enqueue New Guest");
            System.out.println("2. Serve Next Guest");
            System.out.println("3. View Next Guest & Queue Status");        
            System.out.println("4. Display All Guests");    
            System.out.println("5. Cancel Guest Reservation");                
            System.out.println("6. Update / Edit Guest Details");                
            System.out.println("7. Main Menu");                                                                
            System.out.print("Please choose an option (1-7): ");
            
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input! Please enter a number between 1 and 7: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
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
                                if (roomChoice == 1) { room = "Single"; break; }
                                else if (roomChoice == 2) { room = "Double"; break; }
                                else if (roomChoice == 3) { room = "Suite"; break; }
                                else if (roomChoice == 4) { room = "Presidential Suite"; break; }
                            } else {
                                scanner.next();
                            }
                            System.out.println("Error: Invalid choice! Please select a number between 1 and 4.");
                        }

                        int numberOfRooms = 1;
                        while (true) {
                            System.out.print("Enter Number of Rooms Needed (1 to 5): ");
                            if (scanner.hasNextInt()) {
                                numberOfRooms = scanner.nextInt();
                                scanner.nextLine();
                                if (numberOfRooms >= 1 && numberOfRooms <= 5) break;
                            } else {
                                scanner.next();
                            }
                            System.out.println("Error: Number of rooms must be between 1 and 5.");
                        }
                        
                        int duration = 0;
                        while (true) {
                            System.out.print("Enter Stay Duration (1 to 30 Nights): ");
                            if (scanner.hasNextInt()) {
                                duration = scanner.nextInt();
                                scanner.nextLine(); 
                                if (duration >= 1 && duration <= 30) break;
                            } else {
                                scanner.next();
                            }
                            System.out.println("Error: Stay duration must be between 1 and 30 days only.");
                        }

                        int ticket = BookingDataController.generateNextTicketNumber();
                        Guest newGuest = new Guest(ticket, name, gender, contact, room, numberOfRooms, duration);
                        BookingDataController.addGuest(newGuest);
                        
                        System.out.println("\nSuccessfully added! Ticket Assigned: " + ticket + " | Total Price: RM " + String.format("%.2f", newGuest.calculateTotalPrice()));

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

                case 2:
                    if (BookingDataController.isQueueEmpty()) {
                        System.out.println("/n/nThe queue is currently empty.");
                    } else {
                        Guest processed = BookingDataController.serveNextGuest();
                        System.out.println("/n/nSuccessfully processed : " + processed.getFullName() + " (Ticket: " + processed.getTicketNumber() + ")");
                    }
                    break;

                case 3:
                    System.out.println("\n===============================================================");
                    System.out.println("            QUEUE STATUS & NEXT GUEST INFO         ");
                    System.out.println("=================================================================");
                    System.out.println("Total Guests Waiting in Queue : " + BookingDataController.getQueueSize());
                    System.out.println("-----------------------------------------------------------------");
                    if (BookingDataController.isQueueEmpty()) {
                        System.out.println("Status: The queue is currently empty.");
                    } else {
                        Guest nextGuest = BookingDataController.getNextQueueGuest();
                        System.out.println("Next Guest in Line:");
                        System.out.println("  > Ticket Number : " + nextGuest.getTicketNumber());
                        System.out.println("  > Full Name     : " + nextGuest.getFullName());
                        System.out.println("  > Room Type     : " + nextGuest.getRoomType());
                        System.out.println("  > Rooms Count   : " + nextGuest.getNumberOfRooms());
                        System.out.println("  > Stay Duration : " + nextGuest.getStayDuration() + " Nights");
                        System.out.println("  > Total Price   : RM " + String.format("%.2f", nextGuest.calculateTotalPrice()));
                        System.out.println("  > Contact No.   : " + nextGuest.getContactNumber());
                    }
                    System.out.println("===================================================================");
                    break;

                case 4:
                    System.out.println("\n=================================================================================================================================");
                    System.out.println("                                                              ALL REGISTERED GUESTS                                                               ");
                    System.out.println("=================================================================================================================================");
                    
                    if (BookingDataController.hasNoGuests()) {
                        System.out.println("No guest records found in the system or file.");
                    } else {
                        System.out.printf("%-10s | %-24s | %-6s | %-12s | %-18s | %-6s | %-8s | %-12s | %-10s%n", 
                                "Ticket", "Full Name", "Gender", "Contact", "Room", "Rooms", "Nights", "Total (RM)", "Status");
                        System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
                        for (int i = 1; i <= BookingDataController.getTotalGuestCount(); i++) {
                            Guest g = BookingDataController.getGuestAt(i);
                            System.out.printf("%-10d | %-24s | %-6s | %-12s | %-18s | %-6d | %-8d | RM %-9.2f | %-10s%n",
                                    g.getTicketNumber(), g.getFullName(), g.getGender(), 
                                    g.getContactNumber(), g.getRoomType(), g.getNumberOfRooms(), g.getStayDuration(), g.calculateTotalPrice(), g.getStatus());
                        }
                    }
                    System.out.println("=================================================================================================================================");
                    break;

                case 5:
                    System.out.println("\n--- Cancel Guest Reservation ---");
                    if (BookingDataController.hasNoGuests()) {
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
                    
                    Guest targetGuest = BookingDataController.findGuestByTicket(cancelTicket);
                    if (targetGuest == null) {
                        System.out.println("Error: Ticket number '" + cancelTicket + "' not found.");
                        break;
                    }

                    System.out.println("\nFound Guest: " + targetGuest.getFullName() + " | Room: " + targetGuest.getRoomType());
                    
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
                        BookingDataController.cancelReservation(cancelTicket);
                        System.out.println("Success: Reservation " + cancelTicket + " for " + targetGuest.getFullName() + " has been cancelled.");
                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                    break;

                case 6:
                    System.out.println("\n--- Update Guest Details ---");
                    System.out.print("Enter Ticket Number of the guest to update (e.g., 10000001): ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid input! Please enter a valid integer Ticket Number: ");
                        scanner.next();
                    }
                    int editTicket = scanner.nextInt();
                    scanner.nextLine();
                    
                    Guest g = BookingDataController.findGuestByTicket(editTicket);
                    if (g == null) {
                        System.out.println("Error: Ticket number '" + editTicket + "' not found.");
                        break;
                    }

                    if (!"Waiting".equalsIgnoreCase(g.getStatus())) {
                        System.out.println("Error: Guest with ticket '" + editTicket + "' has already been served and cannot be edited.");
                        break;
                    }
                    
                    System.out.println("\nFound Guest: " + g.getFullName() + " | Room: " + g.getRoomType());
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
                        }
                    } else if (updateChoice == 2) {
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
                        }
                    } else if (updateChoice == 3) {
                        int newRooms = 0;
                        while (true) {
                            System.out.print("Enter new number of rooms (1 to 5): ");
                            if (scanner.hasNextInt()) {
                                newRooms = scanner.nextInt();
                                scanner.nextLine();
                                if (newRooms >= 1 && newRooms <= 5) break;
                            } else {
                                scanner.next();
                            }
                            System.out.println("Error: Number of rooms must be between 1 and 5.");
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
                                if (newDuration >= 1 && newDuration <= 30) break;
                            } else {
                                scanner.next();
                            }
                            System.out.println("Error: Stay duration must be between 1 and 30 days only.");
                        }
                        g.setStayDuration(newDuration);
                        System.out.println("Stay duration updated successfully!");
                    } else {
                        System.out.println("Invalid option choice.");
                    }
                    
                    BookingDataController.refreshQueueFromList();
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