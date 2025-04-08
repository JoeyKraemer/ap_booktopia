package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.services.TreeConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // For development
public class HomeController {

    @Autowired
    private TreeConverterService<Integer, String> treeConverterService;

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
    public ResponseEntity<?> insertIntoTree(@RequestParam Integer key, @RequestParam(required = false) String value) {
        treeConverterService.insert(key, value);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("currentTree", treeConverterService.getCurrentTreeType());
        
        return ResponseEntity.ok(response);
    }
}