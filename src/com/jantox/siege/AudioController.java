package com.jantox.siege;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

public class AudioController {

    Audio gunshot;
    Audio switchtool;

    public AudioController() {
        try {
            gunshot = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/gunshot.wav"));
            switchtool = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/switch_tool.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSound(int i) {
        if(i == 0)
            gunshot.playAsSoundEffect(2f,2f,false);
        else if(i == 1) {
            switchtool.playAsSoundEffect(1f,1f,false);
        }
    }

    public void update() {
        SoundStore.get().poll(0);
    }

}
