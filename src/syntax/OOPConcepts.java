package syntax;

import java.util.*;

/**
 * OBJECT-ORIENTED PROGRAMMING (OOP) IN JAVA - Complete Reference
 *
 * Covers:
 *   1. Classes and Objects
 *   2. Encapsulation (access modifiers)
 *   3. Inheritance
 *   4. Polymorphism (compile-time and runtime)
 *   5. Abstraction (abstract classes and interfaces)
 *   6. Static members
 *   7. Final keyword
 *   8. Inner classes (static, non-static, anonymous, local)
 *   9. Enums
 *  10. Records (Java 16+)
 *  11. Sealed classes (Java 17+)
 */
public class OOPConcepts {

    // =========================================================================
    // 1. CLASSES AND OBJECTS
    //    - Fields, constructors, methods, this keyword
    // =========================================================================

    static class Person {
        // Fields (instance variables)
        private String name;
        private int age;
        private static int count = 0; // class variable

        // Default constructor
        public Person() {
            this("Unknown", 0); // delegates to parameterized constructor
        }

        // Parameterized constructor
        public Person(String name, int age) {
            this.name = name;    // 'this' disambiguates field vs. parameter
            this.age = age;
            count++;
        }

        // Copy constructor
        public Person(Person other) {
            this(other.name, other.age);
        }

        // Instance method
        public String greet() {
            return "Hi, I'm " + name + " and I'm " + age + " years old.";
        }

        // Static method — no 'this', can only access static members
        public static int getCount() { return count; }

        // Getters and Setters (Encapsulation)
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) {
            if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
            this.age = age;
        }

