# Java Syntax & Data Structures Reference

A comprehensive Java reference repository covering data structures, algorithms, and language syntax — designed to quickly refresh your memory.

## 📁 Project Structure

```
src/
├── datastructures/
│   ├── linkedlist/
│   │   ├── SinglyLinkedList.java     — 3 implementations (basic, tail-pointer, iterable+merge-sort)
│   │   ├── DoublyLinkedList.java     — 3 implementations (basic, deque/palindrome, LRU cache)
│   │   └── CircularLinkedList.java   — 2 implementations (singly circular, doubly circular)
│   ├── trees/
│   │   ├── BinaryTree.java           — 3 implementations (basic traversals, array-based, serializable)
│   │   ├── BinarySearchTree.java     — 3 implementations (recursive, iterative, order-statistic)
│   │   └── AVLTree.java              — 3 implementations (recursive, ranked, rotation demo)
│   ├── heap/
│   │   └── Heap.java                 — 3 implementations (min-heap, max-heap, PriorityQueue patterns)
│   ├── stack/
│   │   └── Stack.java                — 3 implementations (array, linked-list, min-stack)
│   ├── queue/
│   │   └── Queue.java                — 3 implementations (circular array, linked-list, priority+deque)
│   ├── hashtable/
│   │   └── HashTable.java            — 3 implementations (chaining, linear probing, double hashing)
│   └── search/
│       └── SearchAlgorithms.java     — BFS (6 variants) + DFS (6 variants)
└── syntax/
    ├── BasicSyntax.java              — primitives, operators, control flow, arrays, strings, streams
    ├── OOPConcepts.java              — classes, inheritance, polymorphism, abstraction, enums, records
    └── GenericsDemo.java             — generics, wildcards, lambdas, functional interfaces, Optional
```

## 🚀 How to Run

**Practice app (new):**
```bash
mkdir -p out
javac -d out src/*.java
java -cp out Launch
```

This app gives topic-based questions, checks your output, and then shows a step-by-step optimal solution.

**Compile everything:**
```bash
mkdir -p out
javac -d out src/datastructures/linkedlist/*.java src/datastructures/trees/*.java \
  src/datastructures/heap/*.java src/datastructures/stack/*.java \
  src/datastructures/queue/*.java src/datastructures/hashtable/*.java \
  src/datastructures/search/*.java src/syntax/*.java
```

**Run any demo:**
```bash
java -cp out datastructures.linkedlist.SinglyLinkedList
java -cp out datastructures.linkedlist.DoublyLinkedList
java -cp out datastructures.linkedlist.CircularLinkedList
java -cp out datastructures.trees.BinaryTree
java -cp out datastructures.trees.BinarySearchTree
java -cp out datastructures.trees.AVLTree
java -cp out datastructures.heap.Heap
java -cp out datastructures.stack.Stack
java -cp out datastructures.queue.Queue
java -cp out datastructures.hashtable.HashTable
java -cp out datastructures.search.SearchAlgorithms
java -cp out syntax.BasicSyntax
java -cp out syntax.OOPConcepts
java -cp out syntax.GenericsDemo
```

## 📚 Data Structures Quick Reference

| Structure | File | Implementations | Key Notes |
|-----------|------|-----------------|-----------|
| Singly Linked List | `linkedlist/SinglyLinkedList.java` | Basic, Tail-pointer, Iterable | Cycle detection (Floyd's), merge sort |
| Doubly Linked List | `linkedlist/DoublyLinkedList.java` | Basic, Deque, LRU Cache | O(1) tail removal, palindrome check |
| Circular Linked List | `linkedlist/CircularLinkedList.java` | Singly Circular, Doubly Circular | Round-robin scheduling |
| Binary Tree | `trees/BinaryTree.java` | Basic, Array-based, Serializable | All 4 traversals, LCA, max path sum |
| Binary Search Tree | `trees/BinarySearchTree.java` | Recursive, Iterative, Order-Statistic | Kth smallest, floor/ceil, rank |
| AVL Tree | `trees/AVLTree.java` | Recursive, Ranked, Rotation Demo | All 4 rotations (LL/RR/LR/RL) |
| Heap | `heap/Heap.java` | Min-Heap, Max-Heap, PriorityQueue | HeapSort, Kth largest, Median Finder |
| Stack | `stack/Stack.java` | Array, LinkedList, Min-Stack | Balanced parens, RPN eval, infix→postfix |
| Queue | `queue/Queue.java` | Circular Array, LinkedList, Priority | Sliding window max, binary number gen |
| Hash Table | `hashtable/HashTable.java` | Chaining, Linear Probing, Double Hashing | Rehashing, tombstone deletion |
| BFS | `search/SearchAlgorithms.java` | Traversal, Shortest Path, Level-order, Bipartite, Components, Matrix | |
| DFS | `search/SearchAlgorithms.java` | Recursive, Iterative, Cycle Detection, Topological Sort, All Paths, SCC | Kosaraju's |

## ⏱ Complexity Cheat Sheet

| Operation | Array | Singly LL | Doubly LL | BST (avg) | AVL Tree | Hash Table |
|-----------|-------|-----------|-----------|-----------|----------|------------|
| Access    | O(1)  | O(n)      | O(n)      | O(log n)  | O(log n) | N/A        |
| Search    | O(n)  | O(n)      | O(n)      | O(log n)  | O(log n) | O(1) avg   |
| Insert    | O(n)  | O(1) head | O(1) head/tail | O(log n) | O(log n) | O(1) avg |
| Delete    | O(n)  | O(n)      | O(1) head/tail | O(log n) | O(log n) | O(1) avg |

| Sort Algorithm | Time | Space | Notes |
|----------------|------|-------|-------|
| Heap Sort | O(n log n) | O(1) | In-place, not stable |
| Merge Sort (LL) | O(n log n) | O(n) | Stable, good for linked lists |
| Arrays.sort (Java) | O(n log n) | O(log n) | Dual-pivot QuickSort for primitives |
| Collections.sort (Java) | O(n log n) | O(n) | TimSort (Merge+Insertion) for objects |
