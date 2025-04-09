package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.services.DataService;
import com.nhlstenden.booktopia.services.TreeConverterService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // For development
@RequestMapping("/api/tree")
public class TreeController {

    @Autowired
    private TreeConverterService<String, JSONObject> treeConverterService;
    
    @Autowired
    private DataService dataService;

    /**
     * Converts the current tree to a different type.
     * 
     * @param targetTree The target tree type (AVL, BST, or BTREE)
     * @return Information about the conversion
     */
    @PostMapping("/convert")
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
    @GetMapping("/current")
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
    @PostMapping("/insert")
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
}