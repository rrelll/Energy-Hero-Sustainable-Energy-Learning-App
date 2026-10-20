// BadgeMenu.java
// Written by: Jayden Aaric anak Pita (100498)
// Modified by Marcella (104863) - Resized to 360x780
// Task: Gamification & Data Developer
// Features: Badge Records, Stars, Leaderboard, Data Persistence
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import javax.swing.*;

public class BadgeMenu extends JFrame 
{

    private JPanel recordsPanel;

    public BadgeMenu() 
    
    {

        setTitle("Badge Menu");
        setSize(360, 780);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        SoundManager.play("music/badge.wav", true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SoundManager.stop();
            }
        });

        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        setContentPane(bg);
        setLayout(null);

        JLabel title = new JLabel("BADGE RECORDS", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 22));
        title.setForeground(Color.BLACK);
        title.setBounds(30, 15, 300, 40);
        bg.add(title);

        recordsPanel = new JPanel();
        recordsPanel.setLayout(new BoxLayout(recordsPanel, BoxLayout.Y_AXIS));
        recordsPanel.setOpaque(false);
        recordsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(recordsPanel);
        scrollPane.setBounds(10, 70, 340, 530);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(155, 89, 182), 2));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        bg.add(scrollPane);

        JButton refreshBtn = new JButton("Refresh");
        JButton closeBtn = new JButton("Close");

        styleButton(refreshBtn, new Color(52, 152, 219));
        styleButton(closeBtn, new Color(231, 76, 60));

        refreshBtn.setBounds(40, 625, 280, 40);
        closeBtn.setBounds(40, 680, 280, 40);

        bg.add(refreshBtn);
        bg.add(closeBtn);

        refreshBtn.addActionListener(e -> loadBadgeRecords());
        closeBtn.addActionListener(e -> dispose());

        loadBadgeRecords();

        setVisible(true);
    }

    private void loadBadgeRecords() 
    {

        recordsPanel.removeAll();

        List<Leaderboard.Entry> entries = Leaderboard.getEntries();

        if (entries.isEmpty()) {

            JLabel emptyLabel = new JLabel(
                    "<html><center>No player records yet.<br>Play a quiz first to earn badges.</center></html>",
                    SwingConstants.CENTER
            );

            emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            recordsPanel.add(Box.createVerticalStrut(160));
            recordsPanel.add(emptyLabel);

        } 
        
        else 
                
        {

            for (Leaderboard.Entry entry : entries) 
                
            {

                int percent = calculatePercentage(entry.score, entry.difficulty);
                String badge = BadgeSystem.getBadge(percent);

                JPanel card = createRecordCard(entry, percent, badge);
                card.setAlignmentX(Component.CENTER_ALIGNMENT);

                recordsPanel.add(card);
                recordsPanel.add(Box.createVerticalStrut(15));
            }

        }

        recordsPanel.revalidate();
        recordsPanel.repaint();
    }

    private JPanel createRecordCard(Leaderboard.Entry entry, int percent, String badge) 
    
    {

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(new Color(255, 255, 255, 235));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBadgeColor(percent), 3),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        card.setMaximumSize(new Dimension(290, 230));
        card.setPreferredSize(new Dimension(290, 230));
        card.setMinimumSize(new Dimension(290, 230));

        JLabel nameLabel = new JLabel("Player: " + entry.name);
        JLabel diffLabel = new JLabel("Difficulty: " + entry.difficulty);
        JLabel scoreLabel = new JLabel("Score: " + entry.score + "  (" + percent + "%)");
        JLabel badgeLabel = new JLabel("Badge: " + badge);
        JLabel timeLabel = new JLabel("Time: " + entry.finishTime);
        JLabel quoteLabel = new JLabel(getQuote(percent));

        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        diffLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        quoteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));

        badgeLabel.setForeground(getBadgeColor(percent));

        nameLabel.setBounds(10, 10, 280, 20);
        diffLabel.setBounds(10, 34, 280, 18);
        scoreLabel.setBounds(10, 54, 280, 18);
        badgeLabel.setBounds(10, 76, 280, 20);

        JPanel starPanel = createStarPanel(percent);
        starPanel.setBounds(10, 100, 280, 36);

        timeLabel.setBounds(10, 145, 280, 16);
        quoteLabel.setBounds(10, 165, 280, 16);

        card.add(nameLabel);
        card.add(diffLabel);
        card.add(scoreLabel);
        card.add(badgeLabel);
        card.add(starPanel);
        card.add(timeLabel);
        card.add(quoteLabel);

        return card;
    }

    private JPanel createStarPanel(int percentage) 
    
    {

        JPanel panel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 6, 0)
        );
        panel.setOpaque(false);

        int starCount;
        String starFile;

        if (percentage >= 80) 
        {
            starCount = 5;
            starFile = "GoldStar.png";
        } 
        else if (percentage >= 60) 
        {
            starCount = 4;
            starFile = "SilverStar.png";
        } 
        else if (percentage >= 40) 
        {
            starCount = 3;
            starFile = "BronzeStar.png";
        } 
        else if (percentage >= 20) 
        {
            starCount = 2;
            starFile = "BronzeStar.png";
        } 
        else 
        {
            starCount = 1;
            starFile = "BronzeStar.png";
        }

        String path = System.getProperty("user.dir")
                + File.separator
                + "images"
                + File.separator
                + starFile;

        ImageIcon icon = new ImageIcon(path);

        if (icon.getIconWidth() == -1) 
        
        {
            panel.add(new JLabel("Stars: " + starCount));
            System.out.println("Star image not found: " + path);
            return panel;
        }

        Image img = icon.getImage().getScaledInstance
        (
                38,
                38,
                Image.SCALE_SMOOTH
        );

        ImageIcon scaled = new ImageIcon(img);

        for (int i = 0; i < starCount; i++) 
        {
            panel.add(new JLabel(scaled));
        }

        return panel;
    }

    private String getQuote(int percent) 
    {

        if (percent >= 80) 
        {
            return "Outstanding!";
        } 
        else if (percent >= 60) 
        {
            return "That's good!";
        } 
        else if (percent >= 40) 
        {
            return "Good try!";
        } 
        else if (percent >= 20) 
        {
            return "You can do better!";
        } 
        else 
        {
            return "Don't give up!";
        }
    }

    private int calculatePercentage(int score, String difficulty) 
    {

        int totalQuestions;

        if (difficulty.equalsIgnoreCase("easy")) 
        {
            totalQuestions = 10;
        } 
        else if (difficulty.equalsIgnoreCase("medium")) 
        {
            totalQuestions = 15;
        } 
        else 
        {
            totalQuestions = 20;
        }

        return (score * 100) / totalQuestions;
    }

    private Color getBadgeColor(int percentage) 
    {

        if (percentage >= 80) 
        {
            return new Color(218, 165, 32);
        } 
        else if (percentage >= 60) 
        {
            return new Color(160, 160, 160);
        } 
        else if (percentage >= 40) 
        {
            return new Color(184, 115, 51);
        } 
        else 
        {
            return Color.BLACK;
        }
    }

    private void styleButton(JButton btn, Color color) 
    {

        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}