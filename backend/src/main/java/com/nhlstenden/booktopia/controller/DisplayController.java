package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // For development
@RequestMapping("/api/display")
public class DisplayController {

    @Autowired
    private DataService dataService;
    
    /**
     * Gets data formatted for display in a table.
     * 
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction (ASC or DESC)
     * @return The table display data
     */
    @GetMapping("/table")
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
    @GetMapping("/cards")
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
}