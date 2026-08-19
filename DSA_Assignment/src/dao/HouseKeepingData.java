package dao;

import adt.LinkedList;
import adt.ListInterface;
import entity.CleaningTask;
import entity.HousekeepingRecord;

public class HouseKeepingData {

    // 1. 初始化客房清洁任务队列/列表 (CleaningTask List)
    public ListInterface<CleaningTask> initCleaningTaskData() {
        ListInterface<CleaningTask> taskList = new LinkedList<>();

        // 创建测试清洁任务数据 (Room ID, Room Type, Priority)
        CleaningTask t1 = new CleaningTask("R101", "Normal");
        CleaningTask t2 = new CleaningTask("R102", "High (VIP)");
        CleaningTask t3 = new CleaningTask("R103", "Normal");
        CleaningTask t4 = new CleaningTask("R104", "Normal");
        CleaningTask t5 = new CleaningTask("R201", "High (VIP)");
        CleaningTask t6 = new CleaningTask("R202", "Normal");
        CleaningTask t7 = new CleaningTask("R301", "High (VIP)");
        CleaningTask t8 = new CleaningTask("R302", "Normal");

        // 设置部分任务的状态和负责人员（模拟已在处理中的任务）
        t1.setAssignedStaff("Ahmad");
        t1.setTaskStatus("Completed");

        t2.setAssignedStaff("Siti");
        t2.setTaskStatus("In Progress");

        t3.setAssignedStaff("John");
        t3.setTaskStatus("In Progress");

        t4.setAssignedStaff("Mary");
        t4.setTaskStatus("In Progress");

        // t5, t6, t7 保持默认的 "Unassigned" 和 "Pending"

        // 添加到自定义 LinkedList 中
        taskList.add(t1);
        taskList.add(t2);
        taskList.add(t3);
        taskList.add(t4);
        taskList.add(t5);
        taskList.add(t6);
        taskList.add(t7);

        return taskList;
    }

    // 2. 初始化客房历史变更日志记录 (HousekeepingRecord Audit Trail)
    public ListInterface<HousekeepingRecord> initHousekeepingRecordData() {
        ListInterface<HousekeepingRecord> recordList = new LinkedList<>();

        // 创建测试历史记录 (Room ID, Prev Status, New Status, Prev Staff, New Staff, Prev Remarks, New Remarks)
        HousekeepingRecord r1 = new HousekeepingRecord(
                "R101", "Dirty", "Clean", "Unassigned", "Ahmad", "Guest checked out", "Cleaned and sanitized"
        );

        HousekeepingRecord r2 = new HousekeepingRecord(
                "R102", "Dirty", "In Progress", "Unassigned", "Siti", "VIP Arrival soon", "Deep cleaning started"
        );

        HousekeepingRecord r3 = new HousekeepingRecord(
                "R201", "Inspection", "In Progress", "Mary", "Mary", "Awaiting inspection", "Fixing bathroom towels"
        );

        // 添加到日志列表中
        recordList.add(r1);
        recordList.add(r2);
        recordList.add(r3);

        return recordList;
    }
}