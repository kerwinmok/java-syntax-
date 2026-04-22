package datastructures.hashtable;

/**
 * HASH TABLE - Three Implementations
 *
 * A hash table maps keys to values using a hash function.
 * It provides average O(1) for get/put/delete operations.
 *
 * Two main collision resolution strategies:
 *   1. Separate Chaining   — each bucket holds a linked list of entries
 *   2. Open Addressing     — probe for next available slot
 *      a. Linear Probing   — probe i+1, i+2, i+3, ...
 *      b. Quadratic Probing — probe i+1², i+2², i+3², ...
 *      c. Double Hashing   — use second hash function as step size
 *
 * Load Factor (α) = n / m  (n = entries, m = buckets)
 *   - Keep α < 0.75 for good performance (Java's HashMap default)
 *   - Separate chaining: degrades to O(α) per operation
 *   - Open addressing: must stay below 1.0; rehash when α > 0.5–0.75
 */
public class HashTable {

    // =========================================================================
    // IMPLEMENTATION 1: Separate Chaining Hash Table
    //   Each bucket is a linked list. Simple and handles high load well.
    // =========================================================================
    static class ChainingHashTable<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double LOAD_FACTOR_THRESHOLD = 0.75;

        private Object[] buckets;
        private int size;
        private int capacity;

        @SuppressWarnings("unchecked")
        private static class Entry<K, V> {
            K key;
            V value;
            Entry<K, V> next;

            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }

        public ChainingHashTable() {
            this(DEFAULT_CAPACITY);
        }

        @SuppressWarnings("unchecked")
        public ChainingHashTable(int capacity) {
            this.capacity = capacity;
            this.buckets = new Object[capacity];
        }

        private int hash(K key) {
            // Handle negative hash codes with bitwise AND
            return (key.hashCode() & 0x7FFFFFFF) % capacity;
        }

