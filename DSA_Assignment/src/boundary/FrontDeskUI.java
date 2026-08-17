package boundary;

import control.FrontDeskManager;
import entity.Guest;
import adt.ListInterface;
import java.util.Scanner;
import utility.ClearScreen;
import utility.Header;

public class FrontDeskUI{
    private FrontDeskManager manager;
    private Scanner scanner;

    public FrontDeskUI(){
        manager = new FrontDeskManager();
        scanner = new Scanner(System.in);
    }

    public void frontDeskMenu(){
        int choice;

        do{
            ClearScreen.clear();
            Header.printHeader();

            System.out.println("=== FRONT DESK SERVICE ===");
            System.out.println("1. Search / View Guest");
            System.out.println("2. Check-In Guest");
            System.out.println("3. Check-Out Guest");
            System.out.println("4. Update Guest");
            System.out.println("5. Delete Guest");
            System.out.println("6. View Guest List");
            System.out.println("7. Main Menu");
            System.out.print("Please choose an option (1-7): ");

            while(!scanner.hasNextInt()){
                System.out.print(
                    "Invalid input! Please enter a number between 1 and 7: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    searchAndViewGuest();
                    break;
                case 2:
                    checkInGuest();
                    break;
                case 3:
                    checkOutGuest();
                    break;
                case 4:
                    updateGuest();
                    break;
                case 5:
                    deleteGuest();
                    break;
                case 6:
                    viewGuestList();
                    break;
                case 7:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose between 1 and 7.");
            }

            if(choice != 7){
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } while(choice != 7);
    }

    // =========================================================
    // 1. SEARCH / VIEW GUEST
    // =========================================================

    private void searchAndViewGuest(){
        System.out.println("\n--- Search / View Guest ---");
        int ticketNumber = getTicketNumber();
        Guest guest = manager.searchGuest(ticketNumber);

        if(guest != null){
            displayGuestDetails(guest);
        }else{
            System.out.println("\nGuest not found.");
        }
    }

    // =========================================================
    // 2. CHECK-IN
    // =========================================================

    private void checkInGuest(){
        System.out.println("\n--- Check-In Guest ---");
        int ticketNumber = getTicketNumber();
        Guest guest = manager.searchGuest(ticketNumber);

        if(guest == null){
            System.out.println("\nGuest not found.");
            return;
        }

        System.out.println("\nGuest Found:");
        System.out.println("Name   : " + guest.getFullName());
        System.out.println("Room   : " + guest.getRoomType());
        System.out.println("Status : " + guest.getStatus());

        if("Checked-In".equalsIgnoreCase(guest.getStatus())){
            System.out.println("\nGuest is already checked-in.");
        }else if("Checked-Out".equalsIgnoreCase(guest.getStatus())){
            System.out.println("\nGuest has already checked-out.");
        }else{
            if(manager.checkInGuest(ticketNumber)){
                System.out.println(
                    "\nGuest successfully checked-in."
                );
            }
        }
    }

    // =========================================================
    // 3. CHECK-OUT
    // =========================================================
    private void checkOutGuest(){
        System.out.println("\n--- Check-Out Guest ---");
        int ticketNumber = getTicketNumber();
        Guest guest = manager.searchGuest(ticketNumber);

        if(guest == null){
            System.out.println("\nGuest not found.");
            return;
        }

        System.out.println("\nGuest Found:");
        System.out.println("Name   : " + guest.getFullName());
        System.out.println("Room   : " + guest.getRoomType());
        System.out.println("Status : " + guest.getStatus());

        if("Checked-Out".equalsIgnoreCase(guest.getStatus())){
            System.out.println("\nGuest is already checked-out.");
        }else if(!"Checked-In".equalsIgnoreCase(guest.getStatus())){
            System.out.println("\nGuest must be checked-in before checking-out.");
        }else{
            if(manager.checkOutGuest(ticketNumber)){
                System.out.println("\nGuest successfully checked-out.");
            }
        }
    }

    // =========================================================
    // 4. UPDATE GUEST
    // =========================================================
    private void updateGuest() {
        System.out.println("\n--- Update Guest ---");
        int ticketNumber = getTicketNumber();
        Guest guest = manager.searchGuest(ticketNumber);

        if (guest == null){
            System.out.println("\nGuest not found.");
            return;
        }

        System.out.println("\nGuest Found:");
        System.out.println("Name   : " + guest.getFullName());
        System.out.println("Room   : " + guest.getRoomType());
        System.out.println("Status : " + guest.getStatus());

        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Contact Number");
        System.out.println("2. Room Type");
        System.out.println("3. Stay Duration");
        System.out.println("4. Cancel");

        System.out.print("Please choose an option (1-4): ");

        while(!scanner.hasNextInt()){
            System.out.print("Invalid input! Please enter a number between 1 and 4: ");
            scanner.next();
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch(choice){
            case 1:
                System.out.print("Enter new contact number: ");
                String contact = scanner.nextLine().trim();

                if(contact.matches("^01[0-14-9]-[0-9]{7,8}$")){
                    manager.updateContact(ticketNumber, contact);
                    System.out.println("\nContact number updated successfully.");
                }else{
                    System.out.println("\nInvalid contact number format.");
                }
                break;

            case 2:
                System.out.println("Available Room Types: Single / Double / Suite / Presidential Suite");
                System.out.print("Enter new room type: ");
                String room = scanner.nextLine().trim();

                if(room.equalsIgnoreCase("Single") ||
                    room.equalsIgnoreCase("Double") ||
                    room.equalsIgnoreCase("Suite") ||
                    room.equalsIgnoreCase("Presidential Suite")){

                    if (room.equalsIgnoreCase("Presidential Suite")){
                        room = "Presidential Suite";
                    }else{
                        room = room.substring(0, 1).toUpperCase()
                            + room.substring(1).toLowerCase();
                    }
                    manager.updateRoomType(ticketNumber, room);

                    System.out.println("\nRoom type updated successfully.");
                }else{
                    System.out.println("\nInvalid room type.");
                }
                break;

            case 3:
                System.out.print("Enter new stay duration (1-30 nights): ");

                while(!scanner.hasNextInt()){
                    System.out.print("Invalid input! Please enter a number: ");
                    scanner.next();
                }
                int duration = scanner.nextInt();
                scanner.nextLine();

                if (duration >= 1 && duration <= 30){
                    manager.updateStayDuration(ticketNumber,duration);
                    System.out.println("\nStay duration updated successfully.");
                }else{
                    System.out.println("\nStay duration must be between 1 and 30 nights.");
                }
                break;

            case 4:
                System.out.println("Update cancelled.");
                break;

            default:
                System.out.println("Invalid option.");
        }
    }

    // =========================================================
    // 5. DELETE GUEST
    // =========================================================
    private void deleteGuest(){
        System.out.println("\n--- Delete Guest ---");
        int ticketNumber = getTicketNumber();
        Guest guest = manager.searchGuest(ticketNumber);

        if(guest == null){
            System.out.println("\nGuest not found.");
            return;
        }

        System.out.println("\nGuest Found:");
        System.out.println("Name   : " + guest.getFullName());
        System.out.println("Room   : " + guest.getRoomType());
        System.out.println("Status : " + guest.getStatus());

        System.out.print("\nAre you sure you want to delete this guest? (Y/N): ");

        String confirmation = scanner.nextLine().trim().toUpperCase();

        if(confirmation.equals("Y")){
            if (manager.deleteGuest(ticketNumber)) {
                System.out.println("\nGuest record deleted successfully.");
            }else{
                System.out.println("\nUnable to delete guest.");
            }
        }else{
            System.out.println("\nDeletion cancelled.");
        }
    }

    // =========================================================
    // 6. VIEW GUEST LIST
    // =========================================================
    private void viewGuestList(){
        System.out.println("\n--- Guest List ---");
        ListInterface<Guest> guestList = manager.getAllGuests();
        System.out.println("\nTotal Number of Customers: " + manager.getNumberOfGuests());
        System.out.println("==========================================================================");

        if(guestList.isEmpty()){
            System.out.println("No guest records found.");
        }else{
            System.out.printf(
                "%-12s | %-20s | %-6s | %-15s | %-20s | %-8s | %-12s%n",
                "Ticket",
                "Name",
                "Gender",
                "Contact",
                "Room",
                "Nights",
                "Status"
            );

            System.out.println("--------------------------------------------------------------------------");

            for(int i = 1; i <= guestList.getNumberOfEntries(); i++){
                Guest guest = guestList.get(i);

                System.out.printf(
                    "%-12d | %-20s | %-6s | %-15s | %-20s | %-8d | %-12s%n",
                    guest.getTicketNumber(),
                    guest.getFullName(),
                    guest.getGender(),
                    guest.getContactNumber(),
                    guest.getRoomType(),
                    guest.getStayDuration(),
                    guest.getStatus()
                );
            }
        }

        System.out.println("==========================================================================");
    }

    // =========================================================
    // HELPER METHOD - GET TICKET NUMBER
    // =========================================================
    private int getTicketNumber(){
        System.out.print("Enter Ticket Number: ");

        while(!scanner.hasNextInt()){
            System.out.print("Invalid input! Please enter a valid ticket number: ");
            scanner.next();
        }
        
        int ticketNumber = scanner.nextInt();
        scanner.nextLine();
        return ticketNumber;
    }

    // =========================================================
    // HELPER METHOD - DISPLAY GUEST
    // =========================================================
    private void displayGuestDetails(Guest guest){
        System.out.println("\n==============================");
        System.out.println("        GUEST DETAILS");
        System.out.println("==============================");
        System.out.println("Ticket Number : " + guest.getTicketNumber());
        System.out.println("Full Name     : " + guest.getFullName());
        System.out.println("Gender        : " + guest.getGender());
        System.out.println("Contact       : " + guest.getContactNumber());
        System.out.println("Room Type     : " + guest.getRoomType());
        System.out.println("Stay Duration : " + guest.getStayDuration() + " Nights");
        System.out.println("Status        : " + guest.getStatus());
        System.out.println("==============================");
    }
}