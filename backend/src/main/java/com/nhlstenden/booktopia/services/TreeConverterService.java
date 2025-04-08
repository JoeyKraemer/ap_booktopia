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
    
    /**
     * Constructs a new TreeConverterService with a B-Tree as the default structure.
     */
    public TreeConverterService() {
        bTree = new BTree<>(3);
        currentTreeType = "BTree";
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
     */
    public void convertToAVL() {
        if (!"AVL".equals(currentTreeType)) {
            // Create a new AVL tree
            avlTree = new AVLTree<>();
            
            // Get all keys and values from the current tree
            List<K> keys = new ArrayList<>();
            List<V> values = new ArrayList<>();
            
            if ("BST".equals(currentTreeType)) {
                // Use inOrderTraversalWithValues to efficiently get keys and JSON values
                List<JSONObject> jsonValues = new ArrayList<>();
                bst.inOrderTraversalWithValues(keys, jsonValues);
                
                // Extract actual values from JSON
                for (int i = 0; i < keys.size(); i++) {
                    K key = keys.get(i);
                    JSONObject json = jsonValues.get(i);
                    if (json != null && json.has(key.toString())) {
                        values.add((V) json.get(key.toString()));
                    }
                }
            } else if ("BTree".equals(currentTreeType)) {
                // Get keys from BTree
                keys = bTree.getSortedKeys();
                
                // Get values for each key
                for (K key : keys) {
                    BTreeNode<K, V> node = bTree.search(key);
                    if (node != null) {
                        JSONObject valuesObj = node.getValues();
                        if (valuesObj != null && valuesObj.has(key.toString())) {
                            values.add((V) valuesObj.get(key.toString()));
                        }
                    }
                }
            }
            
            // Insert all keys and values into the AVL tree
            for (int i = 0; i < keys.size(); i++) {
                if (i < values.size()) {
                    // Convert value to JSONObject for AVLTree
                    JSONObject jsonValue = new JSONObject();
                    jsonValue.put(keys.get(i).toString(), values.get(i));
                    avlTree.insert(keys.get(i), jsonValue);
                }
            }
            
            // Update current tree type
            currentTreeType = "AVL";
        }
    }
    
    /**
     * Converts the current tree structure to a Binary Search Tree.
     * This preserves all data from the current tree.
     */
    public void convertToBST() {
        if (!"BST".equals(currentTreeType)) {
            // Create a new BST with a natural order comparator
            bst = new BinarySearchTree<K>(Comparator.<K>naturalOrder());
            
            // Get all keys and values from the current tree
            List<K> keys = new ArrayList<>();
            List<V> values = new ArrayList<>();
            
            if ("AVL".equals(currentTreeType)) {
                // Use inOrderTraversalWithValues to efficiently get keys and JSON values
                List<JSONObject> jsonValues = new ArrayList<>();
                avlTree.inOrderTraversalWithValues(keys, jsonValues);
                
                // Extract actual values from JSON
                for (int i = 0; i < keys.size(); i++) {
                    K key = keys.get(i);
                    JSONObject json = jsonValues.get(i);
                    if (json != null && json.has(key.toString())) {
                        values.add((V) json.get(key.toString()));
                    }
                }
            } else if ("BTree".equals(currentTreeType)) {
                // Get keys from BTree
                keys = bTree.getSortedKeys();
                
                // Get values for each key
                for (K key : keys) {
                    BTreeNode<K, V> node = bTree.search(key);
                    if (node != null) {
                        JSONObject valuesObj = node.getValues();
                        if (valuesObj != null && valuesObj.has(key.toString())) {
                            values.add((V) valuesObj.get(key.toString()));
                        }
                    }
                }
            }
            
            // Insert all keys and values into the BST
            for (int i = 0; i < keys.size(); i++) {
                if (i < values.size()) {
                    // Convert value to JSONObject for BST
                    JSONObject jsonValue = new JSONObject();
                    jsonValue.put(keys.get(i).toString(), values.get(i));
                    bst.insert(keys.get(i), jsonValue);
                }
            }
            
            // Update current tree type
            currentTreeType = "BST";
        }
    }
    
    /**
     * Converts the current tree structure to a B-Tree.
     * All keys and values from the current structure are preserved in the conversion.
     */
    public void convertToBTree() {
        if (!"BTree".equals(currentTreeType)) {
            // Create a new BTree
            bTree = new BTree<>(3);
            
            // Get all keys and values from the current tree
            List<K> keys = new ArrayList<>();
            List<V> values = new ArrayList<>();
            
            if ("AVL".equals(currentTreeType)) {
                // Use inOrderTraversalWithValues to efficiently get keys and JSON values
                List<JSONObject> jsonValues = new ArrayList<>();
                avlTree.inOrderTraversalWithValues(keys, jsonValues);
                
                // Extract actual values from JSON
                for (int i = 0; i < keys.size(); i++) {
                    K key = keys.get(i);
                    JSONObject json = jsonValues.get(i);
                    if (json != null && json.has(key.toString())) {
                        values.add((V) json.get(key.toString()));
                    }
                }
            } else if ("BST".equals(currentTreeType)) {
                // Use inOrderTraversalWithValues to efficiently get keys and JSON values
                List<JSONObject> jsonValues = new ArrayList<>();
                bst.inOrderTraversalWithValues(keys, jsonValues);
                
                // Extract actual values from JSON
                for (int i = 0; i < keys.size(); i++) {
                    K key = keys.get(i);
                    JSONObject json = jsonValues.get(i);
                    if (json != null && json.has(key.toString())) {
                        values.add((V) json.get(key.toString()));
                    }
                }
            }
            
            // Insert all keys and values into the BTree
            for (int i = 0; i < keys.size(); i++) {
                if (i < values.size()) {
                    bTree.insert(keys.get(i), values.get(i));
                }
            }
            
            // Update current tree type
            currentTreeType = "BTree";
        }
    }
    
    /**
     * Retrieves all keys from the current active tree structure in sorted order.
     * 
     * @return A list of all keys in the current tree, sorted in ascending order
     */
    public List<K> getAllKeys() {
        switch (currentTreeType) {
            case "AVL":
                List<K> avlKeys = new ArrayList<>();
                avlTree.inOrderTraversal(avlKeys);
                return avlKeys;
                
            case "BST":
                List<K> bstKeys = new ArrayList<>();
                bst.inOrderTraversal(bstKeys);
                return bstKeys;
                
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
                    K key = avlKeys.get(i);
                    JSONObject json = avlJsonValues.get(i);
                    if (json != null && json.has(key.toString())) {
                        values.add((V) json.get(key.toString()));
                    }
                }
                break;
                
            case "BST":
                List<K> bstKeys = new ArrayList<>();
                List<JSONObject> bstJsonValues = new ArrayList<>();
                bst.inOrderTraversalWithValues(bstKeys, bstJsonValues);
                
                // Extract the actual values from the JSONObjects
                for (int i = 0; i < bstKeys.size(); i++) {
                    K key = bstKeys.get(i);
                    JSONObject json = bstJsonValues.get(i);
                    if (json != null && json.has(key.toString())) {
                        values.add((V) json.get(key.toString()));
                    }
                }
                break;
                
            case "BTree":
                // Get keys from BTree
                List<K> btreeKeys = bTree.getSortedKeys();
                
                // Get values for each key
                for (K key : btreeKeys) {
                    BTreeNode<K, V> node = bTree.search(key);
                    if (node != null) {
                        JSONObject valuesObj = node.getValues();
                        if (valuesObj != null && valuesObj.has(key.toString())) {
                            values.add((V) valuesObj.get(key.toString()));
                        }
                    }
                }
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
                // Convert value to JSONObject for AVLTree
                JSONObject avlJsonValue = new JSONObject();
                avlJsonValue.put(key.toString(), value);
                avlTree.insert(key, avlJsonValue);
                break;
                
            case "BST":
                // Convert value to JSONObject for BST
                JSONObject bstJsonValue = new JSONObject();
                bstJsonValue.put(key.toString(), value);
                bst.insert(key, bstJsonValue);
                break;
                
            case "BTree":
                bTree.insert(key, value);
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
                if (avlResult != null && avlResult.has(key.toString())) {
                    return (V) avlResult.get(key.toString());
                }
                return null;
                
            case "BST":
                JSONObject bstResult = bst.search(key);
                if (bstResult != null && bstResult.has(key.toString())) {
                    return (V) bstResult.get(key.toString());
                }
                return null;
                
            case "BTree":
                BTreeNode<K, V> node = bTree.search(key);
                if (node != null) {
                    JSONObject valuesObj = node.getValues();
                    if (valuesObj != null && valuesObj.has(key.toString())) {
                        return (V) valuesObj.get(key.toString());
                    }
                }
                return null;
                
            default:
                return null;
        }
    }
}