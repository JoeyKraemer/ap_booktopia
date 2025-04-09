package com.nhlstenden.booktopia.services;

import com.nhlstenden.booktopia.AVL.AVLTree;
import com.nhlstenden.booktopia.BST.BinarySearchTree;
import com.nhlstenden.booktopia.btree.BTree;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Core service for tree operations.
 * This service handles the basic tree functionality like insertion, deletion, and retrieval.
 * 
 * @param <K> The type of keys in the trees (must be Comparable)
 * @param <V> The type of values in the trees
 */
@Service
public class TreeService<K extends Comparable<K>, V> {
    private AVLTree<K> avlTree;
    private BinarySearchTree<K> bst;
    private BTree<K, V> bTree;
    private String currentTreeType;
    private int bTreeDegree;
    
    public TreeService() {
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
     * Sets the current tree type.
     * 
     * @param treeType The tree type to set ("AVL", "BST", or "BTree")
     */
    public void setCurrentTreeType(String treeType) {
        this.currentTreeType = treeType;
    }
    
    /**
     * Sets the AVL tree.
     * 
     * @param avlTree The AVL tree to set
     */
    public void setAVLTree(AVLTree<K> avlTree) {
        this.avlTree = avlTree;
    }
    
    /**
     * Sets the BST tree.
     * 
     * @param bst The BST tree to set
     */
    public void setBST(BinarySearchTree<K> bst) {
        this.bst = bst;
    }
    
    /**
     * Sets the B-tree.
     * 
     * @param bTree The B-tree to set
     */
    public void setBTree(BTree<K, V> bTree) {
        this.bTree = bTree;
    }
    
    /**
     * Gets the current AVL tree.
     * 
     * @return The current AVL tree
     */
    public AVLTree<K> getAVLTree() {
        return avlTree;
    }
    
    /**
     * Gets the current BST tree.
     * 
     * @return The current BST tree
     */
    public BinarySearchTree<K> getBST() {
        return bst;
    }
    
    /**
     * Gets the current B-tree.
     * 
     * @return The current B-tree
     */
    public BTree<K, V> getBTree() {
        return bTree;
    }
    
    /**
     * Retrieves all keys from the current active tree structure in sorted order.
     * 
     * @return A list of all keys in the current tree, sorted in ascending order
     */
    public List<K> getAllKeys() {
        long startTime = System.currentTimeMillis();
        List<K> keys = new ArrayList<>();
        
        switch (currentTreeType) {
            case "AVL":
                avlTree.inOrderTraversal(keys);
                break;
                
            case "BST":
                bst.inOrderTraversal(keys);
                break;
                
            case "BTree":
                keys = bTree.getSortedKeys();
                break;
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("getAllKeys processing time: " + (endTime - startTime) + "ms");
        return keys;
    }
    
    /**
     * Retrieves all values from the current active tree structure.
     * 
     * @return A list of all values in the current tree
     */
    public List<V> getAllValues() {
        long startTime = System.currentTimeMillis();
        List<V> values = new ArrayList<>();
        
        switch (currentTreeType) {
            case "AVL":
                List<K> avlKeys = new ArrayList<>();
                List<JSONObject> avlJsonValues = new ArrayList<>();
                avlTree.inOrderTraversalWithValues(avlKeys, avlJsonValues);
                
                for (JSONObject json : avlJsonValues) {
                    if (json != null) {
                        values.add((V) json);
                    }
                }
                break;
                
            case "BST":
                List<K> bstKeys = new ArrayList<>();
                List<JSONObject> bstJsonValues = new ArrayList<>();
                bst.inOrderTraversalWithValues(bstKeys, bstJsonValues);
                
                for (JSONObject json : bstJsonValues) {
                    if (json != null) {
                        values.add((V) json);
                    }
                }
                break;
                
            case "BTree":
                List<K> btreeKeys = new ArrayList<>();
                List<JSONObject> btreeJsonValues = new ArrayList<>();
                bTree.inOrderTraversalWithValues(btreeKeys, btreeJsonValues);
                
                for (JSONObject json : btreeJsonValues) {
                    if (json != null) {
                        values.add((V) json);
                    }
                }
                break;
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("getAllValues processing time: " + (endTime - startTime) + "ms");
        return values;
    }
    
    /**
     * Inserts a key-value pair into the current active tree structure.
     * 
     * @param key The key to insert
     * @param value The value associated with the key
     */
    public void insert(K key, V value) {
        long startTime = System.currentTimeMillis();
        
        switch (currentTreeType) {
            case "AVL":
                if (value instanceof JSONObject) {
                    avlTree.insert(key, (JSONObject)value);
                } else {
                    JSONObject avlJsonValue = new JSONObject();
                    avlJsonValue.put(key.toString(), value);
                    avlTree.insert(key, avlJsonValue);
                }
                break;
                
            case "BST":
                if (value instanceof JSONObject) {
                    bst.insert(key, (JSONObject)value);
                } else {
                    JSONObject bstJsonValue = new JSONObject();
                    bstJsonValue.put(key.toString(), value);
                    bst.insert(key, bstJsonValue);
                }
                break;
                
            case "BTree":
                if (value instanceof JSONObject) {
                    bTree.insert(key, value);
                } else {
                    JSONObject btreeJsonValue = new JSONObject();
                    btreeJsonValue.put(key.toString(), value);
                    bTree.insert(key, (V)btreeJsonValue);
                }
                break;
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("insert processing time: " + (endTime - startTime) + "ms");
    }
    
    /**
     * Deletes a key-value pair from the current active tree structure.
     * 
     * @param key The key to delete
     */
    public void delete(K key) {
        long startTime = System.currentTimeMillis();
        
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
        
        long endTime = System.currentTimeMillis();
        System.out.println("delete processing time: " + (endTime - startTime) + "ms");
    }
    
    /**
     * Searches for a value associated with the given key in the current active tree structure.
     * 
     * @param key The key to search for
     * @return The value associated with the key, or null if the key is not found
     */
    public V search(K key) {
        long startTime = System.currentTimeMillis();
        V result = null;
        
        switch (currentTreeType) {
            case "AVL":
                JSONObject avlResult = avlTree.search(key);
                result = (V) avlResult;
                break;
                
            case "BST":
                JSONObject bstResult = bst.search(key);
                result = (V) bstResult;
                break;
                
            case "BTree":
                result = bTree.search(key);
                break;
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("search processing time: " + (endTime - startTime) + "ms");
        return result;
    }
}