package datastructures.trees;

/**
 * BINARY TREE - Three Implementations / Variants
 *
 * A binary tree is a hierarchical data structure where each node has
 * at most two children (left and right).
 *
 * Key Properties:
 *   - Height of a tree: longest path from root to leaf
 *   - Full binary tree:  every node has 0 or 2 children
 *   - Complete binary tree: all levels filled except possibly last (filled left to right)
 *   - Perfect binary tree: all leaves at same level, all internal nodes have 2 children
 *
 * Traversal Time Complexities: O(n) for all traversals
 * Space: O(h) where h = height (O(log n) for balanced, O(n) worst case)
 */
public class BinaryTree {

    // =========================================================================
    // IMPLEMENTATION 1: Basic Binary Tree with all 4 traversal methods
    // =========================================================================
    static class BasicBinaryTree<T> {
        Node<T> root;

        static class Node<T> {
            T data;
            Node<T> left;
            Node<T> right;

            Node(T data) { this.data = data; }
        }

        // ---- DEPTH-FIRST TRAVERSALS ----

        // In-Order: Left -> Root -> Right  (gives sorted order for BST)
        public void inOrder(Node<T> node) {
            if (node == null) return;
            inOrder(node.left);
            System.out.print(node.data + " ");
            inOrder(node.right);
        }

        // Pre-Order: Root -> Left -> Right  (useful for tree copying / serialization)
        public void preOrder(Node<T> node) {
            if (node == null) return;
            System.out.print(node.data + " ");
            preOrder(node.left);
            preOrder(node.right);
        }

        // Post-Order: Left -> Right -> Root  (useful for deletion / computing size)
        public void postOrder(Node<T> node) {
            if (node == null) return;
            postOrder(node.left);
            postOrder(node.right);
            System.out.print(node.data + " ");
        }

