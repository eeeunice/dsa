package entity;

//Author : EUNICE LIM NI-XI

public class LostItem {
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_CLAIMED = "Claimed";

    private String itemId;
    private String roomId;
    private String itemName;
    private String dateFound;
    private String status;

    public LostItem(String itemId, String roomId, String itemName, String dateFound) {
        this.itemId = itemId;
        this.roomId = roomId;
        this.itemName = itemName;
        this.dateFound = dateFound;
        this.status = STATUS_PENDING;
    }

    public String getItemId() { return itemId; }
    public String getRoomId() { return roomId; }
    public String getItemName() { return itemName; }
    public String getDateFound() { return dateFound; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}