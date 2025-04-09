package com.nhlstenden.booktopia.services;

import com.nhlstenden.booktopia.AVL.AVLTree;
import com.nhlstenden.booktopia.BST.BinarySearchTree;
import com.nhlstenden.booktopia.btree.BTree;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * Main facade service for the application that handles data operations
 * by delegating to specialized services.
 *
 * @param <K> Type of keys in the trees (must be Comparable)
 * @param <V> Type of values in the trees
 */
@Service
public class DataService<K extends Comparable<K>, V> {
    
    @Autowired
    private TreeService<K, V> treeService;
    
    @Autowired
    private TreeConverterService<K, V> treeConverterService;
    
    @Autowired
    private DataImportService<K, V> dataImportService;
    
    @Autowired
    private DataDisplayService<K, V> dataDisplayService;
    
    @Autowired
    private SearchService<K, V> searchService;
    
    /**
     * Adds a new key-value pair to the current tree structure.
     *
     * @param key The key to insert
     * @param value The value to associate with the key
     */
    public void addData(K key, V value) {
        long startTime = System.currentTimeMillis();
        treeService.insert(key, value);
        long endTime = System.currentTimeMillis();
        System.out.println("addData processing time: " + (endTime - startTime) + "ms");
    }
    
    /**
     * Removes a key-value pair from the current tree structure.
     * 
     * @param key The key to remove
     */
    public void removeData(K key) {
        long startTime = System.currentTimeMillis();
        treeService.delete(key);
        long endTime = System.currentTimeMillis();
        System.out.println("removeData processing time: " + (endTime - startTime) + "ms");
    }
    
    /**
     * Removes a key-value pair from the current tree structure.
     * (Alias for removeData to maintain compatibility with controllers)
     *
     * @param key The key to remove
     */
    public void deleteData(K key) {
        removeData(key);
    }
    
