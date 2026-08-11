package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class CleaningTask {
    private static int idCounter = 1000;
    
    private String taskId;
    private String roomId;
    private String roomType;
    private String requestedTime;
    private String priority; // "High (VIP)", "Normal"
    private String assignedStaff;
    private String taskStatus; // "Pending", "In Progress", "Completed"

    public CleaningTask(String roomId, String roomType, String priority) {
        this.taskId = "T" + (++idCounter);
        this.roomId = roomId;
        this.roomType = roomType;
        this.priority = priority;
        this.assignedStaff = "Unassigned";
        this.taskStatus = "Pending";
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.requestedTime = dtf.format(LocalDateTime.now());
    }
    public String getTaskId() {
        return taskId;
    }
    public String getRoomId() {
        return roomId;
    }
    public String getRoomType() {
        return roomType;
    }
    public String getRequestedTime() {
        return requestedTime;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getAssignedStaff() {
        return assignedStaff;
    }
    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = assignedStaff;
    }
    public String getTaskStatus() {
        return taskStatus;
    }
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
    @Override
    public String toString() {
        return String.format("%-8s | %-8s | %-10s | %-12s | %-10s | %-14s | %-12s",
                taskId, roomId, roomType, priority, requestedTime, assignedStaff, taskStatus);
    }
}