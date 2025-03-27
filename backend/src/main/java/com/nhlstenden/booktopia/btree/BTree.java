package com.nhlstenden.booktopia.btree;

import org.json.JSONObject;

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
                if (newRoot.getKeys()[0].compareTo(key) < 0) {  // Corrected comparison
                    i = 1;
                }
                newRoot.getChildren()[i].insertNonFull(key, value);
                root = newRoot;
            } else {
                root.insertNonFull(key, value);
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
        tree.insert("B", new JSONObject());
        tree.search("B").setValues(new JSONObject().put("test", "test"));
        tree.insert("A", new JSONObject().put("2112", "2123"));
        tree.insert("C", new JSONObject());
        tree.insert("D", new JSONObject());
        tree.insert("E", new JSONObject());
        tree.insert("F", new JSONObject());
        tree.insert("G", new JSONObject());
        tree.printBTree();
    }

}
