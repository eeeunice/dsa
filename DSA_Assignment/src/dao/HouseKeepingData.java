package dao;

import entity.CleaningTask;
import entity.HouseKeepingRecord;
import entity.Room;

public class HouseKeepingData {

    public Room[] initRoomData() {
        Room[] roomArray = new Room[10];

        Room r1 = new Room("101"); r1.setStatus(Room.STATUS_CLEAN); roomArray[0] = r1;
        Room r2 = new Room("102"); r2.setStatus(Room.STATUS_DIRTY); r2.setRemarks("Guest requested early clean"); roomArray[1] = r2;
        Room r3 = new Room("201"); r3.setStatus(Room.STATUS_OCCUPIED); roomArray[2] = r3;
        Room r4 = new Room("202"); r4.setStatus(Room.STATUS_MAINTENANCE); r4.setRemarks("AC leaking water"); roomArray[3] = r4;
        
        Room r5 = new Room("301"); r5.setStatus(Room.STATUS_IN_PROGRESS); r5.setAssignedStaff("Ahmad"); roomArray[4] = r5;
        
        Room r6 = new Room("302"); r6.setStatus(Room.STATUS_DIRTY); roomArray[5] = r6;
        Room r7 = new Room("401"); r7.setStatus(Room.STATUS_CLEAN); roomArray[6] = r7;
        Room r8 = new Room("402"); r8.setStatus(Room.STATUS_OCCUPIED); roomArray[7] = r8;
        Room r9 = new Room("404"); r9.setStatus(Room.STATUS_DIRTY); roomArray[8] = r9;
        Room r10 = new Room("555"); r10.setStatus(Room.STATUS_CLEAN); roomArray[9] = r10;

        return roomArray;
    }

    public CleaningTask[] initCleaningTaskData() {
        CleaningTask[] taskArray = new CleaningTask[4];

        CleaningTask t1 = new CleaningTask("102", "High (VIP)"); 
        t1.setTaskStatus("Pending");
        
        CleaningTask t2 = new CleaningTask("301", "High (VIP)");
        t2.setAssignedStaff("Ahmad"); 
        t2.setTaskStatus("In Progress"); // Aligned with Room 301
        
        CleaningTask t3 = new CleaningTask("302", "Normal");
        t3.setTaskStatus("Pending");
        
        CleaningTask t4 = new CleaningTask("404", "High (VIP)");
        t4.setTaskStatus("Pending");

        taskArray[0] = t1;
        taskArray[1] = t2;
        taskArray[2] = t3;
        taskArray[3] = t4;

        return taskArray;
    }

    public HouseKeepingRecord[] initHousekeepingRecordData() {
        HouseKeepingRecord[] recordArray = new HouseKeepingRecord[10];

        recordArray[0] = new HouseKeepingRecord("101", "Dirty", "Clean", "Unassigned", "Ahmad", "Guest checked out", "Cleaned and sanitized");
        recordArray[1] = new HouseKeepingRecord("102", "Dirty", "In Progress", "Unassigned", "Siti", "VIP Arrival soon", "Deep cleaning started");
        recordArray[2] = new HouseKeepingRecord("201", "Inspection", "In Progress", "Mary", "Mary", "Awaiting inspection", "Fixing bathroom towels");
        recordArray[3] = new HouseKeepingRecord("202", "Dirty", "Maintenance", "Unassigned", "Technician", "AC broken", "Sent to repair");
        recordArray[4] = new HouseKeepingRecord("301", "Dirty", "In Progress", "Unassigned", "Ahmad", "Regular clean", "Cleaning in progress");
        recordArray[5] = new HouseKeepingRecord("302", "Occupied", "Dirty", "Unassigned", "Unassigned", "Guest checkout", "Pending housekeeper");
        recordArray[6] = new HouseKeepingRecord("401", "Dirty", "Clean", "Siti", "Siti", "Standard clean", "Completed");
        recordArray[7] = new HouseKeepingRecord("402", "Clean", "Occupied", "Unassigned", "FrontDesk", "New check-in", "Guest moved in");
        recordArray[8] = new HouseKeepingRecord("404", "Clean", "Dirty", "Unassigned", "Unassigned", "Late checkout", "Requires deep clean");
        recordArray[9] = new HouseKeepingRecord("555", "Dirty", "Clean", "John", "John", "Final inspection", "Ready for guest");

        return recordArray;
    }
}