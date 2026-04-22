package syntax;

import java.util.*;
import java.util.stream.*;

/**
 * JAVA BASIC SYNTAX REFERENCE
 *
 * Covers:
 *   1. Primitive types and variables
 *   2. Operators
 *   3. Control flow (if/else, switch, loops)
 *   4. Arrays (1D and 2D)
 *   5. Strings and common String methods
 *   6. Type casting
 *   7. Varargs
 *   8. Java Collections overview
 *   9. Streams API (Java 8+)
 *  10. Common built-in algorithms (Arrays.sort, Collections.sort, etc.)
 */
public class BasicSyntax {

    public static void main(String[] args) {

        // =====================================================================
        // 1. PRIMITIVE TYPES
        // =====================================================================
        System.out.println("=== 1. PRIMITIVE TYPES ===");

        byte  b  = 127;                    // 8-bit  signed: -128 to 127
        short s  = 32767;                  // 16-bit signed: -32768 to 32767
        int   i  = 2_147_483_647;          // 32-bit signed (underscores for readability)
        long  l  = 9_223_372_036_854_775_807L; // 64-bit signed — note the 'L' suffix
        float f  = 3.14f;                  // 32-bit float — note the 'f' suffix
        double d = 3.141592653589793;      // 64-bit double (default for decimal literals)
        char  c  = 'A';                    // 16-bit Unicode character
        boolean flag = true;

        System.out.printf("byte: %d, short: %d, int: %d%n", b, s, i);
        System.out.printf("long: %d, float: %.2f, double: %.5f%n", l, f, d);
        System.out.printf("char: %c (int value: %d), boolean: %b%n", c, (int) c, flag);

        // Wrapper classes (autoboxing/unboxing)
        Integer boxed = 42;           // autoboxing: int → Integer
        int unboxed = boxed;          // unboxing:   Integer → int
        System.out.println("Boxed: " + boxed + ", Unboxed: " + unboxed);

        // Integer constants
        System.out.println("Max int: " + Integer.MAX_VALUE);
        System.out.println("Min int: " + Integer.MIN_VALUE);

        // =====================================================================
        // 2. OPERATORS
        // =====================================================================
        System.out.println("\n=== 2. OPERATORS ===");

        // Arithmetic
        System.out.println("7 + 3 = " + (7 + 3));
        System.out.println("7 - 3 = " + (7 - 3));
        System.out.println("7 * 3 = " + (7 * 3));
        System.out.println("7 / 3 = " + (7 / 3));   // integer division: 2
        System.out.println("7 % 3 = " + (7 % 3));   // modulo: 1
        System.out.println("7.0 / 3 = " + (7.0 / 3)); // float division: 2.333...

        // Bitwise
        System.out.println("5 & 3  = " + (5 & 3));   // AND: 0101 & 0011 = 0001 = 1
        System.out.println("5 | 3  = " + (5 | 3));   // OR:  0101 | 0011 = 0111 = 7
        System.out.println("5 ^ 3  = " + (5 ^ 3));   // XOR: 0101 ^ 0011 = 0110 = 6
        System.out.println("~5     = " + (~5));        // NOT: -6
        System.out.println("8 >> 1 = " + (8 >> 1));  // right shift: 4
        System.out.println("1 << 3 = " + (1 << 3));  // left shift:  8
        System.out.println("-8 >>> 1 = " + (-8 >>> 1)); // unsigned right shift

        // Compound assignment
        int x = 10;
        x += 5; System.out.println("x += 5 → " + x); // 15
        x -= 3; System.out.println("x -= 3 → " + x); // 12
        x *= 2; System.out.println("x *= 2 → " + x); // 24
        x /= 4; System.out.println("x /= 4 → " + x); // 6
        x %= 4; System.out.println("x %= 4 → " + x); // 2

        // Ternary
        int max = (7 > 5) ? 7 : 5;
        System.out.println("max of 7,5 = " + max);

        // =====================================================================
        // 3. CONTROL FLOW
        // =====================================================================
        System.out.println("\n=== 3. CONTROL FLOW ===");

        // if / else if / else
        int score = 85;
        if (score >= 90) {
            System.out.println("Grade: A");
        } else if (score >= 80) {
            System.out.println("Grade: B");
        } else if (score >= 70) {
            System.out.println("Grade: C");
        } else {
            System.out.println("Grade: F");
        }

        // switch (traditional)
        int day = 3;
        switch (day) {
            case 1: System.out.println("Monday"); break;
            case 2: System.out.println("Tuesday"); break;
            case 3: System.out.println("Wednesday"); break;
            default: System.out.println("Other");
        }

        // switch expression (Java 14+)
        String dayName = switch (day) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            default -> "Other";
        };
        System.out.println("Day: " + dayName);

