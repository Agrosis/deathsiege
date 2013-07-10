package com.jantox.siege.gfx;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;

public class Intent {

    int key;
    String message;
    float fade = 0;

    BitmapFont bf;

    public Intent(int key, String message) {
        this.key = key;
        this.message = message;

        bf = Resources.getFont("terminal");
    }

    public void update() {
        if(fade < 1.0f) {
            fade += 0.03f;
        } else {
            fade = 1f;
        }
    }

    public void off() {
        fade = 0;
    }

    public void render() {
        bf.drawText(message, 533 - (int)((message.length() / 2f) * 8 * 2), 500,2,new Vector3D(0.85, 0.85, 0.85), false, 8, fade);
    }

}
