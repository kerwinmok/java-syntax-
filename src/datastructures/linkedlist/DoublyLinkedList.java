package datastructures.linkedlist;

/**
 * DOUBLY LINKED LIST - Three Implementations
 *
 * A doubly linked list has nodes with pointers to both the next and previous
 * nodes, allowing traversal in both directions.
 *
 * Time Complexities:
 *   - Access:  O(n)
 *   - Search:  O(n)
 *   - Insert (head/tail): O(1)
 *   - Delete (head/tail): O(1)
 *   - Delete (arbitrary with reference): O(1)
 *   - Delete (by value): O(n)
 *
 * Advantage over singly: O(1) removal from tail, bidirectional traversal.
 */
public class DoublyLinkedList {

    // =========================================================================
    // IMPLEMENTATION 1: Basic Doubly Linked List
    // =========================================================================
    static class BasicDoublyLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> prev;
            Node<T> next;

            Node(T data) {
                this.data = data;
            }
        }

        // O(1) - insert at front
        public void addFirst(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
            size++;
        }

        // O(1) - insert at back
        public void addLast(T data) {
            Node<T> newNode = new Node<>(data);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            size++;
        }

        // O(1) - remove from front
        public T removeFirst() {
            if (head == null) throw new RuntimeException("List is empty");
            T data = head.data;
            if (head == tail) {
                head = tail = null;
            } else {
                head = head.next;
                head.prev = null;
            }
            size--;
            return data;
        }

        // O(1) - remove from back (advantage over singly linked list!)
        public T removeLast() {
            if (tail == null) throw new RuntimeException("List is empty");
            T data = tail.data;
            if (head == tail) {
                head = tail = null;
            } else {
                tail = tail.prev;
                tail.next = null;
            }
            size--;
            return data;
        }

        // O(n) - remove by value
        public boolean remove(T data) {
            Node<T> current = head;
            while (current != null) {
                if (current.data.equals(data)) {
                    removeNode(current);
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        // O(1) - remove a given node (when you have a reference to it)
        private void removeNode(Node<T> node) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
            size--;
        }

        // Print forward
        public void printForward() {
            System.out.print("NULL <-> ");
            Node<T> current = head;
            while (current != null) {
                System.out.print(current.data + " <-> ");
                current = current.next;
            }
            System.out.println("NULL");
        }

        // Print backward
        public void printBackward() {
            System.out.print("NULL <-> ");
            Node<T> current = tail;
            while (current != null) {
                System.out.print(current.data + " <-> ");
                current = current.prev;
            }
            System.out.println("NULL");
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
        public T peekFirst() { return head != null ? head.data : null; }
        public T peekLast() { return tail != null ? tail.data : null; }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Doubly Linked List used as a Deque (Double-Ended Queue)
    // =========================================================================
    static class DequeLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> prev;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // Queue operations (FIFO)
        public void enqueue(T data) { addLast(data); }
        public T dequeue() { return removeFirst(); }

        // Stack operations (LIFO)
        public void push(T data) { addFirst(data); }
        public T pop() { return removeFirst(); }

        // Deque operations
        public void addFirst(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
            size++;
        }

        public void addLast(T data) {
            Node<T> newNode = new Node<>(data);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            size++;
        }

        public T removeFirst() {
            if (head == null) throw new RuntimeException("Deque is empty");
            T data = head.data;
            if (head == tail) { head = tail = null; }
            else { head = head.next; head.prev = null; }
            size--;
            return data;
        }

        public T removeLast() {
            if (tail == null) throw new RuntimeException("Deque is empty");
            T data = tail.data;
            if (head == tail) { head = tail = null; }
            else { tail = tail.prev; tail.next = null; }
            size--;
            return data;
        }

        public T peekFirst() { return head != null ? head.data : null; }
        public T peekLast() { return tail != null ? tail.data : null; }
        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        // Check if palindrome using deque - O(n)
        public boolean isPalindrome() {
            if (size <= 1) return true;
            Node<T> front = head;
            Node<T> back = tail;
            int steps = size / 2;
            for (int i = 0; i < steps; i++) {
                if (!front.data.equals(back.data)) return false;
                front = front.next;
                back = back.prev;
            }
            return true;
        }

        public void print() {
            System.out.print("HEAD <-> ");
            Node<T> current = head;
            while (current != null) {
                System.out.print(current.data + " <-> ");
                current = current.next;
            }
            System.out.println("TAIL");
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: LRU Cache using Doubly Linked List + HashMap
    //   - get(key):    O(1)
    //   - put(key):    O(1)
    //   - Most recently used at HEAD, least recently used at TAIL
    // =========================================================================
    static class LRUCache<K, V> {
        private final int capacity;
        private final java.util.HashMap<K, Node<K, V>> map;
        private Node<K, V> head; // most recently used (dummy)
        private Node<K, V> tail; // least recently used (dummy)

        private static class Node<K, V> {
            K key;
            V value;
            Node<K, V> prev;
            Node<K, V> next;

            Node(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new java.util.HashMap<>();
            // Use dummy head and tail to simplify edge cases
            head = new Node<>(null, null);
            tail = new Node<>(null, null);
            head.next = tail;
            tail.prev = head;
        }

        // O(1) get - also marks as recently used
        public V get(K key) {
            Node<K, V> node = map.get(key);
            if (node == null) return null;
            moveToFront(node);
            return node.value;
        }

        // O(1) put - evicts LRU if at capacity
        public void put(K key, V value) {
            Node<K, V> node = map.get(key);
            if (node != null) {
                node.value = value;
                moveToFront(node);
            } else {
                if (map.size() == capacity) {
                    // Evict the least recently used (just before tail)
                    Node<K, V> lruNode = tail.prev;
                    removeNode(lruNode);
                    map.remove(lruNode.key);
                }
                Node<K, V> newNode = new Node<>(key, value);
                addToFront(newNode);
                map.put(key, newNode);
            }
        }

        private void moveToFront(Node<K, V> node) {
            removeNode(node);
            addToFront(node);
        }

        private void addToFront(Node<K, V> node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        private void removeNode(Node<K, V> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        public int size() { return map.size(); }

        public void print() {
            System.out.print("MRU [ ");
            Node<K, V> current = head.next;
            while (current != tail) {
                System.out.print(current.key + "=" + current.value + " ");
                current = current.next;
            }
            System.out.println("] LRU  (size=" + map.size() + "/" + capacity + ")");
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== DOUBLY LINKED LIST IMPLEMENTATIONS ===\n");

        // --- Implementation 1: Basic Doubly Linked List ---
        System.out.println("-- Implementation 1: Basic Doubly Linked List --");
        BasicDoublyLinkedList<Integer> list = new BasicDoublyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.addFirst(0);
        System.out.print("Forward:  ");
        list.printForward();
        System.out.print("Backward: ");
        list.printBackward();
        list.removeFirst();
        list.removeLast();
        System.out.print("After removeFirst & removeLast: ");
        list.printForward();

        // --- Implementation 2: Deque ---
        System.out.println("\n-- Implementation 2: Doubly Linked List as Deque --");
        DequeLinkedList<Character> deque = new DequeLinkedList<>();
        // Use as stack (LIFO)
        deque.push('A');
        deque.push('B');
        deque.push('C');
        System.out.println("Stack pop: " + deque.pop()); // C

        // Check palindrome
        DequeLinkedList<Character> pal = new DequeLinkedList<>();
        for (char c : "racecar".toCharArray()) pal.addLast(c);
        System.out.println("Is 'racecar' palindrome? " + pal.isPalindrome()); // true

        DequeLinkedList<Character> notPal = new DequeLinkedList<>();
        for (char c : "hello".toCharArray()) notPal.addLast(c);
        System.out.println("Is 'hello' palindrome? " + notPal.isPalindrome()); // false

        // --- Implementation 3: LRU Cache ---
        System.out.println("\n-- Implementation 3: LRU Cache (DLL + HashMap) --");
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "one");
        cache.put(2, "two");
        cache.put(3, "three");
        System.out.print("After 3 puts: ");
        cache.print();

        cache.get(1);  // Access key 1 -> moves it to front (MRU)
        System.out.print("After get(1): ");
        cache.print();

        cache.put(4, "four");  // Capacity exceeded -> evict LRU (key 2)
        System.out.print("After put(4) [evicts LRU]: ");
        cache.print();

        System.out.println("get(2) after eviction: " + cache.get(2)); // null
        System.out.println("get(1): " + cache.get(1)); // one
    }
}
