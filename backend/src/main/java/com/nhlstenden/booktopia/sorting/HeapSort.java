package com.nhlstenden.booktopia.sorting;

import com.nhlstenden.booktopia.services.TreeConverterService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A service that implements heap sort algorithm for different tree data structures.
 * This service can sort data from AVL Trees, Binary Search Trees, and B-Trees.
 * 
 * @param <K> The type of keys (must be Comparable)
 * @param <V> The type of values
 */
@Service
public class HeapSort<K extends Comparable<K>, V> {
    
    private final TreeConverterService<K, V> treeConverterService;
    
    /**
     * Constructs a new HeapSort service with a reference to the TreeConverterService.
     * 
     * @param treeConverterService The tree converter service to use
     */
    public HeapSort(TreeConverterService<K, V> treeConverterService) {
        this.treeConverterService = treeConverterService;
    }
    
    /**
     * Sorts the keys from the current active tree using heap sort algorithm.
     * 
     * @return A list of keys sorted in ascending order
     */
    public List<K> sortKeys() {
        long startTime = System.currentTimeMillis();
        
        // Get all keys from the current tree
        List<K> keys = treeConverterService.getAllKeys();
        
        // Apply heap sort
        heapSort(keys);
        
        long endTime = System.currentTimeMillis();
        System.out.println("Heap sort completed in " + (endTime - startTime) + " ms");
        
        return keys;
    }
    
    /**
     * Sorts the keys from the current active tree using heap sort algorithm
     * and returns the corresponding values in the same order.
     * 
     * @return A map of sorted keys to their corresponding values
     */
    public Map<K, V> sortKeysWithValues() {
        long startTime = System.currentTimeMillis();
        
        // Get all keys and values
        List<K> keys = treeConverterService.getAllKeys();
        List<V> values = treeConverterService.getAllValues();
        
        // Create key-value pairs for sorting
        List<KeyValuePair<K, V>> pairs = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = i < values.size() ? values.get(i) : null;
            
            // If value is null, try to get it using search
            if (value == null) {
                value = treeConverterService.search(key);
            }
            
            if (value != null) {
                pairs.add(new KeyValuePair<>(key, value));
            }
        }
        
        // Sort the pairs by key using heap sort
        heapSortPairs(pairs);
        
        // Create a LinkedHashMap to preserve the sorted order
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (KeyValuePair<K, V> pair : pairs) {
            sortedMap.put(pair.key, pair.value);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Heap sort with values completed in " + (endTime - startTime) + " ms");
        
        return sortedMap;
    }
    
    /**
     * Gets all available properties that can be sorted by from the current data.
     * This implementation is consistent with DataService.getSortableProperties().
     * 
     * @return A list of property names
     */
    public List<String> getSortableProperties() {
        List<V> values = treeConverterService.getAllValues();
        
        // Use a set to avoid duplicate property names
        Set<String> propertyNames = new HashSet<>();
        
        // Always include "key" as a sortable property
        propertyNames.add("key");
        
        // Extract property names from the first few values (up to 10)
        int count = 0;
        for (V value : values) {
            if (value != null && value instanceof JSONObject) {
                JSONObject jsonObj = (JSONObject) value;
                for (String key : jsonObj.keySet()) {
                    propertyNames.add(key);
                }
                
                // Limit to checking 10 values to avoid performance issues with large datasets
                if (++count >= 10) {
                    break;
                }
            }
        }
        
        // Convert to list and sort alphabetically
        List<String> sortedProperties = new ArrayList<>(propertyNames);
        Collections.sort(sortedProperties);
        
        return sortedProperties;
    }
    
