package com.nhlstenden.booktopia.btree;

import java.util.List;
import org.json.JSONObject;

public class BTreeNode<K extends Comparable<K>, V> {
    private int t; // Minimum of keys/degree
    private K[] keys;
    private BTreeNode<K, V>[] children;
    private JSONObject values;
    private int n; // Number of keys/degrees
    private boolean isLeaf;

    public BTreeNode(int t) {
        this.t = t;
        this.keys = (K[]) new Comparable[2 * t - 1]; // Cast to K[]
        this.values = new JSONObject();
        this.children = new BTreeNode[2 * t];
        this.n = 0;
        isLeaf = true;
    }

    protected K[] getKeys() {
        return keys;
    }

    protected void setKeys(K[] keys) {
        this.keys = keys;
    }

    protected BTreeNode<K, V>[] getChildren() {
        return children;
    }

    protected void setChildren(BTreeNode<K, V>[] children) {
        this.children = children;
    }

    public JSONObject getValues() {
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

    protected BTreeNode<K, V> search(K key) {
        int i = 0;
        while (i < n && key.compareTo(keys[i]) > 0) {
            i++;
        }

        // If key is found in this node
        if (i < n && keys[i].equals(key)) {
            // Check if this node has the value for this key
            if (values.has(key.toString())) {
                return this;
            }
        }

        // If this is a leaf node and key not found with value, return null
        if (isLeaf) {
            return null;
        }

        // Otherwise, search in the appropriate child
        BTreeNode<K, V> childResult = children[i].search(key);
        if (childResult != null) {
            return childResult;
        }
        
        // If not found in child, but key exists in this node, return this node
        // This helps with cases where the key exists but value might be missing
        if (i < n && keys[i].equals(key)) {
            return this;
        }
        
        return null;
    }

    protected void insertNonFull(K key, V value) {
        int i = n - 1;

        if (isLeaf) {
            // Check if the key already exists in this node
            for (int j = 0; j < n; j++) {
                if (keys[j].equals(key)) {
                    // Key exists, just update the value
                    if (value != null) {
                        if (value instanceof JSONObject) {
                            // If it's a JSONObject, store it directly
                            values.put(key.toString(), value);
                        } else {
                            // For other value types, convert as needed
                            values.put(key.toString(), value);
                        }
                    }
                    return; // Exit after updating the value
                }
            }
            
            // Key doesn't exist, proceed with insertion
            // Make space for the new key and value
            while (i >= 0 && keys[i].compareTo(key) > 0) {
                keys[i + 1] = keys[i];
                i--;
            }
            
            // Insert the key
            keys[i + 1] = key;
            
            // Ensure the value is stored properly
            if (value != null) {
                if (value instanceof JSONObject) {
                    // If it's a JSONObject, store it directly
                    values.put(key.toString(), value);
                } else {
                    // For other value types, convert as needed
                    values.put(key.toString(), value);
                }
            }
            
            n = n + 1;
        } else {
            // Find the child where the key should be inserted
            while (i >= 0 && keys[i].compareTo(key) > 0) {
                i--;
            }
            
            // Check if the key already exists in this node
            if (i >= 0 && keys[i].equals(key)) {
                // Key exists, just update the value
                if (value != null) {
                    if (value instanceof JSONObject) {
                        // If it's a JSONObject, store it directly
                        values.put(key.toString(), value);
                    } else {
                        // For other value types, convert as needed
                        values.put(key.toString(), value);
                    }
                }
                return; // Exit after updating the value
            }
            
            // If the child is full, split it
            if (children[i + 1].getN() == 2 * t - 1) {
                splitChild(i + 1, children[i + 1]);
                
                // After splitting, the middle key goes up and the new child is created
                // Decide which child to go to
                if (keys[i + 1].compareTo(key) < 0) {
                    i++;
                }
            }
            
            // Insert the key into the appropriate child
            children[i + 1].insertNonFull(key, value);
        }
    }

    protected void splitChild(int i, BTreeNode<K, V> y) {
        BTreeNode<K, V> z = new BTreeNode<>(y.t);
        z.setN(t - 1);
        z.setLeaf(y.isLeaf());

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

        if (!y.isLeaf()) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
                
                if (y.children[j + t] != null) {
                    JSONObject childValues = y.children[j + t].getValues();
                    for (String key : JSONObject.getNames(childValues)) {
                        z.values.put(key, childValues.get(key));
                    }
                }
                
                y.children[j + t] = null;
            }
        }

        y.setN(t - 1);

        if (n + 1 >= children.length) {
            BTreeNode<K, V>[] newChildren = new BTreeNode[children.length + 1];
            System.arraycopy(children, 0, newChildren, 0, children.length);
            children = newChildren;
        }

        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;

        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = y.keys[t - 1];
        
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

    protected void printInOrder() {
        for (int i = 0; i < n; i++) {
            if (!isLeaf) {
                children[i].printInOrder();
            }
            System.out.println(keys[i]);
        }
        if (!isLeaf) {
            children[n].printInOrder();
        }
    }

    private int findKey(K key) {
        int idx = 0;
        while (idx < n && keys[idx].compareTo(key) < 0) {
            idx++;
        }
        return idx;
    }

    private void removeFromLeaf(int idx) {
        for (int i = idx + 1; i < n; ++i) {
            keys[i - 1] = keys[i];
        }
        n--;
    }

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

