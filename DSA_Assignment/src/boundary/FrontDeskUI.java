package boundary;

import control.FrontDeskController;
import java.util.Scanner;

public class FrontDeskUI {

    private FrontDeskController manager;
    private Scanner scanner;

    public FrontDeskUI() {
        manager = FrontDeskController.getInstance();
        scanner = new Scanner(System.in);
    }

    // =========================================================
    // FRONT DESK MENU
    // =========================================================

    public void frontDeskMenu() {

        int choice;

        do {
            displayHousekeepingNotifications();

            System.out.println(utility.Header.PURPLE + "=================== FRONT DESK SERVICE ===================" + utility.Header.RESET);
            System.out.println("1. Search / View Guest");
            System.out.println("2. Check-In Guest");
            System.out.println("3. Check-Out Guest");
            System.out.println("4. Update Guest");
            System.out.println("5. Delete Guest");
            System.out.println("6. View Guest List");
            System.out.println("7. View Lost & Found Items");
            System.out.println("8. Exit to Main Menu");
            System.out.println(utility.Header.PURPLE + "  ==========================================================================" + utility.Header.RESET);

            System.out.print("Please choose an option (1-8): ");

            while (!scanner.hasNextInt()) {

                System.out.print(
                        "Invalid input! Please enter a number between 1 and 7: "
                );

                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

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
                    displayLostAndFound();
                    break;

                case 8:
                    System.out.println(utility.Header.GREEN + "\n  Returning to Main Menu..." + utility.Header.RESET);
                    break;

                default:
                    System.out.println("\nInvalid choice. Please choose between 1 to 8.");
            }

            if (choice != 8) {

                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }

        } while (choice != 8);
    }

    // =========================================================
    // 1. SEARCH / VIEW GUEST
    // =========================================================

    private void searchAndViewGuest() {

        System.out.println("\n--- Search / View Guest ---");

        int ticketNumber = getTicketNumber();

        String[] guest = manager.getGuestDetails(ticketNumber);

        if (guest == null) {

            System.out.println("\nGuest not found.");

        } else {

            displayGuestDetails(guest);
        }
    }

    // =========================================================
    // 2. CHECK-IN GUEST
    // =========================================================

    private void checkInGuest() {

        System.out.println("\n--- Check-In Guest ---");

        int ticketNumber = getTicketNumber();

        String[] guest = manager.getGuestDetails(ticketNumber);

        if (guest == null) {

            System.out.println("\nGuest not found.");
            return;
        }

        // =====================================================
        // CUSTOMER INFORMATION
        // =====================================================

        System.out.println(
                "\n========== CUSTOMER INFORMATION =========="
        );

        System.out.println(
                "Ticket Number : " + guest[0]
        );

        System.out.println(
                "Name          : " + guest[1]
        );

        System.out.println(
                "Gender        : " + guest[2]
        );

        System.out.println(
                "Contact       : " + guest[3]
        );

        System.out.println(
                "Room Type     : " + guest[4]
        );

        System.out.println(
                "Rooms         : " + guest[5]
        );

        System.out.println(
                "Stay Duration : " + guest[6] + " night(s)"
        );

        System.out.println(
                "Status        : " + guest[7]
        );

        // =====================================================
        // PRICE
        // =====================================================

        double basePrice =
                manager.getBasePrice(ticketNumber);

        double serviceCharge =
                manager.getServiceCharge(ticketNumber);

        double finalPrice =
                manager.getFinalPrice(ticketNumber);

        System.out.println(
                "------------------------------------------"
        );

        System.out.printf(
                "Base Price    : RM %.2f%n",
                basePrice
        );

        System.out.printf(
                "Service 10%%   : RM %.2f%n",
                serviceCharge
        );

        System.out.printf(
                "Final Price   : RM %.2f%n",
                finalPrice
        );

        System.out.println(
                "=========================================="
        );

        // =====================================================
        // CHECK STATUS
        // =====================================================

        if ("Checked-In".equalsIgnoreCase(guest[7])) {

            System.out.println(
                    "\nGuest is already checked-in."
            );

            return;

        } else if ("Checked-Out".equalsIgnoreCase(guest[7])) {

            System.out.println(
                    "\nGuest has already checked-out."
            );

            return;
        }

        // =====================================================
        // PAYMENT METHOD
        // =====================================================

        System.out.println("\n--- PAYMENT METHOD ---");

        System.out.println("1. Cash");
        System.out.println("2. Credit / Debit Card");
        System.out.println("3. Online Banking");
        System.out.println("4. E-Wallet");

        System.out.print(
                "Select payment method (1-4): "
        );

        int paymentChoice =
                readPaymentChoice();

        String paymentMethod;

        switch (paymentChoice) {

            case 1:
                paymentMethod = "Cash";
                break;

            case 2:
                paymentMethod = "Credit / Debit Card";
                break;

            case 3:
                paymentMethod = "Online Banking";
                break;

            case 4:
                paymentMethod = "E-Wallet";
                break;

            default:
                System.out.println(
                        "Invalid payment method."
                );

                return;
        }

        // =====================================================
        // PAYMENT CONFIRMATION
        // =====================================================

        System.out.println(
                "\nPayment Amount: RM "
                + String.format("%.2f", finalPrice)
        );

        System.out.print(
                "Confirm payment using "
                + paymentMethod
                + "? (Y/N): "
        );

        String confirmation =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        if (!confirmation.equals("Y")) {

            System.out.println(
                    "\nPayment cancelled."
            );

            return;
        }

        // =====================================================
        // PROCESS PAYMENT
        // =====================================================

        boolean paymentSuccessful =
                manager.processPayment(
                        ticketNumber,
                        paymentMethod
                );

        if (!paymentSuccessful) {

            System.out.println(
                    "\nPayment failed."
            );

            return;
        }

        System.out.println(
                "\nPayment successful!"
        );

        // =====================================================
        // GENERATE RECEIPT
        // =====================================================

        System.out.println(
                "\nGenerating receipt..."
        );

        String receipt =
                manager.generateReceipt(
                        ticketNumber
                );

        if (receipt == null) {

            System.out.println(
                    "\nUnable to generate receipt."
            );

            System.out.println(
                    "No clean room is currently available."
            );

            return;
        }

        // =====================================================
        // DISPLAY RECEIPT
        // =====================================================

        System.out.println(receipt);
    }

