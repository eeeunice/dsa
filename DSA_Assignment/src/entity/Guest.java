package entity;

public class Guest {
    private int ticketNumber;
    private String fullName;
    private String contactNumber;
    private String roomType;
    private int numberOfRooms;
    private int stayDuration;
    private int numberOfRooms;
    private String status;
    private char gender;

<<<<<<< HEAD
    public Guest(int ticketNumber, String fullName, char gender, String contactNumber, String roomType, int numberOfRooms, int stayDuration) {
=======
    public Guest(int ticketNumber, String fullName, char gender,
                 String contactNumber, String roomType,
                 int stayDuration, int numberOfRooms) {

>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
        this.ticketNumber = ticketNumber;
        this.fullName = fullName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.stayDuration = stayDuration;
        this.numberOfRooms = numberOfRooms;
        this.status = "Waiting";
    }

<<<<<<< HEAD
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
=======
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

    public void setStayDuration(int stayDuration) {
        this.stayDuration = stayDuration;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
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

<<<<<<< HEAD
    public int getNumberOfRooms() {
        return numberOfRooms;
    }
    
    public int getStayDuration() {  
=======
    public int getStayDuration() {
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
        return stayDuration;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

<<<<<<< HEAD
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
=======
    public String getStatus() {
        return status;
    }

    public void displayInfo() {
        System.out.println(
            "[" + ticketNumber + "] "
            + fullName
            + " | Room: " + roomType
            + " | Rooms: " + numberOfRooms
            + " | Stay: " + stayDuration + " nights"
            + " | Status: " + status
        );
>>>>>>> fcb9286bd1627a5d3cb12426af74c61db722fbbc
    }
}