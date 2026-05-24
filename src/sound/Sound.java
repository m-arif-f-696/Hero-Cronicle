package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    public static final int ENEMY_ATTACK = 0;
    public static final int PLAYER_ATTACK = 1;
    public static final int ASSASIN_SKILL = 2;
    public static final int HEALER_SKILL = 3;
    public static final int TANK_SKILL = 4;
    public static final int SWORDMAN_SKILL = 5;

    public Sound() {
        soundURL[0] = getClass().getClassLoader().getResource("sound/enemy_attack.wav");
        soundURL[1] = getClass().getClassLoader().getResource("sound/player_attack.wav");
        soundURL[2] = getClass().getClassLoader().getResource("sound/assasin_skill.wav");
        soundURL[3] = getClass().getClassLoader().getResource("sound/healer_skill.wav");
        soundURL[4] = getClass().getClassLoader().getResource("sound/tank_skill.wav");
        soundURL[5] = getClass().getClassLoader().getResource("sound/swordman_skill.wav");
    }


    public void setFile(int i){
        try {
            if (soundURL[i] == null) {
                System.out.println("Error: File audio pada indeks " + i + " tidak ditemukan!");
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(){
        if (clip != null) {
            clip.start();
        }
    }

    public void loop(){
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

}
