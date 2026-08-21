package control;

// Author : LOW MIN LING

import adt.BST;
import adt.LinkedList;
import adt.ListInterface;
import dao.FrontData;
import dao.HouseKeepingData;
import entity.FrontDesk;
import entity.Guest;
import entity.LostItem;
import java.util.HashMap;
import java.util.Map;

public class FrontDeskController {
    private static final FrontDeskController INSTANCE = new FrontDeskController();

    private final BST<Guest> guestBST;
    private final Map<Integer, FrontDesk> frontDeskMap;
    private final ListInterface<String> housekeepingNotifications;
    private final FrontData frontDeskData;

    private FrontDeskController() {
        guestBST = new BST<>();
        frontDeskMap = new HashMap<>();
        housekeepingNotifications = new LinkedList<>();
        frontDeskData = new FrontData();

        loadGuestsToBST();
    }

    public static FrontDeskController getInstance() {
        return INSTANCE;
    }

    private void loadGuestsToBST() {
        ListInterface<Guest> guestList =BookingDataController.getSharedGuestList();

        for (int i = 1;i <= guestList.getNumberOfEntries();i++) {
            Guest guest = guestList.get(i);

            if (guest != null) {
                guestBST.add( guest.getTicketNumber(), guest );
                FrontDesk record =new FrontDesk(guest);

                if (guest.getTicketNumber() == 10000001) {
                    record.setRoomID("101");
                    record.setPaymentStatus("Paid");
                    record.setPaymentMethod("Credit / Debit Card");
                    record.setFinalPrice(record.calculateFinalPrice());
                    record.setStatus("Checked-In");
                    record.setReceiptGenerated(true);
                    HouseKeepingController.getInstance().markRoomOccupied("101", record.getFullName(), "Occupied by " + record.getFullName());

                } else if (guest.getTicketNumber() == 10000002) {
                    record.setRoomID("201");
                    record.setPaymentStatus("Paid");
                    record.setPaymentMethod("Online Banking");
                    record.setFinalPrice(record.calculateFinalPrice());
                    record.setStatus("Checked-In");
                    record.setReceiptGenerated(true);
                    HouseKeepingController.getInstance().markRoomOccupied("201", record.getFullName(), "Occupied by " + record.getFullName());

                } else if (guest.getTicketNumber() == 10000003) {
                    record.setRoomID("301");
                    record.setPaymentStatus("Paid");
                    record.setPaymentMethod("E-Wallet");
                    record.setFinalPrice(record.calculateFinalPrice());
                    record.setStatus("Checked-In");
                    record.setReceiptGenerated(true);
                    HouseKeepingController.getInstance().markRoomOccupied("301", record.getFullName(), "Occupied by " + record.getFullName());

                } else {
                    record.setStatus(guest.getStatus() != null ? guest.getStatus() : "Served");
                }
                frontDeskMap.put(guest.getTicketNumber(),record);
            }
        }
    }
    
    public FrontDesk getFrontDeskRecord(int ticketNumber) {
        FrontDesk record = frontDeskMap.get(ticketNumber);
        Guest guest = BookingDataController.findGuestByTicket(ticketNumber);

        if (guest == null) {
            guest = guestBST.search(ticketNumber);
        }

        if (guest == null) {
            return record;
        }

        if (record == null) {
            record = new FrontDesk(guest);
            guestBST.add(guest.getTicketNumber(),guest);
            frontDeskMap.put(ticketNumber,record);
        }
        record.setStatus(guest.getStatus());
        return record;
    }

    public Guest searchGuest(int ticketNumber) {
        return guestBST.search(ticketNumber);
    }

    public boolean guestExists(int ticketNumber) {
        return guestBST.contains(ticketNumber);
    }

    public boolean isRoomAssigned(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            return false;
        }
        
        String cleanId = roomId.trim();
        
        for (Map.Entry<Integer, FrontDesk> entry : frontDeskMap.entrySet()) {
            FrontDesk record = entry.getValue();
            if (record != null && "Checked-In".equalsIgnoreCase(record.getStatus())) {
                if (cleanId.equalsIgnoreCase(record.getRoomID())) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getGuestStatus(int ticketNumber) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);
        return record != null ? record.getStatus(): null;
    }

