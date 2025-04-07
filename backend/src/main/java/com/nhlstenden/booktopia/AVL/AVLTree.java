package com.nhlstenden.booktopia.AVL;

public class AVLTree<T extends Comparable<T>> {
    private Node<T> root;

    public AVLTree() {
        this.root = null;
    }

    // Returns the height of a given node
    private int getHeight(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Returns the balance factor of a node
    private int getBalance(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    // Right rotation
    private Node<T> rotateRight(Node<T> node) {
        Node<T> temp = node.left;
        Node<T> temp2 = temp.right;

        // Rotate to the right
        temp.right = node;
        node.left = temp2;

        // Update heights
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        temp.height = 1 + Math.max(getHeight(temp.left), getHeight(temp.right));
        return temp;
    }

    // Left rotation
    private Node<T> rotateLeft(Node<T> node) {
        Node<T> temp = node.right;
        Node<T> temp2 = temp.left;

        // Rotate to the left
        temp.left = node;
        node.right = temp2;

        // Update heights
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        temp.height = 1 + Math.max(getHeight(temp.left), getHeight(temp.right));
        return temp;
    }

    private Node<T> leftRightRotation(Node<T> node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    private Node<T> rightLeftRotation(Node<T> node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    private Node<T> rebalance(Node<T> node) {
        int balance = getBalance(node);

        if (balance > 1) { // Left heavy
            if (getBalance(node.left) < 0) {
                return leftRightRotation(node);
            } else {
                return rotateRight(node);
            }
        }

        if (balance < -1) { // Right heavy
            if (getBalance(node.right) > 0) {
                return rightLeftRotation(node);
            } else {
                return rotateLeft(node);
            }
        }
        // Already balanced
        return node;
    }

    public void insert (T key) {
        root = insertRecursive(root, key);
    }

    private Node<T> insertRecursive(Node<T> node, T key) {
        if (node == null) {
            return new Node<T>(key);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = insertRecursive(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = insertRecursive(node.right, key);
        } else {
            return node;
        }

        // Update height
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        // Rebalance tree
        return rebalance(node);
    }

    public void delete(T key) {
        root = deleteRec(root, key);
    }

    // Recursive function to delete a key and balance the tree
    private Node<T> deleteRec(Node<T> node, T key) {
        if (node == null) {
            return null;
        }

        if (key.compareTo(node.key) < 0) {
            node.left = deleteRec(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            } else { // Node<T> with two children
                Node<T> temp = minValueNode(node.right);
                node.key = temp.key;
                node.right = deleteRec(node.right, temp.key);
            }
        }

        // Update height
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        // Rebalance tree
        return rebalance(node);
    }

    // Finds the node with the smallest key in a given subtree
    private Node<T> minValueNode(Node<T> node) {
        Node<T> current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    // Public method to search for a key in the AVL tree
    public boolean search(T key) {
        return searchRecursive(root, key);
    }

    // Recursive function to search for a key
    private boolean searchRecursive(Node<T> root, T key) {

        if (root == null) return false;
        if (key.compareTo(root.key) == 0) return true;

        if (key.compareTo(root.key) < 0) {
            return searchRecursive(root.left, key);
        } else {
            return searchRecursive(root.right, key);
        }
    }
}
