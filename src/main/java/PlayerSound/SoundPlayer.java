package PlayerSound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class SoundPlayer {

    private Clip clip;
    private boolean RepeatOnEnd = false;

    // Construtor que carrega o som
    public SoundPlayer(String soundFilePath) {
        try {
            // Carrega o arquivo de som como recurso
            InputStream audioSrc = getClass().getResourceAsStream("/" + soundFilePath);
            if (audioSrc == null) {
                throw new IOException("Arquivo de som não encontrado: " + soundFilePath);
            }

            // Adiciona um buffer de leitura para garantir que o InputStream funcione corretamente
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.addLineListener(event->{
                if(event.getType() == LineEvent.Type.STOP){
                    clip.stop();
                    if(RepeatOnEnd){
                        clip.setFramePosition(0);
                        clip.start();
                    }
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Método para tocar o som
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Recomeça do início se já foi tocado antes
            clip.start();
        }
    }

    public void SetRepeatSound(boolean response){
        this.RepeatOnEnd = response;
    }

    // Método para parar o som
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}
