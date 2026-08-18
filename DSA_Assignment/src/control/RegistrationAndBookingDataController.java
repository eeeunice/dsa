package control;

import adt.ArrayQueue;
import adt.QueueInterface;
import adt.ListInterface;
import dao.GuestData;
import entity.Guest;
import java.util.HashMap;   
import java.util.Map;

public class RegistrationAndBookingDataController {
    private static ListInterface<Guest> sharedGuestList;
    private static QueueInterface<Guest> guestQueue = new ArrayQueue<>();
    private static Map<Integer, Guest> guestHashMap = new HashMap<>(); 
    private static GuestData guestData = new GuestData();

    // Static initializer block loads guest data via GuestData linked node initialization
    static {
        sharedGuestList = guestData.initGuestData();
        for (int i = 1; i <= sharedGuestList.getNumberOfEntries(); i++) {
            Guest g = sharedGuestList.get(i);
            guestHashMap.put(g.getTicketNumber(), g);
            
            if ("Waiting".equalsIgnoreCase(g.getStatus())) {
                guestQueue.enqueue(g);
            }
        }
    }

    public static ListInterface<Guest> getSharedGuestList() {
        return sharedGuestList;
    }

    public static QueueInterface<Guest> getGuestQueue() {
        return guestQueue;
    }

    public static void addGuest(Guest guest) {
        sharedGuestList.add(guest);
        guestQueue.enqueue(guest);
        guestHashMap.put(guest.getTicketNumber(), guest);
    }

    public static Guest findGuestByTicket(int ticketNumber) {
        return guestHashMap.get(ticketNumber);
    }
}