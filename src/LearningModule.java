import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

//by Darryl (103794)
//Features: SDG 7 Learning Content Navigation
// Modified by Marcella (104863):
// - Resized to 360x780
// - Widened title/subtitle labels (300→320px) with 20px padding
// - Widened content area (330→340px) with internal text margin (15px left/right)

public class LearningModule implements ContentManager {

    private JFrame frame;
    private int index = 0;

    // UI COMPONENTS 
    private JTextArea contentArea;
    private JLabel imageLabel;

    private JLabel shadow;
    private JLabel title;
    private JLabel subtitle;

    private JLabel pageShadow;
    private JLabel pageTitle;

    private JPanel comparePanel;
    private JTextArea renewText;
    private JTextArea nonRenewText;

    //  MUSIC VARIABLES 
    private Clip audioClip;
    private FloatControl volumeControl;
    private boolean isMuted = false;

    //  PAGE TITLES 
    private String[] pageTitleArr = {
        "Energy Overview",
        "What is Energy?",
        "Importance of Energy",
        "Energy Sources",
        "Renewable Energy",
        "Non-Renewable Energy",
        "Renewable vs Non-Renewable",
        "Environmental Impact",
        "Energy Conservation",
        "Future of Energy"
    };

    // PAGE CONTENT 
    private String[] content = {
        "Energy Overview\n\n"
        + "It is linked to Sustainable Development Goal 7 (SDG 7), which focuses on ensuring access to affordable, reliable, sustainable, and modern energy for all.\n\n"
        + "Energy powers homes, transport, industries, healthcare, and communication, making it vital for sustainable development.",

        "Energy is the ability to do work or cause change. It cannot be created or destroyed, only converted from one form to another.\n\n"
        + "It exists in many forms such as electrical, thermal, chemical, kinetic, and potential energy.\n\n"
        + "Energy is used in everyday life, from powering electronic devices and vehicles to supporting natural and biological processes.",

        "Energy is essential for supporting daily life and modern development.\n\n"
        + "It powers key sectors such as transportation, healthcare, education, communication, and industry.\n\n"
        + "A stable energy supply improves quality of life and drives economic growth and technological progress.",

        "Energy is obtained from various natural resources that are used to generate power.\n\n"
        + "They are commonly classified into renewable and non-renewable sources based on their availability and sustainability.\n\n"
        + "Understanding energy sources helps in choosing more efficient and sustainable energy solutions.",

        "Renewable energy comes from natural sources that are continuously replenished.\n\n"
        + "It is considered a clean and sustainable energy option with lower environmental impact.\n\n"
        + "Common sources include sunlight, wind, water, and biomass.",

        "Non-renewable energy comes from natural resources that are limited and cannot be replaced quickly.\n\n"
        + "These energy sources take millions of years to form and will eventually be depleted.\n\n"
        + "They are widely used due to their high energy output and reliability.",

        "",

       "Energy production affects the environment in different ways.\n\n"
        + "Fossil fuels release greenhouse gases that contribute to climate change and air pollution.\n\n"
        + "Renewable energy sources such as solar, wind, and hydro produce cleaner energy with lower environmental impact.\n\n"
        + "Using energy efficiently and adopting renewable energy helps protect the environment.",

        "Energy conservation means using energy efficiently and reducing unnecessary waste.\n\n"
        + "It can be achieved by using energy-efficient appliances, turning off unused devices, and adopting sustainable habits.\n\n"
        + "Effective energy conservation helps reduce electricity costs and protects the environment.",

        "The future of energy focuses on developing clean, efficient, and sustainable energy systems.\n\n"
        + "Advancements in technology such as solar power, wind energy, smart grids, and energy storage are improving energy reliability and accessibility.\n\n"
        + "A global shift toward renewable energy is essential to reduce environmental impact and ensure long-term energy security."
    };

