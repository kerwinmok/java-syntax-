package datastructures.trees;

/**
 * AVL TREE - Self-Balancing Binary Search Tree
 *
 * An AVL tree maintains the BST property while ensuring that for every node,
 * the heights of its left and right subtrees differ by at most 1 (balance factor ∈ {-1, 0, 1}).
 *
 * Named after Adelson-Velsky and Landis (1962) — the first self-balancing BST.
 *
 * Balance Factor = height(left subtree) - height(right subtree)
 *
 * Four rotation cases:
 *   1. Left-Left   (LL) → Right rotation
 *   2. Right-Right (RR) → Left rotation
 *   3. Left-Right  (LR) → Left rotation on left child, then Right rotation
 *   4. Right-Left  (RL) → Right rotation on right child, then Left rotation
 *
 * Time Complexities (guaranteed, not average):
 *   - Search:  O(log n)
 *   - Insert:  O(log n)
 *   - Delete:  O(log n)
 */
public class AVLTree {

    // =========================================================================
    // IMPLEMENTATION 1: Classic Recursive AVL Tree
    // =========================================================================
    static class RecursiveAVL {
        Node root;

        static class Node {
            int val;
            int height;
            Node left, right;

            Node(int val) {
                this.val = val;
                this.height = 1; // height of a single node is 1
            }
        }

        private int height(Node node) {
            return node == null ? 0 : node.height;
        }

        private void updateHeight(Node node) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }

        private int balanceFactor(Node node) {
            return node == null ? 0 : height(node.left) - height(node.right);
        }

        // Right rotation (used in LL case)
        //      y                x
        //     / \              / \
        //    x   T3    →    T1   y
        //   / \                 / \
        //  T1  T2              T2  T3
        private Node rotateRight(Node y) {
            Node x = y.left;
            Node T2 = x.right;
            x.right = y;
            y.left = T2;
            updateHeight(y);
            updateHeight(x);
            return x; // x is new root
        }

        // Left rotation (used in RR case)
        //    x                    y
        //   / \                  / \
        //  T1   y      →        x   T3
        //      / \             / \
        //     T2  T3          T1  T2
        private Node rotateLeft(Node x) {
            Node y = x.right;
            Node T2 = y.left;
            y.left = x;
            x.right = T2;
            updateHeight(x);
            updateHeight(y);
            return y; // y is new root
        }

        // Rebalance: apply appropriate rotation based on balance factor
        private Node rebalance(Node node) {
            updateHeight(node);
            int bf = balanceFactor(node);

            // Left-heavy
            if (bf > 1) {
                if (balanceFactor(node.left) < 0) {
                    // Left-Right case: left-rotate left child first
                    node.left = rotateLeft(node.left);
                }
                return rotateRight(node); // Left-Left case
            }

            // Right-heavy
            if (bf < -1) {
                if (balanceFactor(node.right) > 0) {
                    // Right-Left case: right-rotate right child first
                    node.right = rotateRight(node.right);
                }
                return rotateLeft(node); // Right-Right case
            }

            return node; // already balanced
        }

        // Insert - O(log n)
        public void insert(int val) {
            root = insertRec(root, val);
        }

        private Node insertRec(Node node, int val) {
            if (node == null) return new Node(val);
            if (val < node.val) node.left = insertRec(node.left, val);
            else if (val > node.val) node.right = insertRec(node.right, val);
            else return node; // duplicate
            return rebalance(node);
        }

        // Delete - O(log n)
        public void delete(int val) {
            root = deleteRec(root, val);
        }

        private Node deleteRec(Node node, int val) {
            if (node == null) return null;
            if (val < node.val) {
                node.left = deleteRec(node.left, val);
            } else if (val > node.val) {
                node.right = deleteRec(node.right, val);
            } else {
                // Node found - handle three cases
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;
                // Two children: replace with in-order successor
                Node successor = getMin(node.right);
                node.val = successor.val;
                node.right = deleteRec(node.right, successor.val);
            }
            return rebalance(node);
        }

        private Node getMin(Node node) {
            while (node.left != null) node = node.left;
            return node;
        }

        // Search - O(log n)
        public boolean search(int val) {
            Node current = root;
            while (current != null) {
                if (val == current.val) return true;
                current = val < current.val ? current.left : current.right;
            }
            return false;
        }

        // In-order traversal
        public void inOrder() {
            inOrderRec(root);
            System.out.println();
        }

        private void inOrderRec(Node node) {
            if (node == null) return;
            inOrderRec(node.left);
            System.out.print(node.val + "(h=" + node.height + ") ");
            inOrderRec(node.right);
        }

