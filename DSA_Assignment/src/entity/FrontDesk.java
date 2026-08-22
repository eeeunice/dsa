package entity;
//Author : LOW MIN LING

public class FrontDesk {
    private Guest guest;
    private String roomType;
    private String roomID;
    private int numberOfRooms;
    private int stayDuration;
    private String status;
    private double finalPrice;
    private String paymentMethod;
    private String paymentStatus;
    private boolean receiptGenerated;

    public FrontDesk(Guest guest, String roomType, int numberOfRooms, int stayDuration) {
        this.guest = guest;
        this.roomType = roomType;
        this.roomID = null;
        this.numberOfRooms = numberOfRooms;
        this.stayDuration = stayDuration;
        this.status = "Waiting";

        // Payment default values owned by FrontDesk
        this.finalPrice = 0.0;
        this.paymentMethod = "Not Paid";
        this.paymentStatus = "Pending";
        this.receiptGenerated = false;
    }

    public FrontDesk(int ticketNumber, String fullName, char gender,
                     String contactNumber, String roomType,
                     int numberOfRooms, int stayDuration) {
        this(new Guest(ticketNumber, fullName, gender, contactNumber), roomType, numberOfRooms, stayDuration);
    }

    public FrontDesk(Guest guest) {
        this(guest, guest != null ? guest.getRoomType() : "Single",
             guest != null ? guest.getNumberOfRooms() : 1,
             guest != null ? guest.getStayDuration() : 1);
        if (guest != null) {
            this.status = guest.getStatus() != null ? guest.getStatus() : "Waiting";
        }
        this.roomID = null;
        this.finalPrice = calculateFinalPrice();
        this.paymentMethod = "Not Paid";
        this.paymentStatus = "Pending";
        this.receiptGenerated = false;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    // Guest details delegation
    public int getTicketNumber() {
        return guest != null ? guest.getTicketNumber() : 0;
    }

    public void setTicketNumber(int ticketNumber) {
        if (guest != null) {
            guest.setTicketNumber(ticketNumber);
        }
    }

    public String getFullName() {
        return guest != null ? guest.getFullName() : "";
    }

    public void setFullName(String fullName) {
        if (guest != null) {
            guest.setFullName(fullName);
        }
    }

    public char getGender() {
        return guest != null ? guest.getGender() : ' ';
    }

    public void setGender(char gender) {
        if (guest != null) {
            guest.setGender(gender);
        }
    }

    public String getContactNumber() {
        return guest != null ? guest.getContactNumber() : "";
    }

    public void setContactNumber(String contactNumber) {
        if (guest != null) {
            guest.setContactNumber(contactNumber);
        }
    }

    // Booking & Registration attributes
    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
        if (guest != null) {
            guest.setRoomType(roomType);
        }
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomId() {
        return roomID;
    }

    public void setRoomId(String roomID) {
        setRoomID(roomID);
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
        if (guest != null) {
            guest.setNumberOfRooms(numberOfRooms);
        }
    }

    public int getStayDuration() {
        return stayDuration;
    }

    public void setStayDuration(int stayDuration) {
        this.stayDuration = stayDuration;
        if (guest != null) {
            guest.setStayDuration(stayDuration);
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        if (guest != null) {
            guest.setStatus(status);
        }
    }

    // =========================================================
    // PAYMENT ATTRIBUTES & FUNCTIONS (OWNED EXCLUSIVELY BY FRONTDESK)
    // =========================================================

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isReceiptGenerated() {
        return receiptGenerated;
    }

    public void setReceiptGenerated(boolean receiptGenerated) {
        this.receiptGenerated = receiptGenerated;
    }

    // =========================
    // PRICE & CHARGE CALCULATION LOGIC
    // =========================

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

    public double calculateServiceCharge() {
        return calculateTotalPrice() * 0.10;
    }

    public double calculateFinalPrice() {
        return calculateTotalPrice() + calculateServiceCharge();
    }

    public void updateFinalPrice() {
        this.finalPrice = calculateFinalPrice();
    }

    public void displayInfo() {
        System.out.println(
            "[" + getTicketNumber() + "] "
            + getFullName()
            + " | Room: " + roomType
            + " | Room No: " + (roomID != null ? roomID : "Not Assigned")
            + " | Final Price: RM "
            + String.format("%.2f", finalPrice)
            + " | Payment: " + paymentStatus
            + " | Status: " + status
        );
    }

    @Override
    public String toString() {
        return String.format("%-10d | %-24s | %-6s | %-12s | %-18s | %-6d | %-8d | RM %-9.2f | %-10s",
                getTicketNumber(), getFullName(), getGender(),
                getContactNumber(), getRoomType(), getNumberOfRooms(),
                getStayDuration(), calculateTotalPrice(), getStatus());
    }
}
