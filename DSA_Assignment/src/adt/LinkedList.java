package adt;
//Author : LIM CHUN CHUAN

public class LinkedList<T> implements ListInterface<T> {

    private Node<T> firstNode;
    private int numberOfEntries;

    public LinkedList() {
        firstNode = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean add(T newEntry) {
        Node<T> newNode = new Node<>(newEntry);
        if (isEmpty()) {
            firstNode = newNode;
        } else {
            Node<T> lastNode = getNodeAt(numberOfEntries);
            lastNode.setNext(newNode);
        }
        numberOfEntries++;
        return true;
    }

    @Override
    public boolean add(int newPosition, T newEntry) {
        if (newPosition >= 1 && newPosition <= numberOfEntries + 1) {
            Node<T> newNode = new Node<>(newEntry);
            if (newPosition == 1) {
                newNode.setNext(firstNode);
                firstNode = newNode;
            } else {
                Node<T> nodeBefore = getNodeAt(newPosition - 1);
                Node<T> nodeAfter = nodeBefore.getNext();
                newNode.setNext(nodeAfter);
                nodeBefore.setNext(newNode);
            }
            numberOfEntries++;
            return true;
        }
        return false;
    }

    @Override
    public T remove(int givenPosition) {
        T result = null;
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            if (givenPosition == 1) {
                result = firstNode.getData();
                firstNode = firstNode.getNext();
            } else {
                Node<T> nodeBefore = getNodeAt(givenPosition - 1);
                Node<T> nodeToRemove = nodeBefore.getNext();
                result = nodeToRemove.getData();
                Node<T> nodeAfter = nodeToRemove.getNext();
                nodeBefore.setNext(nodeAfter);
            }
            numberOfEntries--;
            return result;
        }
        return null;
    }

    @Override
    public void clear() {
        firstNode = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry) {
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            Node<T> desiredNode = getNodeAt(givenPosition);
            desiredNode.setData(newEntry);
            return true;
        }
        return false;
    }

    @Override
    public T get(int givenPosition) {
        if (givenPosition >= 1 && givenPosition <= numberOfEntries) {
            return getNodeAt(givenPosition).getData();
        }
        return null;
    }

    @Override
    public boolean contains(T anEntry) {
        Node<T> currentNode = firstNode;
        while (currentNode != null) {
            if (currentNode.getData().equals(anEntry)) {
                return true;
            }
            currentNode = currentNode.getNext();
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

    private Node<T> getNodeAt(int givenPosition) {
        Node<T> currentNode = firstNode;
        for (int counter = 1; counter < givenPosition; counter++) {
            currentNode = currentNode.getNext();
        }
        return currentNode;
    }
}
