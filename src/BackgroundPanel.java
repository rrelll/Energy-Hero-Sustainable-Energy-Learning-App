import java.awt.*;
import java.io.File;
import javax.swing.*;

//by Darryl (103794)
public class BackgroundPanel extends JPanel {

    private Image bg;

    public BackgroundPanel(String fileName) {

        String path = System.getProperty("user.dir")
                + File.separator + "images"
                + File.separator + fileName;

        ImageIcon icon = new ImageIcon(path);

        if (icon.getIconWidth() == -1) {
            System.out.println("Background not found: " + path);
            bg = null;
        } else {
            bg = icon.getImage();
        }

        setLayout(null); // IMPORTANT for absolute positioning
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bg != null) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}