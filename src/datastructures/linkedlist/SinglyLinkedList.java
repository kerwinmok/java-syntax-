package datastructures.linkedlist;

/**
 * SINGLY LINKED LIST - Three Implementations
 *
 * A singly linked list is a linear data structure where each node points to
 * the next node in the sequence. The last node points to null.
 *
 * Time Complexities:
 *   - Access:  O(n)
 *   - Search:  O(n)
 *   - Insert (head): O(1)
 *   - Insert (tail): O(n) without tail pointer, O(1) with tail pointer
 *   - Delete (head): O(1)
 *   - Delete (arbitrary): O(n)
 */
public class SinglyLinkedList {

    // =========================================================================
    // IMPLEMENTATION 1: Basic Singly Linked List with head pointer only
    // =========================================================================
    static class BasicSinglyLinkedList<T> {
        private Node<T> head;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next;

            Node(T data) {
                this.data = data;
                this.next = null;
            }
        }

        // Insert at the beginning - O(1)
        public void addFirst(T data) {
            Node<T> newNode = new Node<>(data);
            newNode.next = head;
            head = newNode;
            size++;
        }

        // Insert at the end - O(n)
        public void addLast(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = newNode;
            } else {
                Node<T> current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
        }

        // Insert at a specific index - O(n)
        public void addAt(int index, T data) {
            if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index);
            if (index == 0) {
                addFirst(data);
                return;
            }
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            Node<T> newNode = new Node<>(data);
            newNode.next = current.next;
            current.next = newNode;
            size++;
        }

        // Remove from the beginning - O(1)
        public T removeFirst() {
            if (head == null) throw new RuntimeException("List is empty");
            T data = head.data;
            head = head.next;
            size--;
            return data;
        }

        // Remove from the end - O(n)
        public T removeLast() {
            if (head == null) throw new RuntimeException("List is empty");
            if (head.next == null) {
                T data = head.data;
                head = null;
                size--;
                return data;
            }
            Node<T> current = head;
            while (current.next.next != null) {
                current = current.next;
            }
            T data = current.next.data;
            current.next = null;
            size--;
            return data;
        }

        // Remove a specific value - O(n)
        public boolean remove(T data) {
            if (head == null) return false;
            if (head.data.equals(data)) {
                head = head.next;
                size--;
                return true;
            }
            Node<T> current = head;
            while (current.next != null) {
                if (current.next.data.equals(data)) {
                    current.next = current.next.next;
                    size--;
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        // Search - O(n)
        public boolean contains(T data) {
            Node<T> current = head;
            while (current != null) {
                if (current.data.equals(data)) return true;
                current = current.next;
            }
            return false;
        }

        // Reverse the list in-place - O(n)
        public void reverse() {
            Node<T> prev = null;
            Node<T> current = head;
            while (current != null) {
                Node<T> nextNode = current.next;
                current.next = prev;
                prev = current;
                current = nextNode;
            }
            head = prev;
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        public void print() {
            Node<T> current = head;
            System.out.print("HEAD -> ");
            while (current != null) {
                System.out.print(current.data + " -> ");
                current = current.next;
            }
            System.out.println("NULL");
        }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Singly Linked List with Tail Pointer (faster append)
    // =========================================================================
    static class SinglyLinkedListWithTail<T> {
        private Node<T> head;
        private Node<T> tail;  // tail pointer for O(1) append
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // O(1) add to front
        public void addFirst(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head = newNode;
            }
            size++;
        }

        // O(1) add to back (thanks to tail pointer)
        public void addLast(T data) {
            Node<T> newNode = new Node<>(data);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        // O(1) remove from front
        public T removeFirst() {
            if (head == null) throw new RuntimeException("List is empty");
            T data = head.data;
            head = head.next;
            if (head == null) tail = null;
            size--;
            return data;
        }

        // O(n) remove from back (no prev pointer in singly LL)
        public T removeLast() {
            if (head == null) throw new RuntimeException("List is empty");
            if (head == tail) {
                T data = head.data;
                head = tail = null;
                size--;
                return data;
            }
            Node<T> current = head;
            while (current.next != tail) {
                current = current.next;
            }
            T data = tail.data;
            current.next = null;
            tail = current;
            size--;
            return data;
        }

        // Get middle node using slow/fast pointer technique - O(n)
        public T getMiddle() {
            if (head == null) throw new RuntimeException("List is empty");
            Node<T> slow = head;
            Node<T> fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow.data;
        }

        // Detect cycle using Floyd's cycle detection - O(n)
        public boolean hasCycle() {
            Node<T> slow = head;
            Node<T> fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) return true;
            }
            return false;
        }

        // Get Nth node from end - O(n)
        public T getNthFromEnd(int n) {
            Node<T> ahead = head;
            Node<T> behind = head;
            // Move ahead pointer n steps
            for (int i = 0; i < n; i++) {
                if (ahead == null) throw new IndexOutOfBoundsException("n is larger than list size");
                ahead = ahead.next;
            }
            // Move both until ahead reaches end
            while (ahead != null) {
                ahead = ahead.next;
                behind = behind.next;
            }
            return behind.data;
        }

        public int size() { return size; }

        public void print() {
            Node<T> current = head;
            System.out.print("HEAD -> ");
            while (current != null) {
                System.out.print(current.data + " -> ");
                current = current.next;
            }
            System.out.println("NULL  (tail=" + (tail != null ? tail.data : "null") + ")");
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: Generic Singly Linked List with Iterator support
    // =========================================================================
    static class IterableSinglyLinkedList<T> implements Iterable<T> {
        private Node<T> head;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        public void add(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = newNode;
            } else {
                Node<T> current = head;
                while (current.next != null) current = current.next;
                current.next = newNode;
            }
            size++;
        }

        public T get(int index) {
            if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
            Node<T> current = head;
            for (int i = 0; i < index; i++) current = current.next;
            return current.data;
        }

        // Merge sort on linked list - O(n log n)
        public void sort() {
            head = mergeSort(head);
        }

        @SuppressWarnings("unchecked")
        private Node<T> mergeSort(Node<T> node) {
            if (node == null || node.next == null) return node;
            Node<T> mid = getMiddleNode(node);
            Node<T> secondHalf = mid.next;
            mid.next = null;
            Node<T> left = mergeSort(node);
            Node<T> right = mergeSort(secondHalf);
            return merge(left, right);
        }

        @SuppressWarnings("unchecked")
        private Node<T> merge(Node<T> left, Node<T> right) {
            Node<T> dummy = new Node<>(null);
            Node<T> current = dummy;
            while (left != null && right != null) {
                if (((Comparable<T>) left.data).compareTo(right.data) <= 0) {
                    current.next = left;
                    left = left.next;
                } else {
                    current.next = right;
                    right = right.next;
                }
                current = current.next;
            }
            current.next = (left != null) ? left : right;
            return dummy.next;
        }

        private Node<T> getMiddleNode(Node<T> node) {
            Node<T> slow = node;
            Node<T> fast = node.next;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }

        @Override
        public java.util.Iterator<T> iterator() {
            return new java.util.Iterator<T>() {
                Node<T> current = head;

                @Override
                public boolean hasNext() { return current != null; }

                @Override
                public T next() {
                    if (!hasNext()) throw new java.util.NoSuchElementException();
                    T data = current.data;
                    current = current.next;
                    return data;
                }
            };
        }

        public int size() { return size; }

        public void print() {
            System.out.print("HEAD -> ");
            for (T item : this) System.out.print(item + " -> ");
            System.out.println("NULL");
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== SINGLY LINKED LIST IMPLEMENTATIONS ===\n");

        // --- Implementation 1 ---
        System.out.println("-- Implementation 1: Basic Singly Linked List --");
        BasicSinglyLinkedList<Integer> list1 = new BasicSinglyLinkedList<>();
        list1.addLast(1);
        list1.addLast(2);
        list1.addLast(3);
        list1.addFirst(0);
        list1.addAt(2, 99);
        list1.print();                         // 0 -> 1 -> 99 -> 2 -> 3

        list1.reverse();
        System.out.print("After reverse: ");
        list1.print();                         // 3 -> 2 -> 99 -> 1 -> 0

        list1.remove(99);
        System.out.print("After remove(99): ");
        list1.print();                         // 3 -> 2 -> 1 -> 0

        // --- Implementation 2 ---
        System.out.println("\n-- Implementation 2: Singly Linked List with Tail Pointer --");
        SinglyLinkedListWithTail<String> list2 = new SinglyLinkedListWithTail<>();
        list2.addLast("A");
        list2.addLast("B");
        list2.addLast("C");
        list2.addLast("D");
        list2.addLast("E");
        list2.print();
        System.out.println("Middle element: " + list2.getMiddle());   // C
        System.out.println("2nd from end: " + list2.getNthFromEnd(2)); // D
        System.out.println("Has cycle: " + list2.hasCycle());          // false

        // --- Implementation 3 ---
        System.out.println("\n-- Implementation 3: Iterable Singly Linked List --");
        IterableSinglyLinkedList<Integer> list3 = new IterableSinglyLinkedList<>();
        list3.add(5);
        list3.add(3);
        list3.add(8);
        list3.add(1);
        list3.add(4);
        System.out.print("Before sort: ");
        list3.print();
        list3.sort();
        System.out.print("After sort:  ");
        list3.print();
        System.out.print("Iterator: ");
        for (int val : list3) System.out.print(val + " ");
        System.out.println();
    }
}
