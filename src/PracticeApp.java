import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class PracticeApp {
    private static final String END_SENTINEL = "END";

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, List<Question>> questionBank;

    public PracticeApp(Map<String, List<Question>> questionBank) {
        this.questionBank = questionBank;
    }

    public void run() {
        printBanner();
        while (true) {
            System.out.println();
            System.out.println("Main Menu");
            System.out.println("1) Practice by topic");
            System.out.println("2) Practice all topics (mixed order)");
            System.out.println("3) Exit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                runTopicMode();
            } else if ("2".equals(input)) {
                List<Question> allQuestions = new ArrayList<>();
                for (List<Question> questions : questionBank.values()) {
                    allQuestions.addAll(questions);
                }
                runQuestions(allQuestions, "All Topics");
            } else if ("3".equals(input)) {
                System.out.println("Good luck with your Java practice.");
                return;
            } else {
                System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }
        }
    }

    private void runTopicMode() {
        List<String> topics = new ArrayList<>(questionBank.keySet());

        while (true) {
            System.out.println();
            System.out.println("Topics");
            for (int i = 0; i < topics.size(); i++) {
                String topic = topics.get(i);
                int count = questionBank.get(topic).size();
                System.out.printf("%d) %s (%d questions)%n", i + 1, topic, count);
            }
            System.out.printf("%d) Back%n", topics.size() + 1);
            System.out.print("Choose a topic: ");

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Enter a valid number.");
                continue;
            }

            if (choice == topics.size() + 1) {
                return;
            }
            if (choice < 1 || choice > topics.size()) {
                System.out.println("Out of range. Try again.");
                continue;
            }

            String selectedTopic = topics.get(choice - 1);
            runQuestions(questionBank.get(selectedTopic), selectedTopic);
        }
    }

    private void runQuestions(List<Question> questions, String label) {
        int correct = 0;

        System.out.println();
        System.out.println("Starting: " + label);
        System.out.println("Type your expected output exactly.");
        System.out.println("For multi-line answers, enter one line at a time and type END to submit.");

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.printf("Question %d/%d%n", i + 1, questions.size());
            System.out.println("Topic: " + q.getTopic());
            System.out.println("Title: " + q.getTitle());
            System.out.println();
            System.out.println(q.getPrompt());
            System.out.println();
            System.out.println("Your output (finish with END):");

            String userOutput = readMultiLineAnswer();
            boolean isCorrect = normalize(userOutput).equals(normalize(q.getExpectedOutput()));
            if (isCorrect) {
                correct++;
                System.out.println("Result: CORRECT");
            } else {
                System.out.println("Result: INCORRECT");
                System.out.println("Expected output:");
                System.out.println(q.getExpectedOutput());
            }

            System.out.println();
            System.out.println("Step-by-step optimal solution:");
            for (int step = 0; step < q.getSolutionSteps().size(); step++) {
                System.out.printf("%d) %s%n", step + 1, q.getSolutionSteps().get(step));
            }
            if (!q.getOptimalCode().isEmpty()) {
                System.out.println();
                System.out.println("Reference implementation (simple + optimal):");
                System.out.println(q.getOptimalCode());
            }

            System.out.println();
            System.out.print("Press Enter for next question...");
            scanner.nextLine();
        }

        System.out.println();
        System.out.println("Session complete for " + label + ".");
        System.out.printf("Score: %d/%d (%.1f%%)%n", correct, questions.size(), (100.0 * correct) / questions.size());
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

    private String normalize(String value) {
        return value.replace("\r\n", "\n").trim();
    }

    private void printBanner() {
        System.out.println("============================================================");
        System.out.println(" Java Syntax + Data Structures Practice Trainer");
        System.out.println("============================================================");
        System.out.println("Rule: if your output matches expected output, it is correct.");
        System.out.println("Then you get a step-by-step optimal solution.");
    }
}
