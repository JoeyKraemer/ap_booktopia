package com.nhlstenden.booktopia.btree;

import java.util.List;
import org.json.JSONObject;

public class BTreeNode<K extends Comparable<K>, V> {
    private int t; // Minimum degree
    private K[] keys;
    private BTreeNode<K, V>[] children;
    private JSONObject values;
    private int n; // Number of keys
    private boolean isLeaf;

    public BTreeNode(int t) {
        this.t = t;
        this.keys = (K[]) new Comparable[2 * t - 1];
        this.values = new JSONObject();
        this.children = new BTreeNode[2 * t];
        this.n = 0;
        isLeaf = true;
    }

    // Getters and setters
    protected K[] getKeys() { return keys; }
    protected void setKeys(K[] keys) { this.keys = keys; }
    protected BTreeNode<K, V>[] getChildren() { return children; }
    protected void setChildren(BTreeNode<K, V>[] children) { this.children = children; }
    public JSONObject getValues() { return values; }
    protected void setValues(JSONObject values) { this.values = values; }
    protected int getN() { return n; }
    protected void setN(int n) { this.n = n; }
    protected boolean isLeaf() { return isLeaf; }
    protected void setLeaf(boolean leaf) { isLeaf = leaf; }

    /**
     * Searches for a node containing the specified key
     */
    protected BTreeNode<K, V> search(K key) {
        int i = 0;
        while (i < n && key.compareTo(keys[i]) > 0) i++;

        // If key is found in this node
        if (i < n && keys[i].equals(key)) {
            if (values.has(key.toString())) return this;
        }

        // If leaf node and key not found with value, return null
        if (isLeaf) return null;

        // Search in the appropriate child
        BTreeNode<K, V> childResult = children[i].search(key);
        
        // If not found in child, but key exists in this node, return this node
        if (childResult == null && i < n && keys[i].equals(key)) return this;
        
        return childResult;
    }

    /**
     * Inserts a key-value pair into a non-full node
     */
    protected void insertNonFull(K key, V value) {
        int i = n - 1;

        if (isLeaf) {
            // Check if key already exists
            for (int j = 0; j < n; j++) {
                if (keys[j].equals(key)) {
                    updateValue(key, value);
                    return;
                }
            }
            
            // Make space for new key
            while (i >= 0 && keys[i].compareTo(key) > 0) {
                keys[i + 1] = keys[i];
                i--;
            }
            
            // Insert key and value
            keys[i + 1] = key;
            updateValue(key, value);
            n++;
        } else {
            // Find child where key should be inserted
            while (i >= 0 && keys[i].compareTo(key) > 0) i--;
            
            // Check if key already exists
            if (i >= 0 && keys[i].equals(key)) {
                updateValue(key, value);
                return;
            }
            
            // Split child if full
            if (children[i + 1].getN() == 2 * t - 1) {
                splitChild(i + 1, children[i + 1]);
                if (keys[i + 1].compareTo(key) < 0) i++;
            }
            
            children[i + 1].insertNonFull(key, value);
        }
    }

    /**
     * Updates a value for a key in the values JSONObject
     */
    private void updateValue(K key, V value) {
        if (value != null) {
            values.put(key.toString(), value);
        }
    }

    /**
     * Splits a child node
     */
    protected void splitChild(int i, BTreeNode<K, V> y) {
        BTreeNode<K, V> z = new BTreeNode<>(y.t);
        z.setN(t - 1);
        z.setLeaf(y.isLeaf());

        // Copy keys and values to new node
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
            
            if (y.keys[j + t] != null) {
                String keyStr = y.keys[j + t].toString();
                if (y.values.has(keyStr)) {
                    z.values.put(keyStr, y.values.get(keyStr));
                    y.values.remove(keyStr);
                }
            }
            
            y.keys[j + t] = null;
        }

        // Copy children if not leaf
        if (!y.isLeaf()) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
                
                if (y.children[j + t] != null) {
                    JSONObject childValues = y.children[j + t].getValues();
                    if (childValues != null && JSONObject.getNames(childValues) != null) {
                        for (String key : JSONObject.getNames(childValues)) {
                            z.values.put(key, childValues.get(key));
                        }
                    }
                }
                