    // =========================================================
    // 3. CHECK-OUT GUEST
    // =========================================================

    private void checkOutGuest() {

        System.out.println("\n--- Check-Out Guest ---");

        int ticketNumber = getTicketNumber();

        String[] guest =
                manager.getGuestDetails(ticketNumber);

        if (guest == null) {

            System.out.println(
                    "\nGuest not found."
            );

            return;
        }

        // =====================================================
        // CUSTOMER INFORMATION
        // =====================================================

        System.out.println(
                "\n========== CUSTOMER INFORMATION =========="
        );

        System.out.println(
                "Ticket Number : " + guest[0]
        );

        System.out.println(
                "Name          : " + guest[1]
        );

        System.out.println(
                "Room Number   : " + guest[8]
        );

        System.out.println(
                "Room Type     : " + guest[4]
        );

        System.out.println(
                "Status        : " + guest[7]
        );

        System.out.println(
                "=========================================="
        );

        // =====================================================
        // CHECK STATUS
        // =====================================================

        if ("Checked-Out"
                .equalsIgnoreCase(guest[7])) {

            System.out.println(
                    "\nGuest is already checked-out."
            );

            return;
        }

        if (!"Checked-In"
                .equalsIgnoreCase(guest[7])) {

            System.out.println(
                    "\nGuest must be checked-in "
                    + "before checking-out."
            );

            return;
        }

        String roomID = guest[8];

        // =====================================================
        // CONFIRM CHECK-OUT
        // =====================================================

        System.out.print(
                "\nConfirm check-out? (Y/N): "
        );

        String confirmation =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        if (!confirmation.equals("Y")) {

            System.out.println(
                    "\nCheck-out cancelled."
            );

            return;
        }

        // =====================================================
        // CHECK-OUT
        // =====================================================

        if (manager.checkOutGuest(ticketNumber)) {

            System.out.println(
                    "\nGuest successfully checked-out."
            );

            System.out.println(
                    "Room "
                    + roomID
                    + " has been sent to Housekeeping "
                    + "as DIRTY."
            );

        } else {

            System.out.println(
                    "\nUnable to check-out guest."
            );
        }
    }

    // =========================================================
    // 4. UPDATE GUEST
    // =========================================================

