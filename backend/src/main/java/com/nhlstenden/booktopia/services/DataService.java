package com.nhlstenden.booktopia.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service for data operations on the current tree structure.
 * This service handles adding, deleting, sorting, and importing data.
 */
@Service
public class DataService {

    @Autowired
    private TreeConverterService<String, JSONObject> treeConverterService;
    
    /**
     * Adds a key-value pair to the current tree structure.
     * 
     * @param key The key to add
     * @param value The value associated with the key
     */
    public void addData(String key, JSONObject value) {
        treeConverterService.insert(key, value);
    }
    
    /**
     * Deletes a key-value pair from the current tree structure.
     * 
     * @param key The key to delete
     */
    public void deleteData(String key) {
        treeConverterService.delete(key);
    }
    
    /**
     * Returns all keys in the current tree structure, sorted in ascending order.
     * 
     * @return A list of all keys, sorted
     */
    public List<String> getAllKeysSorted() {
        return treeConverterService.getAllKeys();
    }
    
    /**
     * Returns all values in the current tree structure, sorted by a specific property.
     * 
     * @param property The property to sort by
     * @return A list of all values, sorted by the specified property
     */
    public List<JSONObject> getAllValuesSortedBy(String property) {
        List<JSONObject> values = treeConverterService.getAllValues();
        
        // Sort values by the specified property
        values.sort(new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                // Handle case where property doesn't exist in one or both objects
                if (!o1.has(property) && !o2.has(property)) {
                    return 0;
                } else if (!o1.has(property)) {
                    return -1;
                } else if (!o2.has(property)) {
                    return 1;
                }
                
                // Compare the property values
                try {
                    String val1 = o1.get(property).toString();
                    String val2 = o2.get(property).toString();
                    return val1.compareTo(val2);
                } catch (Exception e) {
                    System.err.println("Error comparing values by property: " + property);
                    return 0;
                }
            }
        });
        
        return values;
    }
    
    /**
     * Imports data from a CSV file into the current tree structure.
     * Uses the header row to determine field names.
     * 
     * @param reader The BufferedReader for the CSV file
     * @return The number of records imported
     */
    public int importFromCsv(BufferedReader reader) throws Exception {
        int count = 0;
        String line;
        
        // Read header line to get field names
        String headerLine = reader.readLine();
        if (headerLine == null) {
            throw new Exception("CSV file is empty or has no header row");
        }
        
        List<String> headers = parseCSVLine(headerLine);
        
        while ((line = reader.readLine()) != null) {
            List<String> fields = parseCSVLine(line);
            
            if (fields.size() > 0) {
                // Use the first column as the key (usually ID or title)
                String key = fields.get(0);
                
                // Create a JSONObject to store the data
                JSONObject data = new JSONObject();
                
                // Map each field to its corresponding header
                for (int i = 0; i < fields.size() && i < headers.size(); i++) {
                    data.put(headers.get(i), fields.get(i));
                }
                
                // Insert into the current tree
                addData(key, data);
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Parse a CSV line, properly handling quoted fields that may contain commas.
     * 
     * @param line The CSV line to parse
     * @return A list of fields from the CSV line
     */
    private List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();
        
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString());
                currentField = new StringBuilder();
            } else {
                currentField.append(c);
            }
        }
        
        result.add(currentField.toString());
        return result;
    }
    
    /**
     * Gets the current tree type from the TreeConverterService.
     * 
     * @return The current tree type
     */
    public String getCurrentTreeType() {
        return treeConverterService.getCurrentTreeType();
    }
    
    /**
     * Gets all available properties that can be used for sorting.
     * This extracts property names from the first few values in the current data structure.
     * 
     * @return A list of property names that can be used for sorting
     */
    public List<String> getSortableProperties() {
        List<JSONObject> values = treeConverterService.getAllValues();
        
        // Use a set to avoid duplicate property names
        Set<String> propertyNames = new HashSet<>();
        
        // Always include "key" as a sortable property
        propertyNames.add("key");
        
        // Extract property names from the first few values (up to 10)
        int count = 0;
        for (JSONObject value : values) {
            if (value != null) {
                for (String key : value.keySet()) {
                    propertyNames.add(key);
                }
                
                // Limit to checking 10 values to avoid performance issues with large datasets
                if (++count >= 10) {
                    break;
                }
            }
        }
        
        // Convert to list and sort alphabetically
        List<String> sortedProperties = new ArrayList<>(propertyNames);
        Collections.sort(sortedProperties);
        
        return sortedProperties;
    }
    
    /**
     * Gets data formatted for display in a table on the frontend.
     * 
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction ("ASC" or "DESC")
     * @return A map containing column definitions and row data
     */
    public Map<String, Object> getTableDisplayData(String sortBy, String sortDirection) {
        Map<String, Object> result = new HashMap<>();
        
        // Get all keys and values
        List<String> keys = treeConverterService.getAllKeys();
        List<JSONObject> values = treeConverterService.getAllValues();
        
        // Create a list to hold all row data
        List<Map<String, Object>> rows = new ArrayList<>();
        
        // Create a set to track all column names
        Set<String> columnNames = new HashSet<>();
        columnNames.add("key"); // Always include key as a column
        
        // Combine keys and values into row data
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            JSONObject value = i < values.size() ? values.get(i) : null;
            
            Map<String, Object> row = new HashMap<>();
            row.put("key", key);
            
            if (value != null) {
                for (String propName : value.keySet()) {
                    Object propValue = value.get(propName);
                    row.put(propName, propValue);
                    columnNames.add(propName);
                }
            }
            
            rows.add(row);
        }
        
        // Sort the rows if sortBy is specified
        if (sortBy != null && !sortBy.isEmpty()) {
            final String finalSortBy = sortBy;
            final boolean ascending = !"DESC".equalsIgnoreCase(sortDirection);
            
            Collections.sort(rows, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> row1, Map<String, Object> row2) {
                    Object val1 = row1.get(finalSortBy);
                    Object val2 = row2.get(finalSortBy);
                    
                    if (val1 == null && val2 == null) {
                        return 0;
                    } else if (val1 == null) {
                        return ascending ? -1 : 1;
                    } else if (val2 == null) {
                        return ascending ? 1 : -1;
                    }
                    
                    int result = val1.toString().compareTo(val2.toString());
                    return ascending ? result : -result;
                }
            });
        }
        
        // Convert column names to a sorted list
        List<String> columns = new ArrayList<>(columnNames);
        Collections.sort(columns);
        
        // Move "key" to the first position
        if (columns.remove("key")) {
            columns.add(0, "key");
        }
        
        // Build the result
        result.put("columns", columns);
        result.put("rows", rows);
        
        return result;
    }
    
    /**
     * Gets data formatted for display in datacards on the frontend.
     * Each datacard has a title (key) and all values.
     * 
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction ("ASC" or "DESC")
     * @return A list of datacards with title and values
     */
    public List<Map<String, Object>> getDataCardDisplayData(String sortBy, String sortDirection) {
        // Get all keys and values
        List<String> keys = treeConverterService.getAllKeys();
        List<JSONObject> values = treeConverterService.getAllValues();
        
        // Create a list to hold all datacard data
        List<Map<String, Object>> datacards = new ArrayList<>();
        
        // Combine keys and values into datacard format
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            JSONObject value = i < values.size() ? values.get(i) : null;
            
            Map<String, Object> datacard = new HashMap<>();
            datacard.put("title", key);
            
            if (value != null) {
                Map<String, Object> cardValues = new HashMap<>();
                
                // Add all fields from the value
                for (String field : value.keySet()) {
                    cardValues.put(field, value.get(field));
                }
                
                datacard.put("values", cardValues);
            } else {
                datacard.put("values", new HashMap<>());
            }
            
            datacards.add(datacard);
        }
        
        // Sort the datacards if sortBy is specified
        if (sortBy != null && !sortBy.isEmpty()) {
            final String finalSortBy = sortBy;
            final boolean ascending = !"DESC".equalsIgnoreCase(sortDirection);
            
            Collections.sort(datacards, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> card1, Map<String, Object> card2) {
                    if (finalSortBy.equals("title")) {
                        String title1 = (String) card1.get("title");
                        String title2 = (String) card2.get("title");
                        
                        if (title1 == null && title2 == null) {
                            return 0;
                        } else if (title1 == null) {
                            return ascending ? -1 : 1;
                        } else if (title2 == null) {
                            return ascending ? 1 : -1;
                        }
                        
                        int result = title1.compareTo(title2);
                        return ascending ? result : -result;
                    } else {
                        // Sort by a value field
                        Map<String, Object> values1 = (Map<String, Object>) card1.get("values");
                        Map<String, Object> values2 = (Map<String, Object>) card2.get("values");
                        
                        Object val1 = values1.get(finalSortBy);
                        Object val2 = values2.get(finalSortBy);
                        
                        if (val1 == null && val2 == null) {
                            return 0;
                        } else if (val1 == null) {
                            return ascending ? -1 : 1;
                        } else if (val2 == null) {
                            return ascending ? 1 : -1;
                        }
                        
                        int result = val1.toString().compareTo(val2.toString());
                        return ascending ? result : -result;
                    }
                }
            });
        }
        
        return datacards;
    }
    
    /**
     * Searches for data in the current tree structure.
     * This method uses the tree-specific search algorithms when possible for exact key matches,
     * and falls back to a more comprehensive search for partial matches in keys and values.
     * 
     * @param query The search query
     * @return A list of matching data items
     */
    public List<Map<String, Object>> searchData(String query) {
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
            JSONObject exactMatch = (JSONObject) treeConverterService.search(query);
            if (exactMatch != null) {
                // Found an exact match by key
                Map<String, Object> result = new HashMap<>();
                result.put("key", query);
                
                // Add all fields from the value
                for (String field : exactMatch.keySet()) {
                    result.put(field, exactMatch.get(field));
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
        List<String> keys = treeConverterService.getAllKeys();
        List<JSONObject> values = treeConverterService.getAllValues();
        
        // Search through all data for partial matches
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            JSONObject value = i < values.size() ? values.get(i) : null;
            
            // Skip if this key was already added as an exact match
            if (key.equals(query) && !results.isEmpty()) {
                continue;
            }
            
            boolean matches = false;
            
            // Check if key contains the query
            if (key.toLowerCase().contains(lowerQuery)) {
                matches = true;
            }
            
            // Check if any value contains the query
            if (!matches && value != null) {
                for (String field : value.keySet()) {
                    String fieldValue = value.get(field).toString();
                    if (fieldValue.toLowerCase().contains(lowerQuery)) {
                        matches = true;
                        break;
                    }
                }
            }
            
            // If there's a match, add to results
            if (matches) {
                Map<String, Object> result = new HashMap<>();
                result.put("key", key);
                
                // Add all fields from the value
                if (value != null) {
                    for (String field : value.keySet()) {
                        result.put(field, value.get(field));
                    }
                }
                
                results.add(result);
            }
        }
        
        return results;
    }
}
