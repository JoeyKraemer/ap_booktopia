package com.nhlstenden.booktopia.BST;

import java.util.Comparator;
import java.util.List;
import org.json.JSONObject;

public class BinarySearchTree<T> {
    private Node root;
    private Comparator<T> comparator;

    // Constructor accepts a Comparator to compare keys
    public BinarySearchTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // Node class stores key and value, with value represented as a JSONObject.
    private class Node {
        T key;
        JSONObject value;
        Node left, right;

        Node(T key, JSONObject value) {
            this.key = key;
            this.value = value;
            left = right = null;
        }
    }

    // Insert method accepting both key and JSON value.
    public void insert(T key, JSONObject value) {
        root = insertRec(root, key, value);
    }

    private Node insertRec(Node root, T key, JSONObject value) {
        if (root == null) {
            return new Node(key, value);
        }

        int cmp = comparator.compare(key, root.key);
        if (cmp < 0) {
            root.left = insertRec(root.left, key, value);
        } else if (cmp > 0) {
            root.right = insertRec(root.right, key, value);
        } else {
            // If the key already exists, update the value.
            root.value = value;
        }
        return root;
    }

    // Search method returns the JSON value for a given key.
    public JSONObject search(T key) {
        Node result = searchRec(root, key);
        return result != null ? result.value : null;
    }

    private Node searchRec(Node root, T key) {
        if (root == null) {
            return null;
        }
        int cmp = comparator.compare(key, root.key);
        if (cmp == 0) {
            return root;
        } else if (cmp < 0) {
            return searchRec(root.left, key);
        } else {
            return searchRec(root.right, key);
        }
    }

    // Delete method removes a node based on its key.
    public void delete(T key) {
        root = deleteRec(root, key);
    }

    private Node deleteRec(Node root, T key) {
        if (root == null) {
            return null;
        }

        int cmp = comparator.compare(key, root.key);
        if (cmp < 0) {
            root.left = deleteRec(root.left, key);
        } else if (cmp > 0) {
            root.right = deleteRec(root.right, key);
        } else {
            // Node with only one child or no child.
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            Node temp = findMin(root.right);
            root.key = temp.key;
            root.value = temp.value;
            // Delete the inorder successor.
            root.right = deleteRec(root.right, temp.key);
        }
        return root;
    }

    // Helper method to find the minimum value node in a given subtree.
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // Inorder Traversal: prints each node as a JSON object.
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
    public void inOrderTraversal(List<T> keys) {
        inOrderTraversalRec(root, keys);
    }
    
    private void inOrderTraversalRec(Node node, List<T> keys) {
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
    public void inOrderTraversalWithValues(List<T> keys, List<JSONObject> values) {
        inOrderTraversalWithValuesRec(root, keys, values);
    }
    
    private void inOrderTraversalWithValuesRec(Node node, List<T> keys, List<JSONObject> values) {
        if (node != null) {
            inOrderTraversalWithValuesRec(node.left, keys, values);
            keys.add(node.key);
            values.add(node.value);
            inOrderTraversalWithValuesRec(node.right, keys, values);
        }
    }

    public void clear() {
        root = null;
    }
}
