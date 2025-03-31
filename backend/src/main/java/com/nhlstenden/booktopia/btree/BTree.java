package com.nhlstenden.booktopia.btree;

import org.json.JSONObject;

import static java.sql.DriverManager.println;

public class BTree {
    private BTreeNode root;
    private int t;

    public BTree (int t) {
        this.t = t;
        root = null;
    }

    protected BTreeNode getRoot() {
        return root;
    }

    public BTreeNode search(String key) {
        return (root == null) ? null : root.search(key);
    }

    public void insert(String key, JSONObject value) {
        if (root == null) {
            root = new BTreeNode(t);
            root.getKeys()[0] = key;
            root.setN(1);
        } else {
            if (root.getN() == 2 * t - 1) {
                BTreeNode newRoot = new BTreeNode(t);
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

    public void delete(String key) {
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
        BTree tree = new BTree(3);

        // Insert keys with values
        tree.insert("B", new JSONObject().put("value", "test_B"));
        tree.insert("A", new JSONObject().put("value", "test_A"));
        tree.insert("C", new JSONObject().put("value", "test_C"));
        tree.insert("D", new JSONObject().put("value", "test_D"));
        tree.insert("E", new JSONObject().put("value", "test_E"));
        tree.insert("F", new JSONObject().put("value", "test_F"));
        tree.insert("G", new JSONObject().put("value", "test_G"));

        System.out.println("Initial BTree:");
        tree.printBTree();

        // Search and print results for key "A"
        BTreeNode nodeA = tree.search("A");
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
        BTreeNode nodeC = tree.search("C");
        if (nodeC != null) {
            System.out.println("Found C: " + nodeC.getValues().toString());
        } else {
            System.out.println("C not found");
        }

        // Delete key "D"
        System.out.println("\nDeleting key D");
        tree.delete("D");
        tree.printBTree();

        // Verify deletion of key "D"
        BTreeNode nodeD = tree.search("D");
        if (nodeD != null) {
            System.out.println("Found D: " + nodeD.getValues().toString());
        } else {
            System.out.println("D not found");
        }
    }

}
