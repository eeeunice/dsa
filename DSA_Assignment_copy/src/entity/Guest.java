package entity;

public class Guest {
    private int ticketNumber;
    private String fullName;
    private String contactNumber;
    private String roomType;
    private int stayDuration;
    private String status;
    private char gender;

    public Guest(int ticketNumber, String fullName,char gender, String contactNumber, String roomType, int stayDuration) {
        this.ticketNumber = ticketNumber;
        this.fullName = fullName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.roomType = roomType;
        this.stayDuration = stayDuration;
        this.status = "Waiting";
    }

    // Setters
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public void setGender(char gender) { this.gender = gender; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public void setStayDuration(int stayDuration) { this.stayDuration = stayDuration; }
    public void setStatus(String status) { this.status = status; }

    // Getters
    public int getTicketNumber() { return ticketNumber; }
    public String getFullName() { return fullName; }
    public char getGender() { return gender; }
    public String getContactNumber() { return contactNumber; }
    public String getRoomType() { return roomType; }
    public int getStayDuration() { return stayDuration; }
    public String getStatus() { return status; }
    
    public void displayInfo() {
        System.out.println("[" + ticketNumber + "] " + fullName + " | Room: " + roomType + " | Status: " + status);
    }
}