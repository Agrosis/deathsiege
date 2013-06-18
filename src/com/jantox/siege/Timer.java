package com.jantox.siege;

import org.lwjgl.Sys;

public class Timer {

    private int fps;
    private long lastfps;
    private long lastFrame;

    public Timer() {
        fps = 0;
        lastfps = 0;
        lastFrame = 0;

        getDelta();
        lastfps = getTime();
    }

    public void update() {
        if(this.getTime() - this.lastfps > 1000) {
            //System.out.println("FPS: " + fps);
            fps = 0;
            lastfps += 1000;
        }
        fps++;
    }

    public int getFps() {
        return fps;
    }

    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public int getDelta() {
        long time = this.getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

}
