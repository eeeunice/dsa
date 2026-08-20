package boundary;

import control.BookingDataController;
import java.util.Scanner;

public class ReportUI {

    private Scanner scanner = new Scanner(System.in);

    public void reportModule() {
        int choice = -1;
        do {
            BookingDataController.clearScreen();
            BookingDataController.printHeader();

            System.out.println("=== Reports & Analytics Module ===");
            System.out.println("1. Most Person Booking Report");
            System.out.println("2. Main Menu");
            System.out.print("Please choose an option (1-2): ");

            if (!scanner.hasNextInt()) {
                if (!scanner.hasNext()) return;
                System.out.print("Invalid input! Please enter a number between 1 and 2: ");
                scanner.next();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    displayMostPersonBookingReport();
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 2:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1 or 2.");
                    break;
            }
        } while (choice != 2);
    }

    public void displayMostPersonBookingReport() {
        BookingDataController.clearScreen();
        BookingDataController.printHeader();

        BookingDataController.RoomStat[] stats = BookingDataController.getRoomBookingStats();

        int grandTotalPersonBookings = 0;
        if (stats != null) {
            for (BookingDataController.RoomStat stat : stats) {
                grandTotalPersonBookings += stat.personBookingCount;
            }
        }

        System.out.println("======================================================================");
        System.out.println("                    MOST PERSON BOOKING REPORT                        ");
        System.out.println("======================================================================");

        if (grandTotalPersonBookings == 0 || stats == null || stats.length == 0) {
            System.out.println("No guest bookings found in system.");
        } else {
            BookingDataController.RoomStat mostBooked = stats[0];
            double percentage = (mostBooked.personBookingCount * 100.0) / grandTotalPersonBookings;

            System.out.println("  >>> MOST PERSON BOOKED ROOM TYPE : " + mostBooked.roomType.toUpperCase() + " <<<");
            System.out.println("  > Total Person Bookings : " + mostBooked.personBookingCount + " guest(s)");
            System.out.println("  > Booking Percentage          : " + String.format("%.1f", percentage) + "% of total person bookings");
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("%-10s | %-24s | %-16s | %-12s%n",
                    "Rank", "Room Type", "Person Bookings", "Percentage %");
            System.out.println("----------------------------------------------------------------------");

            for (int k = 0; k < stats.length; k++) {
                BookingDataController.RoomStat stat = stats[k];
                double bookingShare = grandTotalPersonBookings > 0 ? (stat.personBookingCount * 100.0) / grandTotalPersonBookings : 0.0;
                String rankStr = "#" + (k + 1);
                if (k == 0) rankStr += " (MOST)";

                System.out.printf("%-10s | %-24s | %-16d | %-11.1f%%%n",
                        rankStr, stat.roomType, stat.personBookingCount, bookingShare);
            }

            System.out.println("----------------------------------------------------------------------");
            System.out.printf("%-10s | %-24s | %-16d | %-11.1f%%%n",
                    "TOTAL", "ALL ROOM TYPES", grandTotalPersonBookings, 100.0);
        }
        System.out.println("======================================================================");
    }
}