import java.util.List;

public class Question {
    private final String topic;
    private final String title;
    private final String prompt;
    private final String expectedOutput;
    private final List<String> solutionSteps;
    private final String optimalCode;

    public Question(String topic,
                    String title,
                    String prompt,
                    String expectedOutput,
                    List<String> solutionSteps,
                    String optimalCode) {
        this.topic = topic;
        this.title = title;
        this.prompt = prompt;
        this.expectedOutput = expectedOutput;
        this.solutionSteps = solutionSteps;
        this.optimalCode = optimalCode;
    }

    public String getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public List<String> getSolutionSteps() {
        return solutionSteps;
    }

    public String getOptimalCode() {
        return optimalCode;
    }
}