                y.children[j + t] = null;
            }
        }

        y.setN(t - 1);

        // Ensure children array has enough space
        if (n + 1 >= children.length) {
            BTreeNode<K, V>[] newChildren = new BTreeNode[children.length + 1];
            System.arraycopy(children, 0, newChildren, 0, children.length);
            children = newChildren;
        }

        // Move children to make room for new child
        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;

        // Move keys to make room for median key
        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = y.keys[t - 1];
        
        // Move value for median key
        if (y.keys[t - 1] != null) {
            String keyStr = y.keys[t - 1].toString();
            if (y.values.has(keyStr)) {
                values.put(keyStr, y.values.get(keyStr));
                y.values.remove(keyStr);
            }
        }
        
        y.keys[t - 1] = null;
        n++;
    }

    /**
     * Deletes a key from the tree
     */
    protected void delete(K key) {
        int idx = findKey(key);

        if (idx < n && keys[idx].equals(key)) {
            if (isLeaf) {
                removeFromLeaf(idx);
            } else {
                removeFromNonLeaf(idx);
            }
        } else {
            if (isLeaf) {
                System.out.println("The key " + key + " is not present in the tree.");
                return;
            }

            boolean flag = (idx == n);

            if (children[idx].getN() < t) {
                fill(idx);
            }

            if (flag && idx > n) {
                children[idx - 1].delete(key);
            } else {
                children[idx].delete(key);
            }
        }
    }

    /**
     * Finds the index of a key in the node's keys array
     */
    private int findKey(K key) {
        int idx = 0;
        while (idx < n && keys[idx].compareTo(key) < 0) idx++;
        return idx;
    }

    /**
     * Removes a key from a leaf node
     */
    private void removeFromLeaf(int idx) {
        for (int i = idx + 1; i < n; ++i) {
            keys[i - 1] = keys[i];
        }
        n--;
    }

    /**
     * Removes a key from a non-leaf node
     */
    private void removeFromNonLeaf(int idx) {
        K key = keys[idx];

        if (children[idx].getN() >= t) {
            K pred = getPredecessor(idx);
            keys[idx] = pred;
            children[idx].delete(pred);
        } else if (children[idx + 1].getN() >= t) {
            K succ = getSuccessor(idx);
            keys[idx] = succ;
            children[idx + 1].delete(succ);
        } else {
            merge(idx);
            children[idx].delete(key);
        }
    }

    /**
     * Gets the predecessor key
     */
    private K getPredecessor(int idx) {
        BTreeNode<K, V> cur = children[idx];
        while (!cur.isLeaf()) {
            cur = cur.getChildren()[cur.getN()];
        }
        return cur.getKeys()[cur.getN() - 1];
    }

    /**
     * Gets the successor key
     */
    private K getSuccessor(int idx) {
        BTreeNode<K, V> cur = children[idx + 1];
        while (!cur.isLeaf()) {
            cur = cur.getChildren()[0];
        }
        return cur.getKeys()[0];
    }

    /**
     * Fills a child node that has fewer than t-1 keys
     */
    private void fill(int idx) {
        if (idx != 0 && children[idx - 1].getN() >= t) {
            borrowFromPrev(idx);
        } else if (idx != n && children[idx + 1].getN() >= t) {
            borrowFromNext(idx);
        } else {
            if (idx != n) {
                merge(idx);
            } else {
                merge(idx - 1);
            }
        }
    }

    /**
     * Borrows a key from the previous child
     */
    private void borrowFromPrev(int idx) {
        BTreeNode<K, V> child = children[idx];
        BTreeNode<K, V> sibling = children[idx - 1];

        // Move all keys in child one step ahead
        for (int i = child.getN() - 1; i >= 0; --i) {
            child.getKeys()[i + 1] = child.getKeys()[i];
        }

        // If not leaf, move all child pointers one step ahead
        if (!child.isLeaf()) {
            for (int i = child.getN(); i >= 0; --i) {
                child.getChildren()[i + 1] = child.getChildren()[i];
            }
        }

        // Set child's first key to parent's key at idx-1
        child.getKeys()[0] = keys[idx - 1];

        // Move sibling's last child to child's first
        if (!child.isLeaf()) {
            child.getChildren()[0] = sibling.getChildren()[sibling.getN()];
        }

        // Move sibling's last key to parent
        keys[idx - 1] = sibling.getKeys()[sibling.getN() - 1];

        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

    /**
     * Borrows a key from the next child
     */
    private void borrowFromNext(int idx) {
        BTreeNode<K, V> child = children[idx];
        BTreeNode<K, V> sibling = children[idx + 1];

        // Set last key of child to parent's key at idx
        child.getKeys()[child.getN()] = keys[idx];

        // Move sibling's first child to child's last
        if (!child.isLeaf()) {
            child.getChildren()[child.getN() + 1] = sibling.getChildren()[0];
        }

        // Set parent's key to sibling's first key
        keys[idx] = sibling.getKeys()[0];

        // Move all keys in sibling one step behind
        for (int i = 1; i < sibling.getN(); ++i) {
            sibling.getKeys()[i - 1] = sibling.getKeys()[i];
        }

        // Move child pointers one step behind
        if (!sibling.isLeaf()) {
            for (int i = 1; i <= sibling.getN(); ++i) {
                sibling.getChildren()[i - 1] = sibling.getChildren()[i];
            }
        }

        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

    /**
     * Merges idx-th child with (idx+1)th child
     */
    private void merge(int idx) {
        BTreeNode<K, V> child = children[idx];
        BTreeNode<K, V> sibling = children[idx + 1];

        // Add parent's key to child
        child.getKeys()[t - 1] = keys[idx];

        // Copy sibling's keys to child
        for (int j = 0; j < sibling.getN(); ++j) {
            child.getKeys()[j + t] = sibling.getKeys()[j];
        }

        // Copy sibling's children to child
        if (!child.isLeaf()) {
            for (int j = 0; j <= sibling.getN(); ++j) {
                child.getChildren()[j + t] = sibling.getChildren()[j];
            }
        }

        // Move keys in parent one step left
        for (int i = idx + 1; i < n; ++i) {
            keys[i - 1] = keys[i];
        }

        // Move child pointers one step left
        for (int i = idx + 2; i <= n; ++i) {
            children[i - 1] = children[i];
        }

        child.setN(child.getN() + sibling.getN() + 1);
        n--;
    }

    /**
     * Performs in-order traversal to collect sorted keys
     */
    protected void inOrderTraversal(List<K> sortedKeys) {
        for (int i = 0; i < n; i++) {
            if (!isLeaf) children[i].inOrderTraversal(sortedKeys);
            sortedKeys.add(keys[i]);
        }

        if (!isLeaf) children[n].inOrderTraversal(sortedKeys);
    }

    /**
     * Collects all values from the tree
     */
    protected void getAllValues(List<JSONObject> valuesList) {
        if (values.length() > 0) valuesList.add(values);
        
        if (!isLeaf) {
            for (int i = 0; i <= n; i++) {
                if (children[i] != null) children[i].getAllValues(valuesList);
            }
        }
    }
    
    /**
     * Performs in-order traversal to collect both keys and values
     */
    protected void inOrderTraversalWithValues(List<K> keysList, List<JSONObject> valuesList) {
        for (int i = 0; i < n; i++) {
            if (!isLeaf && children[i] != null) {
                children[i].inOrderTraversalWithValues(keysList, valuesList);
            }
            
            keysList.add(keys[i]);
            addValueToList(keys[i], valuesList);
        }
        
        if (!isLeaf && children[n] != null) {
            children[n].inOrderTraversalWithValues(keysList, valuesList);
        }
    }
    
    /**
     * Helper method to add a value to the values list
     */
    private void addValueToList(K key, List<JSONObject> valuesList) {
        if (values != null && values.has(key.toString())) {
            Object valueObj = values.get(key.toString());
            if (valueObj != null && valueObj != JSONObject.NULL) {
                if (valueObj instanceof JSONObject) {
                    valuesList.add((JSONObject)valueObj);
                } else {
                    JSONObject wrapper = new JSONObject();
                    wrapper.put("value", valueObj);
                    valuesList.add(wrapper);
                }
            } else {
                valuesList.add(null);
            }
        } else {
            valuesList.add(null);
        }
    }

    /**
     * Checks if this node contains the given key
     */
    protected boolean containsKey(K key) {
        for (int i = 0; i < n; i++) {
            if (keys[i].equals(key)) return true;
        }
        return false;
    }
}
