package com.nhlstenden.booktopia.btree;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

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

    public BTreeNode<K, V> searchNode(K key) {
        if (root == null) {
            return null;
        }
        return root.search(key);
    }

    /**
     * Searches for a value associated with the given key.
     * 
     * @param key The key to search for
     * @return The value associated with the key, or null if the key is not found
     */
    public V search(K key) {
        if (root == null) {
            return null;
        }
        
        BTreeNode<K, V> node = root.search(key);
        if (node != null) {
            JSONObject values = node.getValues();
            if (values != null && values.has(key.toString())) {
                try {
                    // Get the value and ensure it's not null or JSONObject.NULL
                    Object valueObj = values.get(key.toString());
                    if (valueObj != null && valueObj != JSONObject.NULL) {
                        return (V) valueObj;
                    }
                } catch (Exception e) {
                    System.err.println("Error retrieving value for key: " + key);
                    e.printStackTrace();
                }
            }
            
            // If we get here, the key exists but has no value or the value is null
            System.out.println("Key exists but has no value: " + key);
        }
        
        return null;
    }

    public List<K> getSortedKeys() {
        List<K> sortedKeys = new ArrayList<>();
        if (root != null) {
            root.inOrderTraversal(sortedKeys);
        }
        return sortedKeys;
    }
    
    /**
     * Gets all values stored in the tree as a list of JSONObjects
     * @return List of JSONObjects containing all values
     */
    public List<JSONObject> getAllValues() {
        List<JSONObject> allValues = new ArrayList<>();
        if (root != null) {
            root.getAllValues(allValues);
        }
        return allValues;
    }
    
    /**
     * Performs an in-order traversal of the tree, collecting keys and their associated values
     * @param keys List to store the keys in sorted order
     * @param values List to store the corresponding values
     */
    public void inOrderTraversalWithValues(List<K> keys, List<JSONObject> values) {
        if (root != null) {
            root.inOrderTraversalWithValues(keys, values);
        }
    }

    /**
     * Inserts a key-value pair into the B-tree.
     * 
     * @param key The key to insert
     * @param value The value associated with the key
     */
    public void insert(K key, V value) {
        if (root == null) {
            root = new BTreeNode<>(t);
            root.getKeys()[0] = key;
            root.setN(1);
            
            // Ensure the value is stored properly
            if (value != null && key != null) {
                root.getValues().put(key.toString(), value);
            }
        } else {
            if (root.getN() == 2 * t - 1) {
                BTreeNode<K, V> newRoot = new BTreeNode<>(t);
                newRoot.setLeaf(false);
                newRoot.setChildren(new BTreeNode[]{root});
                newRoot.splitChild(0, root);

                int i = 0;
                if (newRoot.getKeys()[0].compareTo(key) < 0) {
                    i = 1;
                }
                newRoot.getChildren()[i].insertNonFull(key, value);
                root = newRoot;
            } else {
                root.insertNonFull(key, value);
            }
        }
    }

    public void delete(K key) {
        if (root == null) {
            System.out.println("The tree is empty.");
            return;
        }

        root.delete(key);

        // If the root node has 0 keys, make its first child the new root if it has children
        if (root.getN() == 0) {
            if (root.isLeaf()) {
                root = null;
            } else {
                root = root.getChildren()[0];
            }
        }
    }

    public void printBTree() {
        if (root != null) {
            List<K> sortedKeys = new ArrayList<>();
            root.inOrderTraversal(sortedKeys);
            for (K key : sortedKeys) {
                System.out.print(key + " ");
            }
        }
        System.out.println();
    }

    /**
     * Checks if a key exists in the tree
     * 
     * @param key The key to check
     * @return true if the key exists, false otherwise
     */
    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        BTreeNode<K, V> node = root.search(key);
        return node != null && node.containsKey(key);
    }
    
    /**
     * Checks if a key has a non-null value in the tree
     * 
     * @param key The key to check
     * @return true if the key has a non-null value, false otherwise
     */
    public boolean containsValue(K key) {
        if (root == null) {
            return false;
        }
        BTreeNode<K, V> node = root.search(key);
        if (node != null) {
            JSONObject values = node.getValues();
            return values != null && values.has(key.toString()) && values.get(key.toString()) != null;
        }
        return false;
    }
}
