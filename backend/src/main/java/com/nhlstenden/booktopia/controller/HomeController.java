package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.services.DataService;
import com.nhlstenden.booktopia.services.TreeConverterService;
import com.nhlstenden.booktopia.sorting.HeapSort;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.File;

@RestController
@CrossOrigin(origins = "*") // For development
public class HomeController {

    @Autowired
    private TreeConverterService<String, JSONObject> treeConverterService;
    
    @Autowired
    private DataService dataService;

    @Autowired
    private HeapSort<String, JSONObject> heapSort;

    @GetMapping("/")
    public String home() {
        return "Welcome to Booktopia!";
    }
    
    //--------------------------------------------------
    // Tree management endpoints
    //--------------------------------------------------
    
    /**
     * Converts the current tree to a different type.
     * 
     * @param targetTree The target tree type (AVL, BST, or BTREE)
     * @return Information about the conversion
     */
    @PostMapping("/api/convert")
    public ResponseEntity<?> convertTree(@RequestParam String targetTree) {
        long startTime = System.currentTimeMillis();
        
        try {
            switch (targetTree.toUpperCase()) {
                case "AVL":
                    treeConverterService.convertToAVL();
                    break;
                case "BST":
                    treeConverterService.convertToBST();
                    break;
                case "BTREE":
                    treeConverterService.convertToBTree();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown tree type: " + targetTree);
            }
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Converted to " + targetTree + " successfully");
            response.put("treeType", targetTree);
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
     * Gets information about the current tree.
     * 
     * @return Information about the current tree
     */
    @GetMapping("/api/tree/current")
    public ResponseEntity<?> getCurrentTree() {
        long startTime = System.currentTimeMillis();
        
        try {
            String treeType = dataService.getCurrentTreeType();
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("treeType", treeType);
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
     * Inserts a key-value pair into the current tree.
     * 
     * @param key The key to insert
     * @param value The value to insert
     * @return Information about the insertion
     */
    @PostMapping("/api/tree/insert")
    public ResponseEntity<?> insertIntoTree(@RequestParam String key, @RequestParam String value) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Parse the value as a JSON object
            JSONObject jsonValue = new JSONObject(value);
            
            // Insert into the tree
            treeConverterService.insert(key, jsonValue);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Inserted key-value pair successfully");
            response.put("key", key);
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
    
    //--------------------------------------------------
    // Data management endpoints
    //--------------------------------------------------
    
    /**
     * Adds a new data item to the current tree.
     * 
     * @param key The key for the new data item
     * @param jsonData The JSON data for the new item
     * @return Information about the addition
     */
    @PostMapping("/api/data/add")
    public ResponseEntity<?> addData(@RequestParam String key, @RequestParam String jsonData) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Parse the JSON data
            JSONObject data = new JSONObject(jsonData);
            
            // Add to the current tree
            dataService.addData(key, data);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data added successfully");
            response.put("key", key);
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
     * Deletes a data item from the current tree.
     * 
     * @param key The key of the data item to delete
     * @return Information about the deletion
     */
    @DeleteMapping("/api/data/delete/{key}")
    public ResponseEntity<?> deleteData(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Delete from the current tree
            dataService.deleteData(key);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data deleted successfully");
            response.put("key", key);
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
     * Imports data from a CSV file into the current tree.
     * 
     * @param file The CSV file to import
     * @return Information about the import
     */
    @PostMapping("/api/data/import-csv")
    public ResponseEntity<?> importCsv(@RequestParam("file") MultipartFile file) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Read the CSV file
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            
            // Import the data
            int count = dataService.importFromCsv(reader);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imported " + count + " records successfully");
            response.put("count", count);
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
     * Test endpoint for importing data from a sample CSV file.
     * 
     * @return Information about the import
     */
    @GetMapping("/api/data/import-csv-test")
    public ResponseEntity<?> importCsvTest() {
        long startTime = System.currentTimeMillis();
        
        try {
            // Path to the sample CSV file
            String filePath = "src/main/resources/sample-data.csv";
            
            // Read the CSV file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            
            // Import the data
            int count = dataService.importFromCsv(reader);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imported " + count + " records from sample file");
            response.put("count", count);
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
    
    //--------------------------------------------------
    // Search endpoints
    //--------------------------------------------------
    
    /**
     * Searches for data in the current tree.
     * 
     * @param query The search query
     * @return The search results
     */
    @GetMapping("/api/data/search")
    public ResponseEntity<?> searchData(@RequestParam String query) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Search for the query
            JSONObject result = dataService.search(query);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result != null ? result.toMap() : null);
            response.put("found", result != null);
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
    
    //--------------------------------------------------
    // Display data endpoints
    //--------------------------------------------------
    
    /**
     * Gets data formatted for display in a table.
     * 
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction (ASC or DESC)
     * @return The table display data
     */
    @GetMapping("/api/data/table")
    public ResponseEntity<?> getTableData(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Get data for table display
            Map<String, Object> tableData = dataService.getTableDisplayData(sortBy, sortDirection);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", tableData);
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
     * Gets data formatted for display in datacards.
     * 
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction (ASC or DESC)
     * @return The datacard display data
     */
    @GetMapping("/api/data/cards")
    public ResponseEntity<?> getDataCardData(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Get data for datacard display
            List<Map<String, Object>> cardData = dataService.getDataCardDisplayData(sortBy, sortDirection);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", cardData);
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
    
    //--------------------------------------------------
    // Sorting endpoints
    //--------------------------------------------------
    
    /**
     * Gets all available properties that can be used for sorting.
     * Uses heap sort algorithm for sorting the properties.
     * 
     * @return A list of property names that can be used for sorting
     */
    @GetMapping("/api/data/sortable-properties")
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
    @GetMapping("/api/data/sort-by-property")
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
    @GetMapping("/api/data/heapsort")
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
    @GetMapping("/api/data/heapsort/tree/{treeType}")
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
    
    /**
     * Gets all data sorted by a property.
     * 
     * @param property The property to sort by
     * @return A list of values sorted by the specified property
     */
    @GetMapping("/api/data/sort")
    public ResponseEntity<?> sortData(@RequestParam String property) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Sort data by property
            List<JSONObject> sortedData = dataService.getAllValuesSortedBy(property);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", sortedData);
            response.put("property", property);
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
}
