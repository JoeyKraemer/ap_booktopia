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
}
