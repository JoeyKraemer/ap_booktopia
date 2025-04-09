package com.nhlstenden.booktopia.services;

import com.nhlstenden.booktopia.AVL.AVLTree;
import com.nhlstenden.booktopia.BST.BinarySearchTree;
import com.nhlstenden.booktopia.btree.BTree;
import org.json.JSONObject;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private TreeService<K, V> treeService;
    
    /**
     * Returns the current active tree type.
     * 
     * @return A string representing the current tree type: "AVL", "BST", or "BTree"
     */
    public String getCurrentTreeType() {
        return treeService.getCurrentTreeType();
    }
    
    /**
     * Converts the current tree structure to an AVL Tree.
     * This preserves all data from the current tree.
     * 
     * @return The converted AVL tree
     */
    public AVLTree<K> convertToAVL() {
        long startTime = System.currentTimeMillis();
        
        // Create a new AVL tree
        AVLTree<K> newAVLTree = new AVLTree<>();
        
        // Get all keys and values from the current tree
        List<K> keys = treeService.getAllKeys();
        List<V> values = treeService.getAllValues();
        
        System.out.println("Converting to AVL Tree - Found " + keys.size() + " keys and " + values.size() + " values");
        
        // Insert all key-value pairs into the new AVL tree
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = (i < values.size()) ? values.get(i) : null;
            
            // Skip null values
            if (value == null) {
                System.out.println("Warning: Skipping null value for key: " + key);
                // Try to get the value directly using search
                value = treeService.search(key);
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
        treeService.setCurrentTreeType("AVL");
        treeService.setAVLTree(newAVLTree);
        
        long endTime = System.currentTimeMillis();
        System.out.println("convertToAVL processing time: " + (endTime - startTime) + "ms");
        
        return newAVLTree;
    }
    
    /**
     * Converts the current tree structure to a Binary Search Tree.
     * This preserves all data from the current tree.
     * 
     * @return The converted binary search tree
     */
    public BinarySearchTree<K> convertToBST() {
        long startTime = System.currentTimeMillis();
        
        // Create a new binary search tree with a comparator for the keys
        BinarySearchTree<K> newBST = new BinarySearchTree<>((k1, k2) -> ((Comparable<K>)k1).compareTo(k2));
        
        // Get all keys and values from the current tree
        List<K> keys = treeService.getAllKeys();
        List<V> values = treeService.getAllValues();
        
        System.out.println("Converting to BST - Found " + keys.size() + " keys and " + values.size() + " values");
        
        // Insert all key-value pairs into the new binary search tree
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = (i < values.size()) ? values.get(i) : null;
            
            // Skip null values
            if (value == null) {
                System.out.println("Warning: Skipping null value for key: " + key);
                // Try to get the value directly using search
                value = treeService.search(key);
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
        treeService.setCurrentTreeType("BST");
        treeService.setBST(newBST);
        
        long endTime = System.currentTimeMillis();
        System.out.println("convertToBST processing time: " + (endTime - startTime) + "ms");
        
        return newBST;
    }
    
    /**
     * Converts the current tree structure to a B-Tree.
     * This preserves all data from the current tree.
     * 
     * @return The converted B-tree
     */
    public BTree<K, V> convertToBTree() {
        long startTime = System.currentTimeMillis();
        
        // Create a new B-tree with the specified degree
        BTree<K, V> newBTree = new BTree<>(3);
        
        // Use a map to ensure keys and values stay properly associated
        Map<K, V> keyValueMap = new HashMap<>();
        
        // Get all keys and values
        List<K> keys = treeService.getAllKeys();
        List<V> values = treeService.getAllValues();
        
        // Create the key-value map
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = (i < values.size()) ? values.get(i) : null;
            
            if (value == null) {
                value = treeService.search(key);
            }
            
            if (value != null) {
                keyValueMap.put(key, value);
            }
        }
        
        System.out.println("Converting to BTree - Found " + keyValueMap.size() + " key-value pairs");
        
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
        treeService.setCurrentTreeType("BTree");
        treeService.setBTree(newBTree);
        
        long endTime = System.currentTimeMillis();
        System.out.println("convertToBTree processing time: " + (endTime - startTime) + "ms");
        
        return newBTree;
    }
    
    /**
     * Delegates the search to the underlying TreeService
     * 
     * @param key The key to search for
     * @return The value associated with the key, or null if not found
     */
    public V search(K key) {
        return treeService.search(key);
    }
    
    /**
     * Delegates the insert operation to the underlying TreeService
     * 
     * @param key The key to insert
     * @param value The value to associate with the key
     */
    public void insert(K key, V value) {
        treeService.insert(key, value);
    }
    
    /**
     * Delegates the delete operation to the underlying TreeService
     * 
     * @param key The key to delete
     */
    public void delete(K key) {
        treeService.delete(key);
    }
    
    /**
     * Delegates getting all keys to the underlying TreeService
     * 
     * @return List of all keys in the tree
     */
    public List<K> getAllKeys() {
        return treeService.getAllKeys();
    }
    
    /**
     * Delegates getting all values to the underlying TreeService
     * 
     * @return List of all values in the tree
     */
    public List<V> getAllValues() {
        return treeService.getAllValues();
    }
}