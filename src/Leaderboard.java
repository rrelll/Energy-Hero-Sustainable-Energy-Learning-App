// Leaderboard.java
// by Mohd Aeman (105045)
// Leaderboard.java
// Enhanced and Adjusted by:
// Jayden Aaric anak Pita (104498)
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class Leaderboard implements ScoreStorage 
{

    private static List<Entry> entries = new ArrayList<>();
    private static final String FILE_NAME = "leaderboard.dat";

    static 
    {
        loadFromFile();
    }

    @Override
    public void saveScore(String playerName, int score, String difficulty) 
    {
        addEntry(playerName, score, difficulty);
    }

    @Override
    public void loadScores() 
    {
        loadFromFile();
    }

    public static void addEntry(String name, int score, String difficulty) 
    {
        String finishTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date());

        entries.add(new Entry(name, score, difficulty, finishTime));
        sortEntries();
        saveToFile();
    }

    public static List<Entry> getEntries() 
    {
        return entries;
    }

    public static void removeEntry(int index) 
    {
        if (index >= 0 && index < entries.size()) 
            {
            entries.remove(index);
            saveToFile();
        }
    }

    public static void updateEntry(int index, String name, int score, String difficulty, String time) 
    {
        if (index >= 0 && index < entries.size()) 
            {
            Entry entry = entries.get(index);
            entry.name = name;
            entry.score = score;
            entry.difficulty = difficulty;
            entry.finishTime = time;

            sortEntries();
            saveToFile();
        }
    }

    public static void clearEntries() 
    {
        entries.clear();
        saveToFile();
    }

    private static void sortEntries() 
    {
        entries.sort((a, b) -> b.score - a.score);
    }

    private static void saveToFile() 
    {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) 
        {
            out.writeObject(entries);
        } 
        catch (IOException e)
        {
            try {
                throw new EnergyHeroException("Could not save leaderboard data.", e);
            } catch (EnergyHeroException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage() + "\nYour scores may not persist after closing.",
                    "Save Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadFromFile() 
    {
        File file = new File(FILE_NAME);

        if (!file.exists()) 
        {
            return;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) 
        {
            entries = (List<Entry>) in.readObject();
        } 
        catch (Exception e) 
        {
            try {
                throw new EnergyHeroException("Could not load leaderboard data. File may be corrupted.", e);
            } catch (EnergyHeroException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage() + "\nStarting with an empty leaderboard.",
                    "Load Error",
                    JOptionPane.INFORMATION_MESSAGE
                );
                entries = new ArrayList<>();
            }
        }
    }

    public static class Entry implements Serializable 
    {
        String name;
        int score;
        String difficulty;
        String finishTime;

        Entry(String name, int score, String difficulty, String finishTime) 
        {
            this.name = name;
            this.score = score;
            this.difficulty = difficulty;
            this.finishTime = finishTime;
        }
    }
}