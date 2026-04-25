package datastructures.heap;

/**
 * HEAP - Three Implementations
 *
 * A heap is a complete binary tree stored as an array where the parent
 * is always greater (max-heap) or smaller (min-heap) than its children.
 *
 * Array representation:
 *   - Parent of i:      (i - 1) / 2
 *   - Left child of i:  2 * i + 1
 *   - Right child of i: 2 * i + 2
 *
 * Time Complexities:
 *   - insert (offer): O(log n)  — sift up
 *   - peek:           O(1)
 *   - poll (extract): O(log n)  — sift down
 *   - heapify:        O(n)
 *   - heapsort:       O(n log n)
 */
public class Heap {

    // =========================================================================
    // IMPLEMENTATION 1: Min-Heap (array-based, generic comparable)
    //   Root is always the MINIMUM element
    // =========================================================================
    static class MinHeap<T extends Comparable<T>> {
        private final Object[] data;
        private int size;
        private final int capacity;

        public MinHeap(int capacity) {
            this.capacity = capacity;
            this.data = new Object[capacity];
        }

        // O(1)
        @SuppressWarnings("unchecked")
        public T peek() {
            if (size == 0) throw new RuntimeException("Heap is empty");
            return (T) data[0];
        }

        // O(log n) — insert and sift up
        public void offer(T item) {
            if (size >= capacity) throw new RuntimeException("Heap is full");
            data[size] = item;
            siftUp(size);
            size++;
        }

        // O(log n) — extract min and sift down
        @SuppressWarnings("unchecked")
        public T poll() {
            if (size == 0) throw new RuntimeException("Heap is empty");
            T min = (T) data[0];
            data[0] = data[--size];
            data[size] = null;
            siftDown(0);
            return min;
        }

