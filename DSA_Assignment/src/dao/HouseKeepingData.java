package dao;

import adt.LinkedList;
import adt.ListInterface;
import entity.CleaningTask;
import entity.HouseKeepingRecord;
import entity.Room;

public class HouseKeepingData {

    public ListInterface<Room> initRoomData() {
        ListInterface<Room> roomList = new LinkedList<>();

        Room r1 = new Room("101"); r1.setStatus(Room.STATUS_CLEAN); roomList.add(r1);
        Room r2 = new Room("102"); r2.setStatus(Room.STATUS_DIRTY); r2.setRemarks("Guest requested early clean"); roomList.add(r2);
        Room r3 = new Room("201"); r3.setStatus(Room.STATUS_OCCUPIED); roomList.add(r3);
        Room r4 = new Room("202"); r4.setStatus(Room.STATUS_MAINTENANCE); r4.setRemarks("AC leaking water"); roomList.add(r4);
        Room r5 = new Room("301"); r5.setStatus(Room.STATUS_IN_PROGRESS); r5.setAssignedStaff("Ahmad"); roomList.add(r5);
        Room r6 = new Room("302"); r6.setStatus(Room.STATUS_DIRTY); roomList.add(r6);
        Room r7 = new Room("401"); r7.setStatus(Room.STATUS_CLEAN); roomList.add(r7);
        Room r8 = new Room("402"); r8.setStatus(Room.STATUS_OCCUPIED); roomList.add(r8);
        Room r9 = new Room("404"); r9.setStatus(Room.STATUS_DIRTY); roomList.add(r9);
        Room r10 = new Room("555"); r10.setStatus(Room.STATUS_CLEAN); roomList.add(r10);

        return roomList;
    }

    public ListInterface<CleaningTask> initCleaningTaskData() {
        ListInterface<CleaningTask> taskList = new LinkedList<>();

        CleaningTask t1 = new CleaningTask("101", "Normal");
        CleaningTask t2 = new CleaningTask("102", "High (VIP)");
        CleaningTask t3 = new CleaningTask("201", "Normal");
        CleaningTask t4 = new CleaningTask("202", "Normal");
        CleaningTask t5 = new CleaningTask("301", "High (VIP)");
        CleaningTask t6 = new CleaningTask("302", "Normal");
        CleaningTask t7 = new CleaningTask("401", "Normal");
        CleaningTask t8 = new CleaningTask("402", "Normal");
        CleaningTask t9 = new CleaningTask("404", "High (VIP)");
        CleaningTask t10 = new CleaningTask("555", "Normal");

        t1.setAssignedStaff("Ahmad"); t1.setTaskStatus("Completed");
        t2.setAssignedStaff("Siti"); t2.setTaskStatus("In Progress");
        t3.setAssignedStaff("John"); t3.setTaskStatus("In Progress");
        t4.setAssignedStaff("Mary"); t4.setTaskStatus("In Progress");

        taskList.add(t1); taskList.add(t2); taskList.add(t3); taskList.add(t4);
        taskList.add(t5); taskList.add(t6); taskList.add(t7); taskList.add(t8);
        taskList.add(t9); taskList.add(t10);

        return taskList;
    }

    public ListInterface<HouseKeepingRecord> initHousekeepingRecordData() {
        ListInterface<HouseKeepingRecord> recordList = new LinkedList<>();

        recordList.add(new HouseKeepingRecord("101", "Dirty", "Clean", "Unassigned", "Ahmad", "Guest checked out", "Cleaned and sanitized"));
        recordList.add(new HouseKeepingRecord("102", "Dirty", "In Progress", "Unassigned", "Siti", "VIP Arrival soon", "Deep cleaning started"));
        recordList.add(new HouseKeepingRecord("201", "Inspection", "In Progress", "Mary", "Mary", "Awaiting inspection", "Fixing bathroom towels"));
        recordList.add(new HouseKeepingRecord("202", "Dirty", "Maintenance", "Unassigned", "Technician", "AC broken", "Sent to repair"));
        recordList.add(new HouseKeepingRecord("301", "Dirty", "In Progress", "Unassigned", "Ahmad", "Regular clean", "Cleaning in progress"));
        recordList.add(new HouseKeepingRecord("302", "Occupied", "Dirty", "Unassigned", "Unassigned", "Guest checkout", "Pending housekeeper"));
        recordList.add(new HouseKeepingRecord("401", "Dirty", "Clean", "Siti", "Siti", "Standard clean", "Completed"));
        recordList.add(new HouseKeepingRecord("402", "Clean", "Occupied", "Unassigned", "FrontDesk", "New check-in", "Guest moved in"));
        recordList.add(new HouseKeepingRecord("404", "Clean", "Dirty", "Unassigned", "Unassigned", "Late checkout", "Requires deep clean"));
        recordList.add(new HouseKeepingRecord("555", "Dirty", "Clean", "John", "John", "Final inspection", "Ready for guest"));

        return recordList;
    }
}