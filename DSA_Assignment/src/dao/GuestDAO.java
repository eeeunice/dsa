package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.Guest;
import java.io.*;

public class GuestDAO {
    // Update path so it saves directly inside your dataFile folder
    private static final String FILE_NAME = "src/dataFile/guest";

    public void saveToFile(ListInterface<Guest> guestList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= guestList.getNumberOfEntries(); i++) {
                Guest g = guestList.get(i);
                writer.println(g.getTicketNumber() + ";" + g.getFullName() + ";" + g.getGender() + ";" + g.getContactNumber() + ";" + g.getRoomType() + ";" + g.getStayDuration() + ";" + g.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public ListInterface<Guest> loadFromFile() {
        ListInterface<Guest> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 7) {
                    char gender = data[2].isEmpty() ? 'U' : data[2].charAt(0);
                    Guest g = new Guest(Integer.parseInt(data[0]), data[1], gender, data[3], data[4], Integer.parseInt(data[5]));
                    g.setStatus(data[6]);
                    list.add(g);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return list;
    }
}