package se233.project_2.controller;

import javazoom.jl.player.Player;
import se233.project_2.Launcher;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class AudioFeatures {
    private static Player player;
    private static Thread soundThread;

    public static void playBossSound() {
        // ถ้าเล่นอยู่แล้วให้หยุดก่อน
        stopSound();

        soundThread = new Thread(() -> {
            try {
                while (true) {
                    InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/sound/BUTTERBEAR.mp3");
                    player = new Player(is);
                    player.play();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }

    public static void stopSound() {
        try {

                if (soundThread != null) {
                    player.close();
                    soundThread.interrupt();
                }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playOpenSound() {
        // ถ้าเล่นอยู่แล้วให้หยุดก่อน
        stopSound();

        soundThread = new Thread(() -> {
            try {

                    InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/sound/Contra.mp3");
                    player = new Player(is);
                    player.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        soundThread.setDaemon(true);

        soundThread.start();
    }

    public static void playHitSound() {
        try {
            InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/CUTEKOREAN.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println( e.getMessage());
        }
    }

    public static void playShootSound() {
        try {
            InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/CUTEKOREAN.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println( e.getMessage());
        }
    }

    public static void playGameSound() {
        // ถ้าเล่นอยู่แล้วให้หยุดก่อน
        stopSound();

        soundThread = new Thread(() -> {
            try {

                InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/sound/gameSound.mp3");
                 player = new Player(is);
                player.play();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }
}
