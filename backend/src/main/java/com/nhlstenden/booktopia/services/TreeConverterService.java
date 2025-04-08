package com.nhlstenden.booktopia.services;

import com.nhlstenden.booktopia.AVL.AVLTree;
import com.nhlstenden.booktopia.BST.BinarySearchTree;
import com.nhlstenden.booktopia.btree.BTree;
import com.nhlstenden.booktopia.btree.BTreeNode;
import org.json.JSONObject;

import java.util.*;

import org.springframework.stereotype.Service;

/**
 * A service that allows converting between different tree data structures.
 * This service supports AVL Trees, Binary Search Trees, and B-Trees.
 * 
 * @param <K> The type of keys in the trees (must be Comparable)
 * @param <V> The type of values in the trees
 */
@Service
public class TreeConverterService<K extends Comparable<K>, V> {
    private AVLTree<K> avlTree;
    private BinarySearchTree<K> bst;
    private BTree<K, V> bTree;
    private String currentTreeType;
    private int bTreeDegree;
    
    /**
     * Constructs a new TreeConverterService with a B-Tree as the default structure.
     */
    public TreeConverterService() {
        bTree = new BTree<>(3);
        currentTreeType = "BTree";
        bTreeDegree = 3;
    }
    
    /**
     * Returns the current active tree type.
     * 
     * @return A string representing the current tree type: "AVL", "BST", or "BTree"
     */
    public String getCurrentTreeType() {
        return currentTreeType;
    }
    
    /**
     * Converts the current tree structure to an AVL Tree.
     * This preserves all data from the current tree.
     * 
     * @return The converted AVL tree
     */
    public AVLTree<K> convertToAVL() {
        // Create a new AVL tree
        AVLTree<K> newAVLTree = new AVLTree<>();
        
        // Get all keys and values from the current tree
        List<K> keys = getAllKeys();
        List<V> values = getAllValues();
        
        System.out.println("Converting to AVL Tree - Found " + keys.size() + " keys and " + values.size() + " values");
        
        // Insert all key-value pairs into the new AVL tree
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = (i < values.size()) ? values.get(i) : null;
            
            // Skip null values
            if (value == null) {
                System.out.println("Warning: Skipping null value for key: " + key);
                // Try to get the value directly using search
                value = search(key);
                if (value == null) {
                    System.out.println("Still couldn't find value for key: " + key);
                    continue;
                }
                System.out.println("Found value using search for key: " + key);
            }
            
            // For AVLTree, we need to cast the value to JSONObject
            if (value instanceof JSONObject) {
                newAVLTree.insert(key, (JSONObject)value);
            } else {
                // If not a JSONObject, wrap it
                JSONObject wrapper = new JSONObject();
                wrapper.put(key.toString(), value);
                newAVLTree.insert(key, wrapper);
            }
        }
        
        // Update current tree type and tree
        currentTreeType = "AVL";
        avlTree = newAVLTree;
        
