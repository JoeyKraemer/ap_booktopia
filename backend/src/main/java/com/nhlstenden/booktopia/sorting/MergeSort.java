package com.nhlstenden.booktopia.sorting;

import com.nhlstenden.booktopia.services.TreeConverterService;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MergeSort<K extends Comparable<K>, V> {

    private final TreeConverterService<K, V> treeConverterService;

    public MergeSort(TreeConverterService<K, V> treeConverterService) {
        this.treeConverterService = treeConverterService;
    }

    // Generic merge sort algorithm for any type T.
    public <T> List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return list;
        }
        int mid = list.size() / 2;
        List<T> left = mergeSort(new ArrayList<>(list.subList(0, mid)), comparator);
        List<T> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), comparator);
        return merge(left, right, comparator);
    }

    private <T> List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }

    /**
     * Sorts the keys from the active tree using merge sort.
     * @return a List of keys sorted in ascending order.
     */
    public List<K> sortKeys() {
        long startTime = System.currentTimeMillis();
        List<K> keys = treeConverterService.getAllKeys();
        List<K> sortedKeys = mergeSort(keys, Comparator.naturalOrder());
        long endTime = System.currentTimeMillis();
        System.out.println("Merge sort (keys) completed in " + (endTime - startTime) + " ms");
        return sortedKeys;
    }

    /**
     * Sorts the keys from the active tree and returns the corresponding values in order.
     * @return a Map of sorted keys to their corresponding values.
     */
    public Map<K, V> sortKeysWithValues() {
        long startTime = System.currentTimeMillis();
        List<K> keys = treeConverterService.getAllKeys();
        List<V> values = treeConverterService.getAllValues();
        List<KeyValuePair<K, V>> pairs = new ArrayList<>();

        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = i < values.size() ? values.get(i) : null;
            if (value == null) {
                value = treeConverterService.search(key);
            }
            if (value != null) {
                pairs.add(new KeyValuePair<>(key, value));
            }
        }

        // Use merge sort for the list of key-value pairs using key's natural order.
        List<KeyValuePair<K, V>> sortedPairs = mergeSort(pairs, (p1, p2) -> p1.key.compareTo(p2.key));

        // Create a LinkedHashMap to preserve the sorted order.
        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (KeyValuePair<K, V> pair : sortedPairs) {
            sortedMap.put(pair.key, pair.value);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Merge sort (keys with values) completed in " + (endTime - startTime) + " ms");
        return sortedMap;
    }

    /**
     * Sorts the data extracted from the tree by a specific property.
     *
     * It extracts keys and values from the TreeConverterService and builds data items
     * (each as a Map combining the key and the JSON properties). Then, it applies merge sort.
     *
     * @param property the property to sort by (e.g. "author", "rating"). Use "key" to sort by the key.
     * @param ascending true for ascending order; false for descending.
     * @return a List of data items (maps) sorted by the specified property.
     */
    public List<Map<String, Object>> sortByProperty(String property, boolean ascending) {
        long startTime = System.currentTimeMillis();
        List<K> keys = treeConverterService.getAllKeys();
        List<V> values = treeConverterService.getAllValues();
        List<Map<String, Object>> dataItems = new ArrayList<>();

        // Build data items by combining the key and the properties from the JSON record.
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.get(i);
            V value = i < values.size() ? values.get(i) : null;
            if (value == null) {
                value = treeConverterService.search(key);
            }
            if (value != null && value instanceof JSONObject) {
                JSONObject jsonObj = (JSONObject) value;
                Map<String, Object> item = new HashMap<>();
                item.put("key", key);
                for (String propName : jsonObj.keySet()) {
                    item.put(propName, jsonObj.get(propName));
                }
                dataItems.add(item);
            }
        }

        // Use merge sort to sort data items by the given property.
        List<Map<String, Object>> sortedItems = mergeSort(dataItems, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> item1, Map<String, Object> item2) {
                Object val1 = property.equals("key") ? item1.get("key") : item1.get(property);
                Object val2 = property.equals("key") ? item2.get("key") : item2.get(property);
                if (val1 == null && val2 == null) {
                    return 0;
                } else if (val1 == null) {
                    return ascending ? -1 : 1;
                } else if (val2 == null) {
                    return ascending ? 1 : -1;
                }
                int result = val1.toString().compareTo(val2.toString());
                return ascending ? result : -result;
            }
        });

        long endTime = System.currentTimeMillis();
        System.out.println("Merge sort by property completed in " + (endTime - startTime) + " ms");
        return sortedItems;
    }

    // Inner class to hold key-value pairs for sorting purposes.
    private static class KeyValuePair<K extends Comparable<K>, V> {
        K key;
        V value;

        public KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
