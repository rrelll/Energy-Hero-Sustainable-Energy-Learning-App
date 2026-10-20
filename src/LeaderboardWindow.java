// LeaderboardWindow.java
// by Mohd Aeman (105045)
// Enhanced and Adjusted by:
// Jayden Aaric anak Pita (104498)
// Modified by Marcella (104863) - Resized to 360x780

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LeaderboardWindow extends JFrame 
{

    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private Runnable onClose;

    public LeaderboardWindow(Runnable onClose) 
    {

        this.onClose = onClose;

        setTitle("Leaderboard");
        setSize(360, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
        
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                SoundManager.stop();
                resumeMusic();
                dispose();
            }
        });
        setResizable(false);

        SoundManager.play("music/leaderboard.wav", true);

        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        setContentPane(bg);
        bg.setLayout(null);

        JLabel title = new JLabel("LEADERBOARD", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 22));
        title.setForeground(Color.BLACK);
        title.setBounds(0, 15, 360, 40);
        bg.add(title);

        String[] columns = {"Rank", "Name", "Score", "Difficulty", "Time"};

        tableModel = new DefaultTableModel(columns, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return column != 0;
            }
        };

        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leaderboardTable.setRowHeight(24);
        leaderboardTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        leaderboardTable.getTableHeader().setReorderingAllowed(false);
        leaderboardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaderboardTable.setFillsViewportHeight(true);
        leaderboardTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        leaderboardTable.setGridColor(new Color(200, 200, 200));
        leaderboardTable.setShowGrid(true);
        leaderboardTable.setSelectionBackground(new Color(184, 207, 229));
        leaderboardTable.setSelectionForeground(Color.BLACK);

        leaderboardTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        leaderboardTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        leaderboardTable.getColumnModel().getColumn(2).setPreferredWidth(45);
        leaderboardTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        leaderboardTable.getColumnModel().getColumn(4).setPreferredWidth(110);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBounds(20, 70, 310, 420);
        bg.add(scrollPane);

        JButton saveBtn = new JButton("Save Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear All");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back to Lobby");

        styleButton(saveBtn, new Color(46, 204, 113));      // green
        styleButton(deleteBtn, new Color(231, 76, 60));     // red
        styleButton(clearBtn, new Color(230, 126, 34));     // orange
        styleButton(refreshBtn, new Color(52, 152, 219));   // blue
        styleButton(backBtn, new Color(155, 89, 182));      // purple

        // Top Row: Scaled down and shifted left to prevent clipping the right frame
        saveBtn.setBounds(20, 500, 100, 40);      // Starts at 20
        deleteBtn.setBounds(130, 500, 100, 40);   // Balanced in the middle
        clearBtn.setBounds(240, 500, 90, 40);    // Ends safely at 340

        // Bottom Row: Two halves matching the exact same scaled-down width
        refreshBtn.setBounds(20, 565, 150, 40);   // Left half
        backBtn.setBounds(185, 565, 150, 40);     // Right half ends safely at 340

        bg.add(saveBtn);
        bg.add(deleteBtn);
        bg.add(clearBtn);
        bg.add(refreshBtn);
        bg.add(backBtn);

        saveBtn.addActionListener(e -> saveEditedRecord());
        deleteBtn.addActionListener(e -> deleteSelectedRecord());
        clearBtn.addActionListener(e -> clearAllRecords());
        refreshBtn.addActionListener(e -> loadLeaderboard());

        backBtn.addActionListener(e -> 
            {
            resumeMusic();
            dispose();
        });

        loadLeaderboard();

        setVisible(true);
    }

    private void loadLeaderboard() 
    {

        tableModel.setRowCount(0);

        List<Leaderboard.Entry> entries = Leaderboard.getEntries();

        for (int i = 0; i < entries.size(); i++) {

            Leaderboard.Entry entry = entries.get(i);

            tableModel.addRow(new Object[]
                {
                    i + 1,
                    entry.name,
                    entry.score,
                    entry.difficulty,
                    entry.finishTime
            });
        }
    }

    private void saveEditedRecord() 
    {

        int selectedRow = leaderboardTable.getSelectedRow();

        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }

        try 
        {
            String name = tableModel.getValueAt(selectedRow, 1).toString();
            int score = Integer.parseInt(tableModel.getValueAt(selectedRow, 2).toString());
            String difficulty = tableModel.getValueAt(selectedRow, 3).toString();
            String time = tableModel.getValueAt(selectedRow, 4).toString();

            Leaderboard.updateEntry(selectedRow, name, score, difficulty, time);

            loadLeaderboard();

            JOptionPane.showMessageDialog(this, "Leaderboard record updated.");

        } 
        catch (NumberFormatException e) 
        {
            JOptionPane.showMessageDialog(this, "Score must be a number.");
        }
    }

    private void deleteSelectedRecord() 
    {

        int selectedRow = leaderboardTable.getSelectedRow();

        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a record first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog
        (
                this,
                "Delete this selected leaderboard record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) 
        {
            Leaderboard.removeEntry(selectedRow);
            loadLeaderboard();
            JOptionPane.showMessageDialog(this, "Selected record deleted.");
        }
    }

    private void clearAllRecords() 
    {

        if (Leaderboard.getEntries().isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Leaderboard is already empty.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear ALL leaderboard records?",
                "Confirm Clear All",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) 
        {
            Leaderboard.clearEntries();
            loadLeaderboard();
            JOptionPane.showMessageDialog(this, "All records cleared.");
        }
    }

    private void resumeMusic() 
    {
        if (onClose != null) 
        {
            onClose.run();
            onClose = null;
        }
    }

    private void styleButton(JButton btn, Color color) 
    {

        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}