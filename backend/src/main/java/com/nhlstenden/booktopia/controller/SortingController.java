package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.sorting.HeapSort;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // For development
@RequestMapping("/api/sorting")
public class SortingController {

    @Autowired
    private HeapSort<String, JSONObject> heapSort;
    
    /**
     * Gets all available properties that can be used for sorting.
     * Uses heap sort algorithm for sorting the properties.
     * 
     * @return A list of property names that can be used for sorting
     */
    @GetMapping("/sortable-properties")
    public ResponseEntity<?> getSortableProperties() {
        long startTime = System.currentTimeMillis();
        
        try {
            // Use HeapSort for sortable properties
            List<String> properties = heapSort.getSortableProperties();
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("properties", properties);
            response.put("processingTimeMs", endTime - startTime);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("processingTimeMs", endTime - startTime);
            
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Sorts data by a specific property using heap sort algorithm.
     * 
     * @param property The property to sort by
     * @param direction The sort direction (ASC or DESC)
     * @return A list of data items sorted by the specified property
     */
    @GetMapping("/sort-by-property")
    public ResponseEntity<?> sortByProperty(
            @RequestParam(required = false, defaultValue = "key") String property,
            @RequestParam(required = false, defaultValue = "ASC") String direction) {
        
        long startTime = System.currentTimeMillis();
        
        try {
            boolean ascending = !"DESC".equalsIgnoreCase(direction);
            List<Map<String, Object>> sortedData = heapSort.sortByProperty(property, ascending);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", sortedData);
            response.put("property", property);
            response.put("direction", direction);
            response.put("processingTimeMs", endTime - startTime);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("processingTimeMs", endTime - startTime);
            
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Gets all keys from the current tree, sorted using heap sort algorithm.
     * 
     * @return A list of sorted keys
     */
    @GetMapping("/heapsort")
    public ResponseEntity<?> heapSort() {
        long startTime = System.currentTimeMillis();
        
        try {
            // Sort keys using heap sort
            List<String> sortedKeys = heapSort.sortKeys();
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("keys", sortedKeys);
            response.put("processingTimeMs", endTime - startTime);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("processingTimeMs", endTime - startTime);
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Creates a new tree of the specified type with data sorted using heap sort.
     * 
     * @param treeType The type of tree to create (AVL, BST, or BTree)
     * @return Information about the creation
     */
    @GetMapping("/heapsort/tree/{treeType}")
    public ResponseEntity<?> createSortedTree(@PathVariable String treeType) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Create a sorted tree
            long processingTime = heapSort.createSortedTree(treeType);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Created sorted " + treeType + " successfully");
            response.put("treeType", treeType);
            response.put("processingTimeMs", processingTime);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("processingTimeMs", endTime - startTime);
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}