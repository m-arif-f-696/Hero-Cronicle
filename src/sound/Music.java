package sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Music {
    private enum Track {
        MENU,
        BATTLE
    }

    private final Clip menuClip;
    private final Clip battleClip;

    private Clip currentClip;
    private Track currentTrack;

    public Music() {
        menuClip = loadClip("sound/menuBGM.wav", "sound/menuBDM.wav");
        battleClip = loadClip("sound/battleBGM.wav");
    }

    public void playMenu() {
        play(menuClip, Track.MENU);
    }

    public void playBattle() {
        play(battleClip, Track.BATTLE);
    }

    public void stop() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.setFramePosition(0);
        }
        currentClip = null;
        currentTrack = null;
    }

    public void close() {
        stop();
        closeClip(menuClip);
        closeClip(battleClip);
    }

    private void play(Clip clip, Track track) {
        if (clip == null) {
            return;
        }

        // Kalau track yang sama sudah sedang berjalan, jangan restart dari awal.
        if (currentTrack == track && currentClip != null && currentClip.isRunning()) {
            return;
        }

        if (currentClip != null && currentClip != clip) {
            currentClip.stop();
            currentClip.setFramePosition(0);
        }

        clip.stop();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);

        currentClip = clip;
        currentTrack = track;
    }

    private Clip loadClip(String... resourcePaths) {
        for (String resourcePath : resourcePaths) {
            URL url = getClass().getClassLoader().getResource(resourcePath);
            if (url == null) {
                continue;
            }

            try (AudioInputStream ais = AudioSystem.getAudioInputStream(url)) {
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                return clip;
            } catch (Exception e) {
                System.out.println("Gagal memuat audio: " + resourcePath);
                e.printStackTrace();
                return null;
            }
        }

        System.out.println("Error: File audio tidak ditemukan: " + String.join(", ", resourcePaths));
        return null;
    }

    private void closeClip(Clip clip) {
        if (clip != null) {
            clip.close();
        }
    }
}
