package control;

import adt.BST;
import adt.LinkedList;
import adt.ListInterface;
import dao.FrontData;
import dao.HouseKeepingData;
import entity.Guest;
import entity.LostItem;

public class FrontDeskController {

    private static final FrontDeskController INSTANCE = new FrontDeskController();

    private final BST<Guest> guestBST;
    private final ListInterface<String> housekeepingNotifications;
    private final FrontData frontDeskData;

    private FrontDeskController() {
        guestBST = new BST<>();
        housekeepingNotifications = new LinkedList<>();
        frontDeskData = new FrontData();
        loadGuestsToBST();
    }

    public static FrontDeskController getInstance() {
        return INSTANCE;
    }

    // =========================================================
    // LOAD REGISTRATION DATA → FRONT DESK
    // =========================================================

    private void loadGuestsToBST() {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();
        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            if (guest != null) {
                guestBST.add(guest.getTicketNumber(), guest);
            }
        }
    }

    // =========================================================
    // SEARCH
    // =========================================================

    public Guest searchGuest(int ticketNumber) {
        return guestBST.search(ticketNumber);
    }

    public boolean guestExists(int ticketNumber) {
        return guestBST.contains(ticketNumber);
    }

    public String getGuestStatus(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);
        return guest != null ? guest.getStatus() : null;
    }

    // =========================================================
    // GET GUEST DETAILS
    // =========================================================

    public String[] getGuestDetails(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return null;
        }

        String roomID = guest.getRoomID();
        if (roomID == null || roomID.trim().isEmpty()) {
            roomID = "Not Assigned";
        }

        String[] details = new String[12];
        details[0] = String.valueOf(guest.getTicketNumber());
        details[1] = guest.getFullName();
        details[2] = String.valueOf(guest.getGender());
        details[3] = guest.getContactNumber();
        details[4] = guest.getRoomType();
        details[5] = String.valueOf(guest.getNumberOfRooms());
        details[6] = String.valueOf(guest.getStayDuration());
        details[7] = guest.getStatus();
        details[8] = roomID;
        details[9] = String.format("%.2f", guest.getFinalPrice());
        details[10] = guest.getPaymentStatus();
        details[11] = guest.getPaymentMethod();

        return details;
    }

    // =========================================================
    // PAYMENT
    // =========================================================

    public boolean processPayment(int ticketNumber, String paymentMethod) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return false;
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return false;
        }

        double finalPrice = guest.calculateFinalPrice();
        guest.setFinalPrice(finalPrice);
        guest.setPaymentMethod(paymentMethod);
        guest.setPaymentStatus("Paid");

        return true;
    }

    // =========================================================
    // GET PRICES
    // =========================================================

    public double getBasePrice(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);
        if (guest == null) return 0.0;
        return guest.calculateTotalPrice();
    }

    public double getServiceCharge(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);
        if (guest == null) return 0.0;
        return guest.calculateServiceCharge();
    }

    public double getFinalPrice(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);
        if (guest == null) return 0.0;
        return guest.calculateFinalPrice();
    }

    // =========================================================
    // GENERATE RECEIPT
    // =========================================================

    public String generateReceipt(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return null;
        }

        if (!"Paid".equalsIgnoreCase(guest.getPaymentStatus())) {
            return null;
        }

        String assignedRoom = HouseKeepingController.getInstance().assignCleanRoom();

        if (assignedRoom == null) {
            return null;
        }

        guest.setRoomID(assignedRoom);
        guest.setReceiptGenerated(true);
        guest.setStatus("Checked-In");

        HouseKeepingController.getInstance().markRoomOccupied(
                assignedRoom,
                guest.getFullName(),
                "Occupied by " + guest.getFullName() + " (Ticket: " + ticketNumber + ")"
        );

        frontDeskData.saveGuest(guest);

        return createReceipt(guest);
    }

    private String createReceipt(Guest guest) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n========================================\n");
        receipt.append("           HOTEL CHECK-IN RECEIPT\n");
        receipt.append("========================================\n");
        receipt.append(String.format("Ticket Number : %d%n", guest.getTicketNumber()));
        receipt.append(String.format("Guest Name    : %s%n", guest.getFullName()));
        receipt.append(String.format("Room Type     : %s%n", guest.getRoomType()));
        receipt.append(String.format("Room Number   : %s%n", guest.getRoomID()));
        receipt.append(String.format("Stay Duration : %d Night(s)%n", guest.getStayDuration()));
        receipt.append(String.format("Base Price    : RM %.2f%n", guest.calculateTotalPrice()));
        receipt.append(String.format("Service 10%%   : RM %.2f%n", guest.calculateServiceCharge()));
        receipt.append(String.format("Final Price   : RM %.2f%n", guest.getFinalPrice()));
        receipt.append(String.format("Payment Method: %s%n", guest.getPaymentMethod()));
        receipt.append(String.format("Payment Status: %s%n", guest.getPaymentStatus()));
        receipt.append(String.format("Guest Status  : %s%n", guest.getStatus()));
        receipt.append("========================================\n");
        receipt.append("          Payment Successful!\n");
        receipt.append("          Check-In Successful!\n");
        receipt.append("========================================");

        return receipt.toString();
    }

    // =========================================================
    // CHECK-OUT
    // =========================================================

    public boolean checkOutGuest(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return false;
        }

        String roomID = guest.getRoomID();

        if (roomID == null || roomID.trim().isEmpty()) {
            return false;
        }

        guest.setStatus("Checked-Out");

        HouseKeepingController.getInstance().notifyCheckOut(
                roomID,
                "Unassigned",
                "Room " + roomID + " is Dirty after guest check-out."
        );

        frontDeskData.saveGuest(guest);

        return true;
    }

    // =========================================================
    // UPDATE CONTACT / ROOM TYPE / STAY DURATION
    // =========================================================

    public boolean updateContact(int ticketNumber, String newContact) {
        Guest guest = guestBST.search(ticketNumber);
        if (guest == null) return false;
        guest.setContactNumber(newContact);
        return true;
    }

    public boolean updateRoomType(int ticketNumber, String newRoomType) {
        Guest guest = guestBST.search(ticketNumber);
        if (guest == null) return false;
        guest.setRoomType(newRoomType);
        return true;
    }

    public boolean updateStayDuration(int ticketNumber, int newDuration) {
        Guest guest = guestBST.search(ticketNumber);
        if (guest == null) return false;
        guest.setStayDuration(newDuration);
        return true;
    }

    // =========================================================
    // DELETE GUEST
    // =========================================================

    public boolean deleteGuest(int ticketNumber) {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest != null && guest.getTicketNumber() == ticketNumber) {
                guestList.remove(i);
                guestBST.clear();
                loadGuestsToBST();
                return true;
            }
        }

        return false;
    }

    // =========================================================
    // HOUSEKEEPING NOTIFICATION
    // =========================================================

    public void notifyRoomCleaned(String roomId) {
        housekeepingNotifications.add(
                "Room " + roomId + " is cleaned and ready for the next check-in."
        );
    }

    public String[] consumeHousekeepingNotifications() {
        int total = housekeepingNotifications.getNumberOfEntries();
        String[] messages = new String[total];

        for (int i = 1; i <= total; i++) {
            messages[i - 1] = housekeepingNotifications.get(i);
        }

        housekeepingNotifications.clear();

        return messages;
    }

    // =========================================================
    // LOST & FOUND FACADE (CONVERTS LostItem[] TO String[][])
    // Keeps UI completely decoupled from Entity classes
    // =========================================================

    public String[][] getLostItemsData() {
        HouseKeepingData housekeepingData = new HouseKeepingData();
        LostItem[] items = housekeepingData.initLostItemData();

        if (items == null) {
            return new String[0][5];
        }

        String[][] data = new String[items.length][5];
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                data[i][0] = items[i].getItemId();
                data[i][1] = items[i].getRoomId();
                data[i][2] = items[i].getItemName();
                data[i][3] = items[i].getDateFound();
                data[i][4] = items[i].getStatus();
            }
        }
        return data;
    }

    public String claimLostItem(String itemId) {
        return HouseKeepingController.getInstance().claimLostItem(itemId);
    }

    // =========================================================
    // CHECKED OUT GUESTS
    // =========================================================

    public Guest[] getCheckedOutGuests() {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();
        int count = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            if (guest != null && "Checked-Out".equalsIgnoreCase(guest.getStatus())
                    && guest.getRoomID() != null && !guest.getRoomID().trim().isEmpty()) {
                count++;
            }
        }

        Guest[] result = new Guest[count];
        int index = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            if (guest != null && "Checked-Out".equalsIgnoreCase(guest.getStatus())
                    && guest.getRoomID() != null && !guest.getRoomID().trim().isEmpty()) {
                result[index++] = guest;
            }
        }

        return result;
    }

    // =========================================================
    // ALL GUESTS & DATA CONVERSION
    // =========================================================

    public ListInterface<Guest> getFinalGuestList() {
        return frontDeskData.getFinalGuestList();
    }

    public int getNumberOfGuests() {
        return guestBST.getNumberOfEntries();
    }

    public Guest[] getAllGuests() {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();
        Guest[] guests = new Guest[guestList.getNumberOfEntries()];

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            guests[i - 1] = guestList.get(i);
        }

        return guests;
    }

    public String[][] getAllGuestsData() {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();
        String[][] guests = new String[guestList.getNumberOfEntries()][12];

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            if (guest == null) continue;

            String roomID = guest.getRoomID();
            if (roomID == null || roomID.trim().isEmpty()) {
                roomID = "N/A";
            }

            guests[i - 1][0] = String.valueOf(guest.getTicketNumber());
            guests[i - 1][1] = guest.getFullName();
            guests[i - 1][2] = String.valueOf(guest.getGender());
            guests[i - 1][3] = guest.getContactNumber();
            guests[i - 1][4] = guest.getRoomType();
            guests[i - 1][5] = String.valueOf(guest.getNumberOfRooms());
            guests[i - 1][6] = String.valueOf(guest.getStayDuration());
            guests[i - 1][7] = guest.getStatus();
            guests[i - 1][8] = roomID;
            guests[i - 1][9] = String.format("%.2f", guest.getFinalPrice());
            guests[i - 1][10] = guest.getPaymentStatus();
            guests[i - 1][11] = guest.getPaymentMethod();
        }

        return guests;
    }
}