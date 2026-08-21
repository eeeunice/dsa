package dao;

import adt.LinkedList;
import adt.ListInterface;
import entity.Guest; 

public class GuestData {

    public ListInterface<Guest> initGuestData() {
        ListInterface<Guest> guestList = new LinkedList<>();

        Guest g1 = new Guest(10000001, "ANGELINA", 'F', "011-78960653", "Single", 1, 5);
        g1.setStatus("Served");
        guestList.add(g1);

        Guest g2 = new Guest(10000002, "ELON MUSK", 'M', "011-56782093", "Presidential Suite", 2, 3);
        g2.setStatus("Served");
        guestList.add(g2);

        Guest g3 = new Guest(10000003, "min ling", 'M', "011-45678490", "Suite", 1, 5);
        g3.setStatus("Served");
        guestList.add(g3);

        Guest g4 = new Guest(10000004, "Eric Loo ", 'M', "011-27652348", "Presidential Suite", 1, 3);
        g4.setStatus("Served");
        guestList.add(g4);

        Guest g5 = new Guest(10000005, "lim", 'M', "011-67895423", "Double", 2, 5);
        g5.setStatus("Served");
        guestList.add(g5);

        Guest g6 = new Guest(10000006, "Mohamad bin abdullah", 'M', "012-4627290", "Suite", 1, 10);
        g6.setStatus("Served");
        guestList.add(g6);
        
        Guest g7 = new Guest(10000007, "Ong Zi YU", 'F', "011-24627096", "Presidential Suite", 1, 7);
        g7.setStatus("Served");
        guestList.add(g7);
        
        Guest g8 = new Guest(10000008, "Syamel Aiman", 'M', "014-5558890", "Suite", 2, 10);
        g8.setStatus("Served");
        guestList.add(g8);
        
        Guest g9 = new Guest(10000009, "Beh Bing Hong", 'M', "016-7620990", "Suite", 1, 4);
        g9.setStatus("Served");
        guestList.add(g9);
        
        Guest g10 = new Guest(10000010, "Tan Bee Choo", 'F', "011-88889232", "Double", 1, 1);
        g10.setStatus("Waiting");
        guestList.add(g10);

        return guestList;
    }
}