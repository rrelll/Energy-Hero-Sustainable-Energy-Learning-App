// ScoreStorage.java
// Written by: Jayden Aaric anak Pita (100498)
// Task: Gamification & Data Developer
// Features: Badge Records, Stars, Leaderboard, Data Persistence

public interface ScoreStorage {

    void saveScore(String playerName, int score, String difficulty);

    void loadScores();
}