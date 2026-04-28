import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Launch {
    private static final Color BG = new Color(245, 245, 239);
    private static final Color SURFACE = new Color(250, 250, 247);
    private static final Color INK = new Color(18, 20, 22);
    private static final Color MUTED = new Color(93, 98, 105);
    private static final Color ACCENT = new Color(28, 54, 90);

    private final Map<String, List<Question>> questionBank = QuestionBank.build();
    private final Map<String, Lesson> lessons = buildLessons();

    private JFrame frame;
    private CardLayout contentCards;
    private JPanel contentPanel;

    private JList<String> walkthroughTopicList;
    private JTextArea walkthroughSummary;
    private JTextArea walkthroughCode;
    private JTextArea walkthroughOutput;

    private JList<String> practiceTopicList;
    private JLabel questionMeta;
    private JTextArea questionPrompt;
    private JTextArea expectedOutput;
    private JTextArea solutionSteps;
    private JTextArea optimalCode;
    private JTextField answerField;
    private JLabel resultLabel;

    private List<Question> activeQuestions = new ArrayList<>();
    private int questionIndex = 0;
    private int score = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Launch app = new Launch();
            app.buildUi();
            app.frame.setVisible(true);
        });
    }

    private void buildUi() {
        setLookAndFeel();

        frame = new JFrame("java syntax trainer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1120, 760));
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildMain(), BorderLayout.CENTER);

        frame.setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel title = new JLabel("java syntax trainer");
        title.setForeground(INK);
        title.setFont(new Font("Serif", Font.PLAIN, 34));

        JLabel subtitle = new JLabel("guided walkthrough + questions + examples");
        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(subtitle);

        header.add(left, BorderLayout.WEST);
        return header;
    }

    private JSplitPane buildMain() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(SURFACE);
        nav.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 212)),
            BorderFactory.createEmptyBorder(14, 12, 14, 12)
        ));

        JButton walkthroughBtn = createNavButton("walkthrough");
        walkthroughBtn.addActionListener(e -> showCard("walkthrough"));

        JButton topicPracticeBtn = createNavButton("practice by topic");
        topicPracticeBtn.addActionListener(e -> showCard("practice"));

        JButton mixedBtn = createNavButton("mixed drill");
        mixedBtn.addActionListener(e -> startMixedDrill());

        JButton resetBtn = createNavButton("reset session");
        resetBtn.addActionListener(e -> resetPracticeUi());

        nav.add(walkthroughBtn);
        nav.add(Box.createVerticalStrut(10));
        nav.add(topicPracticeBtn);
        nav.add(Box.createVerticalStrut(10));
        nav.add(mixedBtn);
        nav.add(Box.createVerticalStrut(10));
        nav.add(resetBtn);
        nav.add(Box.createVerticalGlue());

        contentCards = new CardLayout();
        contentPanel = new JPanel(contentCards);
        contentPanel.setOpaque(false);
        contentPanel.add(buildWalkthroughCard(), "walkthrough");
        contentPanel.add(buildPracticeCard(), "practice");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, nav, contentPanel);
        split.setDividerLocation(230);
        split.setBorder(null);
        split.setBackground(BG);
        return split;
    }

    private JPanel buildWalkthroughCard() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(BG);

        List<String> topics = new ArrayList<>(lessons.keySet());
        walkthroughTopicList = new JList<>(topics.toArray(new String[0]));
        walkthroughTopicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        walkthroughTopicList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateWalkthroughTopic();
            }
        });

        JScrollPane leftScroll = new JScrollPane(walkthroughTopicList);
        leftScroll.setPreferredSize(new Dimension(240, 0));

        JPanel detail = new JPanel();
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));
        detail.setBackground(SURFACE);
        detail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 212)),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        walkthroughSummary = createReadArea(4);
        walkthroughCode = createReadArea(10);
        walkthroughOutput = createReadArea(3);

        detail.add(sectionLabel("summary"));
        detail.add(walkthroughSummary);
        detail.add(Box.createVerticalStrut(8));
        detail.add(sectionLabel("example"));
        detail.add(walkthroughCode);
        detail.add(Box.createVerticalStrut(8));
        detail.add(sectionLabel("expected output"));
        detail.add(walkthroughOutput);

        panel.add(leftScroll, BorderLayout.WEST);
        panel.add(detail, BorderLayout.CENTER);

        if (!topics.isEmpty()) {
            walkthroughTopicList.setSelectedIndex(0);
        }
        return panel;
    }

    private JPanel buildPracticeCard() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(BG);

        List<String> topics = new ArrayList<>(questionBank.keySet());
        practiceTopicList = new JList<>(topics.toArray(new String[0]));
        practiceTopicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        practiceTopicList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String topic = practiceTopicList.getSelectedValue();
                if (topic != null) {
                    startTopicPractice(topic);
                }
            }
        });

        JScrollPane leftScroll = new JScrollPane(practiceTopicList);
        leftScroll.setPreferredSize(new Dimension(240, 0));

        JPanel detail = new JPanel();
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));
        detail.setBackground(SURFACE);
        detail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 212)),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));

        questionMeta = new JLabel("select a topic to begin");
        questionMeta.setForeground(ACCENT);
        questionMeta.setFont(new Font("SansSerif", Font.BOLD, 13));

        questionPrompt = createReadArea(8);
        answerField = new JTextField();
        expectedOutput = createReadArea(3);
        solutionSteps = createReadArea(7);
        optimalCode = createReadArea(7);
        resultLabel = new JLabel(" ");
        resultLabel.setForeground(MUTED);

        JButton checkBtn = new JButton("check answer");
        checkBtn.addActionListener(e -> checkCurrentAnswer());

        JButton nextBtn = new JButton("next question");
        nextBtn.addActionListener(e -> nextQuestion());

        JPanel controls = new JPanel(new BorderLayout(8, 0));
        controls.setOpaque(false);
        controls.add(checkBtn, BorderLayout.WEST);
        controls.add(nextBtn, BorderLayout.CENTER);

        detail.add(questionMeta);
        detail.add(Box.createVerticalStrut(8));
        detail.add(sectionLabel("question"));
        detail.add(questionPrompt);
        detail.add(Box.createVerticalStrut(6));
        detail.add(sectionLabel("your output"));
        detail.add(answerField);
        detail.add(Box.createVerticalStrut(8));
        detail.add(controls);
        detail.add(Box.createVerticalStrut(8));
        detail.add(resultLabel);
        detail.add(Box.createVerticalStrut(8));
        detail.add(sectionLabel("expected output"));
        detail.add(expectedOutput);
        detail.add(Box.createVerticalStrut(8));
        detail.add(sectionLabel("step-by-step solution"));
        detail.add(solutionSteps);
        detail.add(Box.createVerticalStrut(8));
        detail.add(sectionLabel("reference implementation"));
        detail.add(optimalCode);

        panel.add(leftScroll, BorderLayout.WEST);
        panel.add(detail, BorderLayout.CENTER);

        return panel;
    }

    private void updateWalkthroughTopic() {
        String topic = walkthroughTopicList.getSelectedValue();
        if (topic == null) {
            return;
        }

        Lesson lesson = lessons.get(topic);
        walkthroughSummary.setText(lesson.summary);
        walkthroughCode.setText(lesson.exampleCode);
        walkthroughOutput.setText(lesson.expectedOutput);
    }

    private void startTopicPractice(String topic) {
        activeQuestions = new ArrayList<>(questionBank.get(topic));
        questionIndex = 0;
        score = 0;
        loadQuestion();
    }

    private void startMixedDrill() {
        showCard("practice");
        activeQuestions = new ArrayList<>();
        for (List<Question> list : questionBank.values()) {
            activeQuestions.addAll(list);
        }
        questionIndex = 0;
        score = 0;
        loadQuestion();
    }

    private void loadQuestion() {
        if (activeQuestions.isEmpty()) {
            questionMeta.setText("no questions found");
            clearQuestionFields();
            return;
        }

        if (questionIndex >= activeQuestions.size()) {
            double pct = (100.0 * score) / activeQuestions.size();
            JOptionPane.showMessageDialog(
                frame,
                String.format("session complete\nscore: %d/%d (%.1f%%)", score, activeQuestions.size(), pct),
                "complete",
                JOptionPane.INFORMATION_MESSAGE
            );
            questionIndex = 0;
            score = 0;
        }

        Question q = activeQuestions.get(questionIndex);
        questionMeta.setText(String.format("%s | question %d/%d", q.getTopic(), questionIndex + 1, activeQuestions.size()));
        questionPrompt.setText(q.getPrompt());
        expectedOutput.setText("");
        solutionSteps.setText("");
        optimalCode.setText("");
        answerField.setText("");
        resultLabel.setText(" ");
    }

    private void checkCurrentAnswer() {
        if (activeQuestions.isEmpty()) {
            return;
        }

        Question q = activeQuestions.get(questionIndex);
        String userAnswer = normalize(answerField.getText());
        String expected = normalize(q.getExpectedOutput());
        boolean correct = expected.equals(userAnswer);

        if (correct) {
            score++;
            resultLabel.setForeground(new Color(18, 120, 56));
            resultLabel.setText("correct");
        } else {
            resultLabel.setForeground(new Color(140, 38, 38));
            resultLabel.setText("incorrect");
        }

        expectedOutput.setText(q.getExpectedOutput());
        solutionSteps.setText(String.join("\n", q.getSolutionSteps()));
        optimalCode.setText(q.getOptimalCode());
    }

    private void nextQuestion() {
        if (activeQuestions.isEmpty()) {
            return;
        }
        questionIndex++;
        loadQuestion();
    }

    private void resetPracticeUi() {
        activeQuestions = new ArrayList<>();
        questionIndex = 0;
        score = 0;
        questionMeta.setText("select a topic to begin");
        clearQuestionFields();
    }

    private void clearQuestionFields() {
        questionPrompt.setText("");
        expectedOutput.setText("");
        solutionSteps.setText("");
        optimalCode.setText("");
        answerField.setText("");
        resultLabel.setText(" ");
    }

    private void showCard(String name) {
        contentCards.show(contentPanel, name);
    }

    private JTextArea createReadArea(int rows) {
        JTextArea area = new JTextArea(rows, 30);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(247, 247, 242));
        area.setForeground(INK);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(223, 223, 216)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return area;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ACCENT);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private JButton createNavButton(String label) {
        JButton button = new JButton(label);
        button.setFocusPainted(false);
        button.setBackground(new Color(241, 241, 236));
        button.setForeground(INK);
        button.setBorder(BorderFactory.createLineBorder(new Color(222, 222, 215)));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return button;
    }

    private String normalize(String value) {
        return value.replace("\r\n", "\n").trim();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
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
