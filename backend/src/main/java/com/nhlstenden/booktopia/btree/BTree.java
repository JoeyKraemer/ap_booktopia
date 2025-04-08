package com.nhlstenden.booktopia.btree;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

/**
 * A B-Tree data structure implementation in Java.
 * 
 * This class provides methods for inserting, deleting, and searching key-value pairs in a B-Tree.
 * 
 * @param <K> The type of the keys in the B-Tree.
 * @param <V> The type of the values in the B-Tree.
 */
public class BTree<K extends Comparable<K>, V> {
    private BTreeNode<K, V> root;
    private int t;

    public BTree(int t) {
        this.t = t;
        root = null;
    }

    protected BTreeNode<K, V> getRoot() {
        return root;
    }

    public BTreeNode<K, V> search(K key) {
        return (root == null) ? null : root.search(key);
    }

    /**
    * Search for a value in the BTree and return it if found
    * @param key The key to search for
    * @return The value associated with the key, or null if not found
    */
    public V searchValue(K key) {
        BTreeNode<K, V> node = search(key);
        if (node == null) {
            return null;
        }
        
        // Find the key in the node
        for (int i = 0; i < node.getN(); i++) {
            if (node.getKeys()[i].equals(key)) {
                try {
                    return (V) node.getValues().get(key.toString());
                } catch (Exception e) {
                    // Key exists in the tree but value might not be properly stored
                    return null;
                }
            }
        }
        return null;
    }

    /**
    * Get all keys in the BTree in sorted order
    * @return List of all keys in sorted order
    */
    public List<K> getSortedKeys() {
        List<K> sortedKeys = new ArrayList<>();
        if (root == null) {
            return sortedKeys;
        }

        collectKeysInOrder(root, sortedKeys);
        return sortedKeys;
    }

