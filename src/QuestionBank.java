import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class QuestionBank {
    private QuestionBank() {
    }

    public static Map<String, List<Question>> build() {
        Map<String, List<Question>> bank = new LinkedHashMap<>();

        bank.put("Syntax", List.of(
            new Question(
                "Syntax",
                "For Loop Sum",
                "Code:\nint sum = 0;\nfor (int i = 1; i <= 5; i++) sum += i;\nSystem.out.println(sum);\n\nWhat is the output?",
                "15",
                List.of(
                    "Initialize sum = 0.",
                    "Add 1 + 2 + 3 + 4 + 5 to sum.",
                    "Final sum is 15.",
                    "Print 15."
                ),
                "int sum = 5 * (5 + 1) / 2;\nSystem.out.println(sum);"
            ),
            new Question(
                "Syntax",
                "Switch Expression",
                "Code:\nint day = 3;\nString name = switch(day) {\n  case 1 -> \"Mon\";\n  case 2 -> \"Tue\";\n  case 3 -> \"Wed\";\n  default -> \"Other\";\n};\nSystem.out.println(name);\n\nWhat is the output?",
                "Wed",
                List.of(
                    "day = 3.",
                    "Switch selects case 3.",
                    "Result string is Wed.",
                    "Print Wed."
                ),
                "String name = switch (day) {\n    case 1 -> \"Mon\";\n    case 2 -> \"Tue\";\n    case 3 -> \"Wed\";\n    default -> \"Other\";\n};"
            ),
            new Question(
                "Syntax",
                "StringBuilder Reverse",
                "Code:\nStringBuilder sb = new StringBuilder(\"abc\");\nsb.append(\"de\");\nsb.reverse();\nSystem.out.println(sb);\n\nWhat is the output?",
                "edcba",
                List.of(
                    "Start with abc.",
                    "Append de to get abcde.",
                    "Reverse to get edcba.",
                    "Print edcba."
                ),
                "StringBuilder sb = new StringBuilder(\"abc\").append(\"de\");\nSystem.out.println(sb.reverse());"
            ),
            new Question(
                "Syntax",
                "Stream Filter Count",
                "Code:\nList<Integer> nums = Arrays.asList(1,2,3,4,5,6);\nlong c = nums.stream().filter(n -> n % 2 == 0).count();\nSystem.out.println(c);\n\nWhat is the output?",
                "3",
                List.of(
                    "Even numbers are 2, 4, 6.",
                    "Count of evens is 3.",
                    "Print 3."
                ),
                "long c = nums.stream().filter(n -> (n & 1) == 0).count();"
            )
        ));

        bank.put("Linked List", List.of(
            new Question(
                "Linked List",
                "Reverse Singly Linked List",
                "Input list: 1 -> 2 -> 3 -> 4 -> null\nAfter reversing, what should be printed (space-separated values)?",
                "4 3 2 1",
                List.of(
                    "Use 3 pointers: prev, curr, next.",
                    "Iterate through list and reverse each next pointer.",
                    "Move prev and curr forward each step.",
                    "When curr is null, prev is new head.",
                    "Traverse and print: 4 3 2 1."
                ),
                "Node prev = null, curr = head;\nwhile (curr != null) {\n    Node next = curr.next;\n    curr.next = prev;\n    prev = curr;\n    curr = next;\n}\nhead = prev;"
            ),
            new Question(
                "Linked List",
                "Find Middle Node",
                "Input list: 10 -> 20 -> 30 -> 40 -> 50 -> null\nWhat value is returned by slow/fast pointer middle-finding?",
                "30",
                List.of(
                    "Initialize slow=head and fast=head.",
                    "Move slow by 1, fast by 2.",
                    "When fast reaches end, slow is middle.",
                    "Middle value is 30."
                ),
                "Node slow = head, fast = head;\nwhile (fast != null && fast.next != null) {\n    slow = slow.next;\n    fast = fast.next.next;\n}\nreturn slow.data;"
            ),
            new Question(
                "Linked List",
                "Cycle Detection (Floyd)",
                "A list has nodes 1->2->3->4 and node 4 points back to node 2.\nWhat is the result of hasCycle()?",
                "true",
                List.of(
                    "Use slow and fast pointers.",
                    "slow moves by 1, fast moves by 2.",
                    "In a cycle, fast eventually meets slow.",
                    "Meeting occurs, so result is true."
                ),
                "while (fast != null && fast.next != null) {\n    slow = slow.next;\n    fast = fast.next.next;\n    if (slow == fast) return true;\n}\nreturn false;"
            )
        ));

        bank.put("Stack", List.of(
            new Question(
                "Stack",
                "Balanced Parentheses",
                "Input string: {[()]}\nOutput true if balanced else false.",
                "true",
                List.of(
                    "Push opening brackets onto stack.",
                    "For each closing bracket, stack top must match.",
                    "Process all characters with valid matches.",
                    "Stack ends empty, so true."
                ),
                "Map<Character, Character> m = Map.of(')', '(', '}', '{', ']', '[');\nfor (char ch : s.toCharArray()) { ... }"
            ),
            new Question(
                "Stack",
                "Evaluate RPN",
                "Tokens: 2 1 + 3 *\nWhat is the output?",
                "9",
                List.of(
                    "Push 2, push 1.",
                    "Operator + pops 1 and 2, pushes 3.",
                    "Push 3.",
                    "Operator * pops 3 and 3, pushes 9.",
                    "Final value is 9."
                ),
                "for (String t : tokens) {\n    if (isOp(t)) {\n        int b = st.pop(), a = st.pop();\n        st.push(apply(a, b, t));\n    } else st.push(Integer.parseInt(t));\n}"
            )
        ));

        bank.put("Queue", List.of(
            new Question(
                "Queue",
                "FIFO Behavior",
                "Operations: enqueue(10), enqueue(20), dequeue(), enqueue(30), peek()\nWhat does peek() output?",
                "20",
                List.of(
                    "Queue after 2 enqueues: [10, 20].",
                    "dequeue removes 10.",
                    "enqueue 30 gives [20, 30].",
                    "peek reads front without removal: 20."
                ),
                "Queue<Integer> q = new ArrayDeque<>();\nq.offer(10); q.offer(20); q.poll(); q.offer(30);\nSystem.out.println(q.peek());"
            ),
            new Question(
                "Queue",
                "Generate Binary Numbers",
                "Using queue method, first 5 binary numbers are printed.\nWhat is the output (space-separated)?",
                "1 10 11 100 101",
                List.of(
                    "Start queue with '1'.",
                    "Pop front each round, print it.",
                    "Push front+'0' and front+'1'.",
                    "First five printed values are 1 10 11 100 101."
                ),
                "Queue<String> q = new ArrayDeque<>();\nq.offer(\"1\");\nfor (int i = 0; i < 5; i++) {\n    String cur = q.poll();\n    System.out.print(cur + \" \" );\n    q.offer(cur + \"0\");\n    q.offer(cur + \"1\");\n}"
            )
        ));

        bank.put("Hash Table", List.of(
            new Question(
                "Hash Table",
                "Chaining Lookup",
                "Keys inserted: (1,\"A\"), (11,\"B\"), (21,\"C\") into table of size 10.\nAssume hash = key % 10.\nWhat is get(11)?",
                "B",
                List.of(
                    "11 % 10 = 1, so key 11 is in bucket 1.",
                    "Bucket 1 stores chain for keys 1,11,21.",
                    "Traverse chain and match key 11.",
                    "Return value B."
                ),
                "int bucket = key % capacity;\nfor (Entry e : table[bucket]) if (e.key == key) return e.value;"
            ),
            new Question(
                "Hash Table",
                "Linear Probing",
                "Table size 7. Insert keys 10, 17, 24 with hash = key % 7.\nWhich index stores 24?",
                "5",
                List.of(
                    "10 % 7 = 3, place 10 at index 3.",
                    "17 % 7 = 3, collision, probe to 4.",
                    "24 % 7 = 3, collision at 3 and 4, probe to 5.",
                    "So 24 is at index 5."
                ),
                "int i = hash(key);\nwhile (table[i] is occupied) i = (i + 1) % m;\ntable[i] = key;"
            )
        ));

        bank.put("Trees", List.of(
            new Question(
                "Trees",
                "BST Inorder",
                "Insert into BST in this order: 50, 30, 70, 20, 40, 60, 80.\nWhat is inorder traversal output (space-separated)?",
                "20 30 40 50 60 70 80",
                List.of(
                    "BST keeps smaller values left, larger values right.",
                    "Inorder traversal visits left, root, right.",
                    "Inorder of BST produces sorted order.",
                    "Output is 20 30 40 50 60 70 80."
                ),
                "void inorder(Node n) {\n    if (n == null) return;\n    inorder(n.left);\n    System.out.print(n.val + \" \" );\n    inorder(n.right);\n}"
            ),
            new Question(
                "Trees",
                "AVL Rotation",
                "Insert into AVL: 30, 20, 10.\nWhat is the root value after rebalancing?",
                "20",
                List.of(
                    "Insert 30 then 20 then 10.",
                    "Tree becomes left-left heavy at 30.",
                    "Perform right rotation on 30.",
                    "New root becomes 20."
                ),
                "if (balance > 1 && key < node.left.key) return rightRotate(node);"
            ),
            new Question(
                "Trees",
                "Binary Tree Level Order",
                "Tree:\n    1\n   / \\\n  2   3\n / \\\n4   5\n\nWhat is level-order traversal (space-separated)?",
                "1 2 3 4 5",
                List.of(
                    "Use a queue and start with root 1.",
                    "Pop 1, push children 2 and 3.",
                    "Pop 2, push children 4 and 5.",
                    "Pop 3, then 4, then 5.",
                    "Output: 1 2 3 4 5."
                ),
                "Queue<Node> q = new ArrayDeque<>();\nq.offer(root);\nwhile (!q.isEmpty()) {\n    Node cur = q.poll();\n    if (cur.left != null) q.offer(cur.left);\n    if (cur.right != null) q.offer(cur.right);\n}"
            )
        ));

        bank.put("Search (BFS/DFS)", List.of(
            new Question(
                "Search (BFS/DFS)",
                "BFS Traversal",
                "Graph adjacency:\n0: [1,2]\n1: [3]\n2: [4]\n3: []\n4: []\nStart BFS from 0.\nWhat order is visited (space-separated)?",
                "0 1 2 3 4",
                List.of(
                    "Initialize queue with start node 0.",
                    "Visit 0, enqueue 1 and 2.",
                    "Visit 1, enqueue 3.",
                    "Visit 2, enqueue 4.",
                    "Visit 3 then 4."
                ),
                "Queue<Integer> q = new ArrayDeque<>();\nboolean[] vis = new boolean[n];\nq.offer(0); vis[0] = true;\nwhile (!q.isEmpty()) { ... }"
            ),
            new Question(
                "Search (BFS/DFS)",
                "DFS Traversal",
                "Graph adjacency (same as previous), recursive DFS starting at 0 and visiting neighbors in listed order.\nWhat order is visited (space-separated)?",
                "0 1 3 2 4",
                List.of(
                    "Start at 0, mark visited.",
                    "Go to first neighbor 1, then from 1 go to 3.",
                    "Backtrack to 0 and visit next neighbor 2.",
                    "From 2 go to 4.",
                    "Visit order is 0 1 3 2 4."
                ),
                "void dfs(int u) {\n    vis[u] = true;\n    System.out.print(u + \" \" );\n    for (int v : adj[u]) if (!vis[v]) dfs(v);\n}"
            )
        ));

        return bank;
    }
}
