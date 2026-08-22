package adt;
//Author : LIM CHUN CHUAN

public interface QueueInterface<T> {
    public void enqueue(T newEntry);
    public T dequeue();
    public T getFront();
    public boolean isEmpty();
    public void clear();
    public int getNumberOfEntries();
}