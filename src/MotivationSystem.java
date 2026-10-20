// MotivationSystem.java
// Written by: Jayden Aaric anak Pita (100498)
// Task: Gamification & Data Developer
// Features: Badge Records, Stars, Leaderboard, Data Persistence


public class MotivationSystem 
{

    public static String getMessage(int percentage) 
    {

        if (percentage >= 80) 
        {

            return "Outstanding!";

        } 
        else if (percentage >= 60) 
        {

            return "That's good!";

        } 
        else if (percentage >= 40) 
        {

            return "Good try!";

        } 
        else if (percentage >= 20) 
        {

            return "You can do better!";

        } 
        else 
        {

            return "Don't give up!";
        }
    }
}