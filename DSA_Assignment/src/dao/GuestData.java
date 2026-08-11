package dao;

import adt.LinkedList;
import adt.ListInterface;
import adt.Node;
import entity.Guest;

public class GuestData {

    // Initializes guest data using linked nodes (Node<Guest>)
    public ListInterface<Guest> initGuestData() {
        ListInterface<Guest> guestList = new LinkedList<>();

        // Create sample guest objects
        Guest g1 = new Guest(10000001, "ANGELINA", 'F', "011-78960653", "Single", 5);
        g1.setStatus("Served");

        Guest g2 = new Guest(10000002, "ELON MUSK", 'M', "011-56782093", "Presidential Suite", 3);
        g2.setStatus("Served");

        Guest g3 = new Guest(10000003, "min ling", 'M', "011-4567890", "Suite", 5);
        g3.setStatus("Served");

        Guest g4 = new Guest(10000004, "Eric Loo ", 'M', "011-2765348", "Presidential Suite", 3);
        g4.setStatus("Waiting");

        Guest g5 = new Guest(10000005, "lim", 'M', "011-67895423", "Double", 5);
        g5.setStatus("Waiting");

        Guest g6 = new Guest(10000006, "Mohamad bin abdullah", 'M', "011-4627890", "Suite", 10);
        g6.setStatus("Waiting");

        // Construct a chain of Linked Nodes
        Node<Guest> n6 = new Node<>(g6, null);
        Node<Guest> n5 = new Node<>(g5, n6);
        Node<Guest> n4 = new Node<>(g4, n5);
        Node<Guest> n3 = new Node<>(g3, n4);
        Node<Guest> n2 = new Node<>(g2, n3);
        Node<Guest> headNode = new Node<>(g1, n2);

        // Traverse the linked nodes and add them into the guest list
        Node<Guest> currentNode = headNode;
        while (currentNode != null) {
            guestList.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }

        return guestList;
    }
}
