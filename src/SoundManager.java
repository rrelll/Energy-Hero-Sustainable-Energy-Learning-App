// by Mohd Aeman (105045)
// Modified by Marcella (104863):
// - Added playEffect() method for short sounds (correct/wrong/endQuiz)
// - Added custom exception handling with JOptionPane popups
// - Pre-loaded sound clips for instant effect playback
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class SoundManager {

    private static Clip clip;
    private static AudioInputStream audioStream;
    private static boolean muted = false;

    private static Clip correctClip;
    private static Clip wrongClip;
    private static Clip endClip;

// ================= PLAY (FIXED BY MARCELLA) =================
    public static synchronized void play(String path, boolean loop) {

        try {
            stop();

            File file = new File(path);

            if (!file.exists()) {
                throw new EnergyHeroException("Cannot find sound file: " + path);
            }

            audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.setFramePosition(0);

            // Setup loop parameters regardless of mute state so the track structure exists
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            // Only call start if the user hasn't explicitly muted the game session globally
            if (!muted) {
                clip.start();
                System.out.println("Playing: " + path);
            } else {
                System.out.println("Sound loaded but held in Mute state: " + path);
            }

            // Debug checker tracker
            Timer debugTimer = new Timer(200, e -> {
                if (clip != null) {
                    System.out.println("Running: " + clip.isRunning());
                }
            });
            debugTimer.setRepeats(false);
            debugTimer.start();

        } catch (Exception e) {
            try {
                throw new EnergyHeroException("Failed to play audio: " + path, e);
            } catch (EnergyHeroException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    ex.getMessage(),
                    "Audio Error",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    // ================= STOP =================
    public static synchronized void stop() {

        try {

            if (clip != null) {

                clip.stop();
                clip.flush();
                clip.close();
                clip = null;
            }

            if (audioStream != null) {

                audioStream.close();
                audioStream = null;
            }

        } catch (Exception e) {
            try {
                throw new EnergyHeroException("Failed to stop audio playback.", e);
            } catch (EnergyHeroException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    // ================= PLAY EFFECT (no background stop) =================
    public static synchronized void playEffect(String path) {

        if (muted) return;

        try {
            Clip sfx = null;
            if (path.contains("correct")) {
                sfx = correctClip;
            } else if (path.contains("wrong")) {
                sfx = wrongClip;
            } else if (path.contains("endQuiz")) {
                sfx = endClip;
            }

            if (sfx != null) {
                sfx.setFramePosition(0);
                sfx.start();
            }

        } catch (Exception e) {
            System.out.println("Effect error: " + e.getMessage());
        }
    }

    // ================= TOGGLE MUTE =================
    public static void toggleMute() {

        muted = !muted;

        if (clip != null) {

            if (muted) {
                clip.stop();
            } else {
                clip.setFramePosition(0);
                clip.start();
            }
        }
    }

    // ================= CHECK =================
    public static boolean isMuted() {
        return muted;
    }
}