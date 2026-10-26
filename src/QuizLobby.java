// QuizLobby.java
// by Mohd Aeman (105045)
// Enhanced and Adjusted by:
// Jayden Aaric anak Pita (104498)
// Modified by Marcella (104863) - Resized to 360x780
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class QuizLobby extends JFrame 
{

    private String playerName = "";
    private String difficulty = null;

    public QuizLobby() 
    {

        setTitle("Quiz Lobby");
        setSize(360, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        bg.setLayout(null);
        setContentPane(bg);

        JLabel title = new JLabel("QUIZ LOBBY", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setForeground(Color.BLACK);
        title.setBounds(30, 20, 300, 40);
        bg.add(title);

        JTextField nameField = new JTextField();
        nameField.setBounds(70, 100, 220, 35);
        bg.add(nameField);

        JButton enterNameBtn = new JButton("Enter Name");
        enterNameBtn.setBounds(110, 155, 140, 35);
        styleButton(enterNameBtn, new Color(52, 152, 219));
        bg.add(enterNameBtn);

        enterNameBtn.addActionListener(e -> {

            String input = nameField.getText().trim();

            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name!");
                return;
            }

            playerName = input;

            JOptionPane.showMessageDialog(this, "Welcome " + playerName + "!");
        });

        JButton easyBtn = new JButton("Easy");
        JButton mediumBtn = new JButton("Medium");
        JButton hardBtn = new JButton("Hard");

        easyBtn.setBounds(110, 225, 140, 35);
        mediumBtn.setBounds(110, 275, 140, 35);
        hardBtn.setBounds(110, 325, 140, 35);

        styleButton(easyBtn, new Color(46, 204, 113));
        styleButton(mediumBtn, new Color(255, 140, 0));
        styleButton(hardBtn, new Color(231, 76, 60));

        bg.add(easyBtn);
        bg.add(mediumBtn);
        bg.add(hardBtn);

        JButton[] diffBtns = {easyBtn, mediumBtn, hardBtn};

        easyBtn.addActionListener(e ->
                selectDifficulty("easy", easyBtn, diffBtns));

        mediumBtn.addActionListener(e ->
                selectDifficulty("medium", mediumBtn, diffBtns));

        hardBtn.addActionListener(e ->
                selectDifficulty("hard", hardBtn, diffBtns));

        JButton startBtn = new JButton("Start Quiz");
        startBtn.setBounds(110, 390, 140, 35);
        styleButton(startBtn, new Color(0, 120, 255));
        bg.add(startBtn);

        startBtn.addActionListener(e -> 
            {

            if (playerName == null || playerName.trim().isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please enter your name first!");
                return;
            }

            if (difficulty == null) 
            
            {
                JOptionPane.showMessageDialog(this, "Please select difficulty!");
                return;
            }

            SoundManager.stop();
            dispose();

            SwingUtilities.invokeLater(() ->
                    new QuizEngine(playerName, difficulty));
        });

        JButton leaderboardBtn = new JButton("Leaderboard");
        leaderboardBtn.setBounds(110, 455, 140, 35);
        styleButton(leaderboardBtn, new Color(155, 89, 182));
        bg.add(leaderboardBtn);

        leaderboardBtn.addActionListener(e -> 
            
        {

            SoundManager.stop();

            new LeaderboardWindow(() ->
                    SoundManager.play("music/quizlobby.wav", true));
        });

        JToggleButton muteBtn = new JToggleButton("Mute");
        muteBtn.setBounds(120, 530, 120, 40);
        bg.add(muteBtn);

        muteBtn.addActionListener(e -> 
        
            {

            SoundManager.toggleMute();

            if (SoundManager.isMuted()) {
                muteBtn.setText("Unmute");
                SoundManager.stop();
            } else {
                muteBtn.setText("Mute");
                SoundManager.play("music/quizlobby.wav", true);
            }
        });

        SoundManager.play("music/quizlobby.wav", true);

        addWindowListener(new WindowAdapter() 
        {

            @Override
            public void windowClosing(WindowEvent e) 

            {
                SoundManager.stop();
            }

            @Override
            public void windowClosed(WindowEvent e) 

            {
                SoundManager.stop();
            }
        });

        setVisible(true);
    }

    private void selectDifficulty(String diff, JButton selected, JButton[] all) 
    {

        difficulty = diff;

        for (JButton b : all) 
            {

            if (b == selected) 
                {
                b.setBackground(b.getBackground().darker());
                b.setBorder(new LineBorder(Color.GREEN, 4));
                } 
                
                else 
                
                {
                b.setBorder(new LineBorder(Color.RED, 2));
                if (b.getText().equalsIgnoreCase("Easy")) {
                    b.setBackground(new Color(46, 204, 113));
                }

                if (b.getText().equalsIgnoreCase("Medium")) {
                    b.setBackground(new Color(255, 140, 0));
                }

                if (b.getText().equalsIgnoreCase("Hard")) {
                    b.setBackground(new Color(231, 76, 60));
                }
            }
        }
    }

    private void styleButton(JButton btn, Color c) 
    {

        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(new LineBorder(c.darker(), 2));

        Color hover = c.brighter();

        btn.addMouseListener(new MouseAdapter() 
        {

            public void mouseEntered(MouseEvent e) 
            
            {
                btn.setBackground(hover);
            }

            public void mouseExited(MouseEvent e) 
            
            {
                btn.setBackground(c);
            }
        });
    }
}