        // O(1) average, O(n) worst case
        @SuppressWarnings("unchecked")
        public void put(K key, V value) {
            int idx = hash(key);
            Entry<K, V> entry = (Entry<K, V>) buckets[idx];
            // Update if key exists
            while (entry != null) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                    return;
                }
                entry = entry.next;
            }
            // Insert at head of chain
            Entry<K, V> newEntry = new Entry<>(key, value);
            newEntry.next = (Entry<K, V>) buckets[idx];
            buckets[idx] = newEntry;
            size++;

            if ((double) size / capacity >= LOAD_FACTOR_THRESHOLD) {
                rehash();
            }
        }

        // O(1) average
        @SuppressWarnings("unchecked")
        public V get(K key) {
            int idx = hash(key);
            Entry<K, V> entry = (Entry<K, V>) buckets[idx];
            while (entry != null) {
                if (entry.key.equals(key)) return entry.value;
                entry = entry.next;
            }
            return null;
        }

        // O(1) average
        @SuppressWarnings("unchecked")
        public boolean remove(K key) {
            int idx = hash(key);
            Entry<K, V> entry = (Entry<K, V>) buckets[idx];
            Entry<K, V> prev = null;
            while (entry != null) {
                if (entry.key.equals(key)) {
                    if (prev == null) buckets[idx] = entry.next;
                    else prev.next = entry.next;
                    size--;
                    return true;
                }
                prev = entry;
                entry = entry.next;
            }
            return false;
        }

        public boolean containsKey(K key) { return get(key) != null; }

        // O(n) — rehash when load factor exceeded
        @SuppressWarnings("unchecked")
        private void rehash() {
            Object[] oldBuckets = buckets;
            capacity *= 2;
            buckets = new Object[capacity];
            size = 0;
            for (Object bucket : oldBuckets) {
                Entry<K, V> entry = (Entry<K, V>) bucket;
                while (entry != null) {
                    put(entry.key, entry.value);
                    entry = entry.next;
                }
            }
            System.out.println("  [Rehashed to capacity " + capacity + "]");
        }

        public int size() { return size; }

        @SuppressWarnings("unchecked")
        public void print() {
            for (int i = 0; i < capacity; i++) {
                Entry<K, V> entry = (Entry<K, V>) buckets[i];
                if (entry != null) {
                    System.out.print("  [" + i + "]: ");
                    while (entry != null) {
                        System.out.print("{" + entry.key + "=" + entry.value + "} -> ");
                        entry = entry.next;
                    }
                    System.out.println("null");
                }
            }
        }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Linear Probing Hash Table (Open Addressing)
    //   When a collision occurs, scan forward one slot at a time.
    //   Simple but can suffer from primary clustering.
    // =========================================================================
    static class LinearProbingHashTable<K, V> {
        private static final int DEFAULT_CAPACITY = 11; // prime number reduces clustering
        private static final double MAX_LOAD = 0.5;

        private Object[] keys;
        private Object[] values;
        private boolean[] deleted; // tombstone for lazy deletion
        private int size;
        private int capacity;

        public LinearProbingHashTable() {
            this(DEFAULT_CAPACITY);
        }

        public LinearProbingHashTable(int capacity) {
            this.capacity = capacity;
            keys = new Object[capacity];
            values = new Object[capacity];
            deleted = new boolean[capacity];
        }

        private int hash(K key) {
            return (key.hashCode() & 0x7FFFFFFF) % capacity;
        }

        // O(1) average
        @SuppressWarnings("unchecked")
        public void put(K key, V value) {
            if ((double) size / capacity >= MAX_LOAD) rehash();

            int idx = hash(key);
            while (keys[idx] != null && !deleted[idx]) {
                if (((K) keys[idx]).equals(key)) {
                    values[idx] = value; // update
                    return;
                }
                idx = (idx + 1) % capacity; // linear probe
            }
            keys[idx] = key;
            values[idx] = value;
            deleted[idx] = false;
            size++;
        }

        // O(1) average
        @SuppressWarnings("unchecked")
        public V get(K key) {
            int idx = hash(key);
            while (keys[idx] != null) {
                if (!deleted[idx] && ((K) keys[idx]).equals(key)) {
                    return (V) values[idx];
                }
                idx = (idx + 1) % capacity;
            }
            return null;
        }

        // Lazy deletion — mark as deleted (tombstone)
        @SuppressWarnings("unchecked")
        public boolean remove(K key) {
            int idx = hash(key);
            while (keys[idx] != null) {
                if (!deleted[idx] && ((K) keys[idx]).equals(key)) {
                    deleted[idx] = true;
                    size--;
                    return true;
                }
                idx = (idx + 1) % capacity;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        private void rehash() {
            Object[] oldKeys = keys;
            Object[] oldValues = values;
            boolean[] oldDeleted = deleted;
            capacity = nextPrime(capacity * 2);
            keys = new Object[capacity];
            values = new Object[capacity];
            deleted = new boolean[capacity];
            size = 0;
            for (int i = 0; i < oldKeys.length; i++) {
                if (oldKeys[i] != null && !oldDeleted[i]) {
                    put((K) oldKeys[i], (V) oldValues[i]);
                }
            }
            System.out.println("  [Rehashed to capacity " + capacity + "]");
        }

        private int nextPrime(int n) {
            while (!isPrime(n)) n++;
            return n;
        }

        private boolean isPrime(int n) {
            if (n < 2) return false;
            for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
            return true;
        }

        public int size() { return size; }

        @SuppressWarnings("unchecked")
        public void print() {
            for (int i = 0; i < capacity; i++) {
                String status = deleted[i] ? "[DELETED]" : (keys[i] == null ? "[empty]" : "{" + keys[i] + "=" + values[i] + "}");
                System.out.println("  [" + i + "]: " + status);
            }
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: Double Hashing Hash Table (Open Addressing)
    //   Uses two hash functions to compute the step size.
    //   Eliminates both primary and secondary clustering.
    //   step = h2(key) = prime - (key % prime)
    // =========================================================================
    static class DoubleHashingTable {
        private static final int DEFAULT_CAPACITY = 11;
        private static final int SECONDARY_PRIME = 7; // must be < capacity and prime

        private Integer[] keys;
        private Integer[] values;
        private boolean[] deleted;
        private int size;
        private int capacity;

        public DoubleHashingTable() {
            this(DEFAULT_CAPACITY);
        }

        public DoubleHashingTable(int capacity) {
            this.capacity = capacity;
            keys = new Integer[capacity];
            values = new Integer[capacity];
            deleted = new boolean[capacity];
        }

        private int h1(int key) { return key % capacity; }
        private int h2(int key) { return SECONDARY_PRIME - (key % SECONDARY_PRIME); }

        public void put(int key, int value) {
            if ((double) size / capacity >= 0.5) rehash();

            int idx = h1(key);
            int step = h2(key);
            while (keys[idx] != null && !deleted[idx]) {
                if (keys[idx] == key) { values[idx] = value; return; }
                idx = (idx + step) % capacity; // double hash probe
            }
            keys[idx] = key;
            values[idx] = value;
            deleted[idx] = false;
            size++;
        }

        public Integer get(int key) {
            int idx = h1(key);
            int step = h2(key);
            while (keys[idx] != null) {
                if (!deleted[idx] && keys[idx] == key) return values[idx];
                idx = (idx + step) % capacity;
            }
            return null;
        }

        public boolean remove(int key) {
            int idx = h1(key);
            int step = h2(key);
            while (keys[idx] != null) {
                if (!deleted[idx] && keys[idx] == key) {
                    deleted[idx] = true;
                    size--;
                    return true;
                }
                idx = (idx + step) % capacity;
            }
            return false;
        }

        private void rehash() {
            Integer[] oldKeys = keys;
            Integer[] oldValues = values;
            boolean[] oldDeleted = deleted;
            capacity = nextPrime(capacity * 2);
            keys = new Integer[capacity];
            values = new Integer[capacity];
            deleted = new boolean[capacity];
            size = 0;
            for (int i = 0; i < oldKeys.length; i++) {
                if (oldKeys[i] != null && !oldDeleted[i]) put(oldKeys[i], oldValues[i]);
            }
            System.out.println("  [Rehashed to capacity " + capacity + "]");
        }

        private int nextPrime(int n) {
            while (!isPrime(n)) n++;
            return n;
        }

        private boolean isPrime(int n) {
            if (n < 2) return false;
            for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
            return true;
        }

        public void print() {
            for (int i = 0; i < capacity; i++) {
                String status = deleted[i] ? "[DELETED]" : (keys[i] == null ? "[empty]" : "{" + keys[i] + "=" + values[i] + "}");
                System.out.println("  [" + i + "]: " + status);
            }
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== HASH TABLE IMPLEMENTATIONS ===\n");

        // --- Implementation 1: Separate Chaining ---
        System.out.println("-- Implementation 1: Separate Chaining Hash Table --");
        ChainingHashTable<String, Integer> ht1 = new ChainingHashTable<>(4); // small to show chaining
        ht1.put("apple", 1);
        ht1.put("banana", 2);
        ht1.put("cherry", 3);
        ht1.put("date", 4);
        ht1.put("elderberry", 5);
        System.out.println("Buckets after 5 inserts:");
        ht1.print();
        System.out.println("get('cherry'): " + ht1.get("cherry"));
        System.out.println("get('grape'):  " + ht1.get("grape"));
        ht1.remove("banana");
        System.out.println("After remove('banana'), get: " + ht1.get("banana"));

        // --- Implementation 2: Linear Probing ---
        System.out.println("\n-- Implementation 2: Linear Probing Hash Table --");
        LinearProbingHashTable<String, Integer> ht2 = new LinearProbingHashTable<>(7);
        String[] words = {"cat", "dog", "bird", "fish", "frog"};
        for (int i = 0; i < words.length; i++) ht2.put(words[i], i + 1);
        System.out.println("Table after 5 inserts:");
        ht2.print();
        System.out.println("get('bird'): " + ht2.get("bird"));
        ht2.remove("bird");
        System.out.println("After remove('bird'):");
        ht2.print();
        System.out.println("get('bird') after remove: " + ht2.get("bird"));

        // --- Implementation 3: Double Hashing ---
        System.out.println("\n-- Implementation 3: Double Hashing Hash Table --");
        DoubleHashingTable ht3 = new DoubleHashingTable(11);
        int[] keys = {20, 34, 45, 70, 56, 16, 27, 38};
        for (int i = 0; i < keys.length; i++) ht3.put(keys[i], keys[i] * 10);
        System.out.println("Table after inserts:");
        ht3.print();
        System.out.println("get(45): " + ht3.get(45));
        System.out.println("get(99): " + ht3.get(99));
        ht3.remove(45);
        System.out.println("get(45) after remove: " + ht3.get(45));
    }
}
