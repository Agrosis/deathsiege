package com.jantox.siege;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Siege {

    private GameInstance game;

    public Siege() {
        this.game = new GameInstance(800, 600);

        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
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

