package entity;

public class Room {
    
    public static final String STATUS_CLEAN = "Clean";
    public static final String STATUS_DIRTY = "Dirty";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_MAINTENANCE = "Maintenance";

    private String roomId;
    private String status;

    public Room(String roomId) {
        this.roomId = roomId;
        this.status = STATUS_DIRTY; // Default status
    }

    public String getRoomId() {
        return roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}