    /**
     * Searches for a specific key in the current tree structure.
     *
     * @param key The key to search for
     * @return The value associated with the key, or null if not found
     */
    public V searchData(K key) {
        long startTime = System.currentTimeMillis();
        V result = treeService.search(key);
        long endTime = System.currentTimeMillis();
        System.out.println("searchData processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Returns the current tree type.
     *
     * @return A string representing the current tree type: "AVL", "BST", or "BTree"
     */
    public String getCurrentTreeType() {
        return treeService.getCurrentTreeType();
    }
    
    /**
     * Converts the current tree structure to an AVL Tree.
     *
     * @return The converted AVL tree
     */
    public AVLTree<K> convertToAVL() {
        long startTime = System.currentTimeMillis();
        AVLTree<K> result = treeConverterService.convertToAVL();
        long endTime = System.currentTimeMillis();
        System.out.println("convertToAVL processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Converts the current tree structure to a Binary Search Tree.
     *
     * @return The converted binary search tree
     */
    public BinarySearchTree<K> convertToBST() {
        long startTime = System.currentTimeMillis();
        BinarySearchTree<K> result = treeConverterService.convertToBST();
        long endTime = System.currentTimeMillis();
        System.out.println("convertToBST processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Converts the current tree structure to a B-Tree.
     *
     * @return The converted B-tree
     */
    public BTree<K, V> convertToBTree() {
        long startTime = System.currentTimeMillis();
        BTree<K, V> result = treeConverterService.convertToBTree();
        long endTime = System.currentTimeMillis();
        System.out.println("convertToBTree processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Imports data from a CSV file into the current tree structure.
     *
     * @param filePath The path to the CSV file
     * @throws IOException If there's an error reading the file
     */
    public void importCSV(String filePath) throws IOException {
        long startTime = System.currentTimeMillis();
        dataImportService.importCSV(filePath);
        long endTime = System.currentTimeMillis();
        System.out.println("importCSV processing time: " + (endTime - startTime) + "ms");
    }
    
    /**
     * Imports data from a CSV file using a BufferedReader.
     * Delegates to DataImportService.
     *
     * @param reader The BufferedReader for the CSV file
     * @return The number of records imported
     * @throws Exception If there's an error importing the data
     */
    public int importFromCsv(BufferedReader reader) throws Exception {
        long startTime = System.currentTimeMillis();
        int count = dataImportService.importFromCsv(reader);
        long endTime = System.currentTimeMillis();
        System.out.println("importFromCsv processing time: " + (endTime - startTime) + "ms");
        return count;
    }
    
    /**
     * Gets all the keys from the current tree structure.
     *
     * @return A list of all keys in the current tree
     */
    public List<K> getAllKeys() {
        long startTime = System.currentTimeMillis();
        List<K> result = treeService.getAllKeys();
        long endTime = System.currentTimeMillis();
        System.out.println("getAllKeys processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Gets all the values from the current tree structure.
     *
     * @return A list of all values in the current tree
     */
    public List<V> getAllValues() {
        long startTime = System.currentTimeMillis();
        List<V> result = treeService.getAllValues();
        long endTime = System.currentTimeMillis();
        System.out.println("getAllValues processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Returns all values in the current tree structure, sorted by a specific property.
     * Delegates to DataDisplayService.
     *
     * @param property The property to sort by
     * @return A list of all values, sorted by the specified property
     */
    public List<V> getAllValuesSortedBy(String property) {
        long startTime = System.currentTimeMillis();
        List<V> result = dataDisplayService.getAllValuesSortedBy(property);
        long endTime = System.currentTimeMillis();
        System.out.println("getAllValuesSortedBy processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Gets data formatted for table display.
     *
     * @return A list of maps containing data formatted for table display
     */
    public List<Map<String, Object>> getTableDisplayData() {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> result = dataDisplayService.getTableDisplayData();
        long endTime = System.currentTimeMillis();
        System.out.println("getTableDisplayData processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Gets data formatted for table display.
     *
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction ("ASC" or "DESC")
     * @return A map containing column definitions and row data
     */
    public Map<String, Object> getTableDisplayData(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();
        
        List<Map<String, Object>> rows = dataDisplayService.getTableDisplayData(sortBy, sortDirection);
        
        // Create a proper response with columns and rows
        Map<String, Object> result = new HashMap<>();
        
        // Get sortable properties for columns
        List<String> properties = dataDisplayService.getSortableProperties();
        
        result.put("columns", properties);
        result.put("rows", rows);
        result.put("processingTimeMs", System.currentTimeMillis() - startTime);
        
        return result;
    }
    
    /**
     * Gets data formatted for card display.
     *
     * @return A list of maps containing data formatted for card display
     */
    public List<Map<String, Object>> getDataCardDisplayData() {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> result = dataDisplayService.getDataCardDisplayData();
        long endTime = System.currentTimeMillis();
        System.out.println("getDataCardDisplayData processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Gets data formatted for card display.
     *
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction ("ASC" or "DESC")
     * @return A list of datacards with title and values
     */
    public List<Map<String, Object>> getDataCardDisplayData(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> result = dataDisplayService.getDataCardDisplayData(sortBy, sortDirection);
        long endTime = System.currentTimeMillis();
        System.out.println("getDataCardDisplayData processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Sorts the data by the specified field.
     *
     * @param field The field to sort by
     * @param ascending Whether to sort in ascending order
     * @return A list of maps containing sorted data
     */
    public List<Map<String, Object>> sortDataBy(String field, boolean ascending) {
        long startTime = System.currentTimeMillis();
        List<Map<String, Object>> result = dataDisplayService.sortDataBy(field, ascending);
        long endTime = System.currentTimeMillis();
        System.out.println("sortDataBy processing time: " + (endTime - startTime) + "ms");
        return result;
    }
    
    /**
     * Searches for data in the current tree structure.
     * This method delegates to the SearchService which uses tree-specific search algorithms
     * when possible for exact key matches, and falls back to a more comprehensive search
     * for partial matches in keys and values.
     * 
     * @param query The search query
     * @return A map containing search results and metadata
     */
    public Map<String, Object> searchData(String query) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = searchService.searchData(query);
        long endTime = System.currentTimeMillis();
        
        // Update processing time with the total time including this method
        result.put("processingTimeMs", endTime - startTime);
        
        System.out.println("searchData processing time: " + (endTime - startTime) + "ms");
        return result;
    }
}