    private void updateGuest() {

        System.out.println("\n--- Update Guest ---");

        int ticketNumber =
                getTicketNumber();

        String[] guest =
                manager.getGuestDetails(ticketNumber);

        if (guest == null) {

            System.out.println(
                    "\nGuest not found."
            );

            return;
        }

        System.out.println("\nGuest Found:");

        System.out.println(
                "Name   : " + guest[1]
        );

        System.out.println(
                "Room   : " + guest[4]
        );

        System.out.println(
                "Status : " + guest[7]
        );

        System.out.println(
                "\nWhat would you like to update?"
        );

        System.out.println(
                "1. Contact Number"
        );

        System.out.println(
                "2. Room Type"
        );

        System.out.println(
                "3. Stay Duration"
        );

        System.out.println(
                "4. Cancel"
        );

        System.out.print(
                "Please choose an option (1-4): "
        );

        while (!scanner.hasNextInt()) {

            System.out.print(
                    "Invalid input! Please enter a number between 1 and 4: "
            );

            scanner.next();
        }

        int choice =
                scanner.nextInt();

        scanner.nextLine();

        switch (choice) {

            // =================================================
            // CONTACT NUMBER
            // =================================================

            case 1:

                System.out.print(
                        "Enter new contact number: "
                );

                String contact =
                        scanner.nextLine()
                                .trim();

                if (contact.matches(
                        "^01[0-14-9]-[0-9]{7,8}$"
                )) {

                    if (manager.updateContact(
                            ticketNumber,
                            contact
                    )) {

                        System.out.println(
                                "\nContact number updated successfully."
                        );

                    } else {

                        System.out.println(
                                "\nUnable to update contact number."
                        );
                    }

                } else {

                    System.out.println(
                            "\nInvalid contact number format."
                    );
                }

                break;

            // =================================================
            // ROOM TYPE
            // =================================================

            case 2:

                System.out.println(
                        "Available Room Types:"
                );

                System.out.println(
                        "Single / Double / Suite / Presidential Suite"
                );

                System.out.print(
                        "Enter new room type: "
                );

                String room =
                        scanner.nextLine()
                                .trim();

                if (room.equalsIgnoreCase("Single")
                        || room.equalsIgnoreCase("Double")
                        || room.equalsIgnoreCase("Suite")
                        || room.equalsIgnoreCase("Presidential Suite")) {

                    if (room.equalsIgnoreCase(
                            "Presidential Suite")) {

                        room = "Presidential Suite";

                    } else {

                        room =
                                room.substring(0, 1)
                                        .toUpperCase()
                                + room.substring(1)
                                        .toLowerCase();
                    }

                    if (manager.updateRoomType(
                            ticketNumber,
                            room
                    )) {

                        System.out.println(
                                "\nRoom type updated successfully."
                        );

                    } else {

                        System.out.println(
                                "\nUnable to update room type."
                        );
                    }

                } else {

                    System.out.println(
                            "\nInvalid room type."
                    );
                }

                break;

            // =================================================
            // STAY DURATION
            // =================================================

            case 3:

                System.out.print(
                        "Enter new stay duration (1-30 nights): "
                );

                while (!scanner.hasNextInt()) {

                    System.out.print(
                            "Invalid input! Please enter a number: "
                    );

                    scanner.next();
                }

                int duration =
                        scanner.nextInt();

                scanner.nextLine();

                if (duration >= 1
                        && duration <= 30) {

                    if (manager.updateStayDuration(
                            ticketNumber,
                            duration
                    )) {

                        System.out.println(
                                "\nStay duration updated successfully."
                        );

                    } else {

                        System.out.println(
                                "\nUnable to update stay duration."
                        );
                    }

                } else {

                    System.out.println(
                            "\nStay duration must be between "
                            + "1 and 30 nights."
                    );
                }

                break;

            // =================================================
            // CANCEL
            // =================================================

            case 4:

                System.out.println(
                        "Update cancelled."
                );

                break;

            default:

                System.out.println(
                        "Invalid option."
                );
        }
    }

    // =========================================================
    // 5. DELETE GUEST
    // =========================================================

    private void deleteGuest() {

        System.out.println("\n--- Delete Guest ---");

        int ticketNumber =
                getTicketNumber();

        String[] guest =
                manager.getGuestDetails(ticketNumber);

        if (guest == null) {

            System.out.println(
                    "\nGuest not found."
            );

            return;
        }

        System.out.println("\nGuest Found:");

        System.out.println(
                "Name   : " + guest[1]
        );

        System.out.println(
                "Room   : " + guest[4]
        );

        System.out.println(
                "Status : " + guest[7]
        );

        System.out.print(
                "\nAre you sure you want to delete this guest? (Y/N): "
        );

        String confirmation =
                scanner.nextLine()
                        .trim()
                        .toUpperCase();

        if (confirmation.equals("Y")) {

            if (manager.deleteGuest(
                    ticketNumber
            )) {

                System.out.println(
                        "\nGuest record deleted successfully."
                );

            } else {

                System.out.println(
                        "\nUnable to delete guest."
                );
            }

        } else {

            System.out.println(
                    "\nDeletion cancelled."
            );
        }
    }

    // =========================================================
    // 6. VIEW GUEST LIST
    // =========================================================

