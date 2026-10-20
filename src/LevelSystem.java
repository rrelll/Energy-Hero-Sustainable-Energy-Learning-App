// LevelSystem.java
// Written by: Jayden Aaric anak Pita (100498)
// Task: Gamification & Data Developer
// Features: Badge Records, Stars, Leaderboard, Data Persistence

public class LevelSystem 
{

    public static String getLevel(int score) 
    
    {

        if(score >= 90)
            return "Level 5 - SDG Champion";
        else if(score >= 70)
            return "Level 4 - Eco Warrior";
        else if(score >= 50)
            return "Level 3 - Green Learner";
        else if(score >= 30)
            return "Level 2 - Beginner";
        else
            return "Level 1 - Rookie";
    }
}