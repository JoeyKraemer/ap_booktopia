package com.nhlstenden.booktopia.btree;

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

    /**
     * Search for a key in the subtree rooted at this node
     * Time Complexity: O(t * log_t(n)) where t is the minimum degree and n is the number of keys
     * 
     * The search algorithm traverses down the tree by comparing the search key with keys in each node:
     * 1. If the key is found in the current node, return this node
     * 2. If the key is not found and we're at a leaf, return null (key not in tree)
     * 3. Otherwise, recursively search the appropriate child subtree
     * 
     * @param key The key to search for
     * @return The node containing the key, or null if not found
     */
    protected BTreeNode<K, V> search(K key) {
        int i = 0;
        // Find the first key greater than or equal to the search key
        while (i < n && key.compareTo(keys[i]) > 0) {
            i++;
        }

        // If key found in this node, return this node
        if (i < n && keys[i].equals(key)) {
            return this;
        }

        // If this is a leaf node and key not found, return null
        if (isLeaf) {
            return null;
        }

        // Recursively search the appropriate child
        return children[i].search(key);
    }

    /**
     * Insert a key-value pair into a non-full node
     * Time Complexity: O(t * log_t(n)) where t is the minimum degree and n is the number of keys
     * 
     * This method assumes that the node is not full (has fewer than 2t-1 keys).
     * The insertion process handles two cases:
     * 1. If the node is a leaf, insert the key directly
     * 2. If the node is internal, find the child that should contain the key and insert there,
     *    splitting the child if necessary
     * 
     * @param key The key to insert
     * @param value The value associated with the key
     */
    protected void insertNonFull(K key, V value) {
        int i = n - 1;
        if (isLeaf) {
            // Case 1: Node is a leaf - insert key directly
            // Shift all greater keys to the right
            while (i >= 0 && keys[i].compareTo(key) > 0) {
                keys[i + 1] = keys[i];
                i--;
            }
            // Insert the new key and value
            keys[i + 1] = key;
            values.put(key.toString(), value);
            n = n + 1;
        } else {
            // Case 2: Node is internal - find the child to insert into
            // Find the child that should contain the key
            while (i >= 0 && keys[i].compareTo(key) > 0) {
                i--;
            }
            
            // Check if the child is full
            if (children[i + 1].getN() == 2 * t - 1) {
                // If child is full, split it first
                splitChild(i + 1, children[i + 1]);
                // After split, determine which of the two children to insert into
                if (keys[i + 1].compareTo(key) < 0) {
                    i++;
                }
            }
            // Recursively insert into the appropriate child
            children[i + 1].insertNonFull(key, value);
        }
    }

    /**
     * Split a full child node during insertion
     * Time Complexity: O(t) where t is the minimum degree
     * 
     * This operation is a key part of maintaining the B-tree properties during insertion.
     * When a node becomes full (has 2t-1 keys), it is split into two nodes:
     * 1. The left node keeps the t-1 smallest keys
     * 2. The median key moves up to the parent
     * 3. The right node gets the t-1 largest keys
     * 
     * @param i The index of the child in the children array
     * @param y The full child node to split
     */
    protected void splitChild(int i, BTreeNode<K, V> y) {
        // Create a new node to hold the larger half of y's keys
        BTreeNode<K, V> z = new BTreeNode<>(y.t);
        z.setN(t - 1);
        z.setLeaf(y.isLeaf());

        // Copy the larger half of keys from y to z
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
            // Also copy the associated values
            if (y.keys[j + t] != null) {
                String keyStr = y.keys[j + t].toString();
                if (y.values.has(keyStr)) {
                    z.values.put(keyStr, (V) y.values.get(keyStr));
                }
            }
            y.keys[j + t] = null;
        }

        // If y is not a leaf, also copy the corresponding children
        if (!y.isLeaf()) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
                y.children[j + t] = null;
            }
        }

        // Update y's key count
        y.setN(t - 1);

        // Ensure there's room in the children array
        if (n + 1 >= children.length) {
            BTreeNode<K, V>[] newChildren = new BTreeNode[children.length + 1];
            System.arraycopy(children, 0, newChildren, 0, children.length);
            children = newChildren;
        }

        // Shift children to make room for the new child
        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        // Insert z as the new child
        children[i + 1] = z;

        // Shift keys to make room for the median key from y
        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        // Move the median key from y up to this node
        keys[i] = y.keys[t - 1];
        // Also copy the value for the median key
        if (y.keys[t - 1] != null) {
            String keyStr = y.keys[t - 1].toString();
            if (y.values.has(keyStr)) {
                values.put(keyStr, (V) y.values.get(keyStr));
            }
        }
        y.keys[t - 1] = null;

        // Increment key count
        n++;
    }

    /**
     * Delete a key from the subtree rooted at this node
     * Time Complexity: O(t * log_t(n)) where t is the minimum degree and n is the number of keys
     * 
     * The deletion algorithm handles several cases:
     * 1. If the key is in this node and it's a leaf, simply remove it
     * 2. If the key is in this node and it's not a leaf, replace it with predecessor/successor
     * 3. If the key is not in this node, recursively delete from the appropriate child,
     *    ensuring that the child has at least t keys before recursion
     * 
     * @param key The key to delete
     */
    protected void delete(K key) {
        int idx = findKey(key);

        // Case 1: Key is present in this node
        if (idx < n && keys[idx].equals(key)) {
            if (isLeaf) {
                // Case 1a: Key is in a leaf node - simply remove it
                removeFromLeaf(idx);
            } else {
                // Case 1b: Key is in an internal node - more complex removal
                removeFromNonLeaf(idx);
            }
        } else {
            // Case 2: Key is not present in this node
            if (isLeaf) {
                // Key not found in the tree
                System.out.println("The key " + key + " is not present in the tree.");
                return;
            }

            // Flag to indicate if the key is in the last child
            boolean flag = (idx == n);

            // Ensure the child has at least t keys before recursion
            if (children[idx].getN() < t) {
                fill(idx);
            }

            // Recursively delete from the appropriate child
            if (flag && idx > n) {
                children[idx - 1].delete(key);
            } else {
                children[idx].delete(key);
            }
        }
    }

    /**
     * Remove a key from a leaf node
     * Time Complexity: O(t) where t is the minimum degree
     * 
     * @param idx The index of the key to remove
     */
    private void removeFromLeaf(int idx) {
        // Shift all keys after idx one position left
        for (int i = idx + 1; i < n; ++i) {
            keys[i - 1] = keys[i];
        }
        n--;
    }

    /**
     * Remove a key from a non-leaf node
     * Time Complexity: O(t * log_t(n)) where t is the minimum degree and n is the number of keys
     * 
     * This method handles removal from an internal node by:
     * 1. Replacing the key with its predecessor if the left child has at least t keys
     * 2. Replacing the key with its successor if the right child has at least t keys
     * 3. Merging the left and right children if both have t-1 keys
     * 
     * @param idx The index of the key to remove
     */
    private void removeFromNonLeaf(int idx) {
        K key = keys[idx];

        // Case 1: If the left child has at least t keys
        if (children[idx].getN() >= t) {
            // Replace key with its predecessor
            K pred = getPredecessor(idx);
            keys[idx] = pred;
            children[idx].delete(pred);
        } 
        // Case 2: If the right child has at least t keys
        else if (children[idx + 1].getN() >= t) {
            // Replace key with its successor
            K succ = getSuccessor(idx);
            keys[idx] = succ;
            children[idx + 1].delete(succ);
        } 
        // Case 3: Both left and right children have t-1 keys
        else {
            // Merge the children and then delete the key
            merge(idx);
            children[idx].delete(key);
        }
    }

    /**
     * Merge a node with its sibling
     * Time Complexity: O(t) where t is the minimum degree
     * 
     * This operation merges node children[idx+1] into children[idx],
     * moving the key at position idx from this node down to the merged node.
     * 
     * @param idx The index of the key and left child for merging
     */
    private void merge(int idx) {
        BTreeNode<K, V> child = children[idx];
        BTreeNode<K, V> sibling = children[idx + 1];

        // Move key from this node down to the child
        child.keys[child.getN()] = keys[idx];

        // Copy keys from sibling to child
        for (int i = 0; i < sibling.getN(); ++i) {
            child.keys[i + t] = sibling.keys[i];
        }

        // Copy children from sibling to child if not leaf nodes
        if (!child.isLeaf()) {
            for (int i = 0; i <= sibling.getN(); ++i) {
                child.children[i + t] = sibling.children[i];
            }
        }

        // Shift keys in this node to fill the gap
        for (int i = idx + 1; i < n; ++i) {
            keys[i - 1] = keys[i];
        }

        // Shift children in this node to fill the gap
        for (int i = idx + 2; i <= n; ++i) {
            children[i - 1] = children[i];
        }

        // Update key count in child and this node
        child.setN(child.getN() + sibling.getN() + 1);
        n--;
    }

    private int findKey(K key) {
        int idx = 0;
        while (idx < n && keys[idx].compareTo(key) < 0) {
            idx++;
        }
        return idx;
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
}
