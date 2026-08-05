package adt;

import entity.Room;

public class ArrayList<T> implements ListInterface<T> {
    private T[] list;
    private int numberOfEntries;
    private static final int DEFAULT_CAPACITY = 50;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayList(int initialCapacity) {
        list = (T[]) new Object[initialCapacity];
        numberOfEntries = 0;
    }

    @Override
    public boolean add(T newEntry) {
        if (numberOfEntries >= list.length) {
            // Optional: expand array capacity if needed
        }
        list[numberOfEntries] = newEntry;
        numberOfEntries++;
        return true;
    }

    @Override
    public boolean add(int newPosition, T newEntry) {
        if (newPosition >= 1 && newPosition <= numberOfEntries + 1) {
            for (int i = numberOfEntries; i >= newPosition; i--) {
                list[i] = list[i - 1];
            }
            list[newPosition - 1] = newEntry;
            numberOfEntries++;
            return true;
        }
        return false;
    }

    @Override
    public T remove(int givenPosition) {
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            T result = list[givenPosition - 1];
            for (int i = givenPosition - 1; i < numberOfEntries - 1; i++) {
                list[i] = list[i + 1];
            }
            numberOfEntries--;
            return result;
        }
        return null;
    }

    @Override
    public void clear() {
        numberOfEntries = 0;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            list[givenPosition - 1] = newEntry;
            return true;
        }
        return false;
    }

    @Override
    public T get(int givenPosition) {
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            return list[givenPosition - 1];
        }
        return null;
    }

    @Override
    public boolean contains(T anEntry) {
        for (int i = 0; i < numberOfEntries; i++) {
            if (list[i].equals(anEntry)) return true;
        }
        return false;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public Room getEntry(int i) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}