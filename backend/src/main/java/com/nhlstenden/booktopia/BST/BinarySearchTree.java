import java.util.Comparator;
import org.json.JSONObject;

public class BinarySearchTree<T> {
    private Node root;
    private Comparator<T> comparator;

    // Constructor accepts a Comparator to compare generic objects
    public BinarySearchTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    // Node class for generic object
    private class Node {
        T data;
        Node left, right;

        Node(T data) {
            this.data = data;
            left = right = null;
        }
    }

    // Insert method for generic objects
    public void insert(T data) {
        root = insertRec(root, data);
    }

    private Node insertRec(Node root, T data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }

        if (comparator.compare(data, root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (comparator.compare(data, root.data) > 0) {
            root.right = insertRec(root.right, data);
        }

        return root;
    }

    // Search method for generic objects
    public boolean search(T data) {
        return searchRec(root, data) != null;
    }

    private Node searchRec(Node root, T data) {
        if (root == null || comparator.compare(data, root.data) == 0) {
            return root;
        }

        if (comparator.compare(data, root.data) < 0) {
            return searchRec(root.left, data);
        }

        return searchRec(root.right, data);
    }

    // Delete method for generic objects
    public void delete(T data) {
        root = deleteRec(root, data);
    }

    private Node deleteRec(Node root, T data) {
        if (root == null) {
            return root;
        }

        if (comparator.compare(data, root.data) < 0) {
            root.left = deleteRec(root.left, data);
        } else if (comparator.compare(data, root.data) > 0) {
            root.right = deleteRec(root.right, data);
        } else {
            // Node with only one child or no child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            root.data = minValue(root.right);

            // Delete the inorder successor
            root.right = deleteRec(root.right, root.data);
        }

        return root;
    }

    // Helper method to find the minimum value node in the tree
    private T minValue(Node root) {
        T minValue = root.data;
        while (root.left != null) {
            minValue = root.left.data;
            root = root.left;
        }
        return minValue;
    }

    // Inorder Traversal to print the tree
    public void inorder() {
        inorderRec(root);
    }

    private void inorderRec(Node root) {
        if (root != null) {
            inorderRec(root.left);
            System.out.println(root.data);
            inorderRec(root.right);
        }
    }
}


// Title, Creator, Genre, Language, ReleaseDate, ProductionCompany, Length