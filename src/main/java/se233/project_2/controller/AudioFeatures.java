package se233.project_2.controller;

import javafx.scene.media.AudioClip;
import javazoom.jl.player.Player;
import se233.project_2.Launcher;

import java.io.InputStream;

public class AudioFeatures {
    private static Player player;
    private static Thread soundThread;
    private static volatile boolean shouldStop = false; //  ตัวแปรควบคุมการหยุด

    public static AudioClip SHOOT_SOUND;
    public static AudioClip HIT_SOUND;

    public static void playBossSound() {
        stopSound();
        shouldStop = false; // reset flag

        soundThread = new Thread(() -> {
            try {
                while (!shouldStop) { //  เช็ค flag
                    InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/sound/BUTTERBEAR.mp3");
                    if (is == null || shouldStop) break; //  เช็คอีกรอบ

                    player = new Player(is);
                    player.play();

                    if (shouldStop) break; // หยุดทันทีถ้ามีการเรียก stopSound()
                }
            } catch (Exception e) {
                if (!shouldStop) { // ไม่แสดง error ถ้าหยุดปกติ
                    e.printStackTrace();
                }
            }
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }

    public static void stopSound() {
        shouldStop = true; //  ตั้ง flag ให้หยุดลูป

        try {
            if (player != null) {
                player.close();
                player = null;
            }

            if (soundThread != null && soundThread.isAlive()) {
                soundThread.interrupt();
                //  รอให้ thread จบ (สูงสุด 500ms)
                soundThread.join(500);
                soundThread = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSounds() {
        try {
            String shootURL = Launcher.class.getResource("/se233/project_2/sound/enemy-gun-2.mp3").toString();
            String hitURL = Launcher.class.getResource("/se233/project_2/sound/enemy-hit.mp3").toString();

            SHOOT_SOUND = new AudioClip(shootURL);
            HIT_SOUND = new AudioClip(hitURL);

            System.out.println("Sounds loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading sounds!");
            e.printStackTrace();
        }
    }

    public static void playOpenSound() {
        stopSound();
        shouldStop = false; //  reset flag

        soundThread = new Thread(() -> {
            try {
                InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/sound/Contra.mp3");
                if (is == null || shouldStop) return; //  เช็ค

                player = new Player(is);
                player.play();
            } catch (Exception e) {
                if (!shouldStop) {
                    e.printStackTrace();
                }
            }
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }

    public static void playHitSound() {
        if (HIT_SOUND != null) {
            HIT_SOUND.play();
        }
    }

    public static void playShootSound() {
        if (SHOOT_SOUND != null) {
            SHOOT_SOUND.play();
        }
    }

    public static void playGameSound() {
        stopSound();
        shouldStop = false; //  reset flag

        soundThread = new Thread(() -> {
            try {
                InputStream is = Launcher.class.getResourceAsStream("/se233/project_2/sound/gameSound.mp3");
                if (is == null || shouldStop) return; // เช็ค

                player = new Player(is);
                player.play();
            } catch (Exception e) {
                if (!shouldStop) {
                    e.printStackTrace();
                }
            }
        });
        soundThread.setDaemon(true);
        soundThread.start();
    }
}