// MCQQuestion.java
// Subclass of Question for Multiple Choice Questions (4 options)

public class MCQQuestion extends Question {

    public MCQQuestion(String text, String[] options, int correctIndex) {
        super(text, options, correctIndex);
    }

    @Override
    public String getType() {
        return "MCQ";
    }
}