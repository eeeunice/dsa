package control;

import adt.BST;
import adt.ListInterface;
import entity.Guest;

public class FrontDeskManager {

    private BST<Guest> guestBST;

    public FrontDeskManager() {
        guestBST = new BST<>();
        loadGuestsToBST();
    }

    private void loadGuestsToBST() {

        ListInterface<Guest> guestList =
                HotelDataController.getSharedGuestList();

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {

            Guest guest = guestList.get(i);

            guestBST.add(
                    guest.getTicketNumber(),
                    guest
            );
        }
    }

    // Search guest using ticket number
    public Guest searchGuest(int ticketNumber) {
        return guestBST.search(ticketNumber);
    }

    // Check whether guest exists
    public boolean guestExists(int ticketNumber) {
        return guestBST.contains(ticketNumber);
    }

    // Get guest status
    public String getGuestStatus(int ticketNumber) {

        Guest guest = guestBST.search(ticketNumber);

        if (guest != null) {
            return guest.getStatus();
        }

        return null;
    }

    // Get total number of guests
    public int getNumberOfGuests() {
        return guestBST.getNumberOfEntries();
    }
}

