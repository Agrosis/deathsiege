package com.jantox.siege.gfx;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;

public class Notification {

    public static final int AUTO_SHOW = 0;
    public static final int OAAT = 1;

    private String title, message;
    private BitmapFont tf, mf;

    private int life, maxlife;
    private boolean expired = false;

    private int msgspeed = 5, msgpos = 0;
    private int ticks = 0;

    private float notescale = 50f;
    private boolean fading = false;
    private int fade = 255;

    public Notification(String title, String message, int life) {
        tf = Resources.getFont("terminal");
        this.title = title;
        this.message = message;

        this.life = 0;
        this.maxlife = life;
    }

    public void update() {
        life++;
        if(life >= maxlife) {
            fading = true;
        }

        if(fading) {
            fade-=2;
            if(fade <= 0) {
                fade = 0;
                this.expired = true;
            }
        }

        notescale -= 1;
        if(notescale <= 1)
            notescale = 2;

        ticks++;
        if(ticks >= msgspeed) {
            ticks = 0;
            msgpos++;
            if(msgpos > title.length()) {
                msgpos = title.length();
            }
        }
    }

    public void render() {
        String vismsg = title.substring(0, msgpos);
        tf.drawText(vismsg, 533 - (int)((vismsg.length() / 2f) * 8 * 3), 75,3,new Vector3D(0.85, 0.85, 0.85), false, 8, fade/255f);
        tf.drawText(message, 533 - (int)((message.length() / 2f) * 8 * notescale), 115,notescale,BitmapFont.YELLOW, false, 8, fade/255f);
    }

    public boolean isExpired() {
        return expired;
    }

}