    private void collectKeysInOrder(BTreeNode<K, V> node, List<K> sortedKeys) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < node.getN(); i++) {
            // First visit the left child
            if (!node.isLeaf()) {
                collectKeysInOrder(node.getChildren()[i], sortedKeys);
            }
            
            // Then add the current key
            sortedKeys.add(node.getKeys()[i]);
        }

        // Visit the rightmost child
        if (!node.isLeaf()) {
            collectKeysInOrder(node.getChildren()[node.getN()], sortedKeys);
        }
    }

    /**
     * Get all values in the BTree in sorted key order
     * @return List of values sorted by their keys
     */
    public List<V> getSortedValues() {
        List<V> sortedValues = new ArrayList<>();
        List<K> sortedKeys = getSortedKeys();
        
        for (K key : sortedKeys) {
            V value = searchValue(key);
            if (value != null) {
                sortedValues.add(value);
            }
        }
        
        return sortedValues;
    }
    
    /**
     * Insert a key-value pair into the BTree
     * Time Complexity: O(t * log_t(n)) where t is the minimum degree and n is the number of keys
     * 
     * The insertion process ensures that:
     * 1. All nodes (except root) have at least t-1 keys and at most 2t-1 keys
     * 2. All leaf nodes are at the same level
     * 3. The tree remains balanced after insertion
     * 
     * @param key The key to insert
     * @param value The value associated with the key
     */
    public void insert(K key, V value) {
        if (root == null) {
            root = new BTreeNode<>(t);
            root.getKeys()[0] = key;
            root.setN(1);
        } else {
            if (root.getN() == 2 * t - 1) {
                // Case 2: Root is full, need to split it
                // Create a new root and make the old root its child
                BTreeNode<K, V> newRoot = new BTreeNode<>(t);
                newRoot.setLeaf(false);
                newRoot.setChildren(new BTreeNode[]{root});
                
                // Split the old root and move the median key up
                newRoot.splitChild(0, root);

                // Determine which child to insert into
                int i = 0;
                if (newRoot.getKeys()[0].compareTo(key) < 0) {
                    i = 1;
                }
                newRoot.getChildren()[i].insertNonFull(key, value);
                root = newRoot;
            } else {
                // Case 3: Root is not full, insert directly
                root.insertNonFull(key, value);
            }
        }
    }

    /**
     * Delete a key from the BTree
     * Time Complexity: O(t * log_t(n)) where t is the minimum degree and n is the number of keys
     * 
     * The deletion process ensures that:
     * 1. All nodes (except root) continue to have at least t-1 keys after deletion
     * 2. The tree remains balanced after deletion
     * 3. If the root has only one child after deletion, that child becomes the new root
     * 
     * @param key The key to delete
     */
    public void delete(K key) {
        if (root == null) {
            System.out.println("The tree is empty.");
            return;
        }

        // Delegate deletion to the root node
        root.delete(key);

        // If the root node has 0 keys, make its first child the new root if it has children
        if (root.getN() == 0) {
            if (root.isLeaf()) {
                // Tree is now empty
                root = null;
            } else {
                // Root has only one child, make that child the new root
                root = root.getChildren()[0];
            }
        }
    }

    public void printBTree() {
        if (root != null) {
            root.printInOrder();
        }
        System.out.println();
    }

    public static void testRun() {
        BTree<String, String> tree = new BTree<>(3);

        // Insert keys with values
        tree.insert("B", "test_B");
        tree.insert("A", "test_A");
        tree.insert("C", "test_C");
        tree.insert("D", "test_D");
        tree.insert("E", "test_E");
        tree.insert("F", "test_F");
        tree.insert("G", "test_G");

        System.out.println("Initial BTree:");
        tree.printBTree();

        // Search and print results for key "A"
        BTreeNode<String, String> nodeA = tree.search("A");
        if (nodeA != null) {
            System.out.println("Found A: " + nodeA.getValues().toString());
        } else {
            System.out.println("A not found");
        }

        // Delete key "A"
        System.out.println("\nDeleting key A");
        tree.delete("A");
        tree.printBTree();

        // Verify deletion of key "A"
        nodeA = tree.search("A");
        if (nodeA != null) {
            System.out.println("Found A: " + nodeA.getValues().toString());
        } else {
            System.out.println("A not found");
        }

        // Delete key "C"
        System.out.println("\nDeleting key C");
        tree.delete("C");
        tree.printBTree();

        // Verify deletion of key "C"
        BTreeNode<String, String> nodeC = tree.search("C");
        if (nodeC != null) {
            System.out.println("Found C: " + nodeC.getValues().toString());
        } else {
            System.out.println("C not found");
        }

        // Delete key "D"
        System.out.println("\nDeleting key D");
        tree.delete("D");
        tree.printBTree();
    }
    
    public static void demonstrateSortAndSearch() {
        BTree<Integer, String> tree = new BTree<>(3);
        
        // Insert some data
        tree.insert(50, "Value-50");
        tree.insert(30, "Value-30");
        tree.insert(70, "Value-70");
        tree.insert(20, "Value-20");
        tree.insert(40, "Value-40");
        tree.insert(60, "Value-60");
        tree.insert(80, "Value-80");
        
        System.out.println("BTree contents:");
        tree.printBTree();
        
        // Demonstrate sorting
        System.out.println("\n--- Sorted Keys ---");
        List<Integer> sortedKeys = tree.getSortedKeys();
        for (Integer key : sortedKeys) {
            System.out.println(key);
        }
        
        System.out.println("\n--- Sorted Values ---");
        List<String> sortedValues = tree.getSortedValues();
        for (String value : sortedValues) {
            System.out.println(value);
        }
        
        // Demonstrate searching
        System.out.println("\n--- Basic Search ---");
        Integer searchKey = 40;
        String value = tree.searchValue(searchKey);
        System.out.println("Search for key " + searchKey + ": " + (value != null ? value : "Not found"));
        
        searchKey = 55; // Key that doesn't exist
        value = tree.searchValue(searchKey);
        System.out.println("Search for key " + searchKey + ": " + (value != null ? value : "Not found"));
    }

}