        // for loop
        System.out.print("for loop: ");
        for (int j = 0; j < 5; j++) System.out.print(j + " ");
        System.out.println();

        // while loop
        System.out.print("while loop: ");
        int k = 0;
        while (k < 5) { System.out.print(k++ + " "); }
        System.out.println();

        // do-while loop
        System.out.print("do-while: ");
        int m = 0;
        do { System.out.print(m++ + " "); } while (m < 5);
        System.out.println();

        // enhanced for-each
        int[] arr = {10, 20, 30, 40, 50};
        System.out.print("for-each: ");
        for (int val : arr) System.out.print(val + " ");
        System.out.println();

        // break / continue
        System.out.print("break at 3: ");
        for (int j = 0; j < 5; j++) {
            if (j == 3) break;
            System.out.print(j + " ");
        }
        System.out.println();

        System.out.print("skip 3: ");
        for (int j = 0; j < 5; j++) {
            if (j == 3) continue;
            System.out.print(j + " ");
        }
        System.out.println();

        // labeled break (for nested loops)
        outer:
        for (int r = 0; r < 3; r++) {
            for (int col = 0; col < 3; col++) {
                if (r == 1 && col == 1) break outer;
                System.out.print("[" + r + "," + col + "] ");
            }
        }
        System.out.println("(broke out of nested loop)");

        // =====================================================================
        // 4. ARRAYS
        // =====================================================================
        System.out.println("\n=== 4. ARRAYS ===");

        // 1D array
        int[] nums = new int[5];           // initialized to 0
        int[] primes = {2, 3, 5, 7, 11};  // inline initialization
        System.out.println("Length: " + primes.length);
        System.out.println("Sorted check: " + Arrays.toString(primes));

        // Array operations
        int[] copy = Arrays.copyOf(primes, primes.length);
        int[] range = Arrays.copyOfRange(primes, 1, 4); // [3,5,7]
        System.out.println("copyOfRange [1,4]: " + Arrays.toString(range));

        Arrays.sort(new int[]{5, 2, 8, 1, 9, 3}); // sort in place
        int[] sorted = {1, 2, 3, 4, 5};
        int idx = Arrays.binarySearch(sorted, 3);
        System.out.println("BinarySearch for 3: index " + idx);

        Arrays.fill(nums, 7);
        System.out.println("Filled with 7: " + Arrays.toString(nums));

        // 2D array
        int[][] matrix = new int[3][4]; // 3 rows, 4 cols
        int[][] grid = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        System.out.println("2D [1][2] = " + grid[1][2]);  // 6

        System.out.println("2D array:");
        for (int[] row : grid) System.out.println("  " + Arrays.toString(row));

        // Jagged array
        int[][] jagged = new int[3][];
        jagged[0] = new int[]{1};
        jagged[1] = new int[]{2, 3};
        jagged[2] = new int[]{4, 5, 6};
        System.out.println("Jagged array row 2: " + Arrays.toString(jagged[2]));

        // =====================================================================
        // 5. STRINGS
        // =====================================================================
        System.out.println("\n=== 5. STRINGS ===");

        String str = "Hello, World!";
        System.out.println("length:       " + str.length());
        System.out.println("charAt(7):    " + str.charAt(7));
        System.out.println("indexOf('o'): " + str.indexOf('o'));
        System.out.println("substring(7): " + str.substring(7));
        System.out.println("substring(0,5): " + str.substring(0, 5));
        System.out.println("toLowerCase:  " + str.toLowerCase());
        System.out.println("toUpperCase:  " + str.toUpperCase());
        System.out.println("replace:      " + str.replace("World", "Java"));
        System.out.println("contains:     " + str.contains("World"));
        System.out.println("startsWith:   " + str.startsWith("Hello"));
        System.out.println("endsWith:     " + str.endsWith("!"));
        System.out.println("trim:         '" + "  spaces  ".trim() + "'");
        System.out.println("strip:        '" + "  spaces  ".strip() + "'"); // Java 11+
        System.out.println("equals:       " + "abc".equals("abc"));
        System.out.println("equalsIgnoreCase: " + "ABC".equalsIgnoreCase("abc"));
        System.out.println("compareTo:    " + "apple".compareTo("banana")); // negative