        // ---- BREADTH-FIRST (LEVEL-ORDER) TRAVERSAL ----
        public void levelOrder() {
            if (root == null) return;
            java.util.Queue<Node<T>> queue = new java.util.LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                for (int i = 0; i < levelSize; i++) {
                    Node<T> node = queue.poll();
                    System.out.print(node.data + " ");
                    if (node.left != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                System.out.println(); // new line per level
            }
        }

        // ---- UTILITY METHODS ----

        // Height of the tree - O(n)
        public int height(Node<T> node) {
            if (node == null) return -1; // -1 for edges; use 0 for node-counting
            return 1 + Math.max(height(node.left), height(node.right));
        }

        // Count total nodes - O(n)
        public int countNodes(Node<T> node) {
            if (node == null) return 0;
            return 1 + countNodes(node.left) + countNodes(node.right);
        }

        // Count leaf nodes - O(n)
        public int countLeaves(Node<T> node) {
            if (node == null) return 0;
            if (node.left == null && node.right == null) return 1;
            return countLeaves(node.left) + countLeaves(node.right);
        }

        // Check if tree is symmetric (mirror of itself) - O(n)
        public boolean isSymmetric() {
            return isMirror(root, root);
        }

        private boolean isMirror(Node<T> left, Node<T> right) {
            if (left == null && right == null) return true;
            if (left == null || right == null) return false;
            return left.data.equals(right.data)
                && isMirror(left.left, right.right)
                && isMirror(left.right, right.left);
        }

        // Lowest Common Ancestor - O(n)
        public Node<T> lca(Node<T> node, T val1, T val2) {
            if (node == null) return null;
            if (node.data.equals(val1) || node.data.equals(val2)) return node;
            Node<T> left = lca(node.left, val1, val2);
            Node<T> right = lca(node.right, val1, val2);
            if (left != null && right != null) return node; // one in each subtree
            return left != null ? left : right;
        }

        // Maximum path sum (handles negative values) - O(n)
        private int maxSum = Integer.MIN_VALUE;

        public int maxPathSum() {
            maxSum = Integer.MIN_VALUE;
            maxPathSumHelper(root);
            return maxSum;
        }

        @SuppressWarnings("unchecked")
        private int maxPathSumHelper(Node<T> node) {
            if (node == null) return 0;
            int leftGain = Math.max(maxPathSumHelper(node.left), 0);
            int rightGain = Math.max(maxPathSumHelper(node.right), 0);
            int price = (Integer) node.data + leftGain + rightGain;
            maxSum = Math.max(maxSum, price);
            return (Integer) node.data + Math.max(leftGain, rightGain);
        }

        // Pretty-print the tree
        public void visualize(Node<T> node, String prefix, boolean isLeft) {
            if (node == null) return;
            visualize(node.right, prefix + (isLeft ? "│   " : "    "), false);
            System.out.println(prefix + (isLeft ? "└── " : "┌── ") + node.data);
            visualize(node.left, prefix + (isLeft ? "    " : "│   "), true);
        }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Binary Tree built from array representation
    //   - Complete binary tree stored in an array (heap-style indexing)
    //   - Parent of i: (i-1)/2
    //   - Left child of i: 2*i + 1
    //   - Right child of i: 2*i + 2
    // =========================================================================
    static class ArrayBinaryTree<T> {
        private final Object[] tree;
        private int size;
        private final int capacity;

        public ArrayBinaryTree(int capacity) {
            this.capacity = capacity;
            this.tree = new Object[capacity];
        }

        public void insert(T data) {
            if (size >= capacity) throw new RuntimeException("Tree is full");
            tree[size++] = data;
        }

        @SuppressWarnings("unchecked")
        public T get(int index) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
            return (T) tree[index];
        }

        public int parent(int i) { return (i - 1) / 2; }
        public int leftChild(int i) { return 2 * i + 1; }
        public int rightChild(int i) { return 2 * i + 2; }
        public boolean hasLeft(int i) { return leftChild(i) < size; }
        public boolean hasRight(int i) { return rightChild(i) < size; }

        // Level-order print (natural since stored level by level)
        public void printLevelOrder() {
            for (int level = 0, levelSize = 1; level * levelSize - 1 < size; levelSize *= 2, level++) {
                int start = levelSize - 1;
                int end = Math.min(start + levelSize, size);
                if (start >= size) break;
                System.out.print("Level " + level + ": ");
                for (int i = start; i < end; i++) {
                    System.out.print(tree[i] + " ");
                }
                System.out.println();
            }
        }

        // In-order traversal using indices
        public void inOrder(int i) {
            if (i >= size) return;
            inOrder(leftChild(i));
            System.out.print(tree[i] + " ");
            inOrder(rightChild(i));
        }

        public int size() { return size; }
    }

    // =========================================================================
    // IMPLEMENTATION 3: Serializable Binary Tree (serialize/deserialize)
    //   - Demonstrates tree -> string and string -> tree conversion
    //   - Uses pre-order serialization with null markers
    // =========================================================================
    static class SerializableBinaryTree {
        static class Node {
            int val;
            Node left, right;
            Node(int val) { this.val = val; }
        }

        Node root;

        // Serialize tree to string: "1,2,null,null,3,null,null"
        public String serialize() {
            StringBuilder sb = new StringBuilder();
            serializeHelper(root, sb);
            return sb.toString();
        }

        private void serializeHelper(Node node, StringBuilder sb) {
            if (node == null) {
                sb.append("null,");
                return;
            }
            sb.append(node.val).append(",");
            serializeHelper(node.left, sb);
            serializeHelper(node.right, sb);
        }

        // Deserialize string back to tree
        public Node deserialize(String data) {
            java.util.Queue<String> tokens = new java.util.LinkedList<>(
                java.util.Arrays.asList(data.split(","))
            );
            return deserializeHelper(tokens);
        }

        private Node deserializeHelper(java.util.Queue<String> tokens) {
            String val = tokens.poll();
            if ("null".equals(val)) return null;
            Node node = new Node(Integer.parseInt(val));
            node.left = deserializeHelper(tokens);
            node.right = deserializeHelper(tokens);
            return node;
        }

