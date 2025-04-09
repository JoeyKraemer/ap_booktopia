package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.services.DataService;
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

@RestController
@CrossOrigin(origins = "*") // For development
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * Adds a new data item to the current tree.
     * 
     * @param key The key for the new data item
     * @param jsonData The JSON data for the new item
     * @return Information about the addition
     */
    @PostMapping("/add")
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
    @DeleteMapping("/delete/{key}")
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
    @PostMapping("/import-csv")
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
    @GetMapping("/import-csv-test")
    public ResponseEntity<?> importCsvTest() {
        long startTime = System.currentTimeMillis();
        
        try {
            // Path to the sample CSV file
            String filePath = "src/main/resources/books.csv";
            
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
    
    /**
     * Searches for data in the current tree.
     * 
     * @param query The search query
     * @return The search results
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchData(@RequestParam String query) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Search for the query
            List<Map<String, Object>> results = dataService.searchData(query);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("results", results);
            response.put("found", !results.isEmpty());
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
     * Gets all data sorted by a property.
     * 
     * @param property The property to sort by
     * @return A list of values sorted by the specified property
     */
    @GetMapping("/sort")
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