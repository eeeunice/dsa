package dao;

import adt.LinkedList;
import adt.ListInterface;
import entity.CleaningTask;
import entity.HousekeepingRecord;
import entity.Room;

public class HouseKeepingData {

    // 1. 初始化基础客房数据 (去除了 Room Type)
    public ListInterface<Room> initRoomData() {
        ListInterface<Room> roomList = new LinkedList<>();

        roomList.add(new Room("101"));
        roomList.add(new Room("102"));
        roomList.add(new Room("201"));
        roomList.add(new Room("202"));
        roomList.add(new Room("301"));
        roomList.add(new Room("302"));

        return roomList;
    }

    // 2. 初始化客房清洁任务数据 (去除了 Room Type)
    public ListInterface<CleaningTask> initCleaningTaskData() {
        ListInterface<CleaningTask> taskList = new LinkedList<>();

        // 创建测试清洁任务数据 (只保留 Room ID 和 Priority)
        CleaningTask t1 = new CleaningTask("101", "Normal");
        CleaningTask t2 = new CleaningTask("102", "High (VIP)");
        CleaningTask t3 = new CleaningTask("201", "Normal");
        CleaningTask t4 = new CleaningTask("202", "Normal");
        CleaningTask t5 = new CleaningTask("301", "High (VIP)");
        CleaningTask t6 = new CleaningTask("302", "Normal");
        CleaningTask t7 = new CleaningTask("303", "High (VIP)");
        CleaningTask t8 = new CleaningTask("304", "Normal");

        // 设置部分任务的状态和负责人员
        t1.setAssignedStaff("Ahmad");
        t1.setTaskStatus("Completed");

        t2.setAssignedStaff("Siti");
        t2.setTaskStatus("In Progress");

        t3.setAssignedStaff("John");
        t3.setTaskStatus("In Progress");

        t4.setAssignedStaff("Mary");
        t4.setTaskStatus("In Progress");

        // 加入列表
        taskList.add(t1);
        taskList.add(t2);
        taskList.add(t3);
        taskList.add(t4);
        taskList.add(t5);
        taskList.add(t6);
        taskList.add(t7);
        taskList.add(t8);

        return taskList;
    }

    // 3. 初始化客房历史变更日志记录 (保持不变)
    public ListInterface<HousekeepingRecord> initHousekeepingRecordData() {
        ListInterface<HousekeepingRecord> recordList = new LinkedList<>();

        // 创建测试历史记录
        HousekeepingRecord r1 = new HousekeepingRecord(
                "101", "Dirty", "Clean", "Unassigned", "Ahmad", "Guest checked out", "Cleaned and sanitized"
        );

        HousekeepingRecord r2 = new HousekeepingRecord(
                "102", "Dirty", "In Progress", "Unassigned", "Siti", "VIP Arrival soon", "Deep cleaning started"
        );

        HousekeepingRecord r3 = new HousekeepingRecord(
                "201", "Inspection", "In Progress", "Mary", "Mary", "Awaiting inspection", "Fixing bathroom towels"
        );

        // 加入列表
        recordList.add(r1);
        recordList.add(r2);
        recordList.add(r3);

        return recordList;
    }
}