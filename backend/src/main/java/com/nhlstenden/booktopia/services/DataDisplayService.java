package com.nhlstenden.booktopia.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Service for formatting data for display in the UI.
 * This service provides methods for table and card display formats.
 * 
 * @param <K> Type of keys in the trees (must be Comparable)
 * @param <V> Type of values in the trees
 */
@Service
public class DataDisplayService<K extends Comparable<K>, V> {

    @Autowired
    private TreeService<K, V> treeService;
    
    /**
     * Gets all available properties that can be used for sorting.
     * This extracts property names from the first few values in the current data structure.
     * 
     * @return A list of property names that can be used for sorting
     */
    public List<String> getSortableProperties() {
        long startTime = System.currentTimeMillis();
        
        List<V> values = treeService.getAllValues();
        
        // Use a set to avoid duplicate property names
        Set<String> propertyNames = new HashSet<>();
        
        // Always include "key" as a sortable property
        propertyNames.add("key");
        
        // Extract property names from the first few values (up to 10)
        int count = 0;
        for (V value : values) {
            if (value != null) {
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    for (String key : jsonObject.keySet()) {
                        propertyNames.add(key);
                    }
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
        
        long endTime = System.currentTimeMillis();
        System.out.println("getSortableProperties processing time: " + (endTime - startTime) + "ms");
        
        return sortedProperties;
    }
    
    /**
     * Gets data formatted for display in a table on the frontend.
     * 
     * @return A list of maps containing data formatted for table display
     */
    public List<Map<String, Object>> getTableDisplayData() {
        return getTableDisplayData(null, null);
    }
    
    /**
     * Gets data formatted for display in a table on the frontend.
     * 
     * @param sortBy The property to sort by (optional)
     * @param sortDirection The sort direction ("ASC" or "DESC")
     * @return A list of maps containing data formatted for table display
     */
    public List<Map<String, Object>> getTableDisplayData(String sortBy, String sortDirection) {
        long startTime = System.currentTimeMillis();
        
        Map<String, Object> tableData = new HashMap<>();
        
        // Get all keys and values
        List<K> keys = treeService.getAllKeys();
        List<V> values = treeService.getAllValues();
        
        // Create a list to hold all row data
        List<Map<String, Object>> rows = new ArrayList<>();
        
        // Create a set to track all column names
        Set<String> columnNames = new HashSet<>();
        columnNames.add("key"); // Always include key as a column
        
        // Combine keys and values into row data
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = i < values.size() ? values.get(i) : null;
            
            Map<String, Object> row = new HashMap<>();
            row.put("key", key);
            
            if (value != null) {
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    for (String propName : jsonObject.keySet()) {
                        Object propValue = jsonObject.get(propName);
                        row.put(propName, propValue);
                        columnNames.add(propName);
                    }
                }
            }
            
            rows.add(row);
        }
        
        // Sort the rows if sortBy is specified
        if (sortBy != null && !sortBy.isEmpty()) {
            final String finalSortBy = sortBy;
            final boolean ascending = !"DESC".equalsIgnoreCase(sortDirection);
            
            Collections.sort(rows, (row1, row2) -> {
                Object val1 = row1.get(finalSortBy);
                Object val2 = row2.get(finalSortBy);
                
                if (val1 == null && val2 == null) {
                    return 0;
                } else if (val1 == null) {
                    return ascending ? -1 : 1;
                } else if (val2 == null) {
                    return ascending ? 1 : -1;
                }
                
                int compareResult = val1.toString().compareTo(val2.toString());
                return ascending ? compareResult : -compareResult;
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
        tableData.put("columns", columns);
        tableData.put("rows", rows);
        tableData.put("processingTimeMs", System.currentTimeMillis() - startTime);
        
        return rows; // Return just the rows for compatibility
    }
    
    /**
     * Gets data formatted for display in datacards on the frontend.
     *
     * @return A list of maps containing data formatted for card display
     */
    public List<Map<String, Object>> getDataCardDisplayData() {
        return getDataCardDisplayData(null, null);
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
        long startTime = System.currentTimeMillis();
        
        // Get all keys and values
        List<K> keys = treeService.getAllKeys();
        List<V> values = treeService.getAllValues();
        
        // Create a list to hold all datacard data
        List<Map<String, Object>> datacards = new ArrayList<>();
        
        // Combine keys and values into datacard format
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = i < values.size() ? values.get(i) : null;
            
            Map<String, Object> datacard = new HashMap<>();
            datacard.put("title", key);
            
            if (value != null) {
                Map<String, Object> cardValues = new HashMap<>();
                
                // Add all fields from the value
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    for (String field : jsonObject.keySet()) {
                        cardValues.put(field, jsonObject.get(field));
                    }
                }
                
                datacard.put("values", cardValues);
            }
            
            datacards.add(datacard);
        }
        
        // Sort the datacards if sortBy is specified
        if (sortBy != null && !sortBy.isEmpty()) {
            final String finalSortBy = sortBy;
            final boolean ascending = !"DESC".equalsIgnoreCase(sortDirection);
            
            Collections.sort(datacards, (card1, card2) -> {
                if (finalSortBy.equals("title")) {
                    K title1 = (K) card1.get("title");
                    K title2 = (K) card2.get("title");
                    
                    if (title1 == null && title2 == null) {
                        return 0;
                    } else if (title1 == null) {
                        return ascending ? -1 : 1;
                    } else if (title2 == null) {
                        return ascending ? 1 : -1;
                    }
                    
                    int compareResult = title1.toString().compareTo(title2.toString());
                    return ascending ? compareResult : -compareResult;
                } else {
                    Map<String, Object> values1 = (Map<String, Object>) card1.get("values");
                    Map<String, Object> values2 = (Map<String, Object>) card2.get("values");
                    
                    if (values1 == null && values2 == null) {
                        return 0;
                    } else if (values1 == null) {
                        return ascending ? -1 : 1;
                    } else if (values2 == null) {
                        return ascending ? 1 : -1;
                    }
                    
                    Object val1 = values1.get(finalSortBy);
                    Object val2 = values2.get(finalSortBy);
                    
                    if (val1 == null && val2 == null) {
                        return 0;
                    } else if (val1 == null) {
                        return ascending ? -1 : 1;
                    } else if (val2 == null) {
                        return ascending ? 1 : -1;
                    }
                    
                    int compareResult = val1.toString().compareTo(val2.toString());
                    return ascending ? compareResult : -compareResult;
                }
            });
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("getDataCardDisplayData processing time: " + (endTime - startTime) + "ms");
        
        return datacards;
    }
    
    /**
     * Sorts the data by the specified field.
     *
     * @param field The field to sort by
     * @param ascending Whether to sort in ascending order
     * @return A list of maps containing sorted data
     */
    public List<Map<String, Object>> sortDataBy(String field, boolean ascending) {
        // Determine sort direction based on ascending flag
        String sortDirection = ascending ? "ASC" : "DESC";
        
        // Reuse the table display data method with sorting parameters
        return getTableDisplayData(field, sortDirection);
    }
    
    /**
     * Returns all values in the current tree structure, sorted by a specific property.
     * 
     * @param property The property to sort by
     * @return A list of all values, sorted by the specified property
     */
    public List<V> getAllValuesSortedBy(String property) {
        long startTime = System.currentTimeMillis();
        
        List<V> values = treeService.getAllValues();
        
        // Sort values by the specified property
        values.sort((o1, o2) -> {
            // Only compare JSONObjects based on their properties
            if (o1 instanceof JSONObject && o2 instanceof JSONObject) {
                JSONObject jsonObject1 = (JSONObject) o1;
                JSONObject jsonObject2 = (JSONObject) o2;
                
                if (!jsonObject1.has(property) && !jsonObject2.has(property)) {
                    return 0;
                } else if (!jsonObject1.has(property)) {
                    return -1;
                } else if (!jsonObject2.has(property)) {
                    return 1;
                }
                
                // Compare the property values
                try {
                    String val1 = jsonObject1.get(property).toString();
                    String val2 = jsonObject2.get(property).toString();
                    return val1.compareTo(val2);
                } catch (Exception e) {
                    System.err.println("Error comparing values by property: " + property);
                    return 0;
                }
            } else {
                return 0;
            }
        });
        
        long endTime = System.currentTimeMillis();
        System.out.println("getAllValuesSortedBy processing time: " + (endTime - startTime) + "ms");
        
        return values;
    }
}