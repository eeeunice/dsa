package dao;

import adt.LinkedList;
import adt.ListInterface;
import adt.Node;
import entity.Guest;

public class GuestData {

    public ListInterface<Guest> initGuestData() {
        ListInterface<Guest> guestList = new LinkedList<>();

        // Updated constructor parameters: (ticket, name, gender, contact, roomType, numberOfRooms, stayDuration)
        Guest g1 = new Guest(10000001, "ANGELINA", 'F', "011-78960653", "Single", 1, 5);
        g1.setStatus("Served");

        Guest g2 = new Guest(10000002, "ELON MUSK", 'M', "011-56782093", "Presidential Suite", 2, 3);
        g2.setStatus("Served");

        Guest g3 = new Guest(10000003, "min ling", 'M', "011-45678490", "Suite", 1, 5);
        g3.setStatus("Served");

        Guest g4 = new Guest(10000004, "Eric Loo ", 'M', "011-27652348", "Presidential Suite", 1, 3);
        g4.setStatus("Waiting");

        Guest g5 = new Guest(10000005, "lim", 'M', "011-67895423", "Double", 2, 5);
        g5.setStatus("Waiting");

        Guest g6 = new Guest(10000006, "Mohamad bin abdullah", 'M', "012-4627290", "Suite", 1, 10);
        g6.setStatus("Waiting");
        
        Guest g7 = new Guest(10000007, "Ong Zi YU", 'F', "011-24627096", "Presidential Suite", 1, 7);
        g7.setStatus("Waiting");
        
        Guest g8 = new Guest(10000008, "Syamel Aiman", 'M', "014-5558890", "Suite", 2, 10);
        g8.setStatus("Waiting");
        
        Guest g9 = new Guest(10000009, "Beh Bing Hong", 'M', "016-7620990", "Suite", 1, 4);
        g9.setStatus("Waiting");
        
        Guest g10 = new Guest(10000010, "Tan Bee Choo", 'F', "011-88889232", "Double", 1, 1);
        g10.setStatus("Waiting");

        Node<Guest> n10 = new Node<>(g10, null);
        Node<Guest> n9 = new Node<>(g9, n10);
        Node<Guest> n8 = new Node<>(g8, n9);
        Node<Guest> n7 = new Node<>(g7, n8);
        Node<Guest> n6 = new Node<>(g6, n7);
        Node<Guest> n5 = new Node<>(g5, n6);
        Node<Guest> n4 = new Node<>(g4, n5);
        Node<Guest> n3 = new Node<>(g3, n4);
        Node<Guest> n2 = new Node<>(g2, n3);
        Node<Guest> headNode = new Node<>(g1, n2);

        Node<Guest> currentNode = headNode;
        while (currentNode != null) {
            guestList.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }

        return guestList;
    }
}