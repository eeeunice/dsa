package boundary;

import adt.ListInterface;
import control.BookingDataController;
import control.HouseKeepingController;
import entity.Guest;
import java.util.Scanner;

public class ReportUI {

    private Scanner scanner = new Scanner(System.in);
    
  
    private HouseKeepingController hkController = HouseKeepingController.getInstance();

    public void reportModule() {
        int choice = -1;
        do {
            BookingDataController.clearScreen();
            BookingDataController.printHeader();

            System.out.println("=== Reports & Analytics Module ===");
            System.out.println("1. Most Person Booking Report");
            System.out.println("2. Daily Front Desk & Booking Summary Report");
            System.out.println("3. (Reserved for other report)");
            System.out.println("4. (Reserved for other report)");
            System.out.println("5. Housekeeping Task Assignment Report");
            System.out.println("6. Staff Cleaning Performance Report");
            System.out.println("7. Main Menu");
            System.out.print("Please choose an option (1-7): ");

            if (!scanner.hasNextInt()) {
                if (!scanner.hasNext()) return;
                System.out.print("Invalid input! Please enter a number between 1 and 7: ");
                scanner.next();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    displayMostPersonBookingReport();
                    promptEnterKey();
                    break;
                case 2:
                    generateDailyReport();
                    promptEnterKey();
                    break;
                case 3:
                case 4:
                    System.out.println("Report under construction...");
                    promptEnterKey();
                    break;
                case 5:
                    generateTaskAssignmentReport();
                    promptEnterKey();
                    break;
                case 6:
                    generateStaffPerformanceReport();
                    promptEnterKey();
                    break;
                case 7:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 and 7.");
                    promptEnterKey();
                    break;
            }
        } while (choice != 7);
    }

    private void promptEnterKey() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
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


    // Option 2: Daily Front Desk & Booking Summary Report
    public void generateDailyReport() {
        BookingDataController.clearScreen();
        BookingDataController.printHeader();

        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        System.out.println("====================================================================================================================================");
        System.out.println("                                         DAILY FRONT DESK & BOOKING SUMMARY REPORT                                          ");
        System.out.println("====================================================================================================================================");

        if (guestList == null || guestList.isEmpty()) {
            System.out.println("No guest or booking records found in system.");
        } else {
            System.out.printf("%-10s | %-20s | %-18s | %-10s | %-6s | %-16s | %-20s | %-12s%n",
                    "Ticket No.", "Guest Name", "Room Type", "Room No.", "Nights", "Final Price", "Payment Method", "Status");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------");

            double totalRevenue = 0.0;
            int totalNights = 0;
            int totalBookings = guestList.getNumberOfEntries();

            for (int i = 1; i <= totalBookings; i++) {
                Guest g = guestList.get(i);
                if (g != null) {
                    entity.FrontDesk fd = new entity.FrontDesk(g);
                    String roomNo = (fd.getRoomID() != null && !fd.getRoomID().trim().isEmpty()) ? fd.getRoomID() : "N/A";
                    double finalPrice = fd.getFinalPrice();
                    String paymentMethod = (fd.getPaymentMethod() != null && !fd.getPaymentMethod().trim().isEmpty()) ? fd.getPaymentMethod() : "Not Paid";

                    totalRevenue += finalPrice;
                    totalNights += g.getStayDuration();

                 System.out.printf("%-10d | %-20s | %-18s | %-10s | %-6d | RM %-13.2f | %-20s | %-12s%n", g.getTicketNumber(), g.getFullName(), g.getRoomType(), roomNo, g.getStayDuration(), finalPrice, paymentMethod, g.getStatus());
                }
            }

            System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("SUMMARY STATISTICS:");
            System.out.printf("  > Total Guest Bookings     : %d%n", totalBookings);
            System.out.printf("  > Total Nights Reserved    : %d night(s)%n", totalNights);
            System.out.printf("  > Total Revenue / Value    : RM %.2f%n", totalRevenue);
        }
        System.out.println("====================================================================================================================================");
    }

    // ===================================================================
    // Option 5: Housekeeping Task / Assignment Report
    // ===================================================================
    public void generateTaskAssignmentReport() {
        BookingDataController.clearScreen();
        BookingDataController.printHeader();

        System.out.println("=========================================================================");
        System.out.println("            HOUSEKEEPING TASK / ASSIGNMENT REPORT                        ");
        System.out.println("=========================================================================");
        System.out.printf("%-18s | %-12s | %-22s | %-12s\n", 
                "Staff Name", "Room No.", "Task Type", "Status");
        System.out.println("-------------------------------------------------------------------------");

        System.out.print(hkController.getTaskAssignmentReport());

        System.out.println("=========================================================================");
    }

    // ===================================================================
    // Option 6: Staff Cleaning Count Report
    // ===================================================================
    public void generateStaffPerformanceReport() {
        BookingDataController.clearScreen();
        BookingDataController.printHeader();
        
        System.out.println("=======================================================");
        System.out.println("          STAFF CLEANING PERFORMANCE REPORT            ");
        System.out.println("=======================================================");
        System.out.printf("%-20s | %-15s\n", "Staff Name", "Tasks Cleaned");
        System.out.println("-------------------------------------------------------");

        System.out.print(hkController.getStaffPerformanceReport());

        System.out.println("=======================================================");
    }
}