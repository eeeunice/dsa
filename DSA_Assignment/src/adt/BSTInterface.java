package adt;

//Author : LOW MIN LING

public interface BSTInterface<T> {

    public boolean add(int key, T newEntry);

    public T search(int key);

    public boolean contains(int key);

    public boolean isEmpty();

    public void clear();

    public int getNumberOfEntries();
}