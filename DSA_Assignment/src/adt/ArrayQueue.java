package adt;

public class ArrayQueue<T> implements QueueInterface<T> {
    private T[] queue;
    private int frontIndex;
    private int backIndex;
    private static final int DEFAULT_CAPACITY = 50;
    
@Override
    public int getNumberOfEntries() {
        if (isEmpty()) {
            return 0;
        } else if (backIndex >= frontIndex) {
            return (backIndex - frontIndex) + 1;
        } else {
            return (queue.length - frontIndex) + (backIndex + 1);
        }
    }
    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }
    

    @SuppressWarnings("unchecked")
    public ArrayQueue(int initialCapacity) {
        queue = (T[]) new Object[initialCapacity];
        frontIndex = 0;
        backIndex = -1;
    }

    @Override
    public void enqueue(T newEntry) {
        backIndex = (backIndex + 1) % queue.length;
        queue[backIndex] = newEntry;
    }

    @Override
    public T dequeue() {
        if (isEmpty()) {
            return null;
        } else {
            T front = queue[frontIndex];
            queue[frontIndex] = null;
            frontIndex = (frontIndex + 1) % queue.length;
            return front;
        }
    }

    @Override
    public T getFront() {
        if (isEmpty()) {
            return null;
        } else {
            return queue[frontIndex];
        }
    }

    @Override
    public boolean isEmpty() {
        return frontIndex == (backIndex + 1) % queue.length;
    }

    @Override
    public void clear() {
        @SuppressWarnings("unchecked")
        T[] tempQueue = (T[]) new Object[queue.length];
        queue = tempQueue;
        frontIndex = 0;
        backIndex = -1;
    }
}