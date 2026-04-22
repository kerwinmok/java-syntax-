package datastructures.stack;

/**
 * STACK - Three Implementations
 *
 * A stack is a Last-In-First-Out (LIFO) data structure.
 * The element added last is the first one to be removed.
 *
 * Core operations:
 *   - push(item): add to top   — O(1)
 *   - pop():      remove top   — O(1)
 *   - peek():     view top     — O(1)
 *   - isEmpty():  check empty  — O(1)
 *
 * Common use cases:
 *   - Undo/redo functionality
 *   - Function call stack (recursion)
 *   - Expression evaluation / parenthesis matching
 *   - Browser back button
 *   - DFS traversal
 */
public class Stack {

    // =========================================================================
    // IMPLEMENTATION 1: Stack backed by a dynamic array (ArrayList)
    // =========================================================================
    static class ArrayStack<T> {
        private final java.util.ArrayList<T> data = new java.util.ArrayList<>();

        // O(1) amortized
        public void push(T item) {
            data.add(item);
        }

        // O(1)
        public T pop() {
            if (isEmpty()) throw new java.util.EmptyStackException();
            return data.remove(data.size() - 1);
        }

        // O(1)
        public T peek() {
            if (isEmpty()) throw new java.util.EmptyStackException();
            return data.get(data.size() - 1);
        }

        public boolean isEmpty() { return data.isEmpty(); }
        public int size() { return data.size(); }

        // Practical application: Check balanced parentheses
        public static boolean isBalanced(String s) {
            ArrayStack<Character> stack = new ArrayStack<>();
            for (char c : s.toCharArray()) {
                if (c == '(' || c == '[' || c == '{') {
                    stack.push(c);
                } else if (c == ')' || c == ']' || c == '}') {
                    if (stack.isEmpty()) return false;
                    char top = stack.pop();
                    if ((c == ')' && top != '(')
                     || (c == ']' && top != '[')
                     || (c == '}' && top != '{')) return false;
                }
            }
            return stack.isEmpty();
        }

        // Practical application: Evaluate postfix (Reverse Polish Notation)
        public static int evalRPN(String[] tokens) {
            ArrayStack<Integer> stack = new ArrayStack<>();
            for (String token : tokens) {
                switch (token) {
                    case "+": stack.push(stack.pop() + stack.pop()); break;
                    case "-": { int b = stack.pop(), a = stack.pop(); stack.push(a - b); break; }
                    case "*": stack.push(stack.pop() * stack.pop()); break;
                    case "/": { int b = stack.pop(), a = stack.pop(); stack.push(a / b); break; }
                    default:  stack.push(Integer.parseInt(token));
                }
            }
            return stack.pop();
        }

        @Override
        public String toString() { return data.toString(); }
    }

    // =========================================================================
    // IMPLEMENTATION 2: Stack backed by a Singly Linked List (no array resizing)
    // =========================================================================
    static class LinkedListStack<T> {
        private Node<T> top;
        private int size;

        private static class Node<T> {
            T data;
            Node<T> next;
            Node(T data, Node<T> next) { this.data = data; this.next = next; }
        }

        // O(1) — insert at head
        public void push(T item) {
            top = new Node<>(item, top);
            size++;
        }

        // O(1)
        public T pop() {
            if (isEmpty()) throw new java.util.EmptyStackException();
            T data = top.data;
            top = top.next;
            size--;
            return data;
        }

        // O(1)
        public T peek() {
            if (isEmpty()) throw new java.util.EmptyStackException();
            return top.data;
        }

        public boolean isEmpty() { return top == null; }
        public int size() { return size; }

        // Practical: Reverse a string using stack
        public static String reverse(String s) {
            LinkedListStack<Character> stack = new LinkedListStack<>();
            for (char c : s.toCharArray()) stack.push(c);
            StringBuilder sb = new StringBuilder();
            while (!stack.isEmpty()) sb.append(stack.pop());
            return sb.toString();
        }

