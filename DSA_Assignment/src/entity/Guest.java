package entity;
//Author : LIM CHUN CHUAN
//Author : EUNICE LIM NI-XI

public class Guest {

    private int ticketNumber;
    private String fullName;
    private char gender;
    private String contactNumber;
    private String roomType;
    private int numberOfRooms;
    private int stayDuration;
    private String status;

    public Guest(int ticketNumber, String fullName, char gender, String contactNumber) {
        this.ticketNumber = ticketNumber;
        this.fullName = fullName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.roomType = "Single";
        this.numberOfRooms = 1;
        this.stayDuration = 1;
        this.status = "Waiting";
    }

    public Guest(int ticketNumber, String fullName, char gender,
                 String contactNumber, String roomType,
                 int numberOfRooms, int stayDuration) {

        this.ticketNumber = ticketNumber;
        this.fullName = fullName;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.stayDuration = stayDuration;
        this.status = "Waiting";
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public int getStayDuration() {
        return stayDuration;
    }

    public void setStayDuration(int stayDuration) {
        this.stayDuration = stayDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double calculateTotalPrice() {
        double ratePerNight = 0.0;
        if (roomType != null) {
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
        }
        return ratePerNight * stayDuration * numberOfRooms;
    }

    public void displayInfo() {
        System.out.println("[" + ticketNumber + "] " + fullName + " | Room: " + roomType + " | Status: " + status
        );
    }
}