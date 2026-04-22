package datastructures.queue;

/**
 * QUEUE - Three Implementations
 *
 * A queue is a First-In-First-Out (FIFO) data structure.
 * Elements are added at the back (enqueue) and removed from the front (dequeue).
 *
 * Core operations:
 *   - enqueue(item): add to back  — O(1)
 *   - dequeue():     remove front — O(1)
 *   - peek():        view front   — O(1)
 *   - isEmpty():     check empty  — O(1)
 *
 * Common use cases:
 *   - BFS traversal
 *   - Task scheduling
 *   - Print spooler / request buffers
 *   - Producer-consumer patterns
 */
public class Queue {

    // =========================================================================
    // IMPLEMENTATION 1: Circular Array Queue (Ring Buffer)
    //   - Fixed capacity, O(1) all operations
    //   - Avoids O(n) shifting by using modular arithmetic for indices
    // =========================================================================
    static class CircularArrayQueue<T> {
        private final Object[] data;
        private int front;
        private int back;
        private int size;
        private final int capacity;

        public CircularArrayQueue(int capacity) {
            this.capacity = capacity;
            this.data = new Object[capacity];
            this.front = 0;
            this.back = 0;
        }

        // O(1) enqueue
        public void enqueue(T item) {
            if (isFull()) throw new RuntimeException("Queue is full");
            data[back] = item;
            back = (back + 1) % capacity;
            size++;
        }

        // O(1) dequeue
        @SuppressWarnings("unchecked")
        public T dequeue() {
            if (isEmpty()) throw new RuntimeException("Queue is empty");
            T item = (T) data[front];
            data[front] = null;
            front = (front + 1) % capacity;
            size--;
            return item;
        }

        // O(1) peek
        @SuppressWarnings("unchecked")
        public T peek() {
            if (isEmpty()) throw new RuntimeException("Queue is empty");
            return (T) data[front];
        }

        public boolean isEmpty() { return size == 0; }
        public boolean isFull() { return size == capacity; }
        public int size() { return size; }

        // Practical: Generate binary numbers from 1 to n using queue
        public static String[] generateBinaryNumbers(int n) {
            CircularArrayQueue<String> queue = new CircularArrayQueue<>(n + 10);
            String[] result = new String[n];
            queue.enqueue("1");
            for (int i = 0; i < n; i++) {
                String front = queue.dequeue();
                result[i] = front;
                queue.enqueue(front + "0");
                queue.enqueue(front + "1");
            }
            return result;
        }

