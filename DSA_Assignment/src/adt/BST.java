package adt;
//Author : LOW MIN LING

public class BST<T> implements BSTInterface<T> {

    private class TreeNode {

        private int key;
        private T data;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(int key, T data) {
            this.key = key;
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private TreeNode root;
    private int numberOfEntries;

    public BST() {
        root = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean add(int key, T newEntry) {
        if (root == null) {
            root = new TreeNode(key, newEntry);
            numberOfEntries++;
            return true;
        }

        TreeNode currentNode = root;

        while (true) {

            if (key == currentNode.key) {
                return false;
            }

            if (key < currentNode.key) {

                if (currentNode.left == null) {
                    currentNode.left = new TreeNode(key, newEntry);
                    numberOfEntries++;
                    return true;
                }

                currentNode = currentNode.left;

            } else {

                if (currentNode.right == null) {
                    currentNode.right = new TreeNode(key, newEntry);
                    numberOfEntries++;
                    return true;
                }

                currentNode = currentNode.right;
            }
        }
    }

    @Override
    public T search(int key) {

        TreeNode currentNode = root;

        while (currentNode != null) {

            if (key == currentNode.key) {
                return currentNode.data;
            }

            if (key < currentNode.key) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
        }

        return null;
    }

    @Override
    public boolean contains(int key) {
        return search(key) != null;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public void clear() {
        root = null;
        numberOfEntries = 0;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }
}
