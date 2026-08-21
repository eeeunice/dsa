package control;

//Author : LIM CHUN CHUAN

import adt.ArrayQueue;
import adt.QueueInterface;
import adt.ListInterface;
import dao.GuestData;
import entity.Guest;
import utility.ClearScreen;
import utility.Header;
import java.util.HashMap;    
import java.util.Map;

public class BookingDataController {
    // design
    public static final String RESET      = Header.RESET;
    public static final String DARK_BLUE  = Header.DARK_BLUE;
    public static final String PURPLE     = Header.PURPLE;
    public static final String YELLOW     = Header.YELLOW;
    public static final String RED        = Header.RED;
    public static final String GREEN      = Header.GREEN;

    public static void clearScreen() {
        ClearScreen.clear();
    }

    public static void printHeader() {
        Header.printHeader();
    }

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

    //share to other
    public static ListInterface<Guest> getSharedGuestList() {
        return sharedGuestList;
    }

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

    //add
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

    //delete
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

    
    public static int registerGuest(String fullName, char gender, String contactNumber, String roomType, int numberOfRooms, int stayDuration) {
        int ticketNumber = generateNextTicketNumber();
        Guest newGuest = new Guest(ticketNumber, fullName, gender, contactNumber, roomType, numberOfRooms, stayDuration);
        addGuest(newGuest);
        return ticketNumber;
    }

    public static double getGuestTotalPrice(int ticketNumber) {
        Guest g = findGuestByTicket(ticketNumber);
        return (g != null) ? g.calculateTotalPrice() : 0.0;
    }

    public static String serveNextGuestInfo() {
        Guest processed = serveNextGuest();
        if (processed != null) {
            return processed.getFullName() + " (Ticket: " + processed.getTicketNumber() + ")";
        }
        return null;
    }

    public static int getNextQueueGuestTicketNumber() {
        Guest g = getNextQueueGuest();
        return (g != null) ? g.getTicketNumber() : 0;
    }

    public static String getNextQueueGuestFullName() {
        Guest g = getNextQueueGuest();
        return (g != null) ? g.getFullName() : "";
    }

    public static String getNextQueueGuestRoomType() {
        Guest g = getNextQueueGuest();
        return (g != null) ? g.getRoomType() : "";
    }

    public static int getNextQueueGuestNumberOfRooms() {
        Guest g = getNextQueueGuest();
        return (g != null) ? g.getNumberOfRooms() : 0;
    }

    public static int getNextQueueGuestStayDuration() {
        Guest g = getNextQueueGuest();
        return (g != null) ? g.getStayDuration() : 0;
    }

    public static double getNextQueueGuestTotalPrice() {
        Guest g = getNextQueueGuest();
        return (g != null) ? g.calculateTotalPrice() : 0.0;
    }

    public static String getNextQueueGuestContactNumber() {
        Guest g = getNextQueueGuest();
        return (g != null) ? g.getContactNumber() : "";
    }

    //Display all the guest in the node
    public static void displayAllGuests() {
        if (hasNoGuests()) {
            System.out.println("No guest records found in the system or file.");
        } else {
            System.out.printf("%-10s | %-24s | %-6s | %-12s | %-18s | %-6s | %-8s | %-12s | %-10s%n", 
                    "Ticket", "Full Name", "Gender", "Contact", "Room", "Rooms", "Nights", "Total (RM)", "Status");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------");
            for (int i = 1; i <= sharedGuestList.getNumberOfEntries(); i++) {
                Guest g = sharedGuestList.get(i);
                System.out.printf("%-10d | %-24s | %-6s | %-12s | %-18s | %-6d | %-8d | RM %-9.2f | %-10s%n",
                        g.getTicketNumber(), g.getFullName(), g.getGender(), 
                        g.getContactNumber(), g.getRoomType(), g.getNumberOfRooms(), g.getStayDuration(), g.calculateTotalPrice(), g.getStatus());
            }
        }
    }

    public static boolean guestExists(int ticketNumber) {
        return findGuestByTicket(ticketNumber) != null;
    }

    public static String getGuestName(int ticketNumber) {
        Guest g = findGuestByTicket(ticketNumber);
        return (g != null) ? g.getFullName() : "";
    }

    public static String getGuestRoomType(int ticketNumber) {
        Guest g = findGuestByTicket(ticketNumber);
        return (g != null) ? g.getRoomType() : "";
    }

    public static String getGuestStatus(int ticketNumber) {
        Guest g = findGuestByTicket(ticketNumber);
        return (g != null) ? g.getStatus() : "";
    }

    //update
    public static boolean updateGuestContact(int ticketNumber, String contactNumber) {
        Guest g = findGuestByTicket(ticketNumber);
        if (g != null) {
            g.setContactNumber(contactNumber);
            refreshQueueFromList();
            return true;
        }
        return false;
    }

    public static boolean updateGuestRoomType(int ticketNumber, String roomType) {
        Guest g = findGuestByTicket(ticketNumber);
        if (g != null) {
            g.setRoomType(roomType);
            refreshQueueFromList();
            return true;
        }
        return false;
    }

    public static boolean updateGuestNumberOfRooms(int ticketNumber, int numberOfRooms) {
        Guest g = findGuestByTicket(ticketNumber);
        if (g != null) {
            g.setNumberOfRooms(numberOfRooms);
            refreshQueueFromList();
            return true;
        }
        return false;
    }

    public static boolean updateGuestStayDuration(int ticketNumber, int stayDuration) {
        Guest g = findGuestByTicket(ticketNumber);
        if (g != null) {
            g.setStayDuration(stayDuration);
            refreshQueueFromList();
            return true;
        }
        return false;
    }

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