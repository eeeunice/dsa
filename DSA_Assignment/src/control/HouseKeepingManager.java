package control;

import adt.ArrayList;
import adt.ListInterface;
import adt.ArrayStack;
import adt.StackInterface;
import entity.Room;

public class HouseKeepingManager {
    
    // Master list of rooms (CRUD)
    private ListInterface<Room> roomList;
    
    // Stack to track previous statuses for the Undo feature
    private StackInterface<String> historyStack;

    public HouseKeepingManager() {
        this.roomList = new ArrayList<>();
        this.historyStack = new ArrayStack<>();
    }

    // --- CREATE ---
    public boolean addRoom(String roomId) {
        if (findRoom(roomId) != null) {
            return false; // Room already exists
        }
        roomList.add(new Room(roomId));
        return true;
    }

    // --- READ ---
    public String getAllRoomsAsString() {
        if (roomList.isEmpty()) {
            return "No rooms in the system.";
        }
        
        StringBuilder output = new StringBuilder();
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room room = roomList.getEntry(i);
            output.append("Room ID: ").append(room.getRoomId())
                  .append(" | Status: ").append(room.getStatus())
                  .append("\n");
        }
        return output.toString();
    }

    // --- UPDATE (With Rollback Support) ---
    public boolean updateRoomStatus(String roomId, String newStatus) {
        Room room = findRoom(roomId);
        if (room != null) {
            // Save current state to stack before changing it (Format: "RoomID,OldStatus")
            String currentState = room.getRoomId() + "," + room.getStatus();
            historyStack.push(currentState);
            
            // Apply new status
            room.setStatus(newStatus);
            return true;
        }
        return false;
    }

    // --- UNDO (The Rollback Feature) ---
    public boolean undoLastStatusUpdate() {
        if (historyStack.isEmpty()) {
            return false; // Nothing to undo
        }
        
        String lastAction = historyStack.pop();
        String[] parts = lastAction.split(","); // Split the saved string
        String roomId = parts[0];
        String previousStatus = parts[1];
        
        Room room = findRoom(roomId);
        if (room != null) {
            room.setStatus(previousStatus); // Revert status
            return true;
        }
        return false;
    }

    // --- DELETE ---
    public boolean deleteRoom(String roomId) {
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room room = roomList.getEntry(i);
            if (room.getRoomId().equals(roomId)) {
                roomList.remove(i);
                return true;
            }
        }
        return false;
    }

    // Helper method
    private Room findRoom(String roomId) {
        for (int i = 1; i <= roomList.getNumberOfEntries(); i++) {
            Room room = roomList.getEntry(i);
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }
}