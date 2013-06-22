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
    Audio hit;
    Audio headshot;
    Audio change;

    Audio shop_switch;
    Audio shop_bought;

    public AudioController() {
        try {
            gunshot = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/gunshot.wav"));
            switch_tool = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/reload.wav"));
            crossbow = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/crossbow_shot.wav"));
            getcoin = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/getcoin.wav"));
            hit = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/hit.wav"));
            headshot = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/headshot.wav"));
            change = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/switch.wav"));
            shop_bought = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("sfx/shop_bought.wav"));
            shop_switch = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("sfx/shop_switch.ogg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSound(int i) {
        if(i == 0) {
            gunshot.playAsSoundEffect(1.5f,1f,false);
        } else if(i == 1) {
            switch_tool.playAsSoundEffect(1f, 1f, false);
        } else if(i == 2) {
            crossbow.playAsSoundEffect(1f,1f,false);
        } else if(i == 3) {
            getcoin.playAsSoundEffect(1f,1f,false);
        } else if(i == 4) {
            hit.playAsSoundEffect(1f,1f,false);
        } else if(i == 5) {
            headshot.playAsSoundEffect(1f,1f,false);
        } else if(i == 6) {
            change.playAsSoundEffect(1f,1f,false);
        } else if(i == 7) {
            shop_switch.playAsSoundEffect(1f,1f,false);
        } else if(i == 8) {
            shop_bought.playAsSoundEffect(1f,1f,false);
        }
    }

    public void update() {
        SoundStore.get().poll(0);
    }

}
