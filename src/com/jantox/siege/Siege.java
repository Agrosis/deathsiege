package com.jantox.siege;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Siege {

    private GameInstance game;

    public Siege() {
        this.game = new GameInstance(1066, 600);

        try {
            Display.create();

            game.setDisplayMode(1066, 600, Boolean.valueOf(Configuration.getProperty("fullscreen")));

            Display.setTitle("Deathsiege");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        game.init();
        game.start();
    }

    public static void main(String[] args) {
        new Siege();
    }

}

