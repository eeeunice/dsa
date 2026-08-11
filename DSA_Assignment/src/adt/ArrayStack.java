package adt;

public class ArrayStack<T> implements StackInterface<T> {
    private T[] array;
    private int topIndex;
    private static final int DEFAULT_CAPACITY = 50;

    public ArrayStack() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayStack(int initialCapacity) {
        @SuppressWarnings("unchecked")
        T[] tempStack = (T[]) new Object[initialCapacity];
        array = tempStack;
        topIndex = -1;
    }

    @Override
    public void push(T newEntry) {
        topIndex++;
        if (topIndex >= array.length) {
            doubleArray();
        }
        array[topIndex] = newEntry;
    }

    @Override
    public T pop() {
        if (isEmpty()) {
            return null;
        } else {
            T top = array[topIndex];
            array[topIndex] = null;
            topIndex--;
            return top;
        }
    }

    @Override
    public T peek() {
        if (isEmpty()) return null;
        return array[topIndex];
    }

    @Override
    public boolean isEmpty() {
        return topIndex < 0;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }

    @Override
    public int getNumberOfEntries() {
        return topIndex + 1;
    }
    
    private void doubleArray() {
        @SuppressWarnings("unchecked")
        T[] oldArray = array;
        T[] newArray = (T[]) new Object[2 * oldArray.length];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        array = newArray;
    }
}