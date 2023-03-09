package client;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    
    static Clip clip;
    
    static Map<String,URL> sounds = new HashMap<>();
    
    URL[] soundURL = new URL[5];
    
    public SoundManager(){
        loadSounds();
    }

    public void loadSounds(){

        System.out.print("Loading Sounds...");
        soundURL[0] = getClass().getResource("/client/res/sounds/attack.wav");
        soundURL[1] = getClass().getResource("/client/res/sounds/oi.wav");
        soundURL[2] = getClass().getResource("/client/res/sounds/yeah.wav");
        soundURL[3] = getClass().getResource("/client/res/sounds/theme.wav");

        sounds.put("attack", soundURL[0]);
        sounds.put("oi", soundURL[1]);
        sounds.put("yeah", soundURL[2]);
        sounds.put("theme", soundURL[3]);

        System.out.print(" finished! \n");
    }

    public static void playSound(String sound){
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(sounds.get(sound));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playTheme(){
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(sounds.get("theme"));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