    public String[] getGuestDetails(int ticketNumber) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return null;
        }

        String roomID = record.getRoomID();

        if (roomID == null || roomID.trim().isEmpty()) {
            roomID = "Not Assigned";
        }

        String[] details = new String[12];
        details[0] = String.valueOf(record.getTicketNumber());
        details[1] = record.getFullName();
        details[2] = String.valueOf( record.getGender());
        details[3] = record.getContactNumber();
        details[4] = record.getRoomType();
        details[5] = String.valueOf(record.getNumberOfRooms());
        details[6] = String.valueOf(record.getStayDuration() );
        details[7] = record.getStatus();
        details[8] = roomID;
        details[9] = String.format("%.2f", record.getFinalPrice());
        details[10] =record.getPaymentStatus();
        details[11] =record.getPaymentMethod();
        return details;
    }

    public boolean processPayment(int ticketNumber,String paymentMethod) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return false;
        }
        
        if ("Waiting".equalsIgnoreCase(record.getStatus())) {
            return false;
        }

        if (paymentMethod == null|| paymentMethod.trim().isEmpty()) {
            return false;
        }

        double finalPrice = record.calculateFinalPrice();

        record.setFinalPrice(finalPrice);
        record.setPaymentMethod(paymentMethod);
        record.setPaymentStatus("Paid");

        return true;
    }
    
    public double getBasePrice(int ticketNumber) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return 0.0;
        }

        return record.calculateTotalPrice();
    }

    public double getServiceCharge(int ticketNumber) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return 0.0;
        }

        return record.calculateServiceCharge();
    }

    public double getFinalPrice(int ticketNumber) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return 0.0;
        }

        return record.calculateFinalPrice();
    }

    public String generateReceipt(int ticketNumber) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return null;
        }

        if ("Waiting".equalsIgnoreCase( record.getStatus())) {
            return null;
        }

        if (!"Paid".equalsIgnoreCase(record.getPaymentStatus())) {
            return null;
        }

        String assignedRoom = record.getRoomID();

        if (assignedRoom != null && !assignedRoom.trim().isEmpty()) {
            String hkStatus =
                    HouseKeepingController
                            .getInstance()
                            .getRoomStatus(
                                    assignedRoom
                            );

            if (!entity.Room.STATUS_CLEAN.equalsIgnoreCase(hkStatus)) {
                assignedRoom =HouseKeepingController.getInstance().assignCleanRoom();
            }
        } else {

            assignedRoom =HouseKeepingController.getInstance() .assignCleanRoom();
        }
        if (assignedRoom == null) {
            return null;
        }

        record.setRoomID(assignedRoom);
        record.setReceiptGenerated(true);
        record.setStatus("Checked-In");

        HouseKeepingController.getInstance().markRoomOccupied(
                        assignedRoom,
                        record.getFullName(),
                        "Occupied by "
                                + record.getFullName()
                                + " (Ticket: "
                                + ticketNumber
                                + ")"
                );
        
        if (record.getGuest() != null) {
            frontDeskData.saveGuest(record.getGuest());
        }
        return createReceipt(record);
    }

    private String createReceipt(FrontDesk record) {
        StringBuilder receipt =new StringBuilder();

        receipt.append("\n========================================\n");
        receipt.append("           HOTEL CHECK-IN RECEIPT\n");
        receipt.append( "========================================\n" );
        receipt.append(
                String.format(
                        "Ticket Number : %d%n",
                        record.getTicketNumber()
                )
        );

        receipt.append(
                String.format(
                        "Guest Name    : %s%n",
                        record.getFullName()
                )
        );

        receipt.append(
                String.format(
                        "Room Type     : %s%n",
                        record.getRoomType()
                )
        );

        receipt.append(
                String.format(
                        "Room Number   : %s%n",
                        record.getRoomID()
                )
        );

        receipt.append(
                String.format(
                        "Stay Duration : %d Night(s)%n",
                        record.getStayDuration()
                )
        );

        receipt.append(
                String.format(
                        "Base Price    : RM %.2f%n",
                        record.calculateTotalPrice()
                )
        );

        receipt.append(
                String.format(
                        "Service Charge (10%%)   : RM %.2f%n",
                        record.calculateServiceCharge()
                )
        );

        receipt.append(
                String.format(
                        "Final Price   : RM %.2f%n",
                        record.getFinalPrice()
                )
        );

        receipt.append(
                String.format(
                        "Payment Method: %s%n",
                        record.getPaymentMethod()
                )
        );

        receipt.append(
                String.format(
                        "Payment Status: %s%n",
                        record.getPaymentStatus()
                )
        );

        receipt.append(
                String.format(
                        "Guest Status  : %s%n",
                        record.getStatus()
                )
        );

        receipt.append("========================================\n");
        receipt.append("          Payment Successful!\n");
        receipt.append("          Check-In Successful!\n");
        receipt.append("========================================");
        return receipt.toString();
    }

    public boolean checkOutGuest( int ticketNumber) {
        FrontDesk record =getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return false;
        }

        String roomID =record.getRoomID();

        if (roomID == null|| roomID.trim().isEmpty()) {
            return false;
        }
        record.setStatus(
                "Checked-Out"
        );

        HouseKeepingController
                .getInstance()
                .notifyCheckOut(
                        roomID,
                        "Unassigned",
                        "Room "
                                + roomID
                                + " is Dirty after guest check-out."
                );

        if (record.getGuest() != null) {
            frontDeskData.saveGuest( record.getGuest() );
        }

        return true;
    }
    
    public boolean updateContact( int ticketNumber, String newContact) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return false;
        }

        record.setContactNumber(newContact );

        return true;
    }

    public boolean updateRoomType(int ticketNumber,String newRoomType) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return false;
        }

        record.setRoomType(newRoomType);
        return true;
    }

    public boolean updateStayDuration( int ticketNumber,int newDuration) {
        FrontDesk record = getFrontDeskRecord(ticketNumber);

        if (record == null) {
            return false;
        }

        record.setStayDuration(newDuration);
        return true;
    }

    public boolean deleteGuest( int ticketNumber) {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest != null&& guest.getTicketNumber()== ticketNumber) {
                guestList.remove(i);
                frontDeskMap.remove( ticketNumber);
                guestBST.clear();
                loadGuestsToBST();
                return true;
            }
        }
        return false;
    }

    public void notifyRoomCleaned(String roomId) {
        housekeepingNotifications.add(
                "Room "
                        + roomId
                        + " is cleaned and ready for the next check-in."
        );
    }

    public String[] consumeHousekeepingNotifications() {
        int total = housekeepingNotifications.getNumberOfEntries();
        String[] messages =new String[total];

        for (int i = 1; i <= total; i++) {
            messages[i - 1] = housekeepingNotifications.get(i);
        }
        housekeepingNotifications.clear();
        return messages;
    }
    public String[][] getLostItemsData() {
        HouseKeepingData housekeepingData = new HouseKeepingData();
        LostItem[] items = housekeepingData.initLostItemData();

        if (items == null) {
            return new String[0][5];
        }

        String[][] data = new String[items.length][5];

        for (int i = 0; i < items.length;i++) {
            if (items[i] != null) {
                data[i][0] =items[i].getItemId();
                data[i][1] =items[i].getRoomId();
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

    public Guest[] getCheckedOutGuests() {
        ListInterface<Guest> guestList = BookingDataController .getSharedGuestList();
        int count = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest != null) {
                FrontDesk record =
                        getFrontDeskRecord(guest.getTicketNumber()
                        );

                if (record != null
                        && "Checked-Out"
                        .equalsIgnoreCase(record.getStatus())
                        && record.getRoomID() != null
                        && !record.getRoomID()
                                .trim()
                                .isEmpty()) {

                    count++;
                }
            }
        }

        Guest[] result = new Guest[count];
        int index = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            
            if (guest != null) {
                FrontDesk record = getFrontDeskRecord(guest.getTicketNumber());

                if (record != null
                        && "Checked-Out"
                        .equalsIgnoreCase(
                                record.getStatus()
                        )
                        && record.getRoomID() != null
                        && !record.getRoomID()
                                .trim()
                                .isEmpty()) {

                    result[index++] = guest;
                }
            }
        }
        return result;
    }

    public ListInterface<Guest> getFinalGuestList() {
        return frontDeskData .getFinalGuestList();
    }

    public int getNumberOfGuests() {
        return guestBST.getNumberOfEntries();
    }

    public Guest[] getAllGuests() {
        ListInterface<Guest> guestList =BookingDataController.getSharedGuestList();

        Guest[] guests =
                new Guest[
                        guestList
                                .getNumberOfEntries()
                ];

        for (int i = 1; i <= guestList.getNumberOfEntries();  i++) {
            guests[i - 1] = guestList.get(i);
        }
        return guests;
    }

    public String[][] getAllGuestsData() {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();
        String[][] guests =new String[guestList.getNumberOfEntries()][12];

        for (int i = 1;i <= guestList.getNumberOfEntries();i++) {
            Guest guest = guestList.get(i);

            if (guest == null) {
                continue;
            }

            FrontDesk record = getFrontDeskRecord(guest.getTicketNumber());

            if (record == null) {
                continue;
            }

            String roomID =record.getRoomID();

            if (roomID == null|| roomID.trim().isEmpty()) {
                roomID = "N/A"; }

            guests[i - 1][0] = String.valueOf(record.getTicketNumber());
            guests[i - 1][1] = record.getFullName();
            guests[i - 1][2] = String.valueOf( record.getGender());
            guests[i - 1][3] = record.getContactNumber();
            guests[i - 1][4] =  record.getRoomType();
            guests[i - 1][5] = String.valueOf(record.getNumberOfRooms());
            guests[i - 1][6] = String.valueOf(record.getStayDuration() );
            guests[i - 1][7] = record.getStatus();
            guests[i - 1][8] = roomID;
            guests[i - 1][9] = String.format("%.2f", record.getFinalPrice());
            guests[i - 1][10] = record.getPaymentStatus();
            guests[i - 1][11] = record.getPaymentMethod();
        }
        return guests;
    }
}