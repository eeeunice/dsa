package boundary;

import control.BookingDataController;
import dao.HouseKeepingData;
import entity.CleaningTask;
import entity.HouseKeepingRecord;
import adt.ArrayQueue;
import adt.QueueInterface;
import java.util.Scanner;

public class ReportUI {

    private Scanner scanner = new Scanner(System.in);
    
    private HouseKeepingData hkData = new HouseKeepingData();

    public void reportModule() {
        int choice = -1;
        do {
            BookingDataController.clearScreen();
            BookingDataController.printHeader();

            System.out.println("=== Reports & Analytics Module ===");
            System.out.println("1. Most Person Booking Report");
            System.out.println("2. (Reserved for other report)");
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

    // ===================================================================
    // Option 5: Housekeeping Task Assignment Report
    // ===================================================================
    public void generateTaskAssignmentReport() {
        BookingDataController.clearScreen();
        BookingDataController.printHeader();
        
        System.out.println("=========================================================================");
        System.out.println("                HOUSEKEEPING TASK ASSIGNMENT REPORT                      ");
        System.out.println("=========================================================================");
        System.out.printf("%-8s | %-8s | %-12s | %-10s | %-14s | %-12s\n", 
                "Task ID", "Room ID", "Priority", "Time", "Assigned Staff", "Status");
        System.out.println("-------------------------------------------------------------------------");

        CleaningTask[] allTasks = hkData.initCleaningTaskData();
        QueueInterface<CleaningTask> taskQueue = new ArrayQueue<>(20);

        for (CleaningTask task : allTasks) {
            if (task != null && !task.getTaskStatus().equalsIgnoreCase("Completed")) {
                taskQueue.enqueue(task);
            }
        }

        int count = 0;
        while (!taskQueue.isEmpty()) {
            CleaningTask task = taskQueue.dequeue();
            System.out.println(task.toString());
            count++;
        }

        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Total Pending/In Progress Tasks: " + count);
        System.out.println("=========================================================================");
    }

    private static class StaffStat {
        String staffName;
        int count;

        StaffStat(String staffName, int count) {
            this.staffName = staffName;
            this.count = count;
        }
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

        HouseKeepingRecord[] records = hkData.initHousekeepingRecordData();
        StaffStat[] stats = new StaffStat[20];
        int size = 0;

        for (HouseKeepingRecord record : records) {
            if (record == null) continue;

            String staffName = record.getNewStaff();
            
            if (staffName.equalsIgnoreCase("Unassigned") || staffName.equalsIgnoreCase("FrontDesk") || staffName.equalsIgnoreCase("Technician")) {
                continue;
            }

            boolean exists = false;
            for (int i = 0; i < size; i++) {
                if (stats[i].staffName.equalsIgnoreCase(staffName)) {
                    stats[i].count++;
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                stats[size++] = new StaffStat(staffName, 1);
            }
        }

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - i - 1; j++) {
                if (stats[j].count < stats[j + 1].count) {
                    StaffStat temp = stats[j];
                    stats[j] = stats[j + 1];
                    stats[j + 1] = temp;
                }
            }
        }

        int totalCleaned = 0;
        for (int i = 0; i < size; i++) {
            System.out.printf("%-20s | %-15d\n", stats[i].staffName, stats[i].count);
            totalCleaned += stats[i].count;
        }

        System.out.println("-------------------------------------------------------");
        System.out.println("Total Cleaning Records Analyzed: " + totalCleaned);
        System.out.println("=======================================================");
    }
}