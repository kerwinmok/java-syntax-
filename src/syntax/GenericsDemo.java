package syntax;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * GENERICS AND FUNCTIONAL PROGRAMMING IN JAVA - Reference
 *
 * Covers:
 *   1. Generic classes
 *   2. Generic methods
 *   3. Bounded type parameters (extends / super)
 *   4. Wildcards (?, ? extends T, ? super T)
 *   5. Generic interfaces
 *   6. Type erasure
 *   7. Lambda expressions
 *   8. Method references
 *   9. Built-in functional interfaces (Function, Predicate, Consumer, Supplier, etc.)
 *  10. Optional
 *  11. Comparable vs Comparator
 *  12. Exception handling
 */
public class GenericsDemo {

    // =========================================================================
    // 1. GENERIC CLASS
    // =========================================================================

    // A generic pair that can hold any two types
    static class Pair<A, B> {
        private final A first;
        private final B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() { return first; }
        public B getSecond() { return second; }

        // Swap returns a new Pair with reversed types
        public Pair<B, A> swap() { return new Pair<>(second, first); }

        @Override
        public String toString() { return "(" + first + ", " + second + ")"; }
    }

    // Generic stack
    static class GenericStack<T> {
        private final List<T> data = new ArrayList<>();

        public void push(T item) { data.add(item); }

        public T pop() {
            if (data.isEmpty()) throw new EmptyStackException();
            return data.remove(data.size() - 1);
        }

        public T peek() {
            if (data.isEmpty()) throw new EmptyStackException();
            return data.get(data.size() - 1);
        }

        public boolean isEmpty() { return data.isEmpty(); }
        public int size() { return data.size(); }

        @Override
        public String toString() { return data.toString(); }
    }

    // =========================================================================
    // 2. GENERIC METHODS
    // =========================================================================

    // Swap two elements in an array
    static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Find the maximum element
    static <T extends Comparable<T>> T max(T[] arr) {
        if (arr == null || arr.length == 0) throw new IllegalArgumentException();
        T max = arr[0];
        for (T item : arr) if (item.compareTo(max) > 0) max = item;
        return max;
    }

    // =========================================================================
    // 3. BOUNDED TYPE PARAMETERS
    // =========================================================================

    // Upper bound: T must extend Number
    static <T extends Number> double sum(List<T> list) {
        return list.stream().mapToDouble(Number::doubleValue).sum();
    }

    // Multiple bounds: T must be Comparable AND Serializable
    static <T extends Comparable<T> & java.io.Serializable> T clamp(T val, T min, T max) {
        if (val.compareTo(min) < 0) return min;
        if (val.compareTo(max) > 0) return max;
        return val;
    }

    // =========================================================================
    // 4. WILDCARDS
    //    - ? (unknown): read-only, widest
    //    - ? extends T (upper bounded): read from collection of T or its subtypes
    //    - ? super T (lower bounded): write into collection of T or its supertypes
    //    PECS: Producer Extends, Consumer Super
    // =========================================================================

    static double sumWildcard(List<? extends Number> list) {
        double total = 0;
        for (Number n : list) total += n.doubleValue();
        return total;
    }

    static void addNumbers(List<? super Integer> list, int count) {
        for (int i = 0; i < count; i++) list.add(i);
    }

    static void printList(List<?> list) { // unbounded: works with any type
        list.forEach(item -> System.out.print(item + " "));
        System.out.println();
    }

    // =========================================================================
    // 5. GENERIC INTERFACE + IMPLEMENTATIONS
    // =========================================================================

    interface Repository<T, ID> {
        void save(T entity);
        Optional<T> findById(ID id);
        List<T> findAll();
        void delete(ID id);
    }

    static class User {
        int id;
        String name;
        User(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return "User{id=" + id + ", name='" + name + "'}"; }
    }

    static class UserRepository implements Repository<User, Integer> {
        private final Map<Integer, User> store = new HashMap<>();

        @Override public void save(User user) { store.put(user.id, user); }
        @Override public Optional<User> findById(Integer id) { return Optional.ofNullable(store.get(id)); }
        @Override public List<User> findAll() { return new ArrayList<>(store.values()); }
        @Override public void delete(Integer id) { store.remove(id); }
    }

    // =========================================================================
    // 6. COMPARABLE vs COMPARATOR
    // =========================================================================

    static class Student implements Comparable<Student> {
        String name;
        double gpa;

        Student(String name, double gpa) { this.name = name; this.gpa = gpa; }

        // Natural ordering: by name alphabetically
        @Override
        public int compareTo(Student other) {
            return this.name.compareTo(other.name);
        }

        @Override
        public String toString() { return name + "(" + gpa + ")"; }
    }

