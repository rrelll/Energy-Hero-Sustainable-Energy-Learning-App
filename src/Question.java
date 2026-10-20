// Question.java
// by Mohd Aeman (105045)
// Updated to support subclass inheritance (MCQQuestion, TrueFalseQuestion)

public class Question {
    private String text;
    private String[] options;
    private int correctIndex;

    public Question(String text, String[] options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }

    // Overridden by subclasses to identify question type
    public String getType() {
        return "Question";
    }
}