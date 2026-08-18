package boundary;

import java.util.Scanner;
import utility.Header; 

public class HotelManagementMenu {

    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            Header.printHeader();

            System.out.println("1. Walking-In Registration & Standard Booking Procedure");
            System.out.println("2. Housekeeping and Task Log");
            System.out.println("3. Front-Desk Service");
            System.out.println("4. Reports");
            System.out.println("5. Exit System");
            System.out.print("Please select an option (1-5): ");

            while (!scanner.hasNextInt()) {
                System.out.println(Header.RED + "\n  [!] Invalid input! Please enter a number between 1 and 5." + Header.RESET);
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
                  //  HouseKeepingUI houseKeeping = new HouseKeepingUI();
                    //houseKeeping.houseKeepingMenu();
                    break;
                case 3:
                  //  FrontDeskUI frontDesk = new FrontDeskUI();
                    //frontDesk.frontDeskMenu();
                    break;
                case 4:
                    ReportUI hotelReport = new ReportUI();
                    hotelReport.reportModule();
                    break;
                case 5:
                    System.out.println(Header.GREEN + "\n  Exiting system. Thank you!" + Header.RESET);
                    scanner.close();
                    return; // Exits the method entirely
                default:
                    System.out.println(Header.RED + "\n  [!] Invalid option! Please select 1-5." + Header.RESET);
                    break;
            }

            // Optional: Pause before clearing and showing the main menu again
            if (choice != 5) {
                System.out.print("\nPress Enter to return to Main Menu...");
                scanner.nextLine();
            }

        } while (choice != 5);

        scanner.close();
    }

    public static void main(String[] args) {
        showMenu();
    }
}