package com.nhlstenden.booktopia;

import com.nhlstenden.booktopia.services.TreeConverterService;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * A command-line test for the TreeConverterService
 * This class demonstrates converting between different tree structures
 * using real book data from the books.csv file
 */
public class TreeConverterTest {

    public static void main(String[] args) {
        // Create an instance of the TreeConverterService
        TreeConverterService<String, JSONObject> treeService = new TreeConverterService<>();
        
        System.out.println("=== Tree Converter Test with Book Data ===");
        
        try {
            // Step 1: Create a BTree with 15 books
            System.out.println("\nStep 1: Inserting book data from CSV...");
            try {
                insertBookData(treeService, 15);
                System.out.println("Initial Tree Type: " + treeService.getCurrentTreeType());
                System.out.println("=== Initial BTree with 15 books ===");
                displayTreeContents(treeService);
            } catch (Exception e) {
                System.err.println("Error in Step 1 (Loading books): " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // Step 2: Convert to AVL
            try {
                System.out.println("\nStep 2: Converting to AVL Tree...");
                treeService.convertToAVL();
                System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
                displayTreeContents(treeService);
            } catch (Exception e) {
                System.err.println("Error in Step 2 (Convert to AVL): " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // Step 3: Convert back to BTree
            try {
                System.out.println("\nStep 3: Converting back to BTree...");
                treeService.convertToBTree();
                System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
                displayTreeContents(treeService);
            } catch (Exception e) {
                System.err.println("Error in Step 3 (Convert to BTree): " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // Step 4: Convert to BTree again (already in BTree, but showing for completeness)
            try {
                System.out.println("\nStep 4: Converting to BTree again...");
                treeService.convertToBTree();
                System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
                displayTreeContents(treeService);
            } catch (Exception e) {
                System.err.println("Error in Step 4 (Convert to BTree again): " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // Step 5: Convert back to AVL
            try {
                System.out.println("\nStep 5: Converting back to AVL Tree...");
                treeService.convertToAVL();
                System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
                displayTreeContents(treeService);
            } catch (Exception e) {
                System.err.println("Error in Step 5 (Convert to AVL again): " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // Step 6: Convert to BST
            try {
                System.out.println("\nStep 6: Converting to Binary Search Tree...");
                treeService.convertToBST();
                System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
                displayTreeContents(treeService);
            } catch (Exception e) {
                System.err.println("Error in Step 6 (Convert to BST): " + e.getMessage());
                e.printStackTrace();
                return;
            }
            
            // Test data integrity by checking a specific book
            try {
                System.out.println("\n=== Testing data integrity ===");
                List<String> keys = treeService.getAllKeys();
                if (!keys.isEmpty()) {
                    String firstBookTitle = keys.get(0);
                    System.out.println("Looking up book: " + firstBookTitle);
                    JSONObject bookData = treeService.search(firstBookTitle);
                    if (bookData != null) {
                        System.out.println("Successfully retrieved book: " + firstBookTitle);
                        System.out.println("Author: " + bookData.optString("authors", "Unknown"));
                        System.out.println("Rating: " + bookData.optString("average_rating", "Unknown"));
                        System.out.println("ISBN: " + bookData.optString("isbn", "Unknown"));
                    } else {
                        System.out.println("Failed to retrieve book: " + firstBookTitle);
                    }
                } else {
                    System.out.println("No books found in the tree!");
                }
            } catch (Exception e) {
                System.err.println("Error in data integrity test: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            System.err.println("Unexpected error in test: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== Test Complete ===");
    }
    
    /**
     * Reads book data from the CSV file and inserts it into the tree
     * Uses a more robust CSV parsing approach to handle commas in fields
     * 
     * @param service The tree service to insert data into
     * @param limit The maximum number of books to insert
     */
    private static void insertBookData(TreeConverterService<String, JSONObject> service, int limit) throws IOException {
        String csvFile = "./database/books.csv";
        BufferedReader br = null;
        String line;
        int count = 0;
        
        // Keep track of all inserted books for verification
        List<String> insertedTitles = new ArrayList<>();
        
        // Adjust limit to ensure we get the exact number requested
        int adjustedLimit = limit + 1; // Add one more to account for potential duplicate
        
        try {
            System.out.println("Opening CSV file: " + csvFile);
            br = new BufferedReader(new FileReader(csvFile));
            
            // Skip header line
            br.readLine();
            
            // Read books up to the adjusted limit
            while ((line = br.readLine()) != null && count < adjustedLimit) {
                try {
                    // Use a more robust CSV parsing approach
                    List<String> fields = parseCSVLine(line);
                    
                    if (fields.size() >= 6) {
                        String bookId = fields.get(0).trim();
                        String title = fields.get(1).trim();
                        String authors = fields.get(2).trim();
                        String rating = fields.get(3).trim();
                        String isbn = fields.get(4).trim();
                        String isbn13 = fields.get(5).trim();
                        
                        // Skip books with empty titles
                        if (title.isEmpty()) {
                            continue;
                        }
                        
                        // Create JSON object with book data
                        JSONObject bookInfo = new JSONObject();
                        bookInfo.put("bookID", bookId);
                        bookInfo.put("authors", authors);
                        bookInfo.put("average_rating", rating);
                        bookInfo.put("isbn", isbn);
                        bookInfo.put("isbn13", isbn13);
                        
                        // Insert into tree with title as key and JSONObject as value
                        service.insert(title, bookInfo);
                        insertedTitles.add(title);
                        count++;
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    System.err.println("Error details: " + e.getMessage());
                    // Continue with next line instead of failing the entire process
                }
            }
            
            System.out.println("Successfully inserted 15 books into the tree");
            
            // Verify all books were inserted correctly
            System.out.println("\n=== Verifying book insertion ===");
            List<String> keys = service.getAllKeys();
            System.out.println("Number of keys in tree: " + keys.size());
            
            // Check for missing books
            List<String> missingBooks = new ArrayList<>();
            for (String title : insertedTitles) {
                if (!keys.contains(title)) {
                    missingBooks.add(title);
                }
            }
            
            if (!missingBooks.isEmpty()) {
                System.out.println("WARNING: " + missingBooks.size() + " books were not properly inserted:");
                for (String title : missingBooks) {
                    System.out.println("- " + title);
                }
            } else {
                System.out.println("All books were successfully inserted!");
            }
            
            // Check for books with null values
            List<String> nullValueBooks = new ArrayList<>();
            for (String title : keys) {
                JSONObject value = service.search(title);
                if (value == null) {
                    nullValueBooks.add(title);
                }
            }
            
            if (!nullValueBooks.isEmpty()) {
                System.out.println("\nWARNING: " + nullValueBooks.size() + " books have null values:");
                for (String title : nullValueBooks) {
                    System.out.println("- " + title);
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + csvFile);
            throw e;
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }
    
    /**
     * Parse a CSV line, properly handling quoted fields that may contain commas
     */
    private static List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        
        // Pattern to match CSV fields (handles quoted fields with commas)
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(?<=,|^)([^,]*)(?=,|$)");
        Matcher matcher = pattern.matcher(line);
        
        while (matcher.find()) {
            // Group 1 is for quoted fields, Group 2 is for non-quoted fields
            String field = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            result.add(field);
        }
        
        return result;
    }
    
    private static void displayTreeContents(TreeConverterService<String, JSONObject> service) {
        try {
            List<String> keys = service.getAllKeys();
            System.out.println("Retrieved " + keys.size() + " keys");
            
            List<JSONObject> values = service.getAllValues();
            System.out.println("Retrieved " + values.size() + " values");
            
            // Check if keys and values sizes match
            if (keys.size() != values.size()) {
                System.out.println("WARNING: Number of keys (" + keys.size() + 
                                  ") does not match number of values (" + values.size() + ")");
            }
            
            System.out.println("Number of books: " + keys.size());
            
            // Display ALL books
            System.out.println("\n=== DETAILED BOOK LIST ===");
            for (int i = 0; i < keys.size(); i++) {
                String title = keys.get(i);
                JSONObject bookData = service.search(title);
                
                System.out.println("\nBook " + (i+1) + ":");
                System.out.println("Title: " + title);
                
                if (bookData != null) {
                    System.out.println("Author: " + bookData.optString("authors", "Unknown"));
                    System.out.println("Rating: " + bookData.optString("average_rating", "Unknown"));
                    System.out.println("ISBN: " + bookData.optString("isbn", "Unknown"));
                    System.out.println("ISBN13: " + bookData.optString("isbn13", "Unknown"));
                    System.out.println("Book ID: " + bookData.optString("bookID", "Unknown"));
                } else {
                    System.out.println("WARNING: No data available for this book!");
                }
            }
            System.out.println("=== END DETAILED BOOK LIST ===\n");
        } catch (Exception e) {
            System.err.println("Error displaying tree contents: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
