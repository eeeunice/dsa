package control;

import adt.ArrayQueue;
import adt.QueueInterface;
import adt.ListInterface;
import dao.GuestData;
import entity.Guest;
import java.util.HashMap;    
import java.util.Map;

public class BookingDataController {
    private static ListInterface<Guest> sharedGuestList;
    private static QueueInterface<Guest> guestQueue = new ArrayQueue<>();
    private static Map<Integer, Guest> guestHashMap = new HashMap<>(); 
    private static GuestData guestData = new GuestData();

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

    // --- Accessor for shared list (used by reports or other modules) ---
    public static ListInterface<Guest> getSharedGuestList() {
        return sharedGuestList;
    }

    // --- Helper methods for UI without exposing ADTs directly ---

    public static int getQueueSize() {
        return guestQueue.getNumberOfEntries();
    }

    public static boolean isQueueEmpty() {
        return guestQueue.isEmpty();
    }

    public static Guest getNextQueueGuest() {
        if (guestQueue.isEmpty()) {
            return null;
        }
        return guestQueue.getFront();
    }

    public static int getTotalGuestCount() {
        return sharedGuestList.getNumberOfEntries();
    }

    public static Guest getGuestAt(int index) {
        return sharedGuestList.get(index);
    }

    public static boolean hasNoGuests() {
        return sharedGuestList.isEmpty();
    }

    public static void addGuest(Guest guest) {
        sharedGuestList.add(guest);
        guestQueue.enqueue(guest);
        guestHashMap.put(guest.getTicketNumber(), guest);
    }

    public static Guest findGuestByTicket(int ticketNumber) {
        return guestHashMap.get(ticketNumber);
    }

    public static int generateNextTicketNumber() {
        int maxId = 10000000;
        for (int i = 1; i <= sharedGuestList.getNumberOfEntries(); i++) {
            int tNum = sharedGuestList.get(i).getTicketNumber();
            if (tNum > maxId) {
                maxId = tNum;
            }
        }
        return maxId + 1;
    }

    public static Guest serveNextGuest() {
        if (guestQueue.isEmpty()) {
            return null;
        }
        Guest processed = guestQueue.dequeue();
        
        for (int i = 1; i <= sharedGuestList.getNumberOfEntries(); i++) {
            Guest g = sharedGuestList.get(i);
            if (g.getTicketNumber() == processed.getTicketNumber()) {
                g.setStatus("Served");
                break;
            }
        }
        
        refreshQueueFromList();
        return processed;
    }

    public static boolean cancelReservation(int ticketNumber) {
        for (int i = 1; i <= sharedGuestList.getNumberOfEntries(); i++) {
            Guest g = sharedGuestList.get(i);
            if (g.getTicketNumber() == ticketNumber) {
                sharedGuestList.remove(i);
                guestHashMap.remove(ticketNumber);
                refreshQueueFromList();
                return true;
            }
        }
        return false;
    }

    public static void refreshQueueFromList() {
        guestQueue.clear();
        for (int j = 1; j <= sharedGuestList.getNumberOfEntries(); j++) {
            Guest g = sharedGuestList.get(j);
            if ("Waiting".equalsIgnoreCase(g.getStatus())) {
                guestQueue.enqueue(g);
            }
        }
    }

    // --- Inner helper class and method for Room Statistics Reports ---
    public static class RoomStat {
        public String roomType;
        public int personBookingCount;

        public RoomStat(String roomType) {
            this.roomType = roomType;
            this.personBookingCount = 0;
        }
    }

    public static RoomStat[] getRoomBookingStats() {
        RoomStat[] stats = new RoomStat[] {
            new RoomStat("Single"),
            new RoomStat("Double"),
            new RoomStat("Suite"),
            new RoomStat("Presidential Suite")
        };

        if (sharedGuestList != null) {
            for (int i = 1; i <= sharedGuestList.getNumberOfEntries(); i++) {
                Guest g = sharedGuestList.get(i);
                if (g != null && g.getRoomType() != null) {
                    for (RoomStat stat : stats) {
                        if (stat.roomType.equalsIgnoreCase(g.getRoomType())) {
                            stat.personBookingCount++;
                            break;
                        }
                    }
                }
            }
        }

        // Sort stats descending by personBookingCount
        for (int i = 0; i < stats.length - 1; i++) {
            for (int j = 0; j < stats.length - 1 - i; j++) {
                if (stats[j].personBookingCount < stats[j + 1].personBookingCount) {
                    RoomStat temp = stats[j];
                    stats[j] = stats[j + 1];
                    stats[j + 1] = temp;
                }
            }
        }

        return stats;
    }
}