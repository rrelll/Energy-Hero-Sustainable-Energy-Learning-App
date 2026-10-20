// TrueFalseQuestion.java
// Subclass of Question for True/False Questions (2 options)

public class TrueFalseQuestion extends Question {

    public TrueFalseQuestion(String text, int correctIndex) {
        super(text, new String[]{"True", "False"}, correctIndex);
    }

    @Override
    public String getType() {
        return "True/False";
    }
}