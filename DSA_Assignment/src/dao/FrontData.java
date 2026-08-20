package dao;

import adt.LinkedList;
import adt.ListInterface;
import entity.Guest;

public class FrontData {

    private ListInterface<Guest> guestList;

    public FrontData() {
        guestList = new LinkedList<>();
    }

    public ListInterface<Guest> getGuestList() {
        return guestList;
    }

    public void addGuest(Guest guest) {
        guestList.add(guest);
    }

    public Guest findGuest(int ticketNumber) {
        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest.getTicketNumber() == ticketNumber) {
                return guest;
            }
        }

        return null;
    }

    public boolean removeGuest(int ticketNumber) {
        for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
            Guest guest = guestList.get(i);

            if (guest.getTicketNumber() == ticketNumber) {
                guestList.remove(i);
                return true;
            }
        }

        return false;
    }
}