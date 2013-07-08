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

        Matrix4 mat = Matrix4.createRotationMatrix(90, 1);
        //Matrix4 tra = Matrix4.createTranslationMatrix(new Vector3D(2,2,2));

        Vector3D vec = new Vector3D(1,-2,-1);

        Vector3D res = mat.multiplyVectorBy(vec);
        //res = tra.multiplyVectorBy(res);
        res.debug();
    }

}

