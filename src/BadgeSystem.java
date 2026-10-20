// BadgeSystem.java
// Written by: Jayden Aaric anak Pita (100498)
// Task: Gamification & Data Developer
// Features: Badge Records, Stars, Leaderboard, Data Persistence

public class BadgeSystem 
{

    public static String getBadge(int score) 
    {

        if (score >= 80)
            return "Gold Energy Hero";

        else if (score >= 60)
            return "Silver Saver";

        else if (score >= 40)
            return "Bronze Learner";
        
        else
            return "Rookie Explorer";
    }
}