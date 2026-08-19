package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Room {
    
    public static final String STATUS_CLEAN       = "Clean";
    public static final String STATUS_DIRTY       = "Dirty";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_MAINTENANCE = "Maintenance";
    public static final String STATUS_OCCUPIED    = "Occupied";

    private String roomId;
    private String roomType;
    private String status;
    private String assignedStaff;
    private String lastCleanedTime;
    private String remarks;

    public Room(String roomId) {
        this(roomId, "Standard");
    }

    public Room(String roomId, String roomType) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.status = STATUS_DIRTY;
        this.assignedStaff = "Unassigned";
        this.lastCleanedTime = "N/A";
        this.remarks = "None";
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (STATUS_CLEAN.equalsIgnoreCase(status)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            this.lastCleanedTime = dtf.format(LocalDateTime.now());
        }
    }

    public String getAssignedStaff() {
        return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff = (assignedStaff == null || assignedStaff.trim().isEmpty()) ? "Unassigned" : assignedStaff.trim();
    }

    public String getLastCleanedTime() {
        return lastCleanedTime;
    }

    public void setLastCleanedTime(String lastCleanedTime) {
        this.lastCleanedTime = lastCleanedTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = (remarks == null || remarks.trim().isEmpty()) ? "None" : remarks.trim();
    }

    @Override
    public String toString() {
        return String.format("%-8s | %-10s | %-13s | %-15s | %-16s | %-20s",
                roomId, roomType, status, assignedStaff, lastCleanedTime, remarks);
    }
}
