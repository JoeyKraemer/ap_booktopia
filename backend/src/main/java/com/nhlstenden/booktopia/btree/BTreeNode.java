package com.nhlstenden.booktopia.btree;

import org.json.JSONObject;

public class BTreeNode {
    private int t;
    private String[] keys;
    private BTreeNode[] children;
    private JSONObject values;
    private int n; // number of keys/degrees
    private boolean isLeaf;

    public BTreeNode(int t) {
        this.t = t;
        this.keys = new String[2 * t - 1];
        this.values = new JSONObject();
        this.children = new BTreeNode[2 * t];
        this.n = 0;
        isLeaf = true;
    }

    protected String[] getKeys() {
        return keys;
    }

    protected void setKeys(String[] keys) {
        this.keys = keys;
    }

    protected BTreeNode[] getChildren() {
        return children;
    }

    protected void setChildren(BTreeNode[] children) {
        this.children = children;
    }

    protected JSONObject getValues() {
        return values;
    }

    protected void setValues(JSONObject values) {
        this.values = values;
    }

    protected int getN() {
        return n;
    }

    protected void setN(int n) {
        this.n = n;
    }

    protected boolean isLeaf() {
        return isLeaf;
    }

    protected void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public BTreeNode search(String key) {
        int i;
        for (i = 0; i < n; i++) {
            if (keys[i].equals(key)) {
                return this;
            }

            if (isLeaf()) {
                return null;
            }
        }

        return children[i].search(key);
    }

    public void insertNonFull(String key, JSONObject value) {
        int i = n - 1;
        if (isLeaf()) {
            while (i >= 0 && keys[i].compareTo(key) > 0) {
                keys[i + 1] = keys[i];
                i--;
            }
            keys[i + 1] = key;
            values.put(key, value);
            n = n + 1;
        } else {
            while (i >= 0 && keys[i].compareTo(key) > 0) {
                i--;
            }
            if (children[i + 1].n == 2 * t - 1) {
                splitChild(i + 1, children[i + 1]);
                if (keys[i + 1].compareTo(key) < 0) {
                    i++;
                }
            }
            children[i + 1].insertNonFull(key, value);
        }
    }

    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.t);
        z.setN(t - 1);
        z.setLeaf(y.isLeaf());

        // Copy the second half of y's keys to z
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
            y.keys[j + t] = null;  // Clear the copied keys from y
        }

        // Copy the second half of y's children to z, if y is not a leaf
        if (!y.isLeaf()) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
                y.children[j + t] = null;  // Clear the copied children from y
            }
        }

        y.setN(t - 1);

        // Shift children of the current node to make space for z
        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;

        // Shift keys of the current node to make space for the median key
        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = y.keys[t - 1];
        y.keys[t - 1] = null;  // Clear the median key from y

        n++;
    }

    public void printInOrder() {
        for (int i = 0; i < n; i++) {
            if (!isLeaf()) {
                children[i].printInOrder();
            }
            System.out.println(keys[i]);
        }
        if (!isLeaf()) {
            children[n].printInOrder();
        }
    }
}
