package boundary;
//Author : LIM CHUN CHUAN
//Author : EUNICE LIM NI-XI
//Author : Low Min Ling

import adt.ListInterface;
import control.BookingDataController;
import control.FrontDeskController;
import control.HouseKeepingController;
import entity.FrontDesk;
import entity.Guest;
import java.util.Scanner;

public class ReportUI {
    private Scanner scanner = new Scanner(System.in);
    private HouseKeepingController hkController = HouseKeepingController.getInstance();
    private FrontDeskController fdController = FrontDeskController.getInstance();

    public void reportModule() {
        int choice = -1;
        do {
            BookingDataController.clearScreen();
            BookingDataController.printHeader();

            System.out.println(utility.Header.PURPLE + "  =================== Reports & Analytics Module ===================" + utility.Header.RESET);
            System.out.println("1. Most Person Booking Report");
            System.out.println("2. Daily Front Desk & Booking Summary Report");
            System.out.println("3. Front Desk Payment Report");
            System.out.println("4. Front Desk Revenue & Occupancy Report");
            System.out.println("5. Housekeeping Task Assignment Report");
            System.out.println("6. Staff Cleaning Performance Report");
            System.out.println("7. Exit to Main Menu");
            System.out.println(utility.Header.PURPLE + "  ==========================================================================" + utility.Header.RESET);
            System.out.print("Please choose an option (1-7): ");

            if (!scanner.hasNextInt()) {
                if (!scanner.hasNext()) return;
                System.out.println("Invalid input! Please enter a number between 1 to 7: ");
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
                    generatePaymentReport();
                    promptEnterKey();
                    break;
                case 4:
                    generateGuestStatusReport();
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
                    System.out.println(utility.Header.GREEN + "Returning to Main Menu..." + utility.Header.RESET);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a number between 1 to 7.");
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


    // Daily Front Desk & Booking Summary Report
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
                    FrontDesk fd = fdController.getFrontDeskRecord(g.getTicketNumber());
                    String roomNo = (fd != null && fd.getRoomID() != null && !fd.getRoomID().trim().isEmpty()) ? fd.getRoomID() : "N/A";
                    double finalPrice = (fd != null) ? fd.getFinalPrice() : g.calculateTotalPrice();
                    String paymentMethod = (fd != null && fd.getPaymentMethod() != null && !fd.getPaymentMethod().trim().isEmpty()) ? fd.getPaymentMethod() : "Not Paid";
                    String status = (fd != null && fd.getStatus() != null) ? fd.getStatus() : g.getStatus();

                    totalRevenue += finalPrice;
                    totalNights += g.getStayDuration();

                    System.out.printf("%-10d | %-20s | %-18s | %-10s | %-6d | RM %-13.2f | %-20s | %-12s%n",
                            g.getTicketNumber(), g.getFullName(), g.getRoomType(), roomNo, g.getStayDuration(), finalPrice, paymentMethod, status);
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
    // Option 3: Front Desk Payment Report
    // ===================================================================
    public void generatePaymentReport() {
        BookingDataController.clearScreen();
        BookingDataController.printHeader();
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        System.out.println("==========================================================================");
        System.out.println("                    FRONT DESK PAYMENT REPORT                            ");
        System.out.println("==========================================================================");

        if (guestList == null || guestList.isEmpty()) {
            System.out.println("No guest records found in system.");
        } else {
            System.out.printf(
                "%-10s | %-20s | %-18s | %-13s | %-20s | %-12s%n",
                "Ticket",
                "Guest Name",
                "Room Type",
                "Final Price",
                "Payment Method",
                "Payment Status"
                );

        System.out.println("--------------------------------------------------------------------------");

        double totalPaid = 0.0;
        double totalPending = 0.0;
        int paidCount = 0;
        int pendingCount = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest == null) {
                continue;
            }

            // Get Front Desk record
            FrontDesk fd = fdController.getFrontDeskRecord(guest.getTicketNumber());

            if (fd == null) {
                continue;
            }

            String paymentMethod = fd.getPaymentMethod() != null
                            ? fd.getPaymentMethod()
                            : "Not Paid";

            String paymentStatus = fd.getPaymentStatus() != null
                            ? fd.getPaymentStatus()
                            : "Pending";

            double amount = fd.getFinalPrice();

            System.out.printf(
                    "%-10d | %-20s | %-18s | RM %-10.2f | %-20s | %-12s%n",
                    fd.getTicketNumber(),
                    fd.getFullName(),
                    fd.getRoomType(),
                    amount,
                    paymentMethod,
                    paymentStatus
            );

                if ("Paid".equalsIgnoreCase(paymentStatus)) {
                    totalPaid += amount;
                    paidCount++;
                } else {
                    totalPending += amount;
                    pendingCount++;
                }
            }

            System.out.println("--------------------------------------------------------------------------");
            System.out.println("\nPAYMENT SUMMARY");
            System.out.println("----------------------------------------");
            System.out.printf( "  > Paid Transactions       : %d%n",paidCount);
            System.out.printf("  > Pending Transactions    : %d%n",pendingCount);
            System.out.printf("  > Total Paid              : RM %.2f%n",totalPaid);
            System.out.printf("  > Total Pending           : RM %.2f%n",totalPending);
            System.out.printf("  > Total Payment Value     : RM %.2f%n",totalPaid + totalPending);
        }

        System.out.println("==========================================================================");
    }
    
    // ===================================================================
    // Option 4: Front Desk Revenue & Occupancy Report
    // ===================================================================
        public void generateGuestStatusReport() {
            BookingDataController.clearScreen();
            BookingDataController.printHeader();
            ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

            System.out.println("============================================================================");
            System.out.println("              FRONT DESK REVENUE & OCCUPANCY REPORT                       ");
            System.out.println("============================================================================");
            
            if (guestList == null || guestList.isEmpty()) {
                System.out.println("No guest records found in system.");
            } else {
                System.out.printf(
                "%-10s | %-20s | %-18s | %-10s | %-13s | %-12s%n",
                "Ticket",
                "Guest Name",
                "Room Type",
                "Room No.",
                "Final Price",
                "Guest Status"
                );
                System.out.println("----------------------------------------------------------------------------");

            double totalRevenue = 0.0;
            int waitingCount = 0;
            int checkedInCount = 0;
            int checkedOutCount = 0;
            int occupiedRooms = 0;
            int unassignedRooms = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest == null) {
                continue;
            }

            // Get Front Desk record
            FrontDesk fd = fdController.getFrontDeskRecord(guest.getTicketNumber());

            if (fd == null) {
                continue;
            }

            String roomNumber = fd.getRoomID();

                if (roomNumber == null || roomNumber.trim().isEmpty()) {
                    roomNumber = "N/A";
                }

                String status = fd.getStatus();

                if (status == null || status.trim().isEmpty()) {
                    status = "Unknown";
                }
                
                double finalPrice = fd.getFinalPrice();
                System.out.printf(
                    "%-10d | %-20s | %-18s | %-10s | RM %-10.2f | %-12s%n",
                    fd.getTicketNumber(),
                    fd.getFullName(),
                    fd.getRoomType(),
                    roomNumber,
                    finalPrice,
                    status
                );

                // Calculate revenue
                if ("Paid".equalsIgnoreCase(fd.getPaymentStatus())) {
                    totalRevenue += finalPrice;
                }

                // Calculate guest status
                if ("Waiting".equalsIgnoreCase(status)) {
                    waitingCount++;
                } else if ("Checked-In".equalsIgnoreCase(status)) {
                    checkedInCount++;
                } else if ("Checked-Out".equalsIgnoreCase(status)) {
                checkedOutCount++;
                }

                // Calculate room occupancy
                if ("Checked-In".equalsIgnoreCase(status)
                    && !"N/A".equalsIgnoreCase(roomNumber)) {
                    occupiedRooms++;
                } else if ("N/A".equalsIgnoreCase(roomNumber)) {
                    unassignedRooms++;
                }
            }

            System.out.println("----------------------------------------------------------------------------");
            System.out.println("\nREVENUE SUMMARY");
            System.out.println("----------------------------------------");
            System.out.printf("  > Total Revenue           : RM %.2f%n",totalRevenue);
            System.out.println("\nOCCUPANCY SUMMARY");
            System.out.println("----------------------------------------");
            System.out.printf("  > Occupied Rooms         : %d%n",occupiedRooms);
            System.out.printf("  > Unassigned Rooms       : %d%n",unassignedRooms);
            System.out.println("\nGUEST STATUS SUMMARY");
            System.out.println("----------------------------------------");
            System.out.printf("  > Waiting Guests         : %d%n",waitingCount);
            System.out.printf("  > Checked-In Guests      : %d%n",checkedInCount);
            System.out.printf("  > Checked-Out Guests     : %d%n",checkedOutCount);
            System.out.printf("  > Total Guests           : %d%n",waitingCount+ checkedInCount+ checkedOutCount);
        }

        System.out.println("============================================================================");
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