    private void viewGuestList() {

        System.out.println("\n--- Guest List ---");

        String[][] guestList =
                manager.getAllGuestsData();

        System.out.println(
                "\nTotal Number of Customers: "
                + manager.getNumberOfGuests()
        );

        System.out.println(
                "=============================================================================================================="
        );

        if (guestList.length == 0) {

            System.out.println(
                    "No guest records found."
            );

        } else {

            System.out.printf(
                    "%-10s | %-18s | %-13s | %-18s | %-9s | %-8s | %-12s | %-12s | %-12s%n",
                    "Ticket",
                    "Name",
                    "Contact",
                    "Room Type",
                    "Room No.",
                    "Nights",
                    "Final Price",
                    "Payment",
                    "Status"
            );

            System.out.println(
                    "--------------------------------------------------------------------------------------------------------------"
            );

            for (String[] guest : guestList) {

                if (guest == null) {
                    continue;
                }

                System.out.printf(
                        "%-10s | %-18s | %-13s | %-18s | %-9s | %-8s | RM %-9s | %-12s | %-12s%n",
                        guest[0],
                        guest[1],
                        guest[3],
                        guest[4],
                        guest[8],
                        guest[6],
                        guest[9],
                        guest[10],
                        guest[7]
                );
            }
        }

        System.out.println(
                "=============================================================================================================="
        );
    }

    // =========================================================
    // DISPLAY GUEST DETAILS
    // =========================================================

    private void displayGuestDetails(
            String[] guest
    ) {

        System.out.println(
                "\n=============================="
        );

        System.out.println(
                "        GUEST DETAILS"
        );

        System.out.println(
                "=============================="
        );

        System.out.println(
                "Ticket Number : " + guest[0]
        );

        System.out.println(
                "Full Name     : " + guest[1]
        );

        System.out.println(
                "Gender        : " + guest[2]
        );

        System.out.println(
                "Contact       : " + guest[3]
        );

        System.out.println(
                "Room Type     : " + guest[4]
        );

        System.out.println(
                "Room Number   : " + guest[8]
        );

        System.out.println(
                "Stay Duration : " + guest[6] + " Nights"
        );

        System.out.println(
                "Status        : " + guest[7]
        );

        System.out.println(
                "Payment       : " + guest[10]
        );

        System.out.println(
                "=============================="
        );
    }

    // =========================================================
    // GET TICKET NUMBER
    // =========================================================

    private int getTicketNumber() {

        System.out.print(
                "Enter Ticket Number: "
        );

        while (!scanner.hasNextInt()) {

            System.out.print(
                    "Invalid ticket number! Please enter a number: "
            );

            scanner.next();
        }

        int ticketNumber =
                scanner.nextInt();

        scanner.nextLine();

        return ticketNumber;
    }

    // =========================================================
    // READ PAYMENT CHOICE
    // =========================================================

    private int readPaymentChoice() {

        while (!scanner.hasNextInt()) {

            System.out.print(
                    "Invalid input! Please enter a number between 1 and 4: "
            );

            scanner.next();
        }

        int choice =
                scanner.nextInt();

        scanner.nextLine();

        while (choice < 1 || choice > 4) {

            System.out.print(
                    "Invalid payment method! Please enter a number between 1 and 4: "
            );

            while (!scanner.hasNextInt()) {

                System.out.print(
                        "Invalid input! Please enter a number between 1 and 4: "
                );

                scanner.next();
            }

            choice =
                    scanner.nextInt();

            scanner.nextLine();
        }

        return choice;
    }

    // =========================================================
    // HOUSEKEEPING NOTIFICATIONS
    // =========================================================

    private void displayHousekeepingNotifications() {

        String[] notifications =
                manager.consumeHousekeepingNotifications();

        if (notifications.length == 0) {
            return;
        }

        System.out.println(
                "Housekeeping Updates:"
        );

        for (String message : notifications) {

            System.out.println(
                    "- " + message
            );
        }

        System.out.println();
    }
       
    private void displayLostAndFound() {
        String[][] items = FrontDeskController.getInstance().getLostItemsData();

        System.out.println("\n=== FRONT DESK - LOST & FOUND ITEMS ===");
        System.out.printf("%-8s | %-8s | %-25s | %-12s | %-10s\n", 
                          "Item ID", "Room ID", "Item Name", "Date Found", "Status");
        System.out.println("------------------------------------------------------------------");

        if (items == null || items.length == 0) {
            System.out.println("  No lost & found records.");
        } else {
            for (String[] item : items) {
                if (item != null && item[0] != null) {
                    System.out.printf("%-8s | %-8s | %-25s | %-12s | %-10s\n",
                            item[0],
                            item[1],
                            item[2],
                            item[3],
                            item[4]);
                }
            }
        }
    }
}