        // Sift up: fix heap property from index i up toward root
        @SuppressWarnings("unchecked")
        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (((T) data[i]).compareTo((T) data[parent]) < 0) {
                    swap(i, parent);
                    i = parent;
                } else break;
            }
        }

        // Sift down: fix heap property from index i down toward leaves
        @SuppressWarnings("unchecked")
        private void siftDown(int i) {
            while (true) {
                int smallest = i;
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                if (left < size && ((T) data[left]).compareTo((T) data[smallest]) < 0) smallest = left;
                if (right < size && ((T) data[right]).compareTo((T) data[smallest]) < 0) smallest = right;
                if (smallest == i) break;
                swap(i, smallest);
                i = smallest;
            }
        }

        private void swap(int a, int b) {
            Object tmp = data[a];
            data[a] = data[b];
            data[b] = tmp;
        }

        // Heapify an existing array in O(n)
        public static <T extends Comparable<T>> MinHeap<T> heapify(T[] arr) {
            @SuppressWarnings("unchecked")
            MinHeap<T> heap = new MinHeap<>(arr.length);
            heap.size = arr.length;
            System.arraycopy(arr, 0, heap.data, 0, arr.length);
            // Start from last non-leaf and sift down each
            for (int i = arr.length / 2 - 1; i >= 0; i--) {
                heap.siftDown(i);
            }
            return heap;
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        public void print() {
            System.out.print("MinHeap [");
            for (int i = 0; i < size; i++) {
                System.out.print(data[i] + (i < size - 1 ? ", " : ""));
            }
            System.out.println("]  root=" + (size > 0 ? data[0] : "empty"));
        }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Max-Heap (array-based, fixed to int for clarity)
    //   Root is always the MAXIMUM element
    // =========================================================================
    static class MaxHeap {
        private int[] data;
        private int size;

        public MaxHeap(int capacity) {
            data = new int[capacity];
        }

        public int peek() {
            if (size == 0) throw new RuntimeException("Heap is empty");
            return data[0];
        }

        public void offer(int val) {
            if (size >= data.length) throw new RuntimeException("Heap is full");
            data[size] = val;
            siftUp(size++);
        }

        public int poll() {
            if (size == 0) throw new RuntimeException("Heap is empty");
            int max = data[0];
            data[0] = data[--size];
            siftDown(0);
            return max;
        }

        // Build max-heap from array (heapify) - O(n)
        public static MaxHeap heapify(int[] arr) {
            MaxHeap heap = new MaxHeap(arr.length);
            heap.data = arr.clone();
            heap.size = arr.length;
            for (int i = arr.length / 2 - 1; i >= 0; i--) heap.siftDown(i);
            return heap;
        }

        // Heap Sort using max-heap - O(n log n), in-place
        public static int[] heapSort(int[] arr) {
            int[] sorted = arr.clone();
            int n = sorted.length;
            // Build max-heap
            for (int i = n / 2 - 1; i >= 0; i--) siftDownSort(sorted, i, n);
            // Extract elements one by one
            for (int i = n - 1; i > 0; i--) {
                int tmp = sorted[0];
                sorted[0] = sorted[i];
                sorted[i] = tmp;
                siftDownSort(sorted, 0, i);
            }
            return sorted;
        }

        private static void siftDownSort(int[] arr, int i, int n) {
            while (true) {
                int largest = i;
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                if (left < n && arr[left] > arr[largest]) largest = left;
                if (right < n && arr[right] > arr[largest]) largest = right;
                if (largest == i) break;
                int tmp = arr[i]; arr[i] = arr[largest]; arr[largest] = tmp;
                i = largest;
            }
        }

        private void siftUp(int i) {
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (data[i] > data[parent]) {
                    int tmp = data[i]; data[i] = data[parent]; data[parent] = tmp;
                    i = parent;
                } else break;
            }
        }

        private void siftDown(int i) {
            while (true) {
                int largest = i;
                int left = 2 * i + 1;
                int right = 2 * i + 2;
                if (left < size && data[left] > data[largest]) largest = left;
                if (right < size && data[right] > data[largest]) largest = right;
                if (largest == i) break;
                int tmp = data[i]; data[i] = data[largest]; data[largest] = tmp;
                i = largest;
            }
        }

        public int size() { return size; }
        public boolean isEmpty() { return size == 0; }

        public void print() {
            System.out.print("MaxHeap [");
            for (int i = 0; i < size; i++) System.out.print(data[i] + (i < size - 1 ? ", " : ""));
            System.out.println("]  root=" + (size > 0 ? data[0] : "empty"));
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: Java's PriorityQueue + Kth Largest / Median Finder
    //   Demonstrates practical use of Java's built-in heap (PriorityQueue)
    // =========================================================================
    static class PriorityQueueDemo {

        // Find Kth largest element using a min-heap of size k - O(n log k)
        public static int findKthLargest(int[] nums, int k) {
            // Min-heap keeps the k largest elements; root = kth largest
            java.util.PriorityQueue<Integer> minHeap = new java.util.PriorityQueue<>();
            for (int num : nums) {
                minHeap.offer(num);
                if (minHeap.size() > k) minHeap.poll(); // remove smallest
            }
            return minHeap.peek();
        }

        // Find Kth smallest element using a max-heap of size k - O(n log k)
        public static int findKthSmallest(int[] nums, int k) {
            // Max-heap keeps the k smallest; root = kth smallest
            java.util.PriorityQueue<Integer> maxHeap =
                new java.util.PriorityQueue<>(java.util.Collections.reverseOrder());
            for (int num : nums) {
                maxHeap.offer(num);
                if (maxHeap.size() > k) maxHeap.poll();
            }
            return maxHeap.peek();
        }

        // Merge K sorted lists using a min-heap - O(n log k)
        public static int[] mergeKSortedArrays(int[][] arrays) {
            // PQ entry: [value, arrayIndex, elementIndex]
            java.util.PriorityQueue<int[]> pq =
                new java.util.PriorityQueue<>((a, b) -> a[0] - b[0]);

            int totalSize = 0;
            for (int i = 0; i < arrays.length; i++) {
                if (arrays[i].length > 0) {
                    pq.offer(new int[]{arrays[i][0], i, 0});
                    totalSize += arrays[i].length;
                }
            }

            int[] result = new int[totalSize];
            int idx = 0;
            while (!pq.isEmpty()) {
                int[] entry = pq.poll();
                result[idx++] = entry[0];
                int arrIdx = entry[1];
                int elemIdx = entry[2] + 1;
                if (elemIdx < arrays[arrIdx].length) {
                    pq.offer(new int[]{arrays[arrIdx][elemIdx], arrIdx, elemIdx});
                }
            }
            return result;
        }

        // Median Finder using two heaps - O(log n) add, O(1) findMedian
        static class MedianFinder {
            private final java.util.PriorityQueue<Integer> maxHeap; // lower half
            private final java.util.PriorityQueue<Integer> minHeap; // upper half

            public MedianFinder() {
                maxHeap = new java.util.PriorityQueue<>(java.util.Collections.reverseOrder());
                minHeap = new java.util.PriorityQueue<>();
            }

            public void addNum(int num) {
                maxHeap.offer(num);
                minHeap.offer(maxHeap.poll()); // ensure maxHeap.top <= minHeap.top
                if (maxHeap.size() < minHeap.size()) {
                    maxHeap.offer(minHeap.poll()); // balance sizes
                }
            }

            public double findMedian() {
                if (maxHeap.size() > minHeap.size()) return maxHeap.peek();
                return (maxHeap.peek() + minHeap.peek()) / 2.0;
            }
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== HEAP IMPLEMENTATIONS ===\n");

        // --- Min-Heap ---
        System.out.println("-- Implementation 1: Min-Heap --");
        MinHeap<Integer> minHeap = new MinHeap<>(10);
        int[] nums = {5, 3, 8, 1, 4, 9, 2, 7, 6};
        for (int n : nums) minHeap.offer(n);
        minHeap.print();
        System.out.print("Poll all (sorted asc): ");
        while (!minHeap.isEmpty()) System.out.print(minHeap.poll() + " ");
        System.out.println();

        // Heapify
        Integer[] arr = {5, 3, 8, 1, 4};
        MinHeap<Integer> heapified = MinHeap.heapify(arr);
        System.out.print("Heapified [5,3,8,1,4]: ");
        heapified.print();

        // --- Max-Heap ---
        System.out.println("\n-- Implementation 2: Max-Heap --");
        MaxHeap maxHeap = new MaxHeap(10);
        for (int n : nums) maxHeap.offer(n);
        maxHeap.print();
        System.out.print("Poll all (sorted desc): ");
        while (!maxHeap.isEmpty()) System.out.print(maxHeap.poll() + " ");
        System.out.println();

        int[] toSort = {5, 3, 8, 1, 4, 9, 2, 7, 6};
        int[] sorted = MaxHeap.heapSort(toSort);
        System.out.print("Heap Sort result: ");
        for (int v : sorted) System.out.print(v + " ");
        System.out.println();

        // --- PriorityQueue Demo ---
        System.out.println("\n-- Implementation 3: Java PriorityQueue Demo --");
        int[] data = {3, 2, 1, 5, 6, 4};
        System.out.println("Array: [3,2,1,5,6,4]");
        System.out.println("2nd largest: " + PriorityQueueDemo.findKthLargest(data, 2));
        System.out.println("3rd smallest: " + PriorityQueueDemo.findKthSmallest(data, 3));

        int[][] arrays = {{1, 4, 7}, {2, 5, 8}, {3, 6, 9}};
        int[] merged = PriorityQueueDemo.mergeKSortedArrays(arrays);
        System.out.print("Merge K sorted arrays: ");
        for (int v : merged) System.out.print(v + " ");
        System.out.println();

        PriorityQueueDemo.MedianFinder mf = new PriorityQueueDemo.MedianFinder();
        int[] stream = {1, 2, 3, 4, 5};
        System.out.print("Running medians: ");
        for (int n : stream) {
            mf.addNum(n);
            System.out.print(mf.findMedian() + " ");
        }
        System.out.println();
    }
}
