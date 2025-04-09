package com.nhlstenden.booktopia.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service for searching data in the tree structures.
 * This service provides search functionality optimized for different tree types.
 * 
 * @param <K> Type of keys in the trees (must be Comparable)
 * @param <V> Type of values in the trees
 */
@Service
public class SearchService<K extends Comparable<K>, V> {

    @Autowired
    private TreeService<K, V> treeService;
    
    /**
     * Searches for data in the current tree structure.
     * This method uses the tree-specific search algorithms when possible for exact key matches,
     * and falls back to a more comprehensive search for partial matches in keys and values.
     * 
     * @param query The search query
     * @return A list of matching data items
     */
    public List<Map<String, Object>> searchData(String query) {
        long startTime = System.currentTimeMillis();
        
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        // Convert query to lowercase for case-insensitive search
        String lowerQuery = query.toLowerCase();
        
        // Create a list to hold search results
        List<Map<String, Object>> results = new ArrayList<>();
        
        // First, try an exact key search using the tree's native search algorithm
        try {
            // Try to use the query as a key for exact match - use String directly since our keys are Strings
            V exactMatch = treeService.search((K) query);
            if (exactMatch != null) {
                // Found an exact match by key
                Map<String, Object> result = new HashMap<>();
                result.put("key", query);
                
                // Add all fields from the value
                if (exactMatch instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) exactMatch;
                    for (String field : jsonObject.keySet()) {
                        result.put(field, jsonObject.get(field));
                    }
                } else {
                    result.put("value", exactMatch);
                }
                
                results.add(result);
                System.out.println("Found exact match using tree-specific search for key: " + query);
                
                // If we found an exact match, we could return early,
                // but we'll continue to search for partial matches as well
            }
        } catch (Exception e) {
            // Ignore exceptions from trying to use the query as a key
            // This is expected if the query isn't a valid key
            System.out.println("Could not perform exact key search: " + e.getMessage());
        }
        
        // Get all keys and values for partial matching
        List<K> keys = treeService.getAllKeys();
        List<V> values = treeService.getAllValues();
        
        // Search through all data for partial matches
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = i < values.size() ? values.get(i) : null;
            
            // Skip if this key was already added as an exact match
            if (key.equals(query) && !results.isEmpty()) {
                continue;
            }
            
            boolean matches = false;
            
            // Check if key contains the query
            if (key.toString().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }
            
            // Check if any value contains the query
            if (!matches && value != null) {
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    for (String field : jsonObject.keySet()) {
                        String fieldValue = jsonObject.get(field).toString();
                        if (fieldValue.toLowerCase().contains(lowerQuery)) {
                            matches = true;
                            break;
                        }
                    }
                } else {
                    String valueString = value.toString();
                    if (valueString.toLowerCase().contains(lowerQuery)) {
                        matches = true;
                    }
                }
            }
            
            // If there's a match, add to results
            if (matches) {
                Map<String, Object> result = new HashMap<>();
                result.put("key", key);
                
                // Add all fields from the value
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    for (String field : jsonObject.keySet()) {
                        result.put(field, jsonObject.get(field));
                    }
                } else {
                    result.put("value", value);
                }
                
                results.add(result);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("searchData processing time: " + (endTime - startTime) + "ms");
        
        // Add processing time to the response
        Map<String, Object> metadataResult = new HashMap<>();
        metadataResult.put("processingTimeMs", endTime - startTime);
        metadataResult.put("isMetadata", true);
        results.add(metadataResult);
        
        return results;
    }
}