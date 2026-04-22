package datastructures.trees;

/**
 * BINARY SEARCH TREE (BST) - Three Implementations
 *
 * A BST is a binary tree where for every node:
 *   - All values in the LEFT subtree are LESS than the node's value
 *   - All values in the RIGHT subtree are GREATER than the node's value
 *
 * Time Complexities (average / worst):
 *   - Search:  O(log n) / O(n)
 *   - Insert:  O(log n) / O(n)
 *   - Delete:  O(log n) / O(n)
 *   Worst case O(n) happens when tree degenerates into a linked list.
 */
public class BinarySearchTree {

    // =========================================================================
    // IMPLEMENTATION 1: Recursive BST
    // =========================================================================
    static class RecursiveBST {
        Node root;

        static class Node {
            int val;
            Node left, right;
            Node(int val) { this.val = val; }
        }

        // --- Insert ---
        public void insert(int val) {
            root = insertRec(root, val);
        }

        private Node insertRec(Node node, int val) {
            if (node == null) return new Node(val);
            if (val < node.val) node.left = insertRec(node.left, val);
            else if (val > node.val) node.right = insertRec(node.right, val);
            // val == node.val: duplicates ignored
            return node;
        }

        // --- Search ---
        public boolean search(int val) {
            return searchRec(root, val);
        }

        private boolean searchRec(Node node, int val) {
            if (node == null) return false;
            if (val == node.val) return true;
            return val < node.val ? searchRec(node.left, val) : searchRec(node.right, val);
        }

        // --- Delete ---
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
                // Node to delete found - three cases:
                if (node.left == null) return node.right;  // Case 1 & 2
                if (node.right == null) return node.left;  // Case 2

