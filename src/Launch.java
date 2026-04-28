import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Launch {
    private static final String END_SENTINEL = "END";
    private static final String RESET = "\u001B[0m";
    private static final String INK = "\u001B[38;2;18;20;22m";
    private static final String MUTED = "\u001B[38;2;93;98;105m";
    private static final String ACCENT = "\u001B[38;2;28;54;90m";

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, List<Question>> questionBank = QuestionBank.build();
    private final Map<String, Lesson> lessons = buildLessons();

    public static void main(String[] args) {
        new Launch().run();
    }

    private void run() {
        printHero();
        while (true) {
            printMainMenu();
            String input = scanner.nextLine().trim();

            if ("1".equals(input)) {
                runGuidedSyntaxWalkthrough();
            } else if ("2".equals(input)) {
                runTopicPractice();
            } else if ("3".equals(input)) {
                runMixedDrill();
            } else if ("4".equals(input)) {
                printlnAccent("see you next session.");
                return;
            } else {
                printlnMuted("invalid option. choose 1 to 4.");
            }
        }
    }

    private void runGuidedSyntaxWalkthrough() {
        List<String> topics = new ArrayList<>(lessons.keySet());
        while (true) {
            printSectionHeader("guided syntax walkthrough");
            for (int i = 0; i < topics.size(); i++) {
                System.out.printf("%s%d%s) %s%n", ACCENT, i + 1, RESET, topics.get(i));
            }
            System.out.printf("%s%d%s) run all topics%n", ACCENT, topics.size() + 1, RESET);
            System.out.printf("%s%d%s) back%n", ACCENT, topics.size() + 2, RESET);
            System.out.print("choose: ");

            int choice = parseChoice(scanner.nextLine().trim());
            if (choice == topics.size() + 2) {
                return;
            }
            if (choice == topics.size() + 1) {
                for (String topic : topics) {
                    showLesson(topic, lessons.get(topic));
                }
                continue;
            }
            if (choice < 1 || choice > topics.size()) {
                printlnMuted("out of range.");
                continue;
            }

            String topic = topics.get(choice - 1);
            showLesson(topic, lessons.get(topic));
        }
    }

    private void showLesson(String topic, Lesson lesson) {
        printSectionHeader(topic.toLowerCase());
        printlnMuted(lesson.summary);
        System.out.println();
        printlnAccent("example");
        System.out.println(lesson.exampleCode);
        System.out.println();
        printlnAccent("expected output");
        System.out.println(lesson.expectedOutput);
        System.out.println();
        printlnMuted("press enter to continue...");
        scanner.nextLine();
    }

    private void runTopicPractice() {
        List<String> topics = new ArrayList<>(questionBank.keySet());
        while (true) {
            printSectionHeader("question practice by topic");
            for (int i = 0; i < topics.size(); i++) {
                String topic = topics.get(i);
                int count = questionBank.get(topic).size();
                System.out.printf("%s%d%s) %s (%d)%n", ACCENT, i + 1, RESET, topic, count);
            }
            System.out.printf("%s%d%s) back%n", ACCENT, topics.size() + 1, RESET);
            System.out.print("choose: ");

            int choice = parseChoice(scanner.nextLine().trim());
            if (choice == topics.size() + 1) {
                return;
            }
            if (choice < 1 || choice > topics.size()) {
                printlnMuted("out of range.");
                continue;
            }

            String selected = topics.get(choice - 1);
            runQuestions(questionBank.get(selected), selected);
        }
    }

    private void runMixedDrill() {
        List<Question> all = new ArrayList<>();
        for (List<Question> questions : questionBank.values()) {
            all.addAll(questions);
        }
        runQuestions(all, "mixed drill");
    }

    private void runQuestions(List<Question> questions, String label) {
        int correct = 0;
        printSectionHeader(label);
        printlnMuted("type output exactly. for multiline answers, type lines then END.");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println();
            System.out.printf("%squestion %d/%d%s%n", ACCENT, i + 1, questions.size(), RESET);
            System.out.printf("%stopic:%s %s%n", MUTED, RESET, q.getTopic());
            System.out.printf("%stitle:%s %s%n%n", MUTED, RESET, q.getTitle());
            System.out.println(q.getPrompt());
            System.out.println();
            System.out.println("your output (finish with END):");

            String userOutput = readMultiLineAnswer();
            boolean isCorrect = normalize(userOutput).equals(normalize(q.getExpectedOutput()));
            if (isCorrect) {
                correct++;
                System.out.printf("%sresult:%s correct%n", ACCENT, RESET);
            } else {
                System.out.printf("%sresult:%s incorrect%n", ACCENT, RESET);
                printlnMuted("expected output:");
                System.out.println(q.getExpectedOutput());
            }

            System.out.println();
            printlnAccent("step-by-step solution");
            for (int step = 0; step < q.getSolutionSteps().size(); step++) {
                System.out.printf("%d) %s%n", step + 1, q.getSolutionSteps().get(step));
            }

            if (!q.getOptimalCode().isEmpty()) {
                System.out.println();
                printlnAccent("reference implementation");
                System.out.println(q.getOptimalCode());
            }

            System.out.println();
            printlnMuted("press enter for next...");
            scanner.nextLine();
        }

        System.out.println();
        System.out.printf("%sscore%s %d/%d (%.1f%%)%n", INK, RESET, correct, questions.size(),
            questions.isEmpty() ? 0.0 : (100.0 * correct) / questions.size());
    }

    private String readMultiLineAnswer() {
        List<String> lines = new ArrayList<>();
        while (true) {
            String line = scanner.nextLine();
            if (END_SENTINEL.equals(line)) {
                break;
            }
            lines.add(line);
        }
        return String.join("\n", lines);
    }

    private int parseChoice(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private String normalize(String value) {
        return value.replace("\r\n", "\n").trim();
    }

    private void printHero() {
        System.out.println(ACCENT + "============================================================" + RESET);
        System.out.println(INK + " java syntax trainer | clean terminal ui" + RESET);
        System.out.println(MUTED + " inspired by your website tone: lowercase, clean, practical" + RESET);
        System.out.println(ACCENT + "============================================================" + RESET);
    }

    private void printMainMenu() {
        System.out.println();
        printSectionHeader("main menu");
        System.out.printf("%s1%s) guided syntax walkthrough%n", ACCENT, RESET);
        System.out.printf("%s2%s) question practice by topic%n", ACCENT, RESET);
        System.out.printf("%s3%s) mixed drill (all topics)%n", ACCENT, RESET);
        System.out.printf("%s4%s) exit%n", ACCENT, RESET);
        System.out.print("choose: ");
    }

    private void printSectionHeader(String label) {
        System.out.printf("%n%s[%s]%s%n", INK, label, RESET);
    }

    private void printlnAccent(String line) {
        System.out.println(ACCENT + line + RESET);
    }

    private void printlnMuted(String line) {
        System.out.println(MUTED + line + RESET);
    }

    private Map<String, Lesson> buildLessons() {
        Map<String, Lesson> map = new LinkedHashMap<>();

        map.put("Syntax", new Lesson(
            "Core language syntax: loops, conditions, collections, and streams.",
            "int sum = 0;\nfor (int i = 1; i <= 4; i++) {\n    sum += i;\n}\nSystem.out.println(sum);",
            "10"
        ));

        map.put("Linked List", new Lesson(
            "Pointer-style node traversal and reverse operations.",
            "Node prev = null, curr = head;\nwhile (curr != null) {\n    Node next = curr.next;\n    curr.next = prev;\n    prev = curr;\n    curr = next;\n}",
            "head points to reversed list"
        ));

        map.put("Stack", new Lesson(
            "LIFO operations for expression and bracket problems.",
            "Stack<Character> st = new Stack<>();\nst.push('(');\nst.pop();",
            "stack is empty"
        ));

        map.put("Queue", new Lesson(
            "FIFO flows for scheduling and BFS-style traversal.",
            "Queue<Integer> q = new ArrayDeque<>();\nq.offer(10);\nq.offer(20);\nSystem.out.println(q.poll());",
            "10"
        ));

        map.put("Hash Table", new Lesson(
            "Fast key lookup with collision handling strategies.",
            "Map<String, Integer> m = new HashMap<>();\nm.put(\"age\", 21);\nSystem.out.println(m.get(\"age\"));",
            "21"
        ));

        map.put("Trees", new Lesson(
            "Hierarchical structures with traversal patterns and balancing.",
            "void inorder(Node n) {\n    if (n == null) return;\n    inorder(n.left);\n    System.out.print(n.val + \" \" );\n    inorder(n.right);\n}",
            "sorted output for BST"
        ));

        map.put("Search (BFS/DFS)", new Lesson(
            "Graph traversal from breadth-first and depth-first perspectives.",
            "Queue<Integer> q = new ArrayDeque<>();\nq.offer(start);\nwhile (!q.isEmpty()) {\n    int u = q.poll();\n}",
            "nodes visited in BFS layers"
        ));

        return map;
    }

    private static final class Lesson {
        private final String summary;
        private final String exampleCode;
        private final String expectedOutput;

        private Lesson(String summary, String exampleCode, String expectedOutput) {
            this.summary = summary;
            this.exampleCode = exampleCode;
            this.expectedOutput = expectedOutput;
        }
    }
}