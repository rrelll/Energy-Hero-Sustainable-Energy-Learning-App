// ResultScreen.java
// Written by: Jayden Aaric anak Pita (100498)
// Task: Gamification & Data Developer
// Features: Badge Records, PNG Stars, Leaderboard, Data Persistence
// Modified by Marcella (104863) - Resized to 360x780
// Modified by Darryl (103794) - Fix the motivation box ratio

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class ResultScreen extends JFrame 
{

    public ResultScreen(int score) 
    {

        System.out.println("RESULT SCREEN RECEIVED SCORE = " + score);

        setTitle("Energy Hero Result");
        setSize(360, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        setContentPane(bg);
        bg.setLayout(null);

        JLabel title = new JLabel("ENERGY HERO RESULT", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 20));
        title.setForeground(new Color(0, 120, 70));
        title.setBounds(30, 20, 300, 40);
        bg.add(title);

        JLabel resultTitle = new JLabel
        (
                MotivationSystem.getMessage(score),
                SwingConstants.CENTER
        );
        resultTitle.setFont(new Font("Poppins", Font.BOLD, 24));
        resultTitle.setForeground(getResultColor(score));
        resultTitle.setBounds(30, 65, 300, 40);
        bg.add(resultTitle);

        JLabel motivationArea = new JLabel(
        "<html><div style='text-align:center;'>"
        + getDetailedMotivation(score)
        + "</div></html>",
        SwingConstants.CENTER
        );

        motivationArea.setFont(new Font("Segoe UI", Font.BOLD, 12));
        motivationArea.setBounds(30, 115, 300, 90);
        bg.add(motivationArea);

        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(new Color(255, 255, 255, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBadgeColor(score), 3),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setBounds(8, 240, 330, 190);

        JLabel scoreLabel = new JLabel("Final Score: " + score + "%", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        scoreLabel.setBounds(10, 12, 310, 28);
        card.add(scoreLabel);

        JPanel starPanel = createStarPanel(score);
        starPanel.setBounds(10, 45, 310, 45);
        card.add(starPanel);

        JLabel badgeLabel = new JLabel("Badge: " + BadgeSystem.getBadge(score), SwingConstants.CENTER);
        badgeLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        badgeLabel.setForeground(getBadgeColor(score));
        badgeLabel.setBounds(10, 95, 310, 28);
        card.add(badgeLabel);

        JLabel levelLabel = new JLabel("Level: " + LevelSystem.getLevel(score), SwingConstants.CENTER);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        levelLabel.setBounds(10, 130, 310, 28);
        card.add(levelLabel);

        bg.add(card);

        JLabel energyQuote = new JLabel(getEnergyQuote(score), SwingConstants.CENTER);
        energyQuote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        energyQuote.setForeground(new Color(30, 90, 50));
        energyQuote.setBounds(30, 450, 300, 30);
        bg.add(energyQuote);

        JButton closeBtn = new JButton("Back to Dashboard");
        closeBtn.setBounds(60, 510, 240, 45);
        styleButton(closeBtn, new Color(46, 204, 113));
        closeBtn.addActionListener(e -> dispose());
        bg.add(closeBtn);

        setVisible(true);
    }

    private JPanel createStarPanel(int percentage) 
    {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
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

        System.out.println("Percentage = " + percentage);
        System.out.println("Star File = " + starFile);
        System.out.println("Star Count = " + starCount);

        String path = System.getProperty("user.dir")
                + File.separator
                + "images"
                + File.separator
                + starFile;

        ImageIcon icon = new ImageIcon(path);

        if (icon.getIconWidth() == -1) 
        {
            panel.add(new JLabel("Star image not found"));
            System.out.println("Star image not found: " + path);
            return panel;
        }

        Image img = icon.getImage().getScaledInstance
        (
                40,
                40,
                Image.SCALE_SMOOTH
        );

        ImageIcon scaled = new ImageIcon(img);

        for (int i = 0; i < starCount; i++) 
        
        {
            panel.add(new JLabel(scaled));
        }

        return panel;
    }

    private String getDetailedMotivation(int percentage) 
    {

        if (percentage >= 80) 
        {
            return "Congratulations! You have demonstrated excellent knowledge "
                    + "of SDG 7 and sustainable energy concepts. Keep up the "
                    + "great work and continue being an Energy Hero!";
        } 
        else if (percentage >= 60) 
        {
            return "You have a good understanding of SDG 7. Keep learning "
                    + "about clean and affordable energy to reach the next level!";
        } 
        else if (percentage >= 40) 
        {
            return "You understand some energy concepts, but there is still "
                    + "room for improvement. Review the learning module and try again.";
        } 
        else if (percentage >= 20) 
        {
            return "You can improve by revisiting the SDG 7 learning materials. "
                    + "Every attempt brings you closer to becoming an Energy Hero.";
        } 
        else 
        {
            return "Do not give up. Learning takes time. Review the sustainable energy notes and "
                    + "try the quiz again.";
        }
    }

    private String getEnergyQuote(int percentage) 
    {

        if (percentage >= 80)
            return "You powered up the future with clean energy knowledge!";
       
        else if (percentage >= 60)
            return "Good progress! Your energy knowledge is growing.";
        
        else if (percentage >= 40)
            return "Keep charging your knowledge and try again.";
        
        else
            return "Every Energy Hero starts with one small step.";
    }

    private Color getResultColor(int percentage) 
    {

        if (percentage >= 80)
            return new Color(46, 204, 113);
        else if (percentage >= 60)
            return new Color(52, 152, 219);
        else if (percentage >= 40)
            return new Color(243, 156, 18);
        else if (percentage >= 20)
            return new Color(230, 126, 34);
        else
            return new Color(231, 76, 60);
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