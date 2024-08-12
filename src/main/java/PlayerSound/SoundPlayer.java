package PlayerSound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class SoundPlayer {
    private Clip clip;
    private boolean RepeatOnEnd = false;
    private static SoundPlayer instance = null;

    private SoundPlayer(String soundFilePath, boolean loop) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/" + soundFilePath);
            if (audioSrc == null) {
                throw new IOException("Arquivo de som não encontrado: " + soundFilePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.stop();
                        clip.close();
                        if (RepeatOnEnd) {
                            clip.setFramePosition(0);
                            clip.start();
                        }
                    }
                });
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static SoundPlayer getInstance(String soundFilePath, boolean loop) {
        if (instance == null) {
            instance = new SoundPlayer(soundFilePath, loop);
        }
        return instance;
    }

    public void play() {
        if (clip != null) {
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
