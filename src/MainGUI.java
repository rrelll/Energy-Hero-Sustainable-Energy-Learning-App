// MainGUI.java
// by Marcella (104863)
// GUI & Integration Developer
// Responsibilities: Smartphone-style GUI, Module Integration, Command Line Execution
// Modified by Marcella (104863):
// - Resized to 360x780 (smartphone-style portrait)
// - Added background music (music/maindashboard.wav)
// - Added mute/unmute toggle button
// - Increased card height (120→135) & description area (50→70) to fix text cutoff
// - Changed "Tap to open" to "->  EXPLORE" in accent color
// - Added window listener to stop music on close
// - Adjusted button layout to prevent overlapping

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainGUI extends JFrame implements UIHandler {

    private static final int WIDTH = 360;
    private static final int HEIGHT = 780;

    public MainGUI() {
        setTitle("Energy Hero - SDG 7");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        setContentPane(bg);
        bg.setLayout(null);

        JLabel shadow = new JLabel("ENERGY HERO", SwingConstants.CENTER);
        shadow.setFont(new Font("Poppins", Font.BOLD, 24));
        shadow.setForeground(new Color(0, 0, 0, 120));
        shadow.setBounds(32, 50, 300, 50);
        bg.add(shadow);

        JLabel title = new JLabel("ENERGY HERO", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(new Color(255, 215, 0));
        title.setBounds(30, 48, 300, 50);
        bg.add(title);

        JLabel subtitle = new JLabel("Sustainable Energy Learning App", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(Color.BLACK);
        subtitle.setBounds(30, 90, 300, 20);
        bg.add(subtitle);

        JLabel welcome = new JLabel("Welcome, Explorer!", SwingConstants.CENTER);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        welcome.setForeground(new Color(80, 80, 80));
        welcome.setBounds(30, 120, 300, 25);
        bg.add(welcome);

        int cardY = 160;
        int cardH = 135;
        int gap = 15;

        JPanel learnCard = createModuleCard(
            "LEARNING MODULE",
            "Learn about SDG 7 and sustainable energy through interactive lessons.",
            new Color(52, 152, 219),
            e -> openLearning()
        );
        learnCard.setBounds(30, cardY, WIDTH - 60, cardH);
        bg.add(learnCard);

        JPanel quizCard = createModuleCard(
            "QUIZ CHALLENGE",
            "Test your knowledge with quiz challenges at different difficulty levels.",
            new Color(46, 204, 113),
            e -> openQuiz()
        );
        quizCard.setBounds(30, cardY + cardH + gap, WIDTH - 60, cardH);
        bg.add(quizCard);

        JPanel badgeCard = createModuleCard(
            "BADGES & REWARDS",
            "View your achievements, badges, star ratings, and leaderboard rankings.",
            new Color(155, 89, 182),
            e -> openBadges()
        );
        badgeCard.setBounds(30, cardY + (cardH + gap) * 2, WIDTH - 60, cardH);
        bg.add(badgeCard);

        JButton exitBtn = new JButton("Exit");
        styleButton(exitBtn, new Color(231, 76, 60));
        exitBtn.setBounds(60, cardY + (cardH + gap) * 3 + 25, WIDTH - 120, 40);
        exitBtn.addActionListener(e -> exitApp());
        bg.add(exitBtn);

        JLabel homeBar = new JLabel("⎯", SwingConstants.CENTER);
        homeBar.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        homeBar.setForeground(new Color(0, 0, 0, 100));
        homeBar.setBounds(140, HEIGHT - 40, 120, 20);
        bg.add(homeBar);

        Timer soundDelay = new Timer(300, e -> {
            SoundManager.play("music/maindashboard.wav", true);
        });
        soundDelay.setRepeats(false);
        soundDelay.start();

        JToggleButton muteBtn = new JToggleButton(SoundManager.isMuted() ? "Unmute" : "Mute");
        muteBtn.setSelected(SoundManager.isMuted());
        muteBtn.setBounds(60, 680, 240, 40);
        muteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        muteBtn.setForeground(Color.WHITE);
        muteBtn.setBackground(new Color(52, 152, 219));
        muteBtn.setFocusPainted(false);
        muteBtn.setBorderPainted(false);
        muteBtn.setOpaque(true);
        muteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        muteBtn.addActionListener(e -> {
            SoundManager.toggleMute();
            muteBtn.setText(SoundManager.isMuted() ? "Unmute" : "Mute");
        });
        bg.add(muteBtn);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SoundManager.stop();
            }
        });

        setVisible(true);
    }

    private JPanel createModuleCard(String heading, String description, Color accent, ActionListener action) {
        JPanel card = new JPanel(null);
        card.setBackground(new Color(255, 255, 255, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headingLabel.setForeground(accent);
        headingLabel.setBounds(12, 10, 260, 22);
        card.add(headingLabel);

        JLabel descLabel = new JLabel("<html><body style='width:260px'>" + description + "</body></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(Color.DARK_GRAY);
        descLabel.setBounds(12, 36, 260, 60);
        card.add(descLabel);

        JLabel tapLabel = new JLabel("→ EXPLORE", SwingConstants.RIGHT);
        tapLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        tapLabel.setForeground(accent);
        tapLabel.setBounds(12, 98, 260, 18);
        card.add(tapLabel);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(card, ActionEvent.ACTION_PERFORMED, null));
            }
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(255, 255, 255, 250));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(255, 255, 255, 230));
            }
        });

        return card;
    }

    @Override
    public void openLearning() {
        SoundManager.stop();
        new LearningModule();
    }

    @Override
    public void openQuiz() {
        SoundManager.stop();
        new QuizLobby();
    }

    @Override
    public void openBadges() {
        SoundManager.stop();
        new BadgeMenu();
    }

    @Override
    public void exitApp() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit Energy Hero",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
