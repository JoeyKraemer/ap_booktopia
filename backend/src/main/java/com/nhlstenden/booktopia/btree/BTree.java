package com.nhlstenden.booktopia.btree;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class BTree<K extends Comparable<K>, V> {
    private BTreeNode<K, V> root;
    private int t;

    public BTree(int t) {
        this.t = t;
        root = null;
    }

    protected BTreeNode<K, V> getRoot() {
        return root;
    }

    public BTreeNode<K, V> search(K key) {
        return (root == null) ? null : root.search(key);
    }

    public List<K> getSortedKeys() {
        List<K> sortedKeys = new ArrayList<>();
        if (root != null) {
            root.inOrderTraversal(sortedKeys);
        }
        return sortedKeys;
    }

    public void insert(K key, V value) {
        if (root == null) {
            root = new BTreeNode<>(t);
            root.getKeys()[0] = key;
            root.setN(1);
        } else {
            if (root.getN() == 2 * t - 1) {
                BTreeNode<K, V> newRoot = new BTreeNode<>(t);
                newRoot.setLeaf(false);
                newRoot.setChildren(new BTreeNode[]{root});
                newRoot.splitChild(0, root);

                int i = 0;
                if (newRoot.getKeys()[0].compareTo(key) < 0) {
                    i = 1;
                }
                newRoot.getChildren()[i].insertNonFull(key, value);
                root = newRoot;
            } else {
                root.insertNonFull(key, value);
            }
        }
    }

    public void delete(K key) {
        if (root == null) {
            System.out.println("The tree is empty.");
            return;
        }

        root.delete(key);

        // If the root node has 0 keys, make its first child the new root if it has children
        if (root.getN() == 0) {
            if (root.isLeaf()) {
                root = null;
            } else {
                root = root.getChildren()[0];
            }
        }
    }

    public void printBTree() {
        if (root != null) {
            root.printInOrder();
        }
        System.out.println();
    }

    public static void testRun() {
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
        BTreeNode<String, String> nodeA = tree.search("A");
        if (nodeA != null) {
            System.out.println("Found A: " + nodeA.getValues().toString());
        } else {
            System.out.println("A not found");
        }

        // Delete key "A"
        System.out.println("\nDeleting key A");
        tree.delete("A");
        tree.printBTree();

        // Verify deletion of key "A"
        nodeA = tree.search("A");
        if (nodeA != null) {
            System.out.println("Found A: " + nodeA.getValues().toString());
        } else {
            System.out.println("A not found");
        }

        // Delete key "C"
        System.out.println("\nDeleting key C");
        tree.delete("C");
        tree.printBTree();

        // Verify deletion of key "C"
        BTreeNode<String, String> nodeC = tree.search("C");
        if (nodeC != null) {
            System.out.println("Found C: " + nodeC.getValues().toString());
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
        BTreeNode<String, String> nodeE = tree.search("E");
        if (nodeE != null) {
            System.out.println("Found E: " + nodeE.getValues().toString());
        } else {
            System.out.println("E not found");
        }
    }
}