    private K getPredecessor(int idx) {
        BTreeNode<K, V> cur = children[idx];
        while (!cur.isLeaf()) {
            cur = cur.getChildren()[cur.getN()];
        }
        return cur.getKeys()[cur.getN() - 1];
    }

    private K getSuccessor(int idx) {
        BTreeNode<K, V> cur = children[idx + 1];
        while (!cur.isLeaf()) {
            cur = cur.getChildren()[0];
        }
        return cur.getKeys()[0];
    }

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

    private void borrowFromPrev(int idx) {
        BTreeNode<K, V> child = children[idx];
        BTreeNode<K, V> sibling = children[idx - 1];

        for (int i = child.getN() - 1; i >= 0; --i) {
            child.getKeys()[i + 1] = child.getKeys()[i];
        }

        if (!child.isLeaf()) {
            for (int i = child.getN(); i >= 0; --i) {
                child.getChildren()[i + 1] = child.getChildren()[i];
            }
        }

        child.getKeys()[0] = keys[idx - 1];

        if (!child.isLeaf()) {
            child.getChildren()[0] = sibling.getChildren()[sibling.getN()];
        }

        keys[idx - 1] = sibling.getKeys()[sibling.getN() - 1];

        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

    private void borrowFromNext(int idx) {
        BTreeNode<K, V> child = children[idx];
        BTreeNode<K, V> sibling = children[idx + 1];

        child.getKeys()[child.getN()] = keys[idx];

        if (!child.isLeaf()) {
            child.getChildren()[child.getN() + 1] = sibling.getChildren()[0];
        }

        keys[idx] = sibling.getKeys()[0];

        for (int i = 1; i < sibling.getN(); ++i) {
            sibling.getKeys()[i - 1] = sibling.getKeys()[i];
        }

        if (!sibling.isLeaf()) {
            for (int i = 1; i <= sibling.getN(); ++i) {
                sibling.getChildren()[i - 1] = sibling.getChildren()[i];
            }
        }

        child.setN(child.getN() + 1);
        sibling.setN(sibling.getN() - 1);
    }

    private void merge(int idx) {
        BTreeNode<K, V> child = children[idx];
        BTreeNode<K, V> sibling = children[idx + 1];

        child.getKeys()[t - 1] = keys[idx];

        for (int j = 0; j < sibling.getN(); ++j) {
            child.getKeys()[j + t] = sibling.getKeys()[j];
        }

        if (!child.isLeaf()) {
            for (int j = 0; j <= sibling.getN(); ++j) {
                child.getChildren()[j + t] = sibling.getChildren()[j];
            }
        }

        for (int i = idx + 1; i < n; ++i) {
            keys[i - 1] = keys[i];
        }

        for (int i = idx + 2; i <= n; ++i) {
            children[i - 1] = children[i];
        }

        child.setN(child.getN() + sibling.getN() + 1);
        n--;
    }

    protected void inOrderTraversal(List<K> sortedKeys) {
        int i;
        for (i = 0; i < n; i++) {
            if (!isLeaf) {
                children[i].inOrderTraversal(sortedKeys);
            }
            sortedKeys.add(keys[i]);
        }

        if (!isLeaf) {
            children[i].inOrderTraversal(sortedKeys);
        }
    }

    protected void getAllValues(List<JSONObject> valuesList) {
        if (values.length() > 0) {
            valuesList.add(values);
        }
        
        if (!isLeaf) {
            for (int i = 0; i <= n; i++) {
                if (children[i] != null) {
                    children[i].getAllValues(valuesList);
                }
            }
        }
    }
    
    protected void inOrderTraversalWithValues(List<K> keysList, List<JSONObject> valuesList) {
        int i;
        for (i = 0; i < n; i++) {
            if (!isLeaf && children[i] != null) {
                children[i].inOrderTraversalWithValues(keysList, valuesList);
            }
            
            keysList.add(keys[i]);
            
            // Get the value directly from the values JSONObject
            if (values != null && values.has(keys[i].toString())) {
                Object valueObj = values.get(keys[i].toString());
                if (valueObj != null && valueObj != JSONObject.NULL) {
                    if (valueObj instanceof JSONObject) {
                        valuesList.add((JSONObject)valueObj);
                    } else {
                        // For non-JSONObject values, create a simple wrapper
                        JSONObject wrapper = new JSONObject();
                        wrapper.put("value", valueObj);
                        valuesList.add(wrapper);
                    }
                } else {
                    // Add a null value to maintain index correspondence with keys
                    System.out.println("Warning: Null value found for key: " + keys[i]);
                    valuesList.add(null);
                }
            } else {
                // Add a null value to maintain index correspondence with keys
                System.out.println("Warning: No value found for key: " + keys[i]);
                valuesList.add(null);
            }
        }
        
        if (!isLeaf && children[i] != null) {
            children[i].inOrderTraversalWithValues(keysList, valuesList);
        }
    }

    /**
     * Checks if this node contains the given key
     * 
     * @param key The key to check
     * @return true if the key exists in this node, false otherwise
     */
    protected boolean containsKey(K key) {
        for (int i = 0; i < n; i++) {
            if (keys[i].equals(key)) {
                return true;
            }
        }
        return false;
    }
}