                // Case 3: two children - replace with in-order successor (min of right subtree)
                int successorVal = findMin(node.right);
                node.val = successorVal;
                node.right = deleteRec(node.right, successorVal);
            }
            return node;
        }

        // --- Find min / max ---
        public int findMin(Node node) {
            while (node.left != null) node = node.left;
            return node.val;
        }

        public int findMax() {
            if (root == null) throw new RuntimeException("BST is empty");
            Node node = root;
            while (node.right != null) node = node.right;
            return node.val;
        }

        // --- In-order gives sorted output ---
        public void inOrder() {
            inOrderRec(root);
            System.out.println();
        }

        private void inOrderRec(Node node) {
            if (node == null) return;
            inOrderRec(node.left);
            System.out.print(node.val + " ");
            inOrderRec(node.right);
        }

        // --- Validate BST ---
        public boolean isValidBST() {
            return validateRec(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        private boolean validateRec(Node node, int min, int max) {
            if (node == null) return true;
            if (node.val <= min || node.val >= max) return false;
            return validateRec(node.left, min, node.val)
                && validateRec(node.right, node.val, max);
        }

        // --- Height ---
        public int height() {
            return heightRec(root);
        }

        private int heightRec(Node node) {
            if (node == null) return -1;
            return 1 + Math.max(heightRec(node.left), heightRec(node.right));
        }

        // --- Kth smallest element ---
        private int kthCount = 0;
        private int kthResult = -1;

        public int kthSmallest(int k) {
            kthCount = 0;
            kthResult = -1;
            kthSmallestRec(root, k);
            return kthResult;
        }

        private void kthSmallestRec(Node node, int k) {
            if (node == null || kthCount >= k) return;
            kthSmallestRec(node.left, k);
            kthCount++;
            if (kthCount == k) { kthResult = node.val; return; }
            kthSmallestRec(node.right, k);
        }

        // --- Floor / Ceil ---
        public int floor(int val) {
            Node result = floorRec(root, val);
            if (result == null) throw new RuntimeException("No floor found");
            return result.val;
        }

        private Node floorRec(Node node, int val) {
            if (node == null) return null;
            if (node.val == val) return node;
            if (node.val > val) return floorRec(node.left, val);
            Node right = floorRec(node.right, val);
            return (right != null) ? right : node;
        }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Iterative BST (no recursion)
    // =========================================================================
    static class IterativeBST {
        Node root;

        static class Node {
            int val;
            Node left, right;
            Node(int val) { this.val = val; }
        }

        // Iterative insert - O(log n) average
        public void insert(int val) {
            Node newNode = new Node(val);
            if (root == null) { root = newNode; return; }
            Node current = root;
            Node parent = null;
            while (current != null) {
                parent = current;
                if (val < current.val) current = current.left;
                else if (val > current.val) current = current.right;
                else return; // duplicate, ignore
            }
            if (val < parent.val) parent.left = newNode;
            else parent.right = newNode;
        }

        // Iterative search - O(log n) average
        public boolean search(int val) {
            Node current = root;
            while (current != null) {
                if (val == current.val) return true;
                current = val < current.val ? current.left : current.right;
            }
            return false;
        }

        // Iterative in-order using explicit stack
        public void inOrder() {
            java.util.Stack<Node> stack = new java.util.Stack<>();
            Node current = root;
            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }
                current = stack.pop();
                System.out.print(current.val + " ");
                current = current.right;
            }
            System.out.println();
        }

        // Iterative level-order (BFS)
        public void levelOrder() {
            if (root == null) return;
            java.util.Queue<Node> queue = new java.util.LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                Node node = queue.poll();
                System.out.print(node.val + " ");
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            System.out.println();
        }

        // Count nodes in range [low, high]
        public int countInRange(int low, int high) {
            int count = 0;
            java.util.Stack<Node> stack = new java.util.Stack<>();
            if (root != null) stack.push(root);
            while (!stack.isEmpty()) {
                Node node = stack.pop();
                if (node.val >= low && node.val <= high) {
                    count++;
                    if (node.left != null) stack.push(node.left);
                    if (node.right != null) stack.push(node.right);
                } else if (node.val < low) {
                    if (node.right != null) stack.push(node.right);
                } else {
                    if (node.left != null) stack.push(node.left);
                }
            }
            return count;
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: BST with parent pointers and order-statistic operations
    // =========================================================================
    static class OrderStatisticBST {
        Node root;

        static class Node {
            int val;
            Node left, right, parent;
            int size; // size of subtree rooted at this node

            Node(int val) {
                this.val = val;
                this.size = 1;
            }
        }

        private void updateSize(Node node) {
            if (node != null) {
                node.size = 1
                    + (node.left != null ? node.left.size : 0)
                    + (node.right != null ? node.right.size : 0);
            }
        }

        public void insert(int val) {
            Node newNode = new Node(val);
            if (root == null) { root = newNode; return; }
            Node current = root;
            while (true) {
                current.size++;
                if (val < current.val) {
                    if (current.left == null) {
                        current.left = newNode;
                        newNode.parent = current;
                        break;
                    }
                    current = current.left;
                } else if (val > current.val) {
                    if (current.right == null) {
                        current.right = newNode;
                        newNode.parent = current;
                        break;
                    }
                    current = current.right;
                } else {
                    // Duplicate found; restore sizes incremented above
                    current = current.parent;
                    while (current != null) {
                        current.size--;
                        current = current.parent;
                    }
                    return;
                }
            }
        }

        // Select: find the kth smallest element (1-indexed) - O(log n) average
        public int select(int k) {
            return selectRec(root, k);
        }

        private int selectRec(Node node, int k) {
            if (node == null) throw new RuntimeException("k out of bounds");
            int leftSize = node.left != null ? node.left.size : 0;
            if (k == leftSize + 1) return node.val;
            if (k <= leftSize) return selectRec(node.left, k);
            return selectRec(node.right, k - leftSize - 1);
        }

        // Rank: how many elements are less than val (0-indexed rank)
        public int rank(int val) {
            return rankRec(root, val);
        }

        private int rankRec(Node node, int val) {
            if (node == null) return 0;
            if (val < node.val) return rankRec(node.left, val);
            if (val > node.val) {
                int leftSize = node.left != null ? node.left.size : 0;
                return leftSize + 1 + rankRec(node.right, val);
            }
            return node.left != null ? node.left.size : 0;
        }

        // In-order traversal
        public void inOrder() {
            inOrderRec(root);
            System.out.println();
        }

        private void inOrderRec(Node node) {
            if (node == null) return;
            inOrderRec(node.left);
            System.out.print(node.val + "(sz=" + node.size + ") ");
            inOrderRec(node.right);
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== BINARY SEARCH TREE (BST) IMPLEMENTATIONS ===\n");

        // --- Implementation 1: Recursive BST ---
        System.out.println("-- Implementation 1: Recursive BST --");
        RecursiveBST bst1 = new RecursiveBST();
        int[] values = {5, 3, 7, 1, 4, 6, 8, 2};
        for (int v : values) bst1.insert(v);

        System.out.print("In-Order (sorted): ");
        bst1.inOrder();
        System.out.println("Search 4: " + bst1.search(4));
        System.out.println("Search 9: " + bst1.search(9));
        System.out.println("Height: " + bst1.height());
        System.out.println("Valid BST: " + bst1.isValidBST());
        System.out.println("Min: " + bst1.findMin(bst1.root));
        System.out.println("Max: " + bst1.findMax());
        System.out.println("3rd smallest: " + bst1.kthSmallest(3));
        System.out.println("Floor(5): " + bst1.floor(5));

        bst1.delete(3);
        System.out.print("After delete(3): ");
        bst1.inOrder();

        // --- Implementation 2: Iterative BST ---
        System.out.println("\n-- Implementation 2: Iterative BST --");
        IterativeBST bst2 = new IterativeBST();
        for (int v : values) bst2.insert(v);

        System.out.print("In-Order (iterative): ");
        bst2.inOrder();
        System.out.print("Level-Order: ");
        bst2.levelOrder();
        System.out.println("Count in range [2,6]: " + bst2.countInRange(2, 6));

        // --- Implementation 3: Order-Statistic BST ---
        System.out.println("\n-- Implementation 3: Order-Statistic BST --");
        OrderStatisticBST bst3 = new OrderStatisticBST();
        int[] vals = {10, 5, 15, 3, 7, 12, 20};
        for (int v : vals) bst3.insert(v);

        System.out.print("In-Order with sizes: ");
        bst3.inOrder();
        System.out.println("1st smallest (select 1): " + bst3.select(1));
        System.out.println("4th smallest (select 4): " + bst3.select(4));
        System.out.println("Rank of 12 (0-indexed): " + bst3.rank(12));
        System.out.println("Rank of 5 (0-indexed):  " + bst3.rank(5));
    }
}
