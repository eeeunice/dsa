package control;

import adt.BST;
import adt.LinkedList;
import adt.ListInterface;
import entity.Guest;

public class FrontDeskController {
    private static final FrontDeskController INSTANCE = new FrontDeskController();

    private final BST<Guest> guestBST;
    private final ListInterface<String> housekeepingNotifications;

    private FrontDeskController() {
        guestBST = new BST<>();
        housekeepingNotifications = new LinkedList<>();
        loadGuestsToBST();
    }

    public static FrontDeskController getInstance() {
        return INSTANCE;
    }

    private void loadGuestsToBST() {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            guestBST.add(guest.getTicketNumber(), guest);
        }
    }

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

    public boolean checkInGuest(int ticketNumber) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return false;
        }

        // 修改点：assignCleanRoom() 现在不需要传递 roomType 参数
        String assignedRoom = HouseKeepingController.getInstance().assignCleanRoom();
        if (assignedRoom == null) {
            return false;
        }

        guest.setRoomID(assignedRoom);
        guest.setStatus("Checked-In");
        HouseKeepingController.getInstance().markRoomOccupied(
                assignedRoom,
                guest.getFullName(),
                "Occupied by " + guest.getFullName() + " (Ticket: " + ticketNumber + ")"
        );
        return true;
    }

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
                guest.getFullName(),
                "Guest " + guest.getFullName() + " checked out (Ticket: " + ticketNumber + ")"
        );
        return true;
    }

    public boolean updateContact(int ticketNumber, String newContact) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return false;
        }
        guest.setContactNumber(newContact);
        return true;
    }

    public boolean updateRoomType(int ticketNumber, String newRoomType) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return false;
        }
        guest.setRoomType(newRoomType);
        return true;
    }

    public boolean updateStayDuration(int ticketNumber, int newDuration) {
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null) {
            return false;
        }
        guest.setStayDuration(newDuration);
        return true;
    }

    public boolean deleteGuest(int ticketNumber) {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest.getTicketNumber() == ticketNumber) {
                guestList.remove(i);
                guestBST.clear();
                loadGuestsToBST();
                return true;
            }
        }

        return false;
    }

    public void notifyRoomCleaned(String roomId) {
        housekeepingNotifications.add("Room " + roomId + " is cleaned and ready for the next check-in.");
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

    public Guest[] getCheckedOutGuests() {
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();
        int checkedOutCount = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            if (guest != null
                    && "Checked-Out".equalsIgnoreCase(guest.getStatus())
                    && guest.getRoomID() != null
                    && !guest.getRoomID().trim().isEmpty()) {
                checkedOutCount++;
            }
        }

        Guest[] checkedOutGuests = new Guest[checkedOutCount];
        int index = 0;

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);
            if (guest != null
                    && "Checked-Out".equalsIgnoreCase(guest.getStatus())
                    && guest.getRoomID() != null
                    && !guest.getRoomID().trim().isEmpty()) {
                checkedOutGuests[index++] = guest;
            }
        }

        return checkedOutGuests;
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
}