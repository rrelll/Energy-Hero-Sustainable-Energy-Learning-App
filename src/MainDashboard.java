import java.awt.*;
import java.io.File;
import javax.swing.*;

//by Darryl (103794)
// Modified by Marcella (104863):
// - Resized to 360x780
// - Added background music (music/maindashboard.wav) with timer delay
// - Added mute/unmute toggle button synced with SoundManager
// - Added window listener to stop music on close

public class MainDashboard extends JFrame {

    // Declare the mute button as a class instance variable
    private JToggleButton muteBtn;

    public MainDashboard() {

        setTitle("Energy Hero - SDG 7");
        setSize(360, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //BACKGROUND 
        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        setContentPane(bg);
        bg.setLayout(null);

        // TITLE SHADOW 
        JLabel shadow = new JLabel("ENERGY HERO", SwingConstants.CENTER);
        shadow.setFont(new Font("Poppins", Font.BOLD, 26));
        shadow.setForeground(new Color(0, 0, 0, 120));
        shadow.setBounds(52, 27, 300, 50);

        //MAIN TITLE 
        JLabel title = new JLabel("ENERGY HERO", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 26));
        title.setForeground(new Color(255, 215, 0)); // gold
        title.setBounds(50, 25, 300, 50);

        //SUBTITLE 
        JLabel subtitle = new JLabel("Sustainable Energy Learning App", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(255, 215, 0)); 
        subtitle.setBounds(50, 70, 300, 20);

        bg.add(shadow);
        bg.add(title);
        bg.add(subtitle);

        //BUTTONS WITH ICONS
        JButton learnBtn = new JButton("Learn SDG 7", loadIcon("learn.jpeg"));
        JButton quizBtn = new JButton("Quiz", loadIcon("quiz.jpeg"));
        JButton badgeBtn = new JButton("Badges", loadIcon("badge.jpeg"));
        JButton exitBtn = new JButton("Exit", loadIcon("exit.jpeg"));

        styleCardButton(learnBtn, new Color(52, 152, 219));   // blue
        styleCardButton(quizBtn, new Color(46, 204, 113));    // green
        styleCardButton(badgeBtn, new Color(155, 89, 182));   // purple
        styleCardButton(exitBtn, new Color(231, 76, 60));     // red

        learnBtn.setBounds(80, 130, 240, 60);
        quizBtn.setBounds(80, 210, 240, 60);
        badgeBtn.setBounds(80, 290, 240, 60);
        exitBtn.setBounds(80, 370, 240, 60);

        bg.add(learnBtn);
        bg.add(quizBtn);
        bg.add(badgeBtn);
        bg.add(exitBtn);

        // ACTIONS 
        learnBtn.addActionListener(e -> new LearningModule());

        // ✅ Updated: open QuizLobby instead of QuizEngine directly
        quizBtn.addActionListener(e -> new QuizLobby());
        badgeBtn.addActionListener(e -> new BadgeMenu());
        exitBtn.addActionListener(e -> System.exit(0));

        // ================= BACKGROUND MUSIC =================
        // Uses a slight timer delay to let the UI render first, calling your SoundManager
        Timer soundDelay = new Timer(300, e -> {
            SoundManager.play("music/maindashboard.wav", true); 
        });
        soundDelay.setRepeats(false);
        soundDelay.start();

        // ================= MUTE TOGGLE =================
        // Automatically checks if the game was already muted in a previous window
        String initialText = SoundManager.isMuted() ? "Unmute" : "Mute";
        muteBtn = new JToggleButton(initialText);
        muteBtn.setSelected(SoundManager.isMuted());
        
        muteBtn.setBounds(80, 450, 240, 40);    
        muteBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        muteBtn.setForeground(Color.WHITE);
        muteBtn.setBackground(new Color(52, 152, 219)); // blue
        muteBtn.setFocusPainted(false);
        muteBtn.setBorderPainted(false);
        muteBtn.setOpaque(true);
        muteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        muteBtn.addActionListener(e -> {
            SoundManager.toggleMute();
            if (SoundManager.isMuted()) {
                muteBtn.setText("Unmute");
            } else {
                muteBtn.setText("Mute");
            }
        });
        bg.add(muteBtn);

        setVisible(true);
    }

    // STYLE BUTTON
    private void styleCardButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);

        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(12);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
    }

    // LOAD ICON 
    private ImageIcon loadIcon(String fileName) {
        String path = System.getProperty("user.dir")
                + File.separator
                + "images"
                + File.separator
                + fileName;

        ImageIcon icon = new ImageIcon(path);

        if (icon.getIconWidth() == -1) {
            System.out.println("Icon not found: " + path);
            return null;
        }

        Image img = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}