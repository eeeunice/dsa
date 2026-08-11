package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HousekeepingLogRecord {
    private String roomId;
    private String previousStatus;
    private String newStatus;
    private String previousStaff;
    private String newStaff;
    private String previousRemarks;
    private String newRemarks;
    private String timestamp;

    public HousekeepingLogRecord(String roomId, String previousStatus, String newStatus,
                                String previousStaff, String newStaff,
                                String previousRemarks, String newRemarks) {
        this.roomId = roomId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.previousStaff = previousStaff;
        this.newStaff = newStaff;
        this.previousRemarks = previousRemarks;
        this.newRemarks = newRemarks;
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = dtf.format(LocalDateTime.now());
    }

    public String getRoomId() {
        return roomId;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public String getPreviousStaff() {
        return previousStaff;
    }

    public String getNewStaff() {
        return newStaff;
    }

    public String getPreviousRemarks() {
        return previousRemarks;
    }

    public String getNewRemarks() {
        return newRemarks;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] Room %s: Status (%s -> %s), Staff (%s -> %s)",
                timestamp, roomId, previousStatus, newStatus, previousStaff, newStaff);
    }
}