        // Check if two trees are identical
        public boolean isIdentical(Node a, Node b) {
            if (a == null && b == null) return true;
            if (a == null || b == null) return false;
            return a.val == b.val
                && isIdentical(a.left, b.left)
                && isIdentical(a.right, b.right);
        }

        // Invert binary tree (mirror) - O(n)
        public Node invert(Node node) {
            if (node == null) return null;
            Node temp = node.left;
            node.left = invert(node.right);
            node.right = invert(temp);
            return node;
        }

        // Get all root-to-leaf paths
        public java.util.List<String> rootToLeafPaths() {
            java.util.List<String> paths = new java.util.ArrayList<>();
            findPaths(root, "", paths);
            return paths;
        }

        private void findPaths(Node node, String path, java.util.List<String> paths) {
            if (node == null) return;
            path += node.val;
            if (node.left == null && node.right == null) {
                paths.add(path);
                return;
            }
            findPaths(node.left, path + "->", paths);
            findPaths(node.right, path + "->", paths);
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== BINARY TREE IMPLEMENTATIONS ===\n");

        // Build this tree:
        //         1
        //        / \
        //       2   3
        //      / \   \
        //     4   5   6

        // --- Implementation 1 ---
        System.out.println("-- Implementation 1: Basic Binary Tree --");
        BasicBinaryTree<Integer> bt = new BasicBinaryTree<>();
        bt.root = new BasicBinaryTree.Node<>(1);
        bt.root.left = new BasicBinaryTree.Node<>(2);
        bt.root.right = new BasicBinaryTree.Node<>(3);
        bt.root.left.left = new BasicBinaryTree.Node<>(4);
        bt.root.left.right = new BasicBinaryTree.Node<>(5);
        bt.root.right.right = new BasicBinaryTree.Node<>(6);

        System.out.print("In-Order:    "); bt.inOrder(bt.root); System.out.println();
        System.out.print("Pre-Order:   "); bt.preOrder(bt.root); System.out.println();
        System.out.print("Post-Order:  "); bt.postOrder(bt.root); System.out.println();
        System.out.println("Level-Order:");
        bt.levelOrder();
        System.out.println("Height: " + bt.height(bt.root));
        System.out.println("Total nodes: " + bt.countNodes(bt.root));
        System.out.println("Leaf nodes: " + bt.countLeaves(bt.root));

        System.out.println("\nTree visualization:");
        bt.visualize(bt.root, "", true);

        // --- Implementation 2: Array-based ---
        System.out.println("\n-- Implementation 2: Array-based Binary Tree --");
        ArrayBinaryTree<Integer> abt = new ArrayBinaryTree<>(7);
        for (int i = 1; i <= 7; i++) abt.insert(i);
        abt.printLevelOrder();
        System.out.print("In-Order: "); abt.inOrder(0); System.out.println();

        // --- Implementation 3: Serializable ---
        System.out.println("\n-- Implementation 3: Serializable Binary Tree --");
        SerializableBinaryTree sbt = new SerializableBinaryTree();
        sbt.root = new SerializableBinaryTree.Node(1);
        sbt.root.left = new SerializableBinaryTree.Node(2);
        sbt.root.right = new SerializableBinaryTree.Node(3);
        sbt.root.left.left = new SerializableBinaryTree.Node(4);

        String serialized = sbt.serialize();
        System.out.println("Serialized: " + serialized);

        SerializableBinaryTree sbt2 = new SerializableBinaryTree();
        sbt2.root = sbt2.deserialize(serialized);
        System.out.println("Deserialized and re-serialized: " + sbt2.serialize());
        System.out.println("Trees identical? " + sbt.isIdentical(sbt.root, sbt2.root));

        System.out.println("Root-to-leaf paths: " + sbt.rootToLeafPaths());
    }
}
