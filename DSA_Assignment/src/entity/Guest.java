package entity;

public class Guest {
    private int ticketNumber;
    private String fullName;
    private String contactNumber;
    private String roomType;
    private String roomID;
    private int numberOfRooms;
    private int stayDuration;
    private String status;
    private char gender;

    public Guest(int ticketNumber, String fullName, char gender, String contactNumber, String roomType, int numberOfRooms, int stayDuration) {
        this.ticketNumber = ticketNumber;
        this.fullName = fullName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.roomType = roomType;
        this.roomID = null;
        this.numberOfRooms = numberOfRooms;
        this.stayDuration = stayDuration;
        this.status = "Waiting";
    }

    public void setFullName(String fullName) {  
        this.fullName = fullName;
    }
    
    public void setContactNumber(String contactNumber) {  
        this.contactNumber = contactNumber; 
    }
    
    public void setGender(char gender) {  
        this.gender = gender; 
    }
    
    public void setRoomType(String roomType) {
        this.roomType = roomType; 
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setRoomId(String roomID) {
        this.roomID = roomID;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }
    
    public void setStayDuration(int stayDuration) {
        this.stayDuration = stayDuration; 
    }
    
    public void setStatus(String status) {
        this.status = status; 
    }

    public int getTicketNumber() {  
        return ticketNumber; 
    }
    
    public String getFullName() {  
        return fullName; 
    }
    
    public char getGender() {  
        return gender; 
    }
    
    public String getContactNumber() {  
        return contactNumber; 
    }
    
    public String getRoomType() {
        return roomType; 
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomId() {
        return roomID;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }
    
    public int getStayDuration() {  
        return stayDuration;
    }
    
    public String getStatus() {
        return status; 
    }

    public double calculateTotalPrice() {
        double ratePerNight = 0.0;
        switch (roomType) {
            case "Single":
                ratePerNight = 150.00;
                break;
            case "Double":
                ratePerNight = 250.00;
                break;
            case "Suite":
                ratePerNight = 500.00;
                break;
            case "Presidential Suite":
                ratePerNight = 1200.00;
                break;
        }
        return ratePerNight * stayDuration * numberOfRooms;
    }
    
    public void displayInfo() {
        System.out.println("[" + ticketNumber + "] " + fullName + " | Room: " + roomType + " (" + numberOfRooms + ") | Total: RM " + String.format("%.2f", calculateTotalPrice()) + " | Status: " + status);
    }
}