    // Custom comparators
    static final Comparator<Student> BY_GPA = Comparator.comparingDouble(s -> s.gpa);
    static final Comparator<Student> BY_GPA_DESC = Comparator.comparingDouble((Student s) -> s.gpa).reversed();
    static final Comparator<Student> BY_NAME_THEN_GPA = Comparator
        .comparing((Student s) -> s.name)
        .thenComparingDouble(s -> s.gpa);

    // =========================================================================
    // 7. BUILT-IN FUNCTIONAL INTERFACES
    // =========================================================================

    // Function<T,R>: takes T, returns R
    // Predicate<T>:  takes T, returns boolean
    // Consumer<T>:   takes T, returns void
    // Supplier<T>:   takes nothing, returns T
    // BiFunction<T,U,R>: takes T and U, returns R
    // UnaryOperator<T>: Function<T,T>
    // BinaryOperator<T>: BiFunction<T,T,T>

    // =========================================================================
    // 8. OPTIONAL
    //    Avoids NullPointerException — represents a value that may or may not exist
    // =========================================================================

    static Optional<String> findFirstLongName(List<String> names, int minLen) {
        return names.stream()
            .filter(n -> n.length() >= minLen)
            .findFirst();
    }

    // =========================================================================
    // 9. EXCEPTION HANDLING
    // =========================================================================

    // Custom checked exception
    static class InsufficientFundsException extends Exception {
        private final double amount;
        public InsufficientFundsException(double amount) {
            super("Insufficient funds: needed " + amount);
            this.amount = amount;
        }
        public double getAmount() { return amount; }
    }

    // Custom unchecked exception
    static class InvalidAgeException extends RuntimeException {
        public InvalidAgeException(int age) {
            super("Invalid age: " + age);
        }
    }

    static class BankAccount {
        private double balance;

        public BankAccount(double balance) { this.balance = balance; }

        public void withdraw(double amount) throws InsufficientFundsException {
            if (amount > balance) throw new InsufficientFundsException(amount - balance);
            balance -= amount;
        }

        public double getBalance() { return balance; }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== GENERICS & FUNCTIONAL PROGRAMMING ===\n");

        // --- 1. Generic Class ---
        System.out.println("-- 1. Generic Class --");
        Pair<String, Integer> pair = new Pair<>("Alice", 30);
        System.out.println("Pair: " + pair);
        System.out.println("Swapped: " + pair.swap());

        GenericStack<Integer> intStack = new GenericStack<>();
        intStack.push(1); intStack.push(2); intStack.push(3);
        System.out.println("GenericStack: " + intStack);
        System.out.println("Pop: " + intStack.pop());

        // --- 2. Generic Methods ---
        System.out.println("\n-- 2. Generic Methods --");
        Integer[] nums = {3, 1, 4, 1, 5, 9, 2, 6};
        System.out.println("Max: " + max(nums));
        swap(nums, 0, 7);
        System.out.println("After swap(0,7): " + Arrays.toString(nums));

        String[] words = {"banana", "apple", "cherry"};
        System.out.println("Max string: " + max(words));

        // --- 3. Bounded Types ---
        System.out.println("\n-- 3. Bounded Type Parameters --");
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
        System.out.println("Sum(ints): " + sum(ints));
        System.out.println("Sum(doubles): " + sum(doubles));
        System.out.println("clamp(15, 0, 10): " + clamp(15, 0, 10));
        System.out.println("clamp(-3, 0, 10): " + clamp(-3, 0, 10));
        System.out.println("clamp(5, 0, 10):  " + clamp(5, 0, 10));

        // --- 4. Wildcards ---
        System.out.println("\n-- 4. Wildcards --");
        System.out.println("sumWildcard(ints): " + sumWildcard(ints));
        System.out.println("sumWildcard(doubles): " + sumWildcard(doubles));

        List<Number> numList = new ArrayList<>();
        addNumbers(numList, 5);
        System.out.print("addNumbers (? super Integer): ");
        printList(numList);

        // --- 5. Generic Interface ---
        System.out.println("-- 5. Generic Interface (Repository pattern) --");
        UserRepository repo = new UserRepository();
        repo.save(new User(1, "Alice"));
        repo.save(new User(2, "Bob"));
        repo.save(new User(3, "Charlie"));

        Optional<User> found = repo.findById(2);
        found.ifPresent(u -> System.out.println("Found: " + u));

        System.out.println("All users: " + repo.findAll());
        repo.delete(2);
        System.out.println("After delete(2): " + repo.findAll());

        // --- 6. Comparable vs Comparator ---
        System.out.println("\n-- 6. Comparable vs Comparator --");
        List<Student> students = Arrays.asList(
            new Student("Charlie", 3.5),
            new Student("Alice", 3.9),
            new Student("Bob", 3.7),
            new Student("Dave", 3.5)
        );

