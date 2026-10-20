// QuizEngine.java
// by Mohd Aeman (105045)
// Enhanced and Adjusted by:
// Jayden Aaric anak Pita (104498)
// Modified by Marcella (104863):
// - Resized to 360x780 Resized to 360x780, widened labels, content area with text margin.
// - Added [CORRECT]/[WRONG] feedback label with auto-hide timer.
// - Added playSfx() for direct sound playback without delay.
// - Disabled "Next" button during feedback to prevent double-click.
// - Added custom EnergyHeroException handling.

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.sound.sampled.*;
import javax.swing.*;

public class QuizEngine extends JFrame implements QuestionBank {

    private ArrayList<Question> questions = new ArrayList<>();

    private int currentIndex = 0;
    private int score = 0;
    private String playerName;
    private String difficulty;

    private JLabel questionLabel;
    private JLabel progressLabel;
    private JProgressBar progressBar;

    private GlowOptionButton[] optionButtons;
    private ButtonGroup group;

    private JButton nextBtn;
    private JButton prevBtn;

    private JToggleButton muteBtn;
    private JLabel timerLabel;
    private JLabel resultLabel;

    private Timer countdownTimer;
    private int timeLeft;

    public QuizEngine(String playerName, String difficulty) {

        this.playerName = playerName;
        this.difficulty = difficulty;

        setTitle("Energy Hero");
        setSize(360, 780);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
        });

        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        bg.setLayout(null);
        setContentPane(bg);

        // ================= TITLE =================
        JLabel title = new JLabel("ENERGY HERO", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setBounds(30, 20, 300, 40);
        bg.add(title);

        // ================= QUESTION =================
        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Modified by Marcella - smaller font for wrap
        questionLabel.setBounds(15, 55, 330, 80); // Modified by Marcella - taller bounds for 2 lines
        questionLabel.setForeground(Color.BLACK);
        bg.add(questionLabel);

        // ================= PROGRESS =================
        progressLabel = new JLabel("", SwingConstants.CENTER);
        progressLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        progressLabel.setBounds(110, 130, 140, 20);
        progressLabel.setForeground(Color.BLACK);
        bg.add(progressLabel);

        progressBar = new JProgressBar();
        progressBar.setBounds(30, 155, 300, 12);
        progressBar.setStringPainted(true);
        bg.add(progressBar);

        // ================= RESULT FEEDBACK =================
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resultLabel.setBounds(15, 175, 330, 25);
        resultLabel.setVisible(false);
        bg.add(resultLabel);

        // ================= OPTIONS =================
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setBounds(15, 200, 330, 240); // Modified by Marcella - increased height for text wrap
        optionsPanel.setOpaque(false);

        optionButtons = new GlowOptionButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new GlowOptionButton();
            group.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        bg.add(optionsPanel);

        // ================= TIMER =================
        timerLabel = new JLabel("Time: 20", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        timerLabel.setBounds(110, 450, 140, 30); // Modified by Marcella - moved down slightly
        timerLabel.setForeground(Color.BLACK);
        bg.add(timerLabel);

        // ================= BUTTONS =================
        prevBtn = new JButton("Back");
        nextBtn = new JButton("Next");

        prevBtn.setBounds(45, 495, 125, 40); // Modified by Marcella - moved down slightly
        nextBtn.setBounds(190, 495, 125, 40); // Modified by Marcella - moved down slightly

        bg.add(prevBtn);
        bg.add(nextBtn);

        prevBtn.addActionListener(e -> previous());
        nextBtn.addActionListener(e -> { nextBtn.setEnabled(false); checkAnswer(); });

        // ================= MUSIC (FIXED) =================
        Timer soundDelay = new Timer(300, e -> {
        SoundManager.play("music/inQuiz.wav", true);
        });

        soundDelay.setRepeats(false);
        soundDelay.start();

        // ================= MUTE =================
        muteBtn = new JToggleButton("Mute");
        muteBtn.setBounds(120, 555, 120, 40); // Modified by Marcella - moved down slightly
        bg.add(muteBtn);

        muteBtn.addActionListener(e -> {
            SoundManager.toggleMute();

            if (SoundManager.isMuted()) {
                muteBtn.setText("Unmute");
            } else {
                muteBtn.setText("Mute");
            }
        });

        // ================= LOAD QUESTIONS =================
        loadQuestions(difficulty);

        progressBar.setMaximum(getTotalQuestions());
        showQuestion(0);

        setVisible(true);
    }

    // ================= CLEANUP =================
    private void cleanup() {
        if (countdownTimer != null) countdownTimer.stop();
        SoundManager.stop();
    }

    @Override
    public void dispose() {
        cleanup();
        super.dispose();
    }

    // ================= CUSTOM BUTTON =================
    class GlowOptionButton extends JRadioButton {

        private boolean hover = false;

        public GlowOptionButton() {
            setFont(new Font("Segoe UI", Font.BOLD, 12)); // Modified by Marcella - smaller font for text wrap
            setOpaque(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setVerticalAlignment(SwingConstants.CENTER);
            setHorizontalAlignment(SwingConstants.CENTER);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRoundRect(5, 6, w - 10, h - 10, 20, 20);

            g2.setColor(
                    hover
                            ? new Color(255, 235, 180, 220)
                            : new Color(255, 248, 220, 220)
            );

            g2.fillRoundRect(0, 0, w - 10, h - 10, 20, 20);

            super.paintComponent(g);
        }
    }

    // ================= QUESTION FLOW =================
    private void showQuestion(int index) {

        if (countdownTimer != null) countdownTimer.stop();

        Question q = getQuestion(index);

        questionLabel.setText("<html><div style='text-align:center'>" + q.getText() + "</div></html>"); // Modified by Marcella - HTML wrap

        String[] opts = q.getOptions();

        for (int i = 0; i < optionButtons.length; i++) {
            if (i < opts.length) {
                optionButtons[i].setVisible(true);
                optionButtons[i].setText("<html><center>" + opts[i] + "</center></html>"); // Modified by Marcella - HTML wrap for long text
            } else {
                optionButtons[i].setVisible(false);
            }
        }

        group.clearSelection();

        progressLabel.setText("Q " + (index + 1) + " / " + getTotalQuestions());
        progressBar.setValue(index + 1);

        nextBtn.setEnabled(true);

        timeLeft = 20;
        timerLabel.setText("Time: " + timeLeft);

        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);

            if (timeLeft <= 0) {
                nextQuestion();
            }
        });

        countdownTimer.start();
    }

    private void nextQuestion() {
        currentIndex++;

        if (currentIndex < getTotalQuestions()) {
            showQuestion(currentIndex);
        } else {
            showResult();
        }
    }

   private void checkAnswer() {

        if (countdownTimer != null) countdownTimer.stop();

        Question q = getQuestion(currentIndex);

        boolean isCorrect = false;

        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                if (i == q.getCorrectIndex()) {
                    score++;
                    isCorrect = true;
                }
                break;
            }
        }

        String feedback = isCorrect ? "[CORRECT]" : "[WRONG]";
        Color fbColor = isCorrect ? new Color(46, 204, 113) : new Color(231, 76, 60);

        resultLabel.setText(feedback);
        resultLabel.setForeground(fbColor);
        resultLabel.setVisible(true);

        Timer hideFeedback = new Timer(800, e -> resultLabel.setVisible(false));
        hideFeedback.setRepeats(false);
        hideFeedback.start();

        playSfx(isCorrect ? "music/correct.wav" : "music/wrong.wav");

        Timer goNext = new Timer(600, e -> nextQuestion());
        goNext.setRepeats(false);
        goNext.start();
    }
    
    private void playSfx(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) return;
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip sfx = AudioSystem.getClip();
            sfx.open(audio);
            sfx.start();
        } catch (Exception e) {
            // ignore sound errors
        }
    }


    private void previous() {
        if (currentIndex > 0) {
            currentIndex--;
            showQuestion(currentIndex);
        }
    }

    // ================= RESULT =================
   private void showResult() {

    if (countdownTimer != null) countdownTimer.stop();

    SoundManager.playEffect("music/endQuiz.wav");

    int percent = (score * 100) / getTotalQuestions();

    // Save score to leaderboard
    Leaderboard.addEntry(playerName, score, difficulty);

    // Open gamification result screen
    new ResultScreen(percent);

    dispose();
}

    // ================= QUESTION BANK =================
    @Override
    public void addQuestion(Question q) {
        questions.add(q);
    }

    @Override
    public Question getQuestion(int index) {
        return questions.get(index);
    }

    @Override
    public int getTotalQuestions() {
        return questions.size();
    }

    // ================= FULL QUESTIONS =================
    private void loadQuestions(String difficulty) {

    ArrayList<Question> easy = new ArrayList<>();
    ArrayList<Question> medium = new ArrayList<>();
    ArrayList<Question> hard = new ArrayList<>();

    // ==================== EASY (20 Questions) ====================

    // MCQQuestion (10)
    easy.add(new MCQQuestion("What is the main focus of SDG 7?",
            new String[]{"Clean Water", "Affordable and Clean Energy", "Climate Action", "Quality Education"}, 1));

    easy.add(new MCQQuestion("Energy is the ability to:",
            new String[]{"Produce water", "Create matter", "Do work or cause change", "Stop movement"}, 2));

    easy.add(new MCQQuestion("Which of the following is a form of energy?",
            new String[]{"Plastic", "Thermal Energy", "Metal", "Glass"}, 1));

    easy.add(new MCQQuestion("Which sector depends on energy?",
            new String[]{"Healthcare", "Transportation", "Industry", "All of the above"}, 3));

    easy.add(new MCQQuestion("Which is a renewable energy source?",
            new String[]{"Coal", "Oil", "Solar", "Natural Gas"}, 2));

    easy.add(new MCQQuestion("Renewable energy comes from sources that are:",
            new String[]{"Limited", "Continuously replenished", "Imported", "Artificial"}, 1));

    easy.add(new MCQQuestion("Which renewable source uses sunlight?",
            new String[]{"Biomass", "Wind", "Solar", "Hydro"}, 2));

    easy.add(new MCQQuestion("Which is a non-renewable energy source?",
            new String[]{"Wind", "Hydro", "Coal", "Solar"}, 2));

    easy.add(new MCQQuestion("Energy conservation helps to:",
            new String[]{"Waste electricity", "Increase pollution", "Reduce electricity costs", "Increase fuel use"}, 2));

    easy.add(new MCQQuestion("Which appliance is usually more energy-efficient?",
            new String[]{"LED Light", "Incandescent Bulb", "Oil Lamp", "Candle"}, 0));

    // TrueFalseQuestion (10)
    easy.add(new TrueFalseQuestion("Energy can be created and destroyed.", 1));
    easy.add(new TrueFalseQuestion("Energy is essential for daily life.", 0));
    easy.add(new TrueFalseQuestion("Electrical energy is a form of energy.", 0));
    easy.add(new TrueFalseQuestion("Renewable energy has lower environmental impact.", 0));
    easy.add(new TrueFalseQuestion("Wind energy is renewable.", 0));
    easy.add(new TrueFalseQuestion("Coal is a renewable energy source.", 1));
    easy.add(new TrueFalseQuestion("Turning off unused devices conserves energy.", 0));
    easy.add(new TrueFalseQuestion("Solar energy comes from sunlight.", 0));
    easy.add(new TrueFalseQuestion("Energy powers communication systems.", 0));
    easy.add(new TrueFalseQuestion("Renewable energy sources will never be replenished.", 1));

    // ==================== MEDIUM (20 Questions) ====================

    // MCQQuestion (10)
    medium.add(new MCQQuestion("Energy sources are generally classified into:",
            new String[]{"Liquid and Solid", "Renewable and Non-renewable", "Cheap and Expensive", "Large and Small"}, 1));

    medium.add(new MCQQuestion("Which form of energy is stored due to position?",
            new String[]{"Kinetic", "Thermal", "Potential", "Electrical"}, 2));

    medium.add(new MCQQuestion("Which energy source relies on moving air?",
            new String[]{"Solar", "Wind", "Biomass", "Coal"}, 1));

    medium.add(new MCQQuestion("Why is renewable energy considered sustainable?",
            new String[]{"It is imported", "It is naturally replenished", "It is unlimited electricity", "It is always cheap"}, 1));

    medium.add(new MCQQuestion("Which is NOT a renewable energy source?",
            new String[]{"Hydro", "Solar", "Natural Gas", "Wind"}, 2));

    medium.add(new MCQQuestion("Fossil fuels contribute to:",
            new String[]{"Climate Change", "Air Pollution", "Greenhouse Gas Emissions", "All of the above"}, 3));

    medium.add(new MCQQuestion("Which technology improves energy distribution and management?",
            new String[]{"Smart Grid", "Calculator", "Generator Only", "Battery Charger"}, 0));

    medium.add(new MCQQuestion("What is a disadvantage of renewable energy?",
            new String[]{"Pollution", "Limited Resources", "Weather Dependence", "Toxic Waste"}, 2));

    medium.add(new MCQQuestion("Which action supports energy conservation?",
            new String[]{"Leaving lights on", "Using efficient appliances", "Wasting electricity", "Running unused devices"}, 1));

    medium.add(new MCQQuestion("Biomass is classified as:",
            new String[]{"Renewable Energy", "Non-renewable Energy", "Fossil Fuel", "Nuclear Fuel"}, 0));

    // TrueFalseQuestion (10)
    medium.add(new TrueFalseQuestion("Renewable energy sources include biomass.", 0));
    medium.add(new TrueFalseQuestion("Fossil fuels produce greenhouse gases.", 0));
    medium.add(new TrueFalseQuestion("Natural gas is renewable.", 1));
    medium.add(new TrueFalseQuestion("Energy conservation reduces energy waste.", 0));
    medium.add(new TrueFalseQuestion("Smart grids are part of future energy development.", 0));
    medium.add(new TrueFalseQuestion("Renewable energy always works regardless of weather.", 1));
    medium.add(new TrueFalseQuestion("Hydropower uses water to generate energy.", 0));
    medium.add(new TrueFalseQuestion("Energy supports education and healthcare.", 0));
    medium.add(new TrueFalseQuestion("Non-renewable resources can be replaced quickly.", 1));
    medium.add(new TrueFalseQuestion("Using LED lights can improve energy efficiency.", 0));

    // ==================== HARD (20 Questions) ====================

    // MCQQuestion (10)
    hard.add(new MCQQuestion("Which statement best describes the law of energy conservation?",
            new String[]{"Energy can be created", "Energy can be destroyed", "Energy can only be converted between forms", "Energy disappears after use"}, 2));

    hard.add(new MCQQuestion("Why are non-renewable resources considered unsustainable?",
            new String[]{"They are weather dependent", "They take millions of years to form", "They are always expensive", "They cannot produce electricity"}, 1));

    hard.add(new MCQQuestion("Which combination consists entirely of renewable energy sources?",
            new String[]{"Coal, Solar, Wind", "Oil, Hydro, Biomass", "Solar, Wind, Hydro", "Natural Gas, Wind, Solar"}, 2));

    hard.add(new MCQQuestion("Which factor makes non-renewable energy widely used?",
            new String[]{"Low pollution", "Unlimited supply", "High output and reliability", "No environmental impact"}, 2));

    hard.add(new MCQQuestion("Which energy source contributes least to greenhouse gas emissions during operation?",
            new String[]{"Coal", "Oil", "Natural Gas", "Wind"}, 3));

    hard.add(new MCQQuestion("Which technology is important for storing renewable energy?",
            new String[]{"Batteries", "Candles", "Transformers Only", "Diesel Engines"}, 0));

    hard.add(new MCQQuestion("What is one major goal of the global energy transition?",
            new String[]{"Increase pollution", "Reduce renewable energy", "Reduce environmental impact", "Increase fossil fuel use"}, 2));

    hard.add(new MCQQuestion("Which sector benefits directly from reliable energy access?",
            new String[]{"Healthcare", "Industry", "Communication", "All of the above"}, 3));

    hard.add(new MCQQuestion("Which energy source is dependent on weather conditions?",
            new String[]{"Coal", "Solar", "Natural Gas", "Uranium"}, 1));

    hard.add(new MCQQuestion("Which is NOT a benefit of energy conservation?",
            new String[]{"Reduced costs", "Reduced pollution", "Increased waste", "Resource preservation"}, 2));

    // TrueFalseQuestion (10)
    hard.add(new TrueFalseQuestion("Renewable energy helps reduce greenhouse gas emissions.", 0));
    hard.add(new TrueFalseQuestion("A stable energy supply contributes to economic growth.", 0));
    hard.add(new TrueFalseQuestion("Fossil fuels have no effect on climate change.", 1));
    hard.add(new TrueFalseQuestion("Energy storage technologies can improve renewable energy reliability.", 0));
    hard.add(new TrueFalseQuestion("Renewable energy sources generally have lower environmental impacts than fossil fuels.", 0));
    hard.add(new TrueFalseQuestion("Coal, oil, and natural gas are all examples of non-renewable energy.", 0));
    hard.add(new TrueFalseQuestion("The future of energy focuses on sustainable systems.", 0));
    hard.add(new TrueFalseQuestion("Renewable energy sources are always cheaper to install initially than fossil fuel systems.", 1));
    hard.add(new TrueFalseQuestion("Energy conservation can help achieve environmental sustainability.", 0));
    hard.add(new TrueFalseQuestion("Long-term energy security can be supported by increasing renewable energy adoption.", 0));

    // ==================== Load by Difficulty ====================

    Collections.shuffle(easy);
    Collections.shuffle(medium);
    Collections.shuffle(hard);

    switch (difficulty.toLowerCase()) {
        case "easy":
            questions.addAll(easy);
            break;
        case "medium":
            questions.addAll(medium);
            break;
        case "hard":
            questions.addAll(hard);
            break;
    }
}
}