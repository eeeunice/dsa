package boundary;

import control.BookingDataController;
import java.util.Scanner;

public class HotelManagementMenu {

    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            BookingDataController.printHeader();

            System.out.println("1. Walking-In Registration & Standard Booking Procedure");
            System.out.println("2. Housekeeping and Task Log");
            System.out.println("3. Front-Desk Service");
            System.out.println("4. Reports");
            System.out.println("5. Exit System");
            System.out.print("Please select an option (1-5): ");

            while (!scanner.hasNextInt()) {
                System.out.println(BookingDataController.RED + "\n  [!] Invalid input! Please enter a number between 1 and 5." + BookingDataController.RESET);
                System.out.print("  Select an option (1-5): ");
                scanner.next();
            }

            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    RegistrationAndBookingUI registrationAndBooking = new RegistrationAndBookingUI();
                    registrationAndBooking.bookingModule();
                    break;
                case 2:
                    System.out.println(BookingDataController.PURPLE + "\n  Housekeeping module coming soon..." + BookingDataController.RESET);
                    break;
                case 3:
                    System.out.println(BookingDataController.PURPLE + "\n  Front-Desk module coming soon..." + BookingDataController.RESET);
                    break;
                case 4:
                    ReportUI reportUI = new ReportUI();
                    reportUI.reportModule();
                    break;
                case 5:
                    System.out.println(BookingDataController.GREEN + "\n  Exiting system. Thank you!" + BookingDataController.RESET);
                    scanner.close();
                    return; 
                default:
                    System.out.println(BookingDataController.RED + "\n  [!] Invalid option! Please select 1-5." + BookingDataController.RESET);
                    break;
            }

            // Optional: Pause before clearing and showing the main menu again
            if (choice != 5) {
                System.out.print("\nPress Enter to return to Main Menu...");
                scanner.nextLine();
            }

        } while (choice != 5); // Keeps the menu running until option 5 is chosen

        scanner.close();
    }

    public static void main(String[] args) {
        showMenu();
    }
}