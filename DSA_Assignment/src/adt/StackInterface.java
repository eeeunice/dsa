package adt;
//Author : EUNICE LIM NI-XI

public interface StackInterface<T> {
    void push(T newEntry);
    T pop();
    T peek();
    boolean isEmpty();
    void clear();
    int getNumberOfEntries();
}
