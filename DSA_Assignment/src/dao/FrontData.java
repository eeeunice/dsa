package dao;
//Author : LOW MIN LING

import adt.LinkedList;
import adt.ListInterface;
import entity.Guest;

public class FrontData {

    private ListInterface<Guest> finalGuestList;

    public FrontData() {
        finalGuestList = new LinkedList<>();
    }

    public void saveGuest(Guest guest) {

        if (guest == null) {
            return;
        }

        // Prevent duplicate ticket number
        for (int i = 1; i <= finalGuestList.getNumberOfEntries(); i++) {

            Guest existing = finalGuestList.get(i);

            if (existing != null
                    && existing.getTicketNumber() == guest.getTicketNumber()) {

                finalGuestList.remove(i);
                break;
            }
        }

        finalGuestList.add(guest);
    }

    public Guest findGuest(int ticketNumber) {

        for (int i = 1; i <= finalGuestList.getNumberOfEntries(); i++) {

            Guest guest = finalGuestList.get(i);

            if (guest != null
                    && guest.getTicketNumber() == ticketNumber) {

                return guest;
            }
        }

        return null;
    }

    public ListInterface<Guest> getFinalGuestList() {
        return finalGuestList;
    }

    public int getNumberOfGuests() {
        return finalGuestList.getNumberOfEntries();
    }
}