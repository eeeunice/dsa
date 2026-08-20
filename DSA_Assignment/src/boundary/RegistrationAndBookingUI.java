package boundary;

import control.BookingDataController;

import java.util.Scanner;

public class RegistrationAndBookingUI {
    private Scanner scanner = new Scanner(System.in);
    
    public void bookingModule() {
        int choice;
        do {
            BookingDataController.clearScreen();
            BookingDataController.printHeader();
            
            System.out.println(utility.Header.PURPLE + "=================== Walk-In Registration & Standard Booking ===================" + utility.Header.RESET);
            System.out.println("1. Register & Enqueue New Guest");
            System.out.println("2. Serve Next Guest");
            System.out.println("3. View Next Guest & Queue Status");        
            System.out.println("4. Display All Guests");    
            System.out.println("5. Cancel Guest Reservation");                
            System.out.println("6. Update / Edit Guest Details");                
            System.out.println("7. Exit to Main Menu");  
            System.out.println(utility.Header.PURPLE + "  ==========================================================================" + utility.Header.RESET);
            
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

                        int ticket = BookingDataController.registerGuest(name, gender, contact, room, numberOfRooms, duration);
                        double totalPrice = BookingDataController.getGuestTotalPrice(ticket);
                        
                        System.out.println("\nSuccessfully added! Ticket Assigned: " + ticket + " | Total Price: RM " + String.format("%.2f", totalPrice));

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
                        System.out.println("\n\nThe queue is currently empty.");
                    } else {
                        String processedInfo = BookingDataController.serveNextGuestInfo();
                        System.out.println("\n\nSuccessfully processed : " + processedInfo);
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
                        System.out.println("Next Guest in Line:");
                        System.out.println("  > Ticket Number : " + BookingDataController.getNextQueueGuestTicketNumber());
                        System.out.println("  > Full Name     : " + BookingDataController.getNextQueueGuestFullName());
                        System.out.println("  > Room Type     : " + BookingDataController.getNextQueueGuestRoomType());
                        System.out.println("  > Rooms Count   : " + BookingDataController.getNextQueueGuestNumberOfRooms());
                        System.out.println("  > Stay Duration : " + BookingDataController.getNextQueueGuestStayDuration() + " Nights");
                        System.out.println("  > Total Price   : RM " + String.format("%.2f", BookingDataController.getNextQueueGuestTotalPrice()));
                        System.out.println("  > Contact No.   : " + BookingDataController.getNextQueueGuestContactNumber());
                    }
                    System.out.println("===================================================================");
                    break;

                case 4:
                    System.out.println("\n=================================================================================================================================");
                    System.out.println("                                                              ALL REGISTERED GUESTS                                                               ");
                    System.out.println("=================================================================================================================================");
                    BookingDataController.displayAllGuests();
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
                    
                    if (!BookingDataController.guestExists(cancelTicket)) {
                        System.out.println("Error: Ticket number '" + cancelTicket + "' not found.");
                        break;
                    }

                    String targetName = BookingDataController.getGuestName(cancelTicket);
                    String targetRoom = BookingDataController.getGuestRoomType(cancelTicket);
                    System.out.println("\nFound Guest: " + targetName + " | Room: " + targetRoom);
                    
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
                        System.out.println("Success: Reservation " + cancelTicket + " for " + targetName + " has been cancelled.");
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
                    
                    if (!BookingDataController.guestExists(editTicket)) {
                        System.out.println("Error: Ticket number '" + editTicket + "' not found.");
                        break;
                    }

                    if (!"Waiting".equalsIgnoreCase(BookingDataController.getGuestStatus(editTicket))) {
                        System.out.println("Error: Guest with ticket '" + editTicket + "' has already been served and cannot be edited.");
                        break;
                    }
                    
                    System.out.println("\nFound Guest: " + BookingDataController.getGuestName(editTicket) + " | Room: " + BookingDataController.getGuestRoomType(editTicket));
                    System.out.println("Note: Guest Full Name cannot be changed.");
                    System.out.println("What would you like to update?");
                    System.out.println("1. Update Contact Number");
                    System.out.println("2. Update Room Type");
                    System.out.println("3. Update Number of Rooms");
                    System.out.println("4. Update Stay Duration");
                    System.out.println("5. Back to Menu"); // Added Option 5
                    System.out.print("Choose option (1-5): "); // Updated range
                    
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid input! Please enter a number between 1 and 5: ");
                        scanner.next();
                    }
                    int updateChoice = scanner.nextInt();
                    scanner.nextLine(); 
                    
                    if (updateChoice == 5) {
                        System.out.println("Returning to booking menu...");
                        break; // Exits Case 6 back to the main module loop
                    } else if (updateChoice == 1) {
                        System.out.print("Enter new contact number: ");
                        String newContact = scanner.nextLine().trim();
                        if (newContact.matches("^01[0-14-9]-[0-9]{7,8}$")) {
                            BookingDataController.updateGuestContact(editTicket, newContact);
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
                            String selectedRoom = null;
                            if (roomChoice == 1) selectedRoom = "Single";
                            else if (roomChoice == 2) selectedRoom = "Double";
                            else if (roomChoice == 3) selectedRoom = "Suite";
                            else if (roomChoice == 4) selectedRoom = "Presidential Suite";
                            
                            if (selectedRoom != null) {
                                BookingDataController.updateGuestRoomType(editTicket, selectedRoom);
                                System.out.println("Room type updated successfully!");
                            } else {
                                System.out.println("Invalid room choice! Update cancelled.");
                            }
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
                        BookingDataController.updateGuestNumberOfRooms(editTicket, newRooms);
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
                        BookingDataController.updateGuestStayDuration(editTicket, newDuration);
                        System.out.println("Stay duration updated successfully!");
                    } else {
                        System.out.println("Invalid option choice.");
                    }
                    
                    break;
                    
                case 7:
                    System.out.println(utility.Header.GREEN + "\n  Returning to Main Menu..." + utility.Header.RESET);
                    break;

                default:
                    System.out.println("Invalid choice. Please choose between 1 to 7.");
            }
            
            if (choice != 7) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } while (choice != 7);
    }
}