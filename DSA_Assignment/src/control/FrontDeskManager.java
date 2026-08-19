package control;

import adt.BST;
import adt.ListInterface;
import entity.Guest;

public class FrontDeskManager{
    private BST<Guest> guestBST;
    private HouseKeepingManager houseKeepingManager; //housekeeping

    public FrontDeskManager(){
        guestBST = new BST<>();
        loadGuestsToBST();
        houseKeepingManager = new HouseKeepingManager(); //housekeeping
    }

    // Load all guests from the guest list into BST
    private void loadGuestsToBST(){
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++){
            Guest guest = guestList.get(i);

            guestBST.add(
                    guest.getTicketNumber(),guest);
        }
    }

    // Search guest using ticket number
    public Guest searchGuest(int ticketNumber){
        return guestBST.search(ticketNumber);
    }

    // Check whether guest exists
    public boolean guestExists(int ticketNumber){
        return guestBST.contains(ticketNumber);
    }

    // Get guest status
    public String getGuestStatus(int ticketNumber){
        Guest guest = guestBST.search(ticketNumber);

        if (guest != null){
            return guest.getStatus();
        }
        return null;
    }

    // Check-in guest
    public boolean checkInGuest(int ticketNumber){
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null){
            return false;
        }
        
        String assignedRoom = houseKeepingManager.assignCleanRoom(guest.getRoomType());
        if (assignedRoom != null) {
            guest.setRoomID(assignedRoom);
            houseKeepingManager.updateRoomStatus(
                assignedRoom, "Clean", guest.getFullName(),
                "Occupied by " + guest.getFullName() + " (Ticket: " + ticketNumber + ")"
            );
        }
        
        guest.setStatus("Checked-In");
        return true;
    }

    // Check-out guest
    public boolean checkOutGuest(int ticketNumber){
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null){
            return false;
        }
        
        String roomID = guest.getRoomID();
        if (roomID != null) {
            houseKeepingManager.notifyCheckOut(roomID,guest.getFullName(),
                "Guest " + guest.getFullName() + " checked out (Ticket: " + ticketNumber + ")"
            );
        }

        guest.setStatus("Checked-Out");
        return true;
    }

    // Update guest contact number
    public boolean updateContact(int ticketNumber, String newContact){
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null){
            return false;
        }
        guest.setContactNumber(newContact);
        return true;
    }

    // Update guest room type
    public boolean updateRoomType(int ticketNumber, String newRoomType){
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null){
            return false;
        }
        guest.setRoomType(newRoomType);
        return true;
    }

    // Update guest stay duration
    public boolean updateStayDuration(int ticketNumber, int newDuration){
        Guest guest = guestBST.search(ticketNumber);

        if (guest == null){
            return false;
        }
        guest.setStayDuration(newDuration);
        return true;
    }

    // Delete guest from the shared guest list
    public boolean deleteGuest(int ticketNumber){
        ListInterface<Guest> guestList = BookingDataController.getSharedGuestList();

        for (int i = 1; i <= guestList.getNumberOfEntries(); i++){
            Guest guest = guestList.get(i);

            if (guest.getTicketNumber() == ticketNumber){
                guestList.remove(i);

                // Rebuild BST after deletion
                guestBST.clear();
                loadGuestsToBST();
                return true;
            }
        }

        return false;
    }
    
    //link housekeeping
    public FrontDeskManager(HouseKeepingManager sharedHKManager){
        guestBST = new BST<>();
        houseKeepingManager = sharedHKManager;
        loadGuestsToBST();
    }

    //housekeeping
    public HouseKeepingManager getHouseKeepingManager() {
        return houseKeepingManager;
    }

    // Get total number of guests
    public int getNumberOfGuests(){
        return guestBST.getNumberOfEntries();
    }

    // Get all guests
    public ListInterface<Guest> getAllGuests(){
        return BookingDataController.getSharedGuestList();
    }
}