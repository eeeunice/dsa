package boundary;

import adt.ListInterface;
import control.RegistrationDataController;
import entity.Guest;
import utility.ClearScreen;
import utility.Header;
import java.util.Scanner;

public class ReportUI {

    private Scanner scanner = new Scanner(System.in);

    // Inner helper class for aggregating person bookings by room type
    private static class RoomStat {
        String roomType;
        int personBookingCount;

        RoomStat(String roomType) {
            this.roomType = roomType;
            this.personBookingCount = 0;
        }
    }

    public void reportModule() {
        int choice = -1;
        do {
            ClearScreen.clear();
            Header.printHeader();

            System.out.println("=== Reports & Analytics Module ===");
            System.out.println("1. Most Person Booking Report");
            System.out.println("2. House Keeping Report");
            System.out.println("7. Main Menu");
            System.out.print("Please choose an option (1-7): ");

            if (!scanner.hasNextInt()) {
                if (!scanner.hasNext()) return;
                System.out.print("Invalid input! Please enter a number between 1 and 2: ");
                scanner.next();
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (choice) {
                case 1:
                    displayMostPersonBookingReport();
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 2:
                    displayHousekeepingReport();
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1 - 3.");
                    System.out.print("\nPress Enter to continue...");
                    scanner.nextLine();
                    break;
            }
        } while (choice != 3);
    }

    public void displayMostPersonBookingReport() {
        ClearScreen.clear();
        Header.printHeader();

        ListInterface<Guest> guestList = RegistrationDataController.getSharedGuestList();

        RoomStat[] stats = new RoomStat[] {
            new RoomStat("Single"),
            new RoomStat("Double"),
            new RoomStat("Suite"),
            new RoomStat("Presidential Suite")
        };

        int grandTotalPersonBookings = 0;

        // Aggregate statistics from all guests
        if (guestList != null) {
            for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
                Guest g = guestList.get(i);
                if (g != null && g.getRoomType() != null) {
                    for (RoomStat stat : stats) {
                        if (stat.roomType.equalsIgnoreCase(g.getRoomType())) {
                            stat.personBookingCount++;
                            break;
                        }
                    }
                }
            }
        }

        // Compute grand totals
        for (RoomStat stat : stats) {
            grandTotalPersonBookings += stat.personBookingCount;
        }

        // Sort stats descending by personBookingCount
        for (int i = 0; i < stats.length - 1; i++) {
            for (int j = 0; j < stats.length - 1 - i; j++) {
                if (stats[j].personBookingCount < stats[j + 1].personBookingCount) {
                    RoomStat temp = stats[j];
                    stats[j] = stats[j + 1];
                    stats[j + 1] = temp;
                }
            }
        }

        System.out.println("======================================================================");
        System.out.println("                    MOST PERSON BOOKING REPORT                        ");
        System.out.println("======================================================================");

        if (grandTotalPersonBookings == 0) {
            System.out.println("No guest bookings found in system.");
        } else {
            RoomStat mostBooked = stats[0];
            double percentage = (mostBooked.personBookingCount * 100.0) / grandTotalPersonBookings;

            System.out.println("  >>> MOST PERSON BOOKED ROOM TYPE : " + mostBooked.roomType.toUpperCase() + " <<<");
            System.out.println("  > Total Person Bookings : " + mostBooked.personBookingCount + " guest(s)");
            System.out.println("  > Booking Percentage (Total): " + String.format("%.1f", percentage) + "%");
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("%-10s | %-24s | %-16s | %-12s%n",
                    "Rank", "Room Type", "Person Bookings", "Percentage %");
            System.out.println("----------------------------------------------------------------------");

            for (int k = 0; k < stats.length; k++) {
                RoomStat stat = stats[k];
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
    // --- 3. HOUSEKEEPING REPORT (For Management) ---
    public void displayHousekeepingReport() {
        ClearScreen.clear();
        Header.printHeader();
        
        ListInterface<Guest> guestList = RegistrationDataController.getSharedGuestList();
        
        RoomStat[] stats = new RoomStat[] {
            new RoomStat("Single"),
            new RoomStat("Double"),
            new RoomStat("Suite"),
            new RoomStat("Presidential Suite")
        };
        
        int totalRoomsCleanedToday = 0;

        if (guestList != null) {
            for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
                Guest g = guestList.get(i);
                
                if (g != null && g.getRoomType() != null) {
                    
                    boolean isCleanedToday = true;
                    
                    if (isCleanedToday) {
                        for (RoomStat stat : stats) {
                            if (stat.roomType.equalsIgnoreCase(g.getRoomType())) {
                                stat.personBookingCount++;
                                totalRoomsCleanedToday++;
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        for (int i = 0; i < stats.length - 1; i++) {
            for (int j = 0; j < stats.length - 1 - i; j++) {
                if (stats[j].personBookingCount < stats[j + 1].personBookingCount) {
                    RoomStat temp = stats[j];
                    stats[j] = stats[j + 1];
                    stats[j + 1] = temp;
                }
            }
        }

        System.out.println("======================================================================");
        System.out.println("             HOUSEKEEPING: DAILY CLEANING SUMMARY REPORT              ");
        System.out.println("======================================================================");
        
        if (totalRoomsCleanedToday == 0) {
            System.out.println("No rooms have been cleaned yet today.");
        } else {
            System.out.println("  > MANAGER SUMMARY");
            System.out.println("  > Total Rooms Cleaned Today: " + totalRoomsCleanedToday + " Room(s)");
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("%-20s | %-20s | %-15s%n", "Room Type", "Rooms Cleaned", "Workload %");
            System.out.println("----------------------------------------------------------------------");
            
            for (RoomStat stat : stats) {
                double workloadPercentage = (stat.personBookingCount * 100.0) / totalRoomsCleanedToday;
                System.out.printf("%-20s | %-20d | %-14.1f%%%n", 
                        stat.roomType, 
                        stat.personBookingCount, 
                        workloadPercentage);
            }
            
            System.out.println("----------------------------------------------------------------------");
            System.out.printf("%-20s | %-20d | %-14.1f%%%n", "GRAND TOTAL", totalRoomsCleanedToday, 100.0);
        }
        System.out.println("======================================================================");
    }
}