    // CONSTRUCTOR 
    public LearningModule() {

        // FRAME SETUP
        frame = new JFrame("Learning Module");
        frame.setSize(360, 780);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // BACKGROUND PANEL
        BackgroundPanel bg = new BackgroundPanel("bg.jpeg");
        bg.setLayout(null);
        frame.setContentPane(bg);

        // MUSIC LOAD 
        loadMusic();

        // STOP MUSIC WHEN WINDOW CLOSES (FIX DASHBOARD ISSUE)
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (audioClip != null) {
                    audioClip.stop();
                    audioClip.close();
                }
            }
        });

        // HEADER LABELS 
        shadow = new JLabel("WELCOME TO OUR LEARNING MODULE", SwingConstants.CENTER);
        shadow.setFont(new Font("Segoe UI", Font.BOLD, 16));
        shadow.setForeground(new Color(0, 0, 0, 100));
        shadow.setBounds(23, 35, 320, 50);

        title = new JLabel("WELCOME TO OUR LEARNING MODULE", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(255, 215, 0));
        title.setBounds(20, 33, 320, 50);

        subtitle = new JLabel("Energy Learning App", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.BLACK);
        subtitle.setBounds(20, 75, 320, 20);

        // ================= PAGE TITLE =================
        pageShadow = new JLabel("", SwingConstants.CENTER);
        pageShadow.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pageShadow.setForeground(new Color(0, 0, 0, 100));
        pageShadow.setBounds(23, 18, 320, 50);

        pageTitle = new JLabel("", SwingConstants.CENTER);
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pageTitle.setForeground(new Color(255, 215, 0));
        pageTitle.setBounds(20, 16, 320, 50);

        // ================= IMAGE DISPLAY =================
        imageLabel = new JLabel();
        imageLabel.setBounds(30, 110, 300, 150);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBorder(BorderFactory.createLineBorder(
                new Color(255, 215, 0, 120), 2));

        //  CONTENT AREA 
        contentArea = new JTextArea();
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setBounds(10, 275, 340, 290);
        contentArea.setMargin(new Insets(10, 15, 10, 15));
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setOpaque(false);

        // COMPARISON PANEL 
        comparePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        comparePanel.setBounds(15, 275, 330, 290);
        comparePanel.setOpaque(false);

        renewText = new JTextArea(
            "RENEWABLE ENERGY\n\n"
          + "ADVANTAGES:\n"
          + "• Sustainable\n"
          + "• Naturally replenished\n"
          + "• Low impact\n\n"
          + "DISADVANTAGES:\n"
          + "• Weather dependent\n"
          + "• High cost"
        );
        renewText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        renewText.setBackground(new Color(210, 255, 210));
        renewText.setEditable(false);

        nonRenewText = new JTextArea(
            "NON-RENEWABLE ENERGY\n\n"
          + "ADVANTAGES:\n"
          + "• High output\n"
          + "• Reliable\n\n"
          + "DISADVANTAGES:\n"
          + "• Pollution\n"
          + "• Limited resources"
        );
        nonRenewText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nonRenewText.setBackground(new Color(255, 210, 210));
        nonRenewText.setEditable(false);

        comparePanel.add(renewText);
        comparePanel.add(nonRenewText);

        //  BUTTONS
        JButton nextBtn = new JButton("Next");
        JButton prevBtn = new JButton("Back");
        JButton muteBtn = new JButton("Sound ON");

        // BUTTON STYLING
        styleButton(nextBtn, new Color(0, 120, 255));
        styleButton(prevBtn, new Color(255, 140, 0));
        styleButton(muteBtn, new Color(0, 180, 90));

        int y = 690;

        prevBtn.setBounds(10, y, 100, 35);
        muteBtn.setBounds(120, y, 110, 35);
        nextBtn.setBounds(240, y, 100, 35);

        // BUTTON ACTIONS
        nextBtn.addActionListener(e -> next());
        prevBtn.addActionListener(e -> previous());

        muteBtn.addActionListener(e -> {
            toggleMute();
            if (isMuted) {
                muteBtn.setText("Sound OFF");
                styleButton(muteBtn, new Color(200, 60, 60));
            } else {
                muteBtn.setText("Sound ON");
                styleButton(muteBtn, new Color(0, 180, 90));
            }
        });

        // ADD COMPONENTS TO PANEL
        bg.add(shadow);
        bg.add(title);
        bg.add(subtitle);
        bg.add(pageShadow);
        bg.add(pageTitle);
        bg.add(imageLabel);
        bg.add(contentArea);
        bg.add(comparePanel);
        bg.add(prevBtn);
        bg.add(muteBtn);
        bg.add(nextBtn);

        comparePanel.setVisible(false);

        show(0);
        frame.setVisible(true);
    }

    // MUSIC LOADING 
    private void loadMusic() {
        try {
            File file = new File("music/backgroundsound.wav");
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);

            audioClip = AudioSystem.getClip();
            audioClip.open(audio);
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

            volumeControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (Exception e) {
            try {
                throw new EnergyHeroException("Could not play background music.", e);
            } catch (EnergyHeroException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage() + "\nThe learning module will work without sound.",
                    "Music Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    //  MUTE FUNCTION
    private void toggleMute() {
        if (volumeControl == null) return;

        if (!isMuted) {
            volumeControl.setValue(volumeControl.getMinimum());
        } else {
            volumeControl.setValue(-10f);
        }

        isMuted = !isMuted;
    }

    //  BUTTON STYLE METHOD 
    private void styleButton(JButton btn, Color c) {
        btn.setBackground(c);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    // NAVIGATION 
    public void next() {
        if (index < size() - 1) show(index + 1);
    }

    public void previous() {
        if (index > 0) show(index - 1);
    }

    public int size() {
        return pageTitleArr.length;
    }

    // PAGE SWITCH 
    public void show(int i) {

        index = i;

        boolean home = (index == 0);

        shadow.setVisible(home);
        title.setVisible(home);
        subtitle.setVisible(home);

        pageTitle.setVisible(!home);
        pageShadow.setVisible(!home);

        if (!home) {
            pageTitle.setText(pageTitleArr[index]);
            pageShadow.setText(pageTitleArr[index]);
        }

        contentArea.setVisible(false);
        comparePanel.setVisible(false);
        imageLabel.setVisible(false);

        if (index == 6) {
            setImage("images/comparison.jpeg");
            comparePanel.setVisible(true);
            imageLabel.setVisible(true);
            return;
        }

        contentArea.setVisible(true);
        contentArea.setText(content[index]);

        switch (index) {
            case 0 -> setImage("images/sdg7.jpeg");
            case 1 -> setImage("images/energy.jpeg");
            case 2 -> setImage("images/why.jpeg");
            case 3 -> setImage("images/types.jpeg");
            case 4 -> setImage("images/renewable.jpeg");
            case 5 -> setImage("images/nonrenewable.jpeg");
            case 7 -> setImage("images/environment.jpeg");
            case 8 -> setImage("images/saveenergy.jpeg");
            case 9 -> setImage("images/future.jpeg");
        }

        imageLabel.setVisible(true);
    }

    // IMAGE SET 
    private void setImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(300, 160, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(img));
    }
}