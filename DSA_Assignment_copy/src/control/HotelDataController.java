package control;

import adt.ArrayQueue;
import adt.QueueInterface;
import adt.ArrayList;
import adt.ListInterface;
import dao.GuestDAO;
import entity.Guest;
import java.util.HashMap;   
import java.util.Map;

public class HotelDataController {
    private static ListInterface<Guest> sharedGuestList = new ArrayList<>();
    private static QueueInterface<Guest> guestQueue = new ArrayQueue<>();
    private static Map<Integer, Guest> guestHashMap = new HashMap<>(); 
    private static GuestDAO guestDAO = new GuestDAO();

    // Static initializer block loads saved text file data automatically on boot
    static {
        sharedGuestList = guestDAO.loadFromFile();
        for (int i = 1; i <= sharedGuestList.getNumberOfEntries(); i++) {
            Guest g = sharedGuestList.get(i);
            guestHashMap.put(g.getTicketNumber(), g);
            
            // CRITICAL FIX: Only enqueue guests who are still waiting!
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