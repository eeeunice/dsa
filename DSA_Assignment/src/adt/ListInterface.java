package adt;

<<<<<<< HEAD
=======
import entity.Room;

>>>>>>> c8370ef42b7c08cce802b72853b49e442959cfee
public interface ListInterface<T> {
    public boolean add(T newEntry);
    public boolean add(int newPosition, T newEntry);
    public T remove(int givenPosition);
    public void clear();
    public boolean replace(int givenPosition, T newEntry);
    public T get(int givenPosition);
    public boolean contains(T anEntry);
    public int getNumberOfEntries();
    public boolean isEmpty();
<<<<<<< HEAD
=======

    public T getEntry(int given);
>>>>>>> c8370ef42b7c08cce802b72853b49e442959cfee
}