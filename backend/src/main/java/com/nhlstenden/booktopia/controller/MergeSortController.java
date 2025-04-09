package com.nhlstenden.booktopia.controller;

import com.nhlstenden.booktopia.sorting.MergeSort;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/mergeSort")
public class MergeSortController {

    @Autowired
    private MergeSort<String, JSONObject> mergeSortService;

    /**
     * GET endpoint to return all keys sorted using merge sort.
     */
    @GetMapping("/keys")
    public ResponseEntity<?> mergeSortKeys() {
        try {
            List<String> sortedKeys = mergeSortService.sortKeys();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("keys", sortedKeys);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET endpoint to return keys with their corresponding values sorted by key using merge sort.
     */
    @GetMapping("/keysWithValues")
    public ResponseEntity<?> mergeSortKeysWithValues() {
        try {
            Map<String, JSONObject> sortedData = mergeSortService.sortKeysWithValues();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", sortedData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * GET endpoint to return data sorted by a specified property using merge sort.
     *
     * Example usage:
     *   /api/mergeSort/sortByProperty?property=author&direction=ASC
     *
     * @param property the property to sort by; default is "key"
     * @param direction sort direction ("ASC" or "DESC"); default is "ASC"
     */
    @GetMapping("/sortByProperty")
    public ResponseEntity<?> mergeSortByProperty(
            @RequestParam(required = false, defaultValue = "key") String property,
            @RequestParam(required = false, defaultValue = "ASC") String direction) {
        long startTime = System.currentTimeMillis();
        try {
            boolean ascending = !"DESC".equalsIgnoreCase(direction);
            List<Map<String, Object>> sortedData = mergeSortService.sortByProperty(property, ascending);
            long endTime = System.currentTimeMillis();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", sortedData);
            response.put("property", property);
            response.put("direction", direction);
            response.put("processingTimeMs", endTime - startTime);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