        // Check all balance factors
        public boolean isBalanced() {
            return isBalancedRec(root);
        }

        private boolean isBalancedRec(Node node) {
            if (node == null) return true;
            int bf = balanceFactor(node);
            if (bf < -1 || bf > 1) return false;
            return isBalancedRec(node.left) && isBalancedRec(node.right);
        }

        // Pretty-print
        public void visualize(Node node, String prefix, boolean isLeft) {
            if (node == null) return;
            visualize(node.right, prefix + (isLeft ? "│   " : "    "), false);
            System.out.println(prefix + (isLeft ? "└── " : "┌── ") + node.val + "(bf=" + balanceFactor(node) + ")");
            visualize(node.left, prefix + (isLeft ? "    " : "│   "), true);
        }

        public int height() { return height(root); }
    }

    // =========================================================================
    // IMPLEMENTATION 2: AVL Tree with Rank (order statistics)
    // =========================================================================
    static class RankedAVL {
        Node root;

        static class Node {
            int val, height, size;
            Node left, right;

            Node(int val) {
                this.val = val;
                this.height = 1;
                this.size = 1;
            }
        }

        private int height(Node n) { return n == null ? 0 : n.height; }
        private int size(Node n) { return n == null ? 0 : n.size; }

        private void update(Node n) {
            if (n != null) {
                n.height = 1 + Math.max(height(n.left), height(n.right));
                n.size = 1 + size(n.left) + size(n.right);
            }
        }

        private int bf(Node n) { return n == null ? 0 : height(n.left) - height(n.right); }

        private Node rotateRight(Node y) {
            Node x = y.left;
            y.left = x.right;
            x.right = y;
            update(y);
            update(x);
            return x;
        }

        private Node rotateLeft(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            update(x);
            update(y);
            return y;
        }

        private Node balance(Node n) {
            update(n);
            int b = bf(n);
            if (b > 1) {
                if (bf(n.left) < 0) n.left = rotateLeft(n.left);
                return rotateRight(n);
            }
            if (b < -1) {
                if (bf(n.right) > 0) n.right = rotateRight(n.right);
                return rotateLeft(n);
            }
            return n;
        }

        public void insert(int val) { root = insert(root, val); }

        private Node insert(Node n, int val) {
            if (n == null) return new Node(val);
            if (val < n.val) n.left = insert(n.left, val);
            else if (val > n.val) n.right = insert(n.right, val);
            else return n;
            return balance(n);
        }

        // kth smallest (1-indexed)
        public int kthSmallest(int k) {
            return kthSmallest(root, k);
        }

        private int kthSmallest(Node n, int k) {
            if (n == null) throw new RuntimeException("k out of range");
            int leftSize = size(n.left);
            if (k == leftSize + 1) return n.val;
            if (k <= leftSize) return kthSmallest(n.left, k);
            return kthSmallest(n.right, k - leftSize - 1);
        }

        // rank: number of elements less than val
        public int rank(int val) {
            return rank(root, val);
        }

        private int rank(Node n, int val) {
            if (n == null) return 0;
            if (val < n.val) return rank(n.left, val);
            if (val > n.val) return size(n.left) + 1 + rank(n.right, val);
            return size(n.left);
        }

        public void inOrder() {
            inOrder(root);
            System.out.println();
        }

        private void inOrder(Node n) {
            if (n == null) return;
            inOrder(n.left);
            System.out.print(n.val + " ");
            inOrder(n.right);
        }

        public boolean isBalanced() { return isBalanced(root); }

        private boolean isBalanced(Node n) {
            if (n == null) return true;
            int b = bf(n);
            return b >= -1 && b <= 1 && isBalanced(n.left) && isBalanced(n.right);
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: AVL Tree demonstrating all 4 rotation cases explicitly
    // =========================================================================
    static class AVLRotationDemo {
        Node root;

        static class Node {
            int val, height;
            Node left, right;
            Node(int val) { this.val = val; this.height = 1; }
        }

        private int h(Node n) { return n == null ? 0 : n.height; }
        private void updateH(Node n) {
            if (n != null) n.height = 1 + Math.max(h(n.left), h(n.right));
        }
        private int bf(Node n) { return h(n.left) - h(n.right); }

        // LL Case - Single Right Rotation
        private Node llRotation(Node y) {
            System.out.println("  >> LL rotation at node " + y.val);
            Node x = y.left;
            y.left = x.right;
            x.right = y;
            updateH(y); updateH(x);
            return x;
        }

        // RR Case - Single Left Rotation
        private Node rrRotation(Node x) {
            System.out.println("  >> RR rotation at node " + x.val);
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            updateH(x); updateH(y);
            return y;
        }

        // LR Case - Left on left child, then Right
        private Node lrRotation(Node z) {
            System.out.println("  >> LR rotation at node " + z.val);
            z.left = rrRotation(z.left);
            return llRotation(z);
        }

        // RL Case - Right on right child, then Left
        private Node rlRotation(Node z) {
            System.out.println("  >> RL rotation at node " + z.val);
            z.right = llRotation(z.right);
            return rrRotation(z);
        }

        private Node balance(Node n) {
            updateH(n);
            int b = bf(n);
            if (b > 1) {
                return bf(n.left) >= 0 ? llRotation(n) : lrRotation(n);
            }
            if (b < -1) {
                return bf(n.right) <= 0 ? rrRotation(n) : rlRotation(n);
            }
            return n;
        }

        public void insert(int val) {
            System.out.println("Inserting " + val + "...");
            root = insert(root, val);
        }

        private Node insert(Node n, int val) {
            if (n == null) return new Node(val);
            if (val < n.val) n.left = insert(n.left, val);
            else if (val > n.val) n.right = insert(n.right, val);
            else return n;
            return balance(n);
        }

        public void inOrder() {
            inOrder(root); System.out.println();
        }

        private void inOrder(Node n) {
            if (n == null) return;
            inOrder(n.left);
            System.out.print(n.val + " ");
            inOrder(n.right);
        }

        public void visualize(Node node, String prefix, boolean isLeft) {
            if (node == null) return;
            visualize(node.right, prefix + (isLeft ? "│   " : "    "), false);
            System.out.println(prefix + (isLeft ? "└── " : "┌── ") + node.val + "(h=" + node.height + ")");
            visualize(node.left, prefix + (isLeft ? "    " : "│   "), true);
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== AVL TREE IMPLEMENTATIONS ===\n");

        // --- Implementation 1: Recursive AVL ---
        System.out.println("-- Implementation 1: Recursive AVL Tree --");
        RecursiveAVL avl1 = new RecursiveAVL();
        int[] vals = {30, 20, 40, 10, 25, 35, 50, 5, 15};
        for (int v : vals) avl1.insert(v);

        System.out.print("In-Order with heights: ");
        avl1.inOrder();
        System.out.println("Tree height: " + avl1.height());
        System.out.println("Is balanced: " + avl1.isBalanced());
        System.out.println("Search 25: " + avl1.search(25));
        System.out.println("Search 99: " + avl1.search(99));

        System.out.println("\nTree visualization:");
        avl1.visualize(avl1.root, "", true);

        avl1.delete(20);
        System.out.println("\nAfter delete(20):");
        avl1.visualize(avl1.root, "", true);
        System.out.println("Is balanced after delete: " + avl1.isBalanced());

        // --- Implementation 2: Ranked AVL ---
        System.out.println("\n-- Implementation 2: Ranked AVL Tree --");
        RankedAVL avl2 = new RankedAVL();
        int[] vals2 = {10, 5, 15, 3, 7, 12, 20};
        for (int v : vals2) avl2.insert(v);

        System.out.print("In-Order: ");
        avl2.inOrder();
        System.out.println("1st smallest: " + avl2.kthSmallest(1));
        System.out.println("4th smallest: " + avl2.kthSmallest(4));
        System.out.println("Rank of 12: " + avl2.rank(12));
        System.out.println("Is balanced: " + avl2.isBalanced());

        // --- Implementation 3: Rotation Demo ---
        System.out.println("\n-- Implementation 3: AVL Rotation Demo --");
        System.out.println("Inserting sequence that triggers all rotation types:");

        System.out.println("\n[LL Case - insert 30, 20, 10]:");
        AVLRotationDemo ll = new AVLRotationDemo();
        ll.insert(30); ll.insert(20); ll.insert(10);
        System.out.print("Result: "); ll.inOrder();

        System.out.println("\n[RR Case - insert 10, 20, 30]:");
        AVLRotationDemo rr = new AVLRotationDemo();
        rr.insert(10); rr.insert(20); rr.insert(30);
        System.out.print("Result: "); rr.inOrder();

        System.out.println("\n[LR Case - insert 30, 10, 20]:");
        AVLRotationDemo lr = new AVLRotationDemo();
        lr.insert(30); lr.insert(10); lr.insert(20);
        System.out.print("Result: "); lr.inOrder();

        System.out.println("\n[RL Case - insert 10, 30, 20]:");
        AVLRotationDemo rl = new AVLRotationDemo();
        rl.insert(10); rl.insert(30); rl.insert(20);
        System.out.print("Result: "); rl.inOrder();
    }
}
