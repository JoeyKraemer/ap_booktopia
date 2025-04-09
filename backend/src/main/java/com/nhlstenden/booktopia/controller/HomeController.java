package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.services.DataService;
import com.nhlstenden.booktopia.services.TreeConverterService;
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

    @GetMapping("/")
    public String home() {
        return "Welcome to Booktopia!";
    }
    
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
                    return ResponseEntity.badRequest().body("Invalid tree type: " + targetTree);
            }
            
            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("currentTree", treeConverterService.getCurrentTreeType());
            response.put("processingTimeMs", processingTime);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long processingTime = endTime - startTime;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            response.put("processingTimeMs", processingTime);
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/api/tree/current")
    public ResponseEntity<?> getCurrentTree() {
        Map<String, Object> response = new HashMap<>();
        response.put("treeType", treeConverterService.getCurrentTreeType());
        response.put("keys", treeConverterService.getAllKeys());
        response.put("values", treeConverterService.getAllValues());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/api/tree/insert")
    public ResponseEntity<?> insertIntoTree(@RequestBody Map<String, Object> requestData) {
        try {
            String key = (String) requestData.get("key");
            Map<String, Object> valueMap = (Map<String, Object>) requestData.get("value");
            
            if (key == null || valueMap == null) {
                return ResponseEntity.badRequest().body("Key and value are required");
            }
            
            JSONObject value = new JSONObject(valueMap);
            treeConverterService.insert(key, value);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("currentTree", treeConverterService.getCurrentTreeType());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error inserting data: " + e.getMessage());
        }
    }
    
    // New endpoints for data operations
    
    @PostMapping("/api/data/add")
    public ResponseEntity<?> addData(@RequestBody Map<String, Object> requestData) {
        long startTime = System.currentTimeMillis();
        
        try {
            String key = (String) requestData.get("key");
            Map<String, Object> valueMap = (Map<String, Object>) requestData.get("value");
            
            if (key == null || valueMap == null) {
                return ResponseEntity.badRequest().body("Key and value are required");
            }
            
            JSONObject value = new JSONObject(valueMap);
            dataService.addData(key, value);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data added successfully");
            response.put("currentTree", dataService.getCurrentTreeType());
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
    
    @DeleteMapping("/api/data/delete/{key}")
    public ResponseEntity<?> deleteData(@PathVariable String key) {
        long startTime = System.currentTimeMillis();
        
        try {
            dataService.deleteData(key);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data deleted successfully");
            response.put("currentTree", dataService.getCurrentTreeType());
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

    // add 2 sorting algorithms @NICO
    @GetMapping("/api/data/sort")
    public ResponseEntity<?> sortData(@RequestParam(required = false, defaultValue = "key") String sortBy) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("currentTree", dataService.getCurrentTreeType());
            
            if (sortBy.equalsIgnoreCase("key")) {
                response.put("data", dataService.getAllKeysSorted());
            } else {
                response.put("data", dataService.getAllValuesSortedBy(sortBy));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sorting data: " + e.getMessage());
        }
    }
    
    @PostMapping("/api/data/import-csv")
    public ResponseEntity<?> importCsvData(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            int count = dataService.importFromCsv(reader);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", count + " records imported successfully");
            response.put("currentTree", dataService.getCurrentTreeType());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing CSV data: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/data/sortable-properties")
    public ResponseEntity<?> getSortableProperties() {
        long startTime = System.currentTimeMillis();
        
        try {
            List<String> properties = dataService.getSortableProperties();
            
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
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/api/data/table")
    public ResponseEntity<?> getTableData(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        long startTime = System.currentTimeMillis();
        
        try {
            Map<String, Object> tableData = dataService.getTableDisplayData(sortBy, sortDirection);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("tableData", tableData);
            response.put("currentTree", dataService.getCurrentTreeType());
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
    
    @GetMapping("/api/data/cards")
    public ResponseEntity<?> getDataCardData(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<Map<String, Object>> cardData = dataService.getDataCardDisplayData(sortBy, sortDirection);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cardData", cardData);
            response.put("currentTree", dataService.getCurrentTreeType());
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
     * Endpoint for searching data in the current tree structure.
     * This allows searching for matches in keys and all fields of the values.
     * 
     * @param query The search query
     * @return ResponseEntity with the search results
     */
    @GetMapping("/api/data/search") // add another algorithm @NICO
    public ResponseEntity<?> searchData(@RequestParam String query) {
        long startTime = System.currentTimeMillis();
        
        try {
            List<Map<String, Object>> searchResults = dataService.searchData(query);
            
            long endTime = System.currentTimeMillis();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("results", searchResults);
            response.put("count", searchResults.size());
            response.put("currentTree", dataService.getCurrentTreeType());
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
     * Temporary GET endpoint for importing CSV data for testing purposes.
     * This allows testing the CSV import functionality directly from a browser.
     * 
     * @return ResponseEntity with the import results
     */
    @GetMapping("/api/data/import-csv-test")
    public ResponseEntity<?> importCsvDataTest() {
        try {
            // Path to the CSV file (adjust this to your actual path)
            String csvFilePath = "src/main/resources/us_disaster_declarations.csv";
            File csvFile = new File(csvFilePath);
            
            if (!csvFile.exists()) {
                return ResponseEntity.badRequest().body("CSV file not found at: " + csvFilePath);
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(csvFile));
            int count = dataService.importFromCsv(reader);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", count + " records imported successfully");
            response.put("currentTree", dataService.getCurrentTreeType());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing CSV data: " + e.getMessage());
        }
    }
}
