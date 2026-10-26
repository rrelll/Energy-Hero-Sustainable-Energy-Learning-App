// StarRating.java
// Written by: Jayden Aaric anak Pita (100498)
// Task: Gamification & Data Developer
// Features: Badge Records, Stars, Leaderboard, Data Persistence

public class StarRating 
{

    public static String getStars(int score) 
    {

        if(score >= 90)
            return "★★★★★";
        else if(score >= 70)
            return "★★★★";
        else if(score >= 50)
            return "★★★";
        else if(score >= 30)
            return "★★";
        else
            return "★";
    }
}