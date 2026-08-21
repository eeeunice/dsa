package dao;

import entity.CleaningTask;
import entity.HouseKeepingRecord;
import entity.LostItem;
import entity.Room;

public class HouseKeepingData {

    public Room[] initRoomData() {
        Room[] roomArray = new Room[10];

        Room r1 = new Room("101"); r1.setStatus(Room.STATUS_OCCUPIED); r1.setAssignedStaff("FrontDesk"); r1.setRemarks("Occupied by ANGELINA"); roomArray[0] = r1;
        Room r2 = new Room("102"); r2.setStatus(Room.STATUS_CLEAN); r2.setLastCleanedTime("2026-08-21 10:15"); roomArray[1] = r2;
        Room r3 = new Room("201"); r3.setStatus(Room.STATUS_OCCUPIED); r3.setAssignedStaff("FrontDesk"); r3.setRemarks("Occupied by ELON MUSK"); roomArray[2] = r3;
        Room r4 = new Room("202"); r4.setStatus(Room.STATUS_MAINTENANCE); r4.setAssignedStaff("Technician"); r4.setRemarks("AC leaking water"); roomArray[3] = r4;
        
        Room r5 = new Room("301"); r5.setStatus(Room.STATUS_OCCUPIED); r5.setAssignedStaff("FrontDesk"); r5.setRemarks("Occupied by min ling"); roomArray[4] = r5;
        
        Room r6 = new Room("302"); r6.setStatus(Room.STATUS_CLEAN); r6.setLastCleanedTime("2026-08-21 11:00"); roomArray[5] = r6;
        Room r7 = new Room("401"); r7.setStatus(Room.STATUS_CLEAN); r7.setLastCleanedTime("2026-08-21 08:45"); roomArray[6] = r7;
        Room r8 = new Room("402"); r8.setStatus(Room.STATUS_OCCUPIED); r8.setAssignedStaff("FrontDesk"); r8.setRemarks("Occupied by Eric Loo"); roomArray[7] = r8;
        Room r9 = new Room("404"); r9.setStatus(Room.STATUS_DIRTY); r9.setRemarks("Guest requested deep clean"); roomArray[8] = r9;
        Room r10 = new Room("555"); r10.setStatus(Room.STATUS_CLEAN); r10.setLastCleanedTime("2026-08-21 12:30"); roomArray[9] = r10;

        return roomArray;
    }

    public CleaningTask[] initCleaningTaskData() {
        CleaningTask[] taskArray = new CleaningTask[4];

        CleaningTask t1 = new CleaningTask("404", "High (VIP)"); 
        t1.setAssignedStaff("FrontDesk");
        t1.setTaskStatus("Pending");
        
        CleaningTask t2 = new CleaningTask("202", "High (VIP)");
        t2.setAssignedStaff("Technician"); 
        t2.setTaskStatus("In Progress");
        
        CleaningTask t3 = new CleaningTask("302", "Normal");
        t3.setAssignedStaff("Siti");
        t3.setTaskStatus("Completed");
        
        CleaningTask t4 = new CleaningTask("102", "High (VIP)");
        t4.setAssignedStaff("Ahmad");
        t4.setTaskStatus("Completed");

        taskArray[0] = t1;
        taskArray[1] = t2;
        taskArray[2] = t3;
        taskArray[3] = t4;

        return taskArray;
    }

    public HouseKeepingRecord[] initHousekeepingRecordData() {
        HouseKeepingRecord[] recordArray = new HouseKeepingRecord[10];

        recordArray[0] = new HouseKeepingRecord("101", "Dirty", "Clean", "Unassigned", "Ahmad", "Guest checked out", "Cleaned and sanitized");
        recordArray[1] = new HouseKeepingRecord("102", "Dirty", "Clean", "Unassigned", "Siti", "VIP Arrival soon", "Deep cleaning completed");
        recordArray[2] = new HouseKeepingRecord("201", "Clean", "Occupied", "Unassigned", "FrontDesk", "Guest check-in", "Assigned to ELON MUSK");
        recordArray[3] = new HouseKeepingRecord("202", "Dirty", "Maintenance", "Unassigned", "Technician", "AC broken", "Sent to repair");
        recordArray[4] = new HouseKeepingRecord("301", "Clean", "Occupied", "Unassigned", "FrontDesk", "Guest check-in", "Assigned to min ling");
        recordArray[5] = new HouseKeepingRecord("302", "Dirty", "Clean", "Unassigned", "Siti", "Regular clean", "Cleaned and inspected");
        recordArray[6] = new HouseKeepingRecord("401", "Dirty", "Clean", "Siti", "Siti", "Standard clean", "Completed");
        recordArray[7] = new HouseKeepingRecord("402", "Clean", "Occupied", "Unassigned", "FrontDesk", "New check-in", "Occupied by Eric Loo");
        recordArray[8] = new HouseKeepingRecord("404", "Clean", "Dirty", "Unassigned", "Unassigned", "Late checkout", "Requires deep clean");
        recordArray[9] = new HouseKeepingRecord("555", "Dirty", "Clean", "John", "John", "Final inspection", "Ready for guest");

        return recordArray;
    }

    public LostItem[] initLostItemData() {
        LostItem[] lostItemArray = new LostItem[10];

        lostItemArray[0] = new LostItem("L001", "101", "Black Leather Wallet", "2026-08-10");
        
        LostItem item2 = new LostItem("L002", "102", "iPhone 14 Pro", "2026-08-12");
        item2.setStatus(LostItem.STATUS_CLAIMED);
        lostItemArray[1] = item2;
        
        lostItemArray[2] = new LostItem("L003", "201", "Ray-Ban Sunglasses", "2026-08-14");
        lostItemArray[3] = new LostItem("L004", "202", "Silver Wristwatch", "2026-08-15");
        
        LostItem item5 = new LostItem("L005", "301", "Sony Wireless Earbuds", "2026-08-16");
        item5.setStatus(LostItem.STATUS_CLAIMED);
        lostItemArray[4] = item5;
        
        lostItemArray[5] = new LostItem("L006", "302", "Passport Holder", "2026-08-17");
        lostItemArray[6] = new LostItem("L007", "401", "Blue Winter Jacket", "2026-08-18");
        
        LostItem item8 = new LostItem("L008", "402", "Gold Ring", "2026-08-19");
        item8.setStatus(LostItem.STATUS_CLAIMED);
        lostItemArray[7] = item8;
        
        lostItemArray[8] = new LostItem("L009", "404", "Kindle Paperwhite", "2026-08-20");
        lostItemArray[9] = new LostItem("L010", "555", "Nintendo Switch Console", "2026-08-21");

        return lostItemArray;
    }
}