    /**
     * Implements the heap sort algorithm for a list of comparable elements.
     * 
     * @param list The list to sort
     */
    public void heapSort(List<K> list) {
        int n = list.size();
        
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, n, i);
        }
        
        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            K temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);
            
            // Call max heapify on the reduced heap
            heapify(list, i, 0);
        }
    }
    
    /**
     * Heapify a subtree rooted with node i which is an index in the list.
     * n is the size of the heap.
     * 
     * @param list The list to heapify
     * @param n The size of the heap
     * @param i The root index of the subtree
     */
    private void heapify(List<K> list, int n, int i) {
        int largest = i;  // Initialize largest as root
        int left = 2 * i + 1;  // Left child
        int right = 2 * i + 2;  // Right child
        
        // If left child is larger than root
        if (left < n && list.get(left).compareTo(list.get(largest)) > 0) {
            largest = left;
        }
        
        // If right child is larger than largest so far
        if (right < n && list.get(right).compareTo(list.get(largest)) > 0) {
            largest = right;
        }
        
        // If largest is not root
        if (largest != i) {
            K swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);
            
            // Recursively heapify the affected sub-tree
            heapify(list, n, largest);
        }
    }
    
    /**
     * Implements the heap sort algorithm for a list of key-value pairs.
     * 
     * @param pairs The list of key-value pairs to sort
     */
    private void heapSortPairs(List<KeyValuePair<K, V>> pairs) {
        int n = pairs.size();
        
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapifyPairs(pairs, n, i);
        }
        
        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            KeyValuePair<K, V> temp = pairs.get(0);
            pairs.set(0, pairs.get(i));
            pairs.set(i, temp);
            
            // Call max heapify on the reduced heap
            heapifyPairs(pairs, i, 0);
        }
    }
    
    /**
     * Heapify a subtree of key-value pairs rooted with node i.
     * 
     * @param pairs The list of key-value pairs
     * @param n The size of the heap
     * @param i The root index of the subtree
     */
    private void heapifyPairs(List<KeyValuePair<K, V>> pairs, int n, int i) {
        int largest = i;  // Initialize largest as root
        int left = 2 * i + 1;  // Left child
        int right = 2 * i + 2;  // Right child
        
        // If left child is larger than root
        if (left < n && pairs.get(left).key.compareTo(pairs.get(largest).key) > 0) {
            largest = left;
        }
        
        // If right child is larger than largest so far
        if (right < n && pairs.get(right).key.compareTo(pairs.get(largest).key) > 0) {
            largest = right;
        }
        
        // If largest is not root
        if (largest != i) {
            KeyValuePair<K, V> swap = pairs.get(i);
            pairs.set(i, pairs.get(largest));
            pairs.set(largest, swap);
            
            // Recursively heapify the affected sub-tree
            heapifyPairs(pairs, n, largest);
        }
    }
    
    /**
     * Sorts data by a specific property using heap sort algorithm.
     * 
     * @param property The property to sort by
     * @param ascending True for ascending order, false for descending
     * @return A list of data items sorted by the specified property
     */
    public List<Map<String, Object>> sortByProperty(String property, boolean ascending) {
        long startTime = System.currentTimeMillis();
        
        // Get all keys and values
        List<K> keys = treeConverterService.getAllKeys();
        List<V> values = treeConverterService.getAllValues();
        
        // Create a list to hold all data items
        List<Map<String, Object>> dataItems = new ArrayList<>();
        
        // Combine keys and values into data items
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = i < values.size() ? values.get(i) : null;
            
            // If value is null, try to get it using search
            if (value == null) {
                value = treeConverterService.search(key);
            }
            
            if (value != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("key", key);
                
                // Add all properties from the value
                if (value instanceof JSONObject) {
                    JSONObject jsonObj = (JSONObject) value;
                    for (String propName : jsonObj.keySet()) {
                        item.put(propName, jsonObj.get(propName));
                    }
                }
                
                dataItems.add(item);
            }
        }
        
        // Sort the data items by the specified property
        Collections.sort(dataItems, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> item1, Map<String, Object> item2) {
                Object val1 = property.equals("key") ? item1.get("key") : item1.get(property);
                Object val2 = property.equals("key") ? item2.get("key") : item2.get(property);
                
                // Handle null values
                if (val1 == null && val2 == null) {
                    return 0;
                } else if (val1 == null) {
                    return ascending ? -1 : 1;
                } else if (val2 == null) {
                    return ascending ? 1 : -1;
                }
                
                // Compare as strings
                int result = val1.toString().compareTo(val2.toString());
                return ascending ? result : -result;
            }
        });
        
        long endTime = System.currentTimeMillis();
        System.out.println("Heap sort by property completed in " + (endTime - startTime) + " ms");
        
        return dataItems;
    }
    
    /**
     * Inner class to represent a key-value pair for sorting.
     */
    private static class KeyValuePair<K extends Comparable<K>, V> {
        K key;
        V value;
        
        public KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    /**
     * Creates a new tree of the specified type with the data sorted using heap sort.
     * 
     * @param targetTreeType The type of tree to create ("AVL", "BST", or "BTree")
     * @return The processing time in milliseconds
     */
    public long createSortedTree(String targetTreeType) {
        long startTime = System.currentTimeMillis();
        
        // Get current tree type
        String currentType = treeConverterService.getCurrentTreeType();
        
        // Get all data sorted by heap sort
        Map<K, V> sortedData = sortKeysWithValues();
        
        // Convert to target tree type if needed
        if (!currentType.equals(targetTreeType)) {
            switch (targetTreeType) {
                case "AVL":
                    treeConverterService.convertToAVL();
                    break;
                case "BST":
                    treeConverterService.convertToBST();
                    break;
                case "BTree":
                    treeConverterService.convertToBTree();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown tree type: " + targetTreeType);
            }
        }
        
        // Clear the current tree by converting to an empty tree of the same type
        switch (targetTreeType) {
            case "AVL":
                treeConverterService.convertToAVL();
                break;
            case "BST":
                treeConverterService.convertToBST();
                break;
            case "BTree":
                treeConverterService.convertToBTree();
                break;
        }
        
        // Insert the sorted data
        for (Map.Entry<K, V> entry : sortedData.entrySet()) {
            treeConverterService.insert(entry.getKey(), entry.getValue());
        }
        
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;
        
        System.out.println("Created sorted " + targetTreeType + " in " + processingTime + " ms");
        
        return processingTime;
    }
}