        // Split and join
        String csv = "a,b,c,d";
        String[] parts = csv.split(",");
        System.out.println("Split: " + Arrays.toString(parts));
        System.out.println("Join:  " + String.join("-", parts));

        // String to char array and back
        char[] chars = str.toCharArray();
        System.out.println("First char: " + chars[0]);
        String fromChars = new String(chars);
        System.out.println("Back to string: " + fromChars.substring(0, 5));

        // String.format / printf
        String formatted = String.format("Name: %-10s Age: %3d", "Alice", 30);
        System.out.println("Formatted: " + formatted);

        // StringBuilder (mutable, faster for concatenation)
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < 5; j++) sb.append(j).append(", ");
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        System.out.println("StringBuilder: " + sb);
        sb.reverse();
        System.out.println("Reversed: " + sb);
        System.out.println("Insert: " + sb.insert(0, ">>").toString());

        // String comparison pitfall
        String s1 = new String("hello");
        String s2 = new String("hello");
        System.out.println("== (reference): " + (s1 == s2));       // false (different objects)
        System.out.println(".equals(): " + s1.equals(s2));          // true  (content equal)

        // =====================================================================
        // 6. TYPE CASTING
        // =====================================================================
        System.out.println("\n=== 6. TYPE CASTING ===");

        // Implicit (widening) — safe, no data loss
        int intVal = 100;
        long longVal = intVal;   // int → long
        double dVal = intVal;    // int → double
        System.out.println("int→long: " + longVal + ", int→double: " + dVal);

        // Explicit (narrowing) — potential data loss
        double pi = 3.99;
        int truncated = (int) pi;    // 3 (truncates, no rounding)
        System.out.println("double→int (3.99): " + truncated);

        long bigLong = 1234567890123L;
        int fromLong = (int) bigLong;  // overflow!
        System.out.println("long→int (overflow): " + fromLong);

        // char ↔ int
        char ch = 'Z';
        int ascii = ch;          // implicit: char → int
        System.out.println("'Z' as int: " + ascii);

        char fromInt = (char) 65;
        System.out.println("65 as char: " + fromInt);  // 'A'

        // String ↔ primitive conversions
        int parsed = Integer.parseInt("42");
        double parsedD = Double.parseDouble("3.14");
        String strFromInt = Integer.toString(42);
        String strFromDouble = String.valueOf(3.14);
        System.out.println("parseInt: " + parsed + ", parseDouble: " + parsedD);
        System.out.println("toString: " + strFromInt + ", valueOf: " + strFromDouble);

        // =====================================================================
        // 7. VARARGS
        // =====================================================================
        System.out.println("\n=== 7. VARARGS ===");
        System.out.println("sum(): " + sum());
        System.out.println("sum(1): " + sum(1));
        System.out.println("sum(1,2,3): " + sum(1, 2, 3));
        System.out.println("sum(1..5): " + sum(1, 2, 3, 4, 5));

        // =====================================================================
        // 8. COLLECTIONS OVERVIEW
        // =====================================================================
        System.out.println("\n=== 8. COLLECTIONS OVERVIEW ===");

        // ArrayList
        List<String> list = new ArrayList<>(Arrays.asList("banana", "apple", "cherry"));
        list.add("date");
        list.remove("banana");
        Collections.sort(list);
        System.out.println("ArrayList sorted: " + list);

        // LinkedList (also a Deque)
        Deque<Integer> deque = new LinkedList<>();
        deque.offerFirst(1); deque.offerLast(2); deque.offerFirst(0);
        System.out.println("LinkedList deque: " + deque);

        // HashSet (unordered, unique)
        Set<String> set = new HashSet<>(Arrays.asList("a", "b", "c", "a", "b"));
        System.out.println("HashSet (no dups): " + set);

        // TreeSet (sorted, unique)
        Set<Integer> treeSet = new TreeSet<>(Arrays.asList(5, 2, 8, 1, 9, 3));
        System.out.println("TreeSet (sorted): " + treeSet);

        // HashMap (key→value, unordered)
        Map<String, Integer> map = new HashMap<>();
        map.put("one", 1); map.put("two", 2); map.put("three", 3);
        map.putIfAbsent("four", 4);
        System.out.println("HashMap: " + map);
        System.out.println("getOrDefault: " + map.getOrDefault("five", 0));
        map.forEach((key, val) -> System.out.print(key + "=" + val + " "));
        System.out.println();

        // TreeMap (sorted by key)
        Map<String, Integer> treeMap = new TreeMap<>(map);
        System.out.println("TreeMap (sorted keys): " + treeMap);

        // PriorityQueue (min-heap)
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.addAll(Arrays.asList(5, 2, 8, 1, 9));
        System.out.print("PriorityQueue poll order: ");
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");
        System.out.println();

        // Stack (use Deque instead in practice)
        java.util.Stack<String> stack = new java.util.Stack<>();
        stack.push("A"); stack.push("B"); stack.push("C");
        System.out.println("Stack pop: " + stack.pop()); // C

        // =====================================================================
        // 9. STREAMS API (Java 8+)
        // =====================================================================
        System.out.println("\n=== 9. STREAMS API ===");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // filter + map + collect
        List<Integer> evenSquares = numbers.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * n)
            .collect(Collectors.toList());
        System.out.println("Even squares: " + evenSquares);

        // reduce
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        // count, min, max
        long count = numbers.stream().filter(n -> n > 5).count();
        int streamMax = numbers.stream().mapToInt(Integer::intValue).max().orElse(0);
        System.out.println("Count > 5: " + count + ", Max: " + streamMax);

        // sorted, distinct, limit, skip
        List<Integer> processed = Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3)
            .stream()
            .distinct()
            .sorted()
            .limit(5)
            .collect(Collectors.toList());
        System.out.println("Distinct, sorted, limit 5: " + processed);

        // String stream
        String joined = Arrays.asList("hello", "world", "java")
            .stream()
            .map(String::toUpperCase)
            .collect(Collectors.joining(", "));
        System.out.println("Joined uppercase: " + joined);

        // groupingBy
        Map<Integer, List<String>> byLength = Arrays.asList("a", "bb", "cc", "ddd", "ee")
            .stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by length: " + byLength);

        // =====================================================================
        // 10. COMMON BUILT-IN ALGORITHMS
        // =====================================================================
        System.out.println("\n=== 10. COMMON BUILT-IN ALGORITHMS ===");

        // Arrays.sort — O(n log n) dual-pivot quicksort
        int[] toSort = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        Arrays.sort(toSort);
        System.out.println("Arrays.sort: " + Arrays.toString(toSort));

        // Arrays.sort with comparator (only for Object arrays)
        Integer[] objArr = {5, 2, 8, 1, 9, 3};
        Arrays.sort(objArr, Comparator.reverseOrder());
        System.out.println("Arrays.sort descending: " + Arrays.toString(objArr));

        // Collections.sort — O(n log n) TimSort
        List<Integer> sortList = new ArrayList<>(Arrays.asList(5, 2, 8, 1, 9, 3));
        Collections.sort(sortList);
        System.out.println("Collections.sort: " + sortList);

        // Collections utilities
        System.out.println("min: " + Collections.min(sortList));
        System.out.println("max: " + Collections.max(sortList));
        Collections.reverse(sortList);
        System.out.println("reverse: " + sortList);
        Collections.shuffle(sortList);
        System.out.println("shuffle: " + sortList); // random order

        // Math utilities
        System.out.println("\nMath.abs(-5): " + Math.abs(-5));
        System.out.println("Math.pow(2,10): " + (int)Math.pow(2, 10));
        System.out.println("Math.sqrt(144): " + Math.sqrt(144));
        System.out.println("Math.ceil(3.2): " + Math.ceil(3.2));
        System.out.println("Math.floor(3.9): " + Math.floor(3.9));
        System.out.println("Math.round(3.5): " + Math.round(3.5));
        System.out.println("Math.max(7,3): " + Math.max(7, 3));
        System.out.println("Math.log(Math.E): " + Math.log(Math.E));
        System.out.println("Math.random(): " + String.format("%.4f", Math.random()));
    }

    // Varargs method — accepts 0 or more ints
    static int sum(int... nums) {
        int total = 0;
        for (int n : nums) total += n;
        return total;
    }
}
