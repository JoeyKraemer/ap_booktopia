package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.services.DataService;
import com.nhlstenden.booktopia.services.TreeConverterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("currentTree", treeConverterService.getCurrentTreeType());
        
        return ResponseEntity.ok(response);
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
        try {
            String key = (String) requestData.get("key");
            Map<String, Object> valueMap = (Map<String, Object>) requestData.get("value");
            
            if (key == null || valueMap == null) {
                return ResponseEntity.badRequest().body("Key and value are required");
            }
            
            JSONObject value = new JSONObject(valueMap);
            dataService.addData(key, value);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data added successfully");
            response.put("currentTree", dataService.getCurrentTreeType());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding data: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/api/data/delete/{key}")
    public ResponseEntity<?> deleteData(@PathVariable String key) {
        try {
            dataService.deleteData(key);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Data deleted successfully");
            response.put("currentTree", dataService.getCurrentTreeType());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting data: " + e.getMessage());
        }
    }
    
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
        try {
            List<String> properties = dataService.getSortableProperties();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("properties", properties);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting sortable properties: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/data/table")
    public ResponseEntity<?> getTableData(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        try {
            Map<String, Object> tableData = dataService.getTableDisplayData(sortBy, sortDirection);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("tableData", tableData);
            response.put("currentTree", dataService.getCurrentTreeType());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting table data: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/data/cards")
    public ResponseEntity<?> getDataCardData(
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDirection) {
        try {
            List<Map<String, Object>> cardData = dataService.getDataCardDisplayData(sortBy, sortDirection);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cardData", cardData);
            response.put("currentTree", dataService.getCurrentTreeType());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting datacard data: " + e.getMessage());
        }
    }
}