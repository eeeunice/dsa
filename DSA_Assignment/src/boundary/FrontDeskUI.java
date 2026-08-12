package boundary;

import control.FrontDeskManager;
import entity.Guest;
import java.util.Scanner;
import utility.ClearScreen;
import utility.Header;

public class FrontDeskUI {

    private FrontDeskManager manager;
    private Scanner scanner;

    public FrontDeskUI() {
        manager = new FrontDeskManager();
        scanner = new Scanner(System.in);
    }

    public void frontDeskMenu() {

        int choice;

        do {

            ClearScreen.clear();
            Header.printHeader();

            System.out.println("=== FRONT DESK SERVICE ===");
            System.out.println("1. Search Guest");
            System.out.println("2. Check Guest Status");
            System.out.println("3. View Guest Details");
            System.out.println("4. Main Menu");
            System.out.print("Please choose an option (1-4): ");

            while (!scanner.hasNextInt()) {
                System.out.print(
                    "Invalid input! Please enter a number between 1 and 4: "
                );
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    searchGuest();
                    break;

                case 2:
                    checkGuestStatus();
                    break;

                case 3:
                    viewGuestDetails();
                    break;

                case 4:
                    System.out.println("Returning to main menu...");
                    break;

                default:
                    System.out.println(
                        "Invalid choice. Please choose between 1 and 4."
                    );
            }

            if (choice != 4) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }

        } while (choice != 4);
    }

    private void searchGuest() {

        System.out.println("\n--- Search Guest ---");

        System.out.print("Enter Ticket Number: ");

        while (!scanner.hasNextInt()) {
            System.out.print(
                "Invalid input! Please enter a valid ticket number: "
            );
            scanner.next();
        }

        int ticketNumber = scanner.nextInt();
        scanner.nextLine();

        Guest guest = manager.searchGuest(ticketNumber);

        if (guest != null) {

            System.out.println("\nGuest Found!");
            System.out.println("----------------------------");
            System.out.println("Ticket Number : "
                    + guest.getTicketNumber());
            System.out.println("Full Name     : "
                    + guest.getFullName());

        } else {

            System.out.println(
                "\nGuest not found."
            );
        }
    }

    private void checkGuestStatus() {

        System.out.println("\n--- Check Guest Status ---");

        System.out.print("Enter Ticket Number: ");

        while (!scanner.hasNextInt()) {
            System.out.print(
                "Invalid input! Please enter a valid ticket number: "
            );
            scanner.next();
        }

        int ticketNumber = scanner.nextInt();
        scanner.nextLine();

        String status = manager.getGuestStatus(ticketNumber);

        if (status != null) {

            System.out.println(
                "\nGuest Status: " + status
            );

        } else {

            System.out.println(
                "\nGuest not found."
            );
        }
    }

    private void viewGuestDetails() {

        System.out.println("\n--- View Guest Details ---");

        System.out.print("Enter Ticket Number: ");

        while (!scanner.hasNextInt()) {
            System.out.print(
                "Invalid input! Please enter a valid ticket number: "
            );
            scanner.next();
        }

        int ticketNumber = scanner.nextInt();
        scanner.nextLine();

        Guest guest = manager.searchGuest(ticketNumber);

        if (guest != null) {

            System.out.println("\n==============================");
            System.out.println("        GUEST DETAILS");
            System.out.println("==============================");

            System.out.println("Ticket Number : "
                    + guest.getTicketNumber());

            System.out.println("Full Name     : "
                    + guest.getFullName());

            System.out.println("Gender        : "
                    + guest.getGender());

            System.out.println("Contact       : "
                    + guest.getContactNumber());

            System.out.println("Room Type     : "
                    + guest.getRoomType());

            System.out.println("Stay Duration : "
                    + guest.getStayDuration()
                    + " Nights");

            System.out.println("Status        : "
                    + guest.getStatus());

            System.out.println("==============================");

        } else {

            System.out.println(
                "\nGuest not found."
            );
        }
    }
}