        Collections.sort(students); // uses Comparable (natural order = by name)
        System.out.println("By name (natural): " + students);

        students.sort(BY_GPA_DESC);
        System.out.println("By GPA desc:       " + students);

        students.sort(BY_NAME_THEN_GPA);
        System.out.println("By name+GPA:       " + students);

        // --- 7. Functional Interfaces & Lambdas ---
        System.out.println("\n-- 7. Functional Interfaces & Lambdas --");

        // Function
        Function<String, Integer> strLen = String::length; // method reference
        Function<Integer, Integer> doubleIt = x -> x * 2;
        Function<String, Integer> lenThenDouble = strLen.andThen(doubleIt); // composition
        System.out.println("'hello' length*2: " + lenThenDouble.apply("hello"));

        // Predicate
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;
        Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
        System.out.println("isEvenAndPositive(4): " + isEvenAndPositive.test(4));
        System.out.println("isEvenAndPositive(-4): " + isEvenAndPositive.test(-4));

        // Consumer
        Consumer<String> print = System.out::println;
        Consumer<String> printUpper = s -> System.out.println(s.toUpperCase());
        Consumer<String> printBoth = print.andThen(printUpper);
        printBoth.accept("hello consumer");

        // Supplier
        Supplier<List<String>> listFactory = ArrayList::new;
        List<String> newList = listFactory.get();
        newList.add("supplied item");
        System.out.println("Supplied list: " + newList);

        // BiFunction
        BiFunction<String, Integer, String> repeat = (str, n) -> str.repeat(n);
        System.out.println("repeat('ab', 3): " + repeat.apply("ab", 3));

        // UnaryOperator
        UnaryOperator<String> trim = String::trim;
        UnaryOperator<String> upper = String::toUpperCase;
        System.out.println("Composed: " + trim.andThen(upper).apply("  hello  "));

        // BinaryOperator
        BinaryOperator<Integer> add = Integer::sum;
        System.out.println("BinaryOperator sum: " + add.apply(3, 4));

        // --- 8. Method References ---
        System.out.println("\n-- 8. Method References --");
        List<String> names = Arrays.asList("Charlie", "Alice", "Bob", "Dave");

        // Static method reference
        names.stream().map(String::valueOf).forEach(System.out::println);

        // Instance method reference on a particular instance
        String prefix = "Hello, ";
        Function<String, String> greet = prefix::concat;
        names.stream().map(greet).forEach(System.out::println);

        // Instance method reference on an arbitrary instance
        names.stream().sorted(String::compareTo).forEach(s -> System.out.print(s + " "));
        System.out.println();

        // Constructor reference
        Function<String, StringBuilder> sbFactory = StringBuilder::new;
        System.out.println("SB: " + sbFactory.apply("test"));

        // --- 9. Optional ---
        System.out.println("\n-- 9. Optional --");
        Optional<String> name1 = findFirstLongName(names, 5);  // "Charlie"
        Optional<String> name2 = findFirstLongName(names, 10); // empty

        System.out.println("Present: " + name1.isPresent() + ", value: " + name1.get());
        System.out.println("name2 or else: " + name2.orElse("none"));
        System.out.println("name2 or get: " + name2.orElseGet(() -> "computed default"));

        // Optional chaining
        String result = Optional.of("  hello world  ")
            .map(String::trim)
            .filter(s -> s.length() > 5)
            .map(String::toUpperCase)
            .orElse("too short");
        System.out.println("Optional chain: " + result);

        // --- 10. Exception Handling ---
        System.out.println("\n-- 10. Exception Handling --");
        BankAccount account = new BankAccount(100.0);

        // try-catch-finally
        try {
            account.withdraw(50.0);
            System.out.println("Withdrew 50, balance: " + account.getBalance());
            account.withdraw(100.0); // should throw
            System.out.println("This line won't execute");
        } catch (InsufficientFundsException e) {
            System.out.println("Caught: " + e.getMessage() + " (needed: " + e.getAmount() + " more)");
        } finally {
            System.out.println("finally block always runs");
        }

        // Multi-catch (Java 7+)
        try {
            String s = null;
            s.length(); // NullPointerException
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Multi-catch: " + e.getClass().getSimpleName());
        }

        // try-with-resources (auto-closes Closeable) — shown conceptually
        // try (InputStream is = new FileInputStream("file.txt")) {
        //     // is.read() ...
        // } catch (IOException e) { ... }
        // InputStream is automatically closed after the block

        // throw / throws
        try {
            validateAge(-5);
        } catch (InvalidAgeException e) {
            System.out.println("Caught custom unchecked: " + e.getMessage());
        }
    }

    static void validateAge(int age) {
        if (age < 0 || age > 150) throw new InvalidAgeException(age);
    }
}