        public void print() {
            System.out.print("Queue [front→back]: ");
            for (int i = 0; i < size; i++) {
                System.out.print(data[(front + i) % capacity] + " ");
            }
            System.out.println("(size=" + size + "/" + capacity + ")");
        }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Linked List Queue
    //   - Unlimited capacity (only bounded by memory)
    //   - O(1) enqueue and dequeue with head/tail pointers
    // =========================================================================
    static class LinkedListQueue<T> {
        private Node<T> head; // front - dequeue from here
        private Node<T> tail; // back  - enqueue to here
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // O(1) enqueue at tail
        public void enqueue(T item) {
            Node<T> newNode = new Node<>(item);
            if (tail == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
            size++;
        }

        // O(1) dequeue from head
        public T dequeue() {
            if (isEmpty()) throw new RuntimeException("Queue is empty");
            T data = head.data;
            head = head.next;
            if (head == null) tail = null;
            size--;
            return data;
        }

        // O(1) peek
        public T peek() {
            if (isEmpty()) throw new RuntimeException("Queue is empty");
            return head.data;
        }

        public boolean isEmpty() { return size == 0; }
        public int size() { return size; }

        // Practical: First non-repeating character in a stream
        public static char firstNonRepeating(String stream) {
            LinkedListQueue<Character> queue = new LinkedListQueue<>();
            int[] freq = new int[26];
            for (char c : stream.toCharArray()) {
                freq[c - 'a']++;
                queue.enqueue(c);
                // Remove from front if its frequency > 1
                while (!queue.isEmpty() && freq[queue.peek() - 'a'] > 1) {
                    queue.dequeue();
                }
            }
            return queue.isEmpty() ? '#' : queue.peek();
        }

        // Practical: Reverse first K elements of a queue
        public void reverseFirstK(int k) {
            if (isEmpty() || k > size) return;
            java.util.Stack<T> stack = new java.util.Stack<>();
            // Pop first k into stack
            for (int i = 0; i < k; i++) stack.push(dequeue());
            // Re-add from stack (reversed)
            while (!stack.isEmpty()) enqueue(stack.pop());
            // Move remaining elements to back
            for (int i = 0; i < size - k; i++) enqueue(dequeue());
        }

        public void print() {
            System.out.print("Queue [front→back]: ");
            Node<T> current = head;
            while (current != null) {
                System.out.print(current.data + " ");
                current = current.next;
            }
            System.out.println("(size=" + size + ")");
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: Priority Queue + Deque (Double-Ended Queue)
    //   3a. PriorityQueue wrapping Java's built-in with custom comparator
    //   3b. Deque implemented with doubly linked list
    // =========================================================================

    // 3a. Task Scheduler using PriorityQueue
    static class TaskScheduler {
        static class Task implements Comparable<Task> {
            String name;
            int priority;
            long timestamp;

            Task(String name, int priority) {
                this.name = name;
                this.priority = priority;
                this.timestamp = System.nanoTime();
            }

            // Higher priority value = runs first; ties broken by arrival order
            @Override
            public int compareTo(Task other) {
                if (this.priority != other.priority) return other.priority - this.priority;
                return Long.compare(this.timestamp, other.timestamp);
            }

            @Override
            public String toString() { return name + "(p=" + priority + ")"; }
        }

        private final java.util.PriorityQueue<Task> pq = new java.util.PriorityQueue<>();

        public void submit(String name, int priority) {
            pq.offer(new Task(name, priority));
        }

        public Task execute() {
            return pq.poll();
        }

        public boolean hasTasks() { return !pq.isEmpty(); }
    }

    // 3b. Sliding Window Maximum using Deque — O(n) solution
    static int[] slidingWindowMax(int[] arr, int k) {
        // Deque stores indices; front always has index of current window's max
        java.util.ArrayDeque<Integer> deque = new java.util.ArrayDeque<>();
        int[] result = new int[arr.length - k + 1];

        for (int i = 0; i < arr.length; i++) {
            // Remove elements outside window
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            // Remove smaller elements from back (they'll never be max)
            while (!deque.isEmpty() && arr[deque.peekLast()] < arr[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            // Start recording once first window is full
            if (i >= k - 1) {
                result[i - k + 1] = arr[deque.peekFirst()];
            }
        }
        return result;
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== QUEUE IMPLEMENTATIONS ===\n");

        // --- Implementation 1: Circular Array Queue ---
        System.out.println("-- Implementation 1: Circular Array Queue --");
        CircularArrayQueue<Integer> cq = new CircularArrayQueue<>(5);
        cq.enqueue(1); cq.enqueue(2); cq.enqueue(3); cq.enqueue(4);
        cq.print();
        System.out.println("Dequeue: " + cq.dequeue());
        System.out.println("Dequeue: " + cq.dequeue());
        cq.enqueue(5); cq.enqueue(6);
        cq.print();

        System.out.println("\nBinary numbers 1..8: ");
        String[] bins = CircularArrayQueue.generateBinaryNumbers(8);
        for (String b : bins) System.out.print(b + " ");
        System.out.println();

        // --- Implementation 2: Linked List Queue ---
        System.out.println("\n-- Implementation 2: Linked List Queue --");
        LinkedListQueue<Integer> lq = new LinkedListQueue<>();
        for (int i = 1; i <= 6; i++) lq.enqueue(i);
        lq.print();
        lq.reverseFirstK(3);
        System.out.print("After reverseFirstK(3): ");
        lq.print();

        System.out.println("First non-repeating in 'aabbcc': " + LinkedListQueue.firstNonRepeating("aabbcc"));
        System.out.println("First non-repeating in 'abcabc': " + LinkedListQueue.firstNonRepeating("abcabc"));
        System.out.println("First non-repeating in 'aabbc': "  + LinkedListQueue.firstNonRepeating("aabbc"));

        // --- Implementation 3: PriorityQueue + Deque ---
        System.out.println("\n-- Implementation 3: Priority Queue (Task Scheduler) --");
        TaskScheduler scheduler = new TaskScheduler();
        scheduler.submit("LowPriorityJob", 1);
        scheduler.submit("HighPriorityJob", 10);
        scheduler.submit("MedPriorityJob", 5);
        scheduler.submit("CriticalJob", 10);
        System.out.println("Execution order:");
        while (scheduler.hasTasks()) System.out.println("  Execute: " + scheduler.execute());

        System.out.println("\n[Sliding Window Maximum]");
        int[] arr = {1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        int[] maxes = slidingWindowMax(arr, k);
        System.out.print("Array: [1,3,-1,-3,5,3,6,7], k=3 → max in each window: ");
        for (int m : maxes) System.out.print(m + " ");
        System.out.println();
    }
}
