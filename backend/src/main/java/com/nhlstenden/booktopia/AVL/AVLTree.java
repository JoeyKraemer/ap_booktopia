package com.nhlstenden.booktopia.AVL;

import org.json.JSONObject;

public class AVLTree<T extends Comparable<T>> {
    private class Node {
        T key;
        JSONObject value;
        int height;
        Node left, right;

        Node(T key, JSONObject value) {
            this.key = key;
            this.value = value;
            height = 1;
        }
    }

    private Node root;

    public AVLTree() {
        root = null;
    }

    // Returns the height of a node.
    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    // Returns the balance factor of a node.
    private int getBalance(Node node) {
        return node == null ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    // Right rotation
    private Node rotateRight(Node node) {
        Node temp = node.left;
        Node temp2 = temp.right;

        // Rotate to the right
        temp.right = node;
        node.left = temp2;

        // Update heights
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        temp.height = 1 + Math.max(getHeight(temp.left), getHeight(temp.right));
        return temp;
    }

    // Left rotation
    private Node rotateLeft(Node node) {
        Node temp = node.right;
        Node temp2 = temp.left;

        // Rotate to the left
        temp.left = node;
        node.right = temp2;

        // Update heights
        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        temp.height = 1 + Math.max(getHeight(temp.left), getHeight(temp.right));
        return temp;
    }

    // Double rotation: left-right.
    private Node leftRightRotation(Node node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    // Double rotation: right-left.
    private Node rightLeftRotation(Node node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    // Rebalance the node if necessary.
    private Node rebalance(Node node) {
        int balance = getBalance(node);
        if (balance > 1) { // Left heavy.
            if (getBalance(node.left) < 0) {
                return leftRightRotation(node);
            } else {
                return rotateRight(node);
            }
        }
        if (balance < -1) { // Right heavy.
            if (getBalance(node.right) > 0) {
                return rightLeftRotation(node);
            } else {
                return rotateLeft(node);
            }
        }
        return node; // Already balanced.
    }

    // Insert method accepting both key and JSON value.
    public void insert(T key, JSONObject value) {
        root = insertRecursive(root, key, value);
    }

    private Node insertRecursive(Node node, T key, JSONObject value) {
        if (node == null) {
            return new Node(key, value);
        }
        if (key.compareTo(node.key) < 0) {
            node.left = insertRecursive(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = insertRecursive(node.right, key, value);
        } else {
            // If the key already exists, update the value.
            node.value = value;
            return node;
        }
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return rebalance(node);
    }

    // Search method returns the JSON value associated with the key.
    public JSONObject search(T key) {
        Node node = searchRecursive(root, key);
        return node != null ? node.value : null;
    }

    private Node searchRecursive(Node node, T key) {
        if (node == null) {
            return null;
        }
        if (key.compareTo(node.key) == 0) {
            return node;
        } else if (key.compareTo(node.key) < 0) {
            return searchRecursive(node.left, key);
        } else {
            return searchRecursive(node.right, key);
        }
    }

    // Delete method removes a node by key.
    public void delete(T key) {
        root = deleteRec(root, key);
    }

    private Node deleteRec(Node node, T key) {
        if (node == null) {
            return node;
        }
        if (key.compareTo(node.key) < 0) {
            node.left = deleteRec(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            node.right = deleteRec(node.right, key);
        } else {
            // Node with one or no child.
            if (node.left == null || node.right == null) {
                Node temp = node.left != null ? node.left : node.right;
                node = (temp == null) ? null : temp;
            } else {
                // Node with two children: Get the inorder successor (smallest in the right subtree).
                Node temp = minValueNode(node.right);
                node.key = temp.key;
                node.value = temp.value;
                node.right = deleteRec(node.right, temp.key);
            }
        }
        if (node == null) {
            return node;
        }
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return rebalance(node);
    }

    // Helper to find the minimum value node in a subtree.
    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // Inorder traversal: prints nodes as JSON objects.
    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(Node node) {
        if (node != null) {
            inorderRec(node.left);
            System.out.println("{ \"key\": " + node.key + ", \"value\": " + node.value.toString() + " }");
            inorderRec(node.right);
        }
    }
    
    /**
     * Performs an inorder traversal and collects all keys in the tree.
     * 
     * @param keys A list to which all keys will be added in sorted order
     */
    public void inOrderTraversal(java.util.List<T> keys) {
        inOrderTraversalRec(root, keys);
    }
    
    private void inOrderTraversalRec(Node node, java.util.List<T> keys) {
        if (node != null) {
            inOrderTraversalRec(node.left, keys);
            keys.add(node.key);
            inOrderTraversalRec(node.right, keys);
        }
    }
    
    /**
     * Performs an inorder traversal and collects both keys and values in the tree.
     * 
     * @param keys A list to which all keys will be added in sorted order
     * @param values A list to which all values will be added in the same order as the keys
     */
    public void inOrderTraversalWithValues(java.util.List<T> keys, java.util.List<JSONObject> values) {
        inOrderTraversalWithValuesRec(root, keys, values);
    }
    
    private void inOrderTraversalWithValuesRec(Node node, java.util.List<T> keys, java.util.List<JSONObject> values) {
        if (node != null) {
            inOrderTraversalWithValuesRec(node.left, keys, values);
            keys.add(node.key);
            values.add(node.value);
            inOrderTraversalWithValuesRec(node.right, keys, values);
        }
    }
}
