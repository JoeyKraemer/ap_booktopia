package com.nhlstenden.booktopia.services;

import com.nhlstenden.booktopia.AVL.AVLTree;
import com.nhlstenden.booktopia.BST.BinarySearchTree;
import com.nhlstenden.booktopia.btree.BTree;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * A service for converting between different tree data structures.
 * 
 * This service provides methods for converting data between AVL Trees, Binary Search Trees (BST),
 * and B-Trees while preserving the data. It also provides methods for inserting data into the
 * current active tree structure and retrieving data from it.
 * 
 * @param <K> The type of the keys in the trees, must be Comparable.
 * @param <V> The type of the values stored in the B-Tree (AVL and BST only store keys in this implementation).
 */
@Service
public class TreeConverterService<K extends Comparable<K>, V> {
    
    private AVLTree<K> avlTree;
    private BinarySearchTree<K> bst;
    private BTree<K, V> bTree;
    private String currentTreeType = "BTree"; // Default
    
    /**
     * Constructs a new TreeConverterService with default tree instances.
     * The default active tree is a B-Tree with degree 3.
     */
    public TreeConverterService() {
        this.bTree = new BTree<>(3); // Default degree
        this.avlTree = new AVLTree<>();
        this.bst = new BinarySearchTree<>(Comparator.naturalOrder());
    }
    
    /**
     * Converts the current tree structure to an AVL Tree.
     * If the current structure is already an AVL Tree, no conversion is performed.
     * All keys from the current structure are preserved in the conversion.
     */
    public void convertToAVL() {
        if ("AVL".equals(currentTreeType)) {
            return; // Already an AVL tree
        }

        List<K> keys = getAllKeys();
        List<V> values = getAllValues();

        avlTree = new AVLTree<>();
        for (K key : keys) {
            avlTree.insert(key);
        }
        
        currentTreeType = "AVL";
    }
    
    /**
     * Converts the current tree structure to a Binary Search Tree (BST).
     * If the current structure is already a BST, no conversion is performed.
     * All keys from the current structure are preserved in the conversion.
     */
    public void convertToBST() {
        if ("BST".equals(currentTreeType)) {
            return; // Already a BST
        }

        List<K> keys = getAllKeys();
        List<V> values = getAllValues();
        
        bst = new BinarySearchTree<>(Comparator.naturalOrder());
        for (K key : keys) {
            bst.insert(key);
        }
        
        currentTreeType = "BST";
    }
    
    /**
     * Converts the current tree structure to a B-Tree.
     * If the current structure is already a B-Tree, no conversion is performed.
     * All keys and values from the current structure are preserved in the conversion.
     */
    public void convertToBTree() {
        if ("BTree".equals(currentTreeType)) {
            return; // Already a BTree
        }
        
        // Get all keys from current tree
        List<K> keys = getAllKeys();
        List<V> values = getAllValues();
        
        // Clear and rebuild BTree
        bTree = new BTree<>(3);
        for (int i = 0; i < keys.size(); i++) {
            bTree.insert(keys.get(i), i < values.size() ? values.get(i) : null);
        }
        
        currentTreeType = "BTree";
    }
    
    /**
     * Inserts a key-value pair into the current active tree structure.
     * 
     * @param key The key to insert
     * @param value The value associated with the key (only used for B-Tree)
     */
    public void insert(K key, V value) {
        switch (currentTreeType) {
            case "AVL":
                avlTree.insert(key);
                break;
            case "BST":
                bst.insert(key);
                break;
            case "BTree":
                bTree.insert(key, value);
                break;
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
     * Note: Only B-Tree stores values in this implementation. AVL and BST only store keys.
     * 
     * @return A list of all values in the current tree, or an empty list if the current tree doesn't store values
     */
    public List<V> getAllValues() {
        switch (currentTreeType) {
            case "BTree":
                return bTree.getSortedValues();
            default:
                return new ArrayList<>(); // BST and AVL don't store values in this implementation
        }
    }
    
    /**
     * Gets the type of the current active tree structure.
     * 
     * @return A string representing the current tree type: "AVL", "BST", or "BTree"
     */
    public String getCurrentTreeType() {
        return currentTreeType;
    }
}