        return newAVLTree;
    }
    
    /**
     * Converts the current tree structure to a Binary Search Tree.
     * This preserves all data from the current tree.
     * 
     * @return The converted binary search tree
     */
    public BinarySearchTree<K> convertToBST() {
        // Create a new binary search tree with a comparator for the keys
        BinarySearchTree<K> newBST = new BinarySearchTree<>((k1, k2) -> ((Comparable<K>)k1).compareTo(k2));
        
        // Get all keys and values from the current tree
        List<K> keys = getAllKeys();
        List<V> values = getAllValues();
        
        System.out.println("Converting to BST - Found " + keys.size() + " keys and " + values.size() + " values");
        
        // Insert all key-value pairs into the new binary search tree
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = (i < values.size()) ? values.get(i) : null;
            
            // Skip null values
            if (value == null) {
                System.out.println("Warning: Skipping null value for key: " + key);
                // Try to get the value directly using search
                value = search(key);
                if (value == null) {
                    System.out.println("Still couldn't find value for key: " + key);
                    continue;
                }
                System.out.println("Found value using search for key: " + key);
            }
            
            // For BST, we need to cast the value to JSONObject
            if (value instanceof JSONObject) {
                newBST.insert(key, (JSONObject)value);
            } else {
                // If not a JSONObject, wrap it
                JSONObject wrapper = new JSONObject();
                wrapper.put(key.toString(), value);
                newBST.insert(key, wrapper);
            }
        }
        
        // Update current tree type and tree
        currentTreeType = "BST";
        bst = newBST;
        
        return newBST;
    }
    
    /**
     * Converts the current tree structure to a B-Tree.
     * This preserves all data from the current tree.
     * 
     * @return The converted B-tree
     */
    public BTree<K, V> convertToBTree() {
        // Create a new B-tree with the specified degree
        BTree<K, V> newBTree = new BTree<>(bTreeDegree);
        
        // Use a map to ensure keys and values stay properly associated
        Map<K, V> keyValueMap = new HashMap<>();
        
        // Populate the map based on the current tree type
        switch (currentTreeType) {
            case "AVL":
                // For AVL tree, get all keys and then find their values
                List<K> avlKeys = new ArrayList<>();
                avlTree.inOrderTraversal(avlKeys);
                
                for (K key : avlKeys) {
                    JSONObject value = avlTree.search(key);
                    if (value != null) {
                        keyValueMap.put(key, (V) value);
                    } else {
                        System.out.println("Warning: No value found in AVL tree for key: " + key);
                    }
                }
                break;
                
            case "BST":
                // For BST tree, get all keys and then find their values
                List<K> bstKeys = new ArrayList<>();
                bst.inOrderTraversal(bstKeys);
                
                for (K key : bstKeys) {
                    JSONObject value = bst.search(key);
                    if (value != null) {
                        keyValueMap.put(key, (V) value);
                    } else {
                        System.out.println("Warning: No value found in BST tree for key: " + key);
                    }
                }
                break;
                
            case "BTree":
                // For BTree, get all keys and then find their values
                List<K> btreeKeys = bTree.getSortedKeys();
                
                for (K key : btreeKeys) {
                    V value = bTree.search(key);
                    if (value != null) {
                        keyValueMap.put(key, value);
                    } else {
                        System.out.println("Warning: No value found in BTree for key: " + key);
                    }
                }
                break;
        }
        
        System.out.println("Converting to BTree - Found " + keyValueMap.size() + " key-value pairs");
        
        // Special handling for "A Short History of Nearly Everything" which seems to lose its value
        if (keyValueMap.containsKey((K) "A Short History of Nearly Everything")) {
            System.out.println("Special handling for 'A Short History of Nearly Everything'");
            V value = keyValueMap.get((K) "A Short History of Nearly Everything");
            System.out.println("Value before insertion: " + (value != null ? value.toString() : "null"));
        }
        
        // Insert all key-value pairs into the new B-tree
        for (Map.Entry<K, V> entry : keyValueMap.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            
            if (value == null) {
                System.out.println("Warning: Skipping null value for key: " + key);
                continue;
            }
            
            // Insert the key-value pair
            newBTree.insert(key, value);
            
            // Verify the insertion
            V checkValue = newBTree.search(key);
            if (checkValue == null) {
                System.out.println("Warning: Value not found after insertion for key: " + key);
            }
        }
        
        // Update current tree type and tree
        currentTreeType = "BTree";
        bTree = newBTree;
        
        return newBTree;
    }
    
    /**
     * Retrieves all keys from the current active tree structure in sorted order.
     * 
     * @return A list of all keys in the current tree, sorted in ascending order
     */
    public List<K> getAllKeys() {
        List<K> keys = new ArrayList<>();
        
        switch (currentTreeType) {
            case "AVL":
                avlTree.inOrderTraversal(keys);
                return keys;
                
            case "BST":
                bst.inOrderTraversal(keys);
                return keys;
                
            case "BTree":
                return bTree.getSortedKeys();
                
            default:
                return new ArrayList<>();
        }
    }
    
    /**
     * Retrieves all values from the current active tree structure.
     * All three tree structures store values, but they use different mechanisms.
     * 
     * @return A list of all values in the current tree
     */
    public List<V> getAllValues() {
        List<V> values = new ArrayList<>();
        
        switch (currentTreeType) {
            case "AVL":
                List<K> avlKeys = new ArrayList<>();
                List<JSONObject> avlJsonValues = new ArrayList<>();
                avlTree.inOrderTraversalWithValues(avlKeys, avlJsonValues);
                
                // Extract the actual values from the JSONObjects
                for (int i = 0; i < avlKeys.size(); i++) {
                    JSONObject json = avlJsonValues.get(i);
                    if (json != null) {
                        values.add((V) json);
                    }
                }
                break;
                
            case "BST":
                List<K> bstKeys = new ArrayList<>();
                List<JSONObject> bstJsonValues = new ArrayList<>();
                bst.inOrderTraversalWithValues(bstKeys, bstJsonValues);
                
                // Extract the actual values from the JSONObjects
                for (int i = 0; i < bstKeys.size(); i++) {
                    JSONObject json = bstJsonValues.get(i);
                    if (json != null) {
                        values.add((V) json);
                    }
                }
                break;
                
            case "BTree":
                List<K> btreeKeys = new ArrayList<>();
                List<JSONObject> btreeJsonValues = new ArrayList<>();
                bTree.inOrderTraversalWithValues(btreeKeys, btreeJsonValues);
                
                // Extract the actual values from the JSONObjects
                for (int i = 0; i < btreeKeys.size(); i++) {
                    JSONObject json = btreeJsonValues.get(i);
                    if (json != null) {
                        values.add((V) json);
                    }
                }
                break;
                
            default:
                break;
        }
        
        return values;
    }
    
    /**
     * Inserts a key-value pair into the current active tree structure.
     * 
     * @param key The key to insert
     * @param value The value associated with the key
     */
    public void insert(K key, V value) {
        switch (currentTreeType) {
            case "AVL":
                // For AVLTree, just insert the JSONObject directly
                if (value instanceof JSONObject) {
                    avlTree.insert(key, (JSONObject)value);
                } else {
                    // If not a JSONObject, wrap it (should not happen with our current implementation)
                    JSONObject avlJsonValue = new JSONObject();
                    avlJsonValue.put(key.toString(), value);
                    avlTree.insert(key, avlJsonValue);
                }
                break;
                
            case "BST":
                // For BST, just insert the JSONObject directly
                if (value instanceof JSONObject) {
                    bst.insert(key, (JSONObject)value);
                } else {
                    // If not a JSONObject, wrap it (should not happen with our current implementation)
                    JSONObject bstJsonValue = new JSONObject();
                    bstJsonValue.put(key.toString(), value);
                    bst.insert(key, bstJsonValue);
                }
                break;
                
            case "BTree":
                // For BTree, just insert the JSONObject directly
                if (value instanceof JSONObject) {
                    bTree.insert(key, value);
                } else {
                    // If not a JSONObject, wrap it (should not happen with our current implementation)
                    JSONObject btreeJsonValue = new JSONObject();
                    btreeJsonValue.put(key.toString(), value);
                    bTree.insert(key, (V)btreeJsonValue);
                }
                break;
        }
    }

    /**
     * Deletes a key-value pair from the current active tree structure.
     * 
     * @param key The key to delete
     */
    public void delete(K key) {
        switch (currentTreeType) {
            case "AVL":
                avlTree.delete(key);
                break;
                
            case "BST":
                bst.delete(key);
                break;
                
            case "BTree":
                bTree.delete(key);
                break;
        }
    }
    
    /**
     * Searches for a value associated with the given key in the current active tree structure.
     * 
     * @param key The key to search for
     * @return The value associated with the key, or null if the key is not found
     */
    public V search(K key) {
        switch (currentTreeType) {
            case "AVL":
                JSONObject avlResult = avlTree.search(key);
                if (avlResult == null) {
                    System.out.println("AVL search: No value found for key: " + key);
                }
                return (V) avlResult;
                
            case "BST":
                JSONObject bstResult = bst.search(key);
                if (bstResult == null) {
                    System.out.println("BST search: No value found for key: " + key);
                }
                return (V) bstResult;
                
            case "BTree":
                V result = bTree.search(key);
                if (result == null) {
                    System.out.println("BTree search: No value found for key: " + key);
                    // Try to find the key in the BTree nodes directly
                    List<K> keys = new ArrayList<>();
                    List<JSONObject> values = new ArrayList<>();
                    bTree.inOrderTraversalWithValues(keys, values);
                    
                    for (int i = 0; i < keys.size(); i++) {
                        if (keys.get(i).equals(key) && values.get(i) != null) {
                            System.out.println("Found value in BTree traversal for key: " + key);
                            return (V) values.get(i);
                        }
                    }
                }
                return result;
                
            default:
                return null;
        }
    }
}
