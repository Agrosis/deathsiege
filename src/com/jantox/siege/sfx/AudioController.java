package com.jantox.siege.sfx;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

public class AudioController {

    Audio gunshot;
    Audio switch_tool;
    Audio crossbow;
    Audio getcoin;

    public AudioController() {
        try {
            gunshot = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/gunshot.wav"));
            switch_tool = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/switch_tool.wav"));
            crossbow = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/crossbow_shot.wav"));
            getcoin = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/getcoin.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSound(int i) {
        if(i == 0)
            gunshot.playAsSoundEffect(2f,5f,false);
        else if(i == 1) {
            switch_tool.playAsSoundEffect(1f, 1f, false);
        } else if(i == 2) {
            crossbow.playAsSoundEffect(1f,1f,false);
        } else if(i == 3) {
            getcoin.playAsSoundEffect(1f,1f,false);
        }
    }

    public void update() {
        SoundStore.get().poll(0);
    }

}
