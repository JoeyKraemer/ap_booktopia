package com.nhlstenden.booktopia;

import com.nhlstenden.booktopia.services.TreeConverterService;

/**
 * A command-line test for the TreeConverterService
 * This class demonstrates converting between different tree structures
 */
public class TreeConverterTest {

    public static void main(String[] args) {
        // Create an instance of the TreeConverterService
        TreeConverterService<Integer, String> treeService = new TreeConverterService<>();
        
        System.out.println("=== Tree Converter Test ===");
        
        // Insert some test data
        System.out.println("\nInserting test data...");
        insertTestData(treeService);
        
        // Display initial state (BTree is default)
        System.out.println("\nInitial Tree Type: " + treeService.getCurrentTreeType());
        displayTreeContents(treeService);
        
        // Convert to AVL Tree
        System.out.println("\n=== Converting to AVL Tree ===");
        treeService.convertToAVL();
        System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
        displayTreeContents(treeService);
        
        // Convert to BST
        System.out.println("\n=== Converting to Binary Search Tree ===");
        treeService.convertToBST();
        System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
        displayTreeContents(treeService);
        
        // Convert back to BTree
        System.out.println("\n=== Converting to B-Tree ===");
        treeService.convertToBTree();
        System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
        displayTreeContents(treeService);
        
        // Add more data to current tree
        System.out.println("\n=== Adding more data to current tree ===");
        treeService.insert(90, "Value-90");
        treeService.insert(100, "Value-100");
        displayTreeContents(treeService);
        
        // Convert to AVL again to show data preservation
        System.out.println("\n=== Converting to AVL Tree again ===");
        treeService.convertToAVL();
        System.out.println("Current Tree Type: " + treeService.getCurrentTreeType());
        displayTreeContents(treeService);
        
        System.out.println("\n=== Test Complete ===");
    }
    
    private static void insertTestData(TreeConverterService<Integer, String> service) {
        service.insert(50, "Value-50");
        service.insert(30, "Value-30");
        service.insert(70, "Value-70");
        service.insert(20, "Value-20");
        service.insert(40, "Value-40");
        service.insert(60, "Value-60");
        service.insert(80, "Value-80");
    }
    
    private static void displayTreeContents(TreeConverterService<Integer, String> service) {
        System.out.println("Keys: " + service.getAllKeys());
        System.out.println("Values: " + service.getAllValues());
    }
}