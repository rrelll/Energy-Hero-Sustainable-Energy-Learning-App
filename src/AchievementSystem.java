// AchievementSystem.java
// Written by: Jayden Aaric anak Pita (100498)
// Task: Gamification & Data Developer
// Features: Badge Records, Stars, Leaderboard, Data Persistence

import javax.swing.*;

public class AchievementSystem 
{

    public static void showAchievement(int percentage) 
    {

        JOptionPane.showMessageDialog(
                null,
                MotivationSystem.getMessage(percentage),
                "Quiz Result",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}