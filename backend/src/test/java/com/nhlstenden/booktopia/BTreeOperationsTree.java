package com.nhlstenden.booktopia;

import com.nhlstenden.booktopia.btree.BTree;
import com.nhlstenden.booktopia.btree.BTreeNode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class BTreeOperationsTree {

    @Test
    public void testBTreeOperations() {
        BTree<String, String> tree = new BTree<>(3);

        // Insert keys with values
        tree.insert("B", "test_B");
        tree.insert("F", "test_A");
        tree.insert("E", "test_C");
        tree.insert("D", "test_D");
        tree.insert("C", "test_E");
        tree.insert("A", "test_F");
        tree.insert("G", "test_G");

        System.out.println("Initial BTree:");
        tree.printBTree();

        // Search and print results for key "A"
        String valueA = tree.search("A");
        if (valueA != null) {
            System.out.println("Found A: Value = " + valueA);
        } else {
            System.out.println("A not found");
        }

        // Delete key "A"
        System.out.println("\nDeleting key A");
        tree.delete("A");
        tree.printBTree();

        // Verify deletion of key "A"
        valueA = tree.search("A");
        if (valueA != null) {
            System.out.println("Found A: Value = " + valueA);
        } else {
            System.out.println("A not found");
        }

        // Delete key "C"
        System.out.println("\nDeleting key C");
        tree.delete("C");
        tree.printBTree();

        // Verify deletion of key "C"
        String valueC = tree.search("C");
        if (valueC != null) {
            System.out.println("Found C: Value = " + valueC);
        } else {
            System.out.println("C not found");
        }

        // Delete key "D"
        System.out.println("\nDeleting key D");
        tree.delete("D");
        tree.printBTree();

        // Sorting example
        System.out.println("\nSorted keys in the BTree:");
        List<String> sortedKeys = tree.getSortedKeys();
        for (String key : sortedKeys) {
            System.out.println(key);
        }

        // Search example for key "E"
        System.out.println("\nSearching for key E:");
        String valueE = tree.search("E");
        if (valueE != null) {
            System.out.println("Found E: Value = " + valueE);
        } else {
            System.out.println("E not found");
        }
    }
}