        // Practical: Infix to Postfix conversion
        public static String infixToPostfix(String infix) {
            LinkedListStack<Character> stack = new LinkedListStack<>();
            StringBuilder result = new StringBuilder();

            for (char c : infix.toCharArray()) {
                if (c == ' ') continue;
                if (Character.isLetterOrDigit(c)) {
                    result.append(c);
                } else if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') result.append(stack.pop());
                    if (!stack.isEmpty()) stack.pop(); // discard '('
                } else {
                    // Operator: pop while higher or equal precedence
                    while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                        result.append(stack.pop());
                    }
                    stack.push(c);
                }
            }
            while (!stack.isEmpty()) result.append(stack.pop());
            return result.toString();
        }

        private static int precedence(char op) {
            if (op == '+' || op == '-') return 1;
            if (op == '*' || op == '/') return 2;
            if (op == '^') return 3;
            return 0;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            Node<T> current = top;
            while (current != null) {
                sb.append(current.data);
                if (current.next != null) sb.append(", ");
                current = current.next;
            }
            return sb.append("]  (top=").append(top != null ? top.data : "empty").append(")").toString();
        }
    }

    // =========================================================================
    // IMPLEMENTATION 3: Min-Stack — O(1) push, pop, peek, AND getMin
    //   Trick: use a second stack to track the current minimum at each level
    // =========================================================================
    static class MinStack {
        private final LinkedListStack<Integer> stack = new LinkedListStack<>();
        private final LinkedListStack<Integer> minStack = new LinkedListStack<>();

        public void push(int val) {
            stack.push(val);
            // Push min: either val or current min (whichever is smaller)
            if (minStack.isEmpty() || val <= minStack.peek()) {
                minStack.push(val);
            }
        }

        public int pop() {
            int val = stack.pop();
            if (val == minStack.peek()) minStack.pop();
            return val;
        }

        public int peek() { return stack.peek(); }
        public int getMin() { return minStack.peek(); }
        public boolean isEmpty() { return stack.isEmpty(); }

        // Bonus: sort a stack using only one extra stack — O(n²)
        public static LinkedListStack<Integer> sortStack(LinkedListStack<Integer> input) {
            LinkedListStack<Integer> sorted = new LinkedListStack<>();
            while (!input.isEmpty()) {
                int tmp = input.pop();
                while (!sorted.isEmpty() && sorted.peek() > tmp) {
                    input.push(sorted.pop());
                }
                sorted.push(tmp);
            }
            return sorted;
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== STACK IMPLEMENTATIONS ===\n");

        // --- Implementation 1: Array Stack ---
        System.out.println("-- Implementation 1: Array Stack --");
        ArrayStack<Integer> stack1 = new ArrayStack<>();
        stack1.push(1);
        stack1.push(2);
        stack1.push(3);
        System.out.println("Stack: " + stack1);
        System.out.println("Peek: " + stack1.peek());
        System.out.println("Pop: " + stack1.pop());
        System.out.println("Stack after pop: " + stack1);

        System.out.println("\n[Balanced Parentheses]");
        System.out.println("'({[]})' balanced? " + ArrayStack.isBalanced("({[]})"));  // true
        System.out.println("'([)]'   balanced? " + ArrayStack.isBalanced("([)]"));    // false
        System.out.println("'{[}'    balanced? " + ArrayStack.isBalanced("{[}"));     // false

        System.out.println("\n[Evaluate RPN]");
        // "2 1 + 3 *" = (2+1)*3 = 9
        System.out.println("\"2 1 + 3 *\" = " + ArrayStack.evalRPN(new String[]{"2","1","+","3","*"}));
        // "4 13 5 / +" = 4 + (13/5) = 6
        System.out.println("\"4 13 5 / +\" = " + ArrayStack.evalRPN(new String[]{"4","13","5","/","+"}));

        // --- Implementation 2: Linked List Stack ---
        System.out.println("\n-- Implementation 2: Linked List Stack --");
        LinkedListStack<String> stack2 = new LinkedListStack<>();
        stack2.push("A");
        stack2.push("B");
        stack2.push("C");
        System.out.println("Stack: " + stack2);
        System.out.println("Reversed 'Hello': " + LinkedListStack.reverse("Hello"));
        System.out.println("Infix 'a+b*(c-d)' → Postfix: " + LinkedListStack.infixToPostfix("a+b*(c-d)"));
        System.out.println("Infix '(a+b)*(c-d)' → Postfix: " + LinkedListStack.infixToPostfix("(a+b)*(c-d)"));

        // --- Implementation 3: Min-Stack ---
        System.out.println("\n-- Implementation 3: Min-Stack (O(1) getMin) --");
        MinStack minStack = new MinStack();
        int[] vals = {5, 3, 7, 2, 8, 1};
        for (int v : vals) {
            minStack.push(v);
            System.out.println("push(" + v + ") → min=" + minStack.getMin());
        }
        System.out.println("pop() = " + minStack.pop() + " → min=" + minStack.getMin());
        System.out.println("pop() = " + minStack.pop() + " → min=" + minStack.getMin());

        System.out.println("\n[Sort a stack]");
        LinkedListStack<Integer> unsorted = new LinkedListStack<>();
        for (int v : new int[]{3, 1, 4, 1, 5, 9, 2, 6}) unsorted.push(v);
        System.out.println("Unsorted: " + unsorted);
        LinkedListStack<Integer> sortedStack = MinStack.sortStack(unsorted);
        System.out.println("Sorted (top=min): " + sortedStack);
    }
}
