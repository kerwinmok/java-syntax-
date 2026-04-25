package datastructures.linkedlist;

/**
 * CIRCULAR LINKED LIST - Two Implementations (Singly & Doubly Circular)
 *
 * In a circular linked list the last node links back to the first node
 * instead of pointing to null.  Useful for:
 *   - Round-robin scheduling
 *   - Circular buffers
 *   - Implementing queues efficiently
 *
 * Time Complexities (same as regular linked list for most operations):
 *   - Insert at tail: O(1) when a tail pointer is kept
 *   - Traversal: O(n)
 */
public class CircularLinkedList {

    // =========================================================================
    // IMPLEMENTATION 1: Singly Circular Linked List
    // =========================================================================
    static class SinglyCircularLinkedList<T> {
        private Node<T> tail; // We keep a TAIL pointer; head = tail.next
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // O(1) - insert at front
        public void addFirst(T data) {
            Node<T> newNode = new Node<>(data);
            if (tail == null) {
                tail = newNode;
                tail.next = tail; // points to itself
            } else {
                newNode.next = tail.next; // new node -> old head
                tail.next = newNode;      // tail -> new head
            }
            size++;
        }

        // O(1) - insert at back
        public void addLast(T data) {
            addFirst(data);          // add as head first
            tail = tail.next;        // then advance tail to that node
        }

        // O(1) - remove from front
        public T removeFirst() {
            if (tail == null) throw new RuntimeException("List is empty");
            Node<T> head = tail.next;
            T data = head.data;
            if (head == tail) {
                tail = null;
            } else {
                tail.next = head.next;
            }
            size--;
            return data;
        }

        // O(n) - remove from back
        public T removeLast() {
            if (tail == null) throw new RuntimeException("List is empty");
            T data = tail.data;
            if (tail.next == tail) {
                tail = null;
            } else {
                Node<T> current = tail.next; // start at head
                while (current.next != tail) {
                    current = current.next;
                }
                current.next = tail.next;
                tail = current;
            }
            size--;
            return data;
        }

        // O(n) - contains
        public boolean contains(T data) {
            if (tail == null) return false;
            Node<T> current = tail.next; // head
            do {
                if (current.data.equals(data)) return true;
                current = current.next;
            } while (current != tail.next);
            return false;
        }

        // Round-robin: rotate the list by moving tail forward
        public T rotateOnce() {
            if (tail == null) throw new RuntimeException("List is empty");
            tail = tail.next;
            return tail.data;
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        public void print() {
            if (tail == null) { System.out.println("(empty)"); return; }
            Node<T> current = tail.next; // head
            System.out.print("CIRCULAR: ");
            do {
                System.out.print(current.data + " -> ");
                current = current.next;
            } while (current != tail.next);
            System.out.println("(back to head)");
        }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Doubly Circular Linked List
    // =========================================================================
    static class DoublyCircularLinkedList<T> {
        private Node<T> head;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> prev;
            Node<T> next;
            Node(T data) { this.data = data; }
        }

        // O(1) - insert at front
        public void addFirst(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = newNode;
                head.next = head;
                head.prev = head;
            } else {
                Node<T> tail = head.prev;
                newNode.next = head;
                newNode.prev = tail;
                tail.next = newNode;
                head.prev = newNode;
                head = newNode;
            }
            size++;
        }

        // O(1) - insert at back
        public void addLast(T data) {
            Node<T> newNode = new Node<>(data);
            if (head == null) {
                head = newNode;
                head.next = head;
                head.prev = head;
            } else {
                Node<T> tail = head.prev;
                tail.next = newNode;
                newNode.prev = tail;
                newNode.next = head;
                head.prev = newNode;
            }
            size++;
        }

        // O(1) - remove from front
        public T removeFirst() {
            if (head == null) throw new RuntimeException("List is empty");
            T data = head.data;
            if (head.next == head) {
                head = null;
            } else {
                Node<T> tail = head.prev;
                head = head.next;
                head.prev = tail;
                tail.next = head;
            }
            size--;
            return data;
        }

        // O(1) - remove from back
        public T removeLast() {
            if (head == null) throw new RuntimeException("List is empty");
            Node<T> tail = head.prev;
            T data = tail.data;
            if (head.next == head) {
                head = null;
            } else {
                Node<T> newTail = tail.prev;
                newTail.next = head;
                head.prev = newTail;
            }
            size--;
            return data;
        }

        // O(n) - remove specific value
        public boolean remove(T data) {
            if (head == null) return false;
            Node<T> current = head;
            do {
                if (current.data.equals(data)) {
                    if (size == 1) {
                        head = null;
                    } else {
                        current.prev.next = current.next;
                        current.next.prev = current.prev;
                        if (current == head) head = current.next;
                    }
                    size--;
                    return true;
                }
                current = current.next;
            } while (current != head);
            return false;
        }

        // Print forward
        public void printForward() {
            if (head == null) { System.out.println("(empty)"); return; }
            Node<T> current = head;
            System.out.print("CIRCULAR FWD: ");
            do {
                System.out.print(current.data + " <-> ");
                current = current.next;
            } while (current != head);
            System.out.println("(back to head)");
        }

        // Print backward
        public void printBackward() {
            if (head == null) { System.out.println("(empty)"); return; }
            Node<T> tail = head.prev;
            Node<T> current = tail;
            System.out.print("CIRCULAR BWD: ");
            do {
                System.out.print(current.data + " <-> ");
                current = current.prev;
            } while (current != tail);
            System.out.println("(back to tail)");
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== CIRCULAR LINKED LIST IMPLEMENTATIONS ===\n");

        // --- Singly Circular ---
        System.out.println("-- Singly Circular Linked List --");
        SinglyCircularLinkedList<Integer> scl = new SinglyCircularLinkedList<>();
        scl.addLast(1);
        scl.addLast(2);
        scl.addLast(3);
        scl.addFirst(0);
        scl.print();

        System.out.println("Contains 2? " + scl.contains(2));
        System.out.println("Contains 9? " + scl.contains(9));
        System.out.println("Rotate once, new tail: " + scl.rotateOnce());
        scl.print();

        System.out.println("Remove first: " + scl.removeFirst());
        scl.print();

        // --- Doubly Circular ---
        System.out.println("\n-- Doubly Circular Linked List --");
        DoublyCircularLinkedList<String> dcl = new DoublyCircularLinkedList<>();
        dcl.addLast("A");
        dcl.addLast("B");
        dcl.addLast("C");
        dcl.addFirst("Z");
        dcl.printForward();
        dcl.printBackward();

        dcl.remove("B");
        System.out.print("After remove(B): ");
        dcl.printForward();

        dcl.removeLast();
        System.out.print("After removeLast: ");
        dcl.printForward();
    }
}