        // toString, equals, hashCode overrides
        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person)) return false;
            Person p = (Person) o;
            return age == p.age && Objects.equals(name, p.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }

    // =========================================================================
    // 2. INHERITANCE
    //    - extends, super keyword, method overriding, @Override
    // =========================================================================

    static class Animal {
        protected String name;
        protected String sound;

        public Animal(String name, String sound) {
            this.name = name;
            this.sound = sound;
        }

        public String speak() {
            return name + " says " + sound;
        }

        public String describe() {
            return "I am an animal named " + name;
        }
    }

    static class Dog extends Animal {
        private String breed;

        public Dog(String name, String breed) {
            super(name, "Woof"); // call parent constructor
            this.breed = breed;
        }

        // Override parent method
        @Override
        public String speak() {
            return super.speak() + "! (I'm a " + breed + ")";
        }

        // New method specific to Dog
        public void fetch() {
            System.out.println(name + " fetches the ball!");
        }
    }

    static class Cat extends Animal {
        private boolean isIndoor;

        public Cat(String name, boolean isIndoor) {
            super(name, "Meow");
            this.isIndoor = isIndoor;
        }

        @Override
        public String speak() {
            return super.speak() + (isIndoor ? " (indoor cat)" : " (outdoor cat)");
        }
    }

    // =========================================================================
    // 3. ABSTRACTION
    //    - Abstract classes (partial implementation + forced override)
    //    - Interfaces (pure contract; default methods since Java 8)
    // =========================================================================

    // Abstract class — cannot be instantiated directly
    abstract static class Shape {
        private String color;

        public Shape(String color) { this.color = color; }

        // Abstract method — subclasses MUST implement
        public abstract double area();
        public abstract double perimeter();

        // Concrete method — shared by all shapes
        public String describe() {
            return String.format("%s %s: area=%.2f, perimeter=%.2f",
                color, getClass().getSimpleName(), area(), perimeter());
        }

        public String getColor() { return color; }
    }

    static class Circle extends Shape {
        private double radius;
        public Circle(String color, double radius) {
            super(color);
            this.radius = radius;
        }
        @Override public double area() { return Math.PI * radius * radius; }
        @Override public double perimeter() { return 2 * Math.PI * radius; }
    }

    static class Rectangle extends Shape {
        private double width, height;
        public Rectangle(String color, double width, double height) {
            super(color);
            this.width = width;
            this.height = height;
        }
        @Override public double area() { return width * height; }
        @Override public double perimeter() { return 2 * (width + height); }
    }

    // Interface — 100% abstract (before Java 8), now supports default/static methods
    interface Drawable {
        void draw();                    // abstract method

        default String getDescription() { // default method (Java 8+)
            return "I am a drawable object";
        }

        static Drawable noop() {        // static method (Java 8+)
            return () -> System.out.println("[nothing to draw]");
        }
    }

    interface Resizable {
        void resize(double factor);
    }

    // A class can implement multiple interfaces
    static class DrawableCircle extends Circle implements Drawable, Resizable {
        private double radius;

        public DrawableCircle(double radius) {
            super("red", radius);
            this.radius = radius;
        }

        @Override
        public void draw() {
            System.out.println("Drawing circle with radius " + radius);
        }

        @Override
        public void resize(double factor) {
            radius *= factor;
            System.out.println("Resized to radius " + radius);
        }
    }

    // Functional interface — has exactly one abstract method, usable as lambda
    @FunctionalInterface
    interface MathOperation {
        int operate(int a, int b);
    }

    // =========================================================================
    // 4. POLYMORPHISM
    //    - Compile-time (method overloading)
    //    - Runtime (method overriding + dynamic dispatch)
    // =========================================================================

    static class Calculator {
        // Method overloading — same name, different parameters
        public int add(int a, int b) { return a + b; }
        public double add(double a, double b) { return a + b; }
        public int add(int a, int b, int c) { return a + b + c; }
        public String add(String a, String b) { return a + b; }
    }

    // =========================================================================
    // 5. STATIC MEMBERS
    // =========================================================================

    static class MathUtils {
        // Static constant (convention: ALL_CAPS)
        public static final double PI = 3.141592653589793;
        public static final int MAX_VALUE = Integer.MAX_VALUE;

        // Static method — no object needed
        public static int factorial(int n) {
            if (n <= 1) return 1;
            return n * factorial(n - 1);
        }

        public static boolean isPrime(int n) {
            if (n < 2) return false;
            for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
            return true;
        }

        // Static initializer block
        static {
            System.out.println("  [MathUtils class loaded]");
        }
    }

    // =========================================================================
    // 6. FINAL KEYWORD
    //    - final variable: constant
    //    - final method:   cannot be overridden
    //    - final class:    cannot be extended
    // =========================================================================

    static final class ImmutablePoint { // final class
        private final double x; // final field — must be set in constructor
        private final double y;

        public ImmutablePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        // "Mutating" an immutable object creates a new one
        public ImmutablePoint translate(double dx, double dy) {
            return new ImmutablePoint(x + dx, y + dy);
        }

        public double distanceTo(ImmutablePoint other) {
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() { return "(" + x + ", " + y + ")"; }
    }

    // =========================================================================
    // 7. ENUMS
    // =========================================================================

    enum Direction {
        NORTH, SOUTH, EAST, WEST;

        public Direction opposite() {
            return switch (this) {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case EAST  -> WEST;
                case WEST  -> EAST;
            };
        }
    }

    enum Planet {
        MERCURY(3.303e+23, 2.4397e6),
        VENUS  (4.869e+24, 6.0518e6),
        EARTH  (5.976e+24, 6.37814e6),
        MARS   (6.421e+23, 3.3972e6);

        private final double mass;   // kg
        private final double radius; // meters
        static final double G = 6.67300E-11;

        Planet(double mass, double radius) {
            this.mass = mass;
            this.radius = radius;
        }

        double surfaceGravity() { return G * mass / (radius * radius); }
        double surfaceWeight(double otherMass) { return otherMass * surfaceGravity(); }
    }

    // =========================================================================
    // 8. INNER CLASSES
    // =========================================================================

    static class Outer {
        private int value = 10;

        // Non-static inner class — has access to outer class instance
        class Inner {
            void show() { System.out.println("Outer value: " + value); }
        }

        // Static nested class — no access to outer instance
        static class StaticNested {
            void show() { System.out.println("I'm a static nested class"); }
        }
    }

    // =========================================================================
    // 9. RECORDS (Java 16+) — immutable data carriers
    // =========================================================================

    record Point(double x, double y) {
        // Compact constructor for validation
        Point {
            if (Double.isNaN(x) || Double.isNaN(y)) {
                throw new IllegalArgumentException("Coordinates cannot be NaN");
            }
        }

        // Custom instance method
        double distanceTo(Point other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }
    }

    // =========================================================================
    // DEMO / MAIN
    // =========================================================================
    public static void main(String[] args) {
        System.out.println("=== OOP CONCEPTS IN JAVA ===\n");

        // --- 1. Classes and Objects ---
        System.out.println("-- 1. Classes & Objects --");
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Bob", 25);
        Person p3 = new Person(p1); // copy constructor
        System.out.println(p1);
        System.out.println(p1.greet());
        System.out.println("p1 equals p3? " + p1.equals(p3));  // true
        System.out.println("Person count: " + Person.getCount()); // 3

        // --- 2. Inheritance ---
        System.out.println("\n-- 2. Inheritance --");
        Dog dog = new Dog("Rex", "Labrador");
        Cat cat = new Cat("Whiskers", true);
        System.out.println(dog.speak());
        System.out.println(cat.speak());
        System.out.println(dog.describe()); // inherited from Animal

        // Upcasting: store Dog as Animal reference
        Animal animal = dog;
        System.out.println(animal.speak()); // runtime polymorphism — calls Dog.speak()
        System.out.println("instanceof Dog: " + (animal instanceof Dog));

        // Pattern matching instanceof (Java 16+)
        if (animal instanceof Dog d) {
            d.fetch(); // no explicit cast needed
        }

        // --- 3. Abstraction ---
        System.out.println("\n-- 3. Abstraction --");
        Shape[] shapes = {
            new Circle("blue", 5),
            new Rectangle("green", 4, 6)
        };
        for (Shape shape : shapes) System.out.println(shape.describe());

        DrawableCircle dc = new DrawableCircle(3.0);
        dc.draw();
        System.out.println(dc.getDescription()); // default interface method
        dc.resize(2.0);

        // Lambda as functional interface
        MathOperation multiply = (a, b) -> a * b;
        MathOperation power = (a, b) -> (int) Math.pow(a, b);
        System.out.println("3 * 4 = " + multiply.operate(3, 4));
        System.out.println("2 ^ 8 = " + power.operate(2, 8));

        // --- 4. Polymorphism ---
        System.out.println("\n-- 4. Polymorphism --");
        Calculator calc = new Calculator();
        System.out.println("add(int,int):    " + calc.add(3, 4));
        System.out.println("add(double,double): " + calc.add(3.1, 4.2));
        System.out.println("add(String,String): " + calc.add("Hello, ", "World!"));

        // --- 5. Static members ---
        System.out.println("\n-- 5. Static Members --");
        System.out.println("PI = " + MathUtils.PI);
        System.out.println("5! = " + MathUtils.factorial(5));
        System.out.println("17 prime? " + MathUtils.isPrime(17));

        // --- 6. Final (Immutable) ---
        System.out.println("\n-- 6. Final / Immutable --");
        ImmutablePoint pt1 = new ImmutablePoint(0, 0);
        ImmutablePoint pt2 = pt1.translate(3, 4);
        System.out.println("pt1=" + pt1 + ", pt2=" + pt2);
        System.out.println("Distance: " + pt1.distanceTo(pt2));

        // --- 7. Enums ---
        System.out.println("\n-- 7. Enums --");
        Direction dir = Direction.NORTH;
        System.out.println("Direction: " + dir + ", Opposite: " + dir.opposite());
        System.out.println("All directions: " + Arrays.toString(Direction.values()));

        double earthWeight = 75.0; // kg
        double mass = earthWeight / Planet.EARTH.surfaceGravity();
        System.out.printf("Weight of %.1fkg on Earth, Mars: %.2fkg%n",
            earthWeight, Planet.MARS.surfaceWeight(mass));

        // --- 8. Inner classes ---
        System.out.println("\n-- 8. Inner Classes --");
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.show();

        Outer.StaticNested nested = new Outer.StaticNested();
        nested.show();

        // Anonymous class
        Drawable anonymousDrawable = new Drawable() {
            @Override
            public void draw() {
                System.out.println("Drawing via anonymous class");
            }
        };
        anonymousDrawable.draw();

        // --- 9. Records ---
        System.out.println("\n-- 9. Records (Java 16+) --");
        Point point1 = new Point(0, 0);
        Point point2 = new Point(3, 4);
        System.out.println("p1=" + point1 + ", p2=" + point2);
        System.out.println("Distance: " + point1.distanceTo(point2));
        // Records auto-generate: toString, equals, hashCode, getters (x(), y())
        System.out.println("x=" + point1.x() + ", y=" + point1.y());
        System.out.println("points equal? " + point1.equals(new Point(0, 0))); // true
    }
}
