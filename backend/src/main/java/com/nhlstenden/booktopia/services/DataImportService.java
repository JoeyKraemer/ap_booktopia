package com.nhlstenden.booktopia.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for importing data from various sources.
 * Currently supports CSV imports.
 * 
 * @param <K> Type of keys in the trees (must be Comparable)
 * @param <V> Type of values in the trees
 */
@Service
public class DataImportService<K extends Comparable<K>, V> {

    @Autowired
    private TreeService<K, V> treeService;
    
    /**
     * Imports data from a CSV file into the current tree structure.
     * Uses the header row to determine field names.
     * 
     * @param filePath The path to the CSV file
     * @throws IOException If there's an error reading the file
     */
    public void importCSV(String filePath) throws IOException {
        long startTime = System.currentTimeMillis();
        
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            importFromCsv(reader);
        } catch (Exception e) {
            throw new IOException("Error importing CSV: " + e.getMessage(), e);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("importCSV processing time: " + (endTime - startTime) + "ms");
    }
    
    /**
     * Imports data from a CSV file into the current tree structure.
     * Uses the header row to determine field names.
     * 
     * @param reader The BufferedReader for the CSV file
     * @return The number of records imported
     */
    public int importFromCsv(BufferedReader reader) throws Exception {
        long startTime = System.currentTimeMillis();
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
                
                // Insert into the current tree - need to cast to the generic types
                // This assumes K is String and V is JSONObject in practice
                treeService.insert((K)key, (V)data);
                count++;
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("importFromCsv processing time: " + (endTime - startTime) + "ms");
        
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
}