package com.jantox.siege;

import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;
import com.jantox.siege.level.Level;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import java.applet.Applet;
import java.awt.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class SiegeApplet extends Applet {

    Canvas displayParent;
    Thread gameThread;

    boolean running = false;

    private GameInstance game;

    public SiegeApplet() {

    }

    public void start() {

    }

    public void stop() {

    }

    public void startLWJGL() {
        gameThread = new Thread() {
            public void run() {
                running = true;

                try {
                    Display.setParent(displayParent);
                    Display.create();

                    game = new GameInstance(1066, 600);
                    game.init();
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
                game.start();
            }
        };

        gameThread.start();
    }

    public void stopLWJGL() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        remove(displayParent);
        super.destroy();
    }

    public void init() {
        this.setLayout(new BorderLayout());

        displayParent = new Canvas() {
            public final void addNotify() {
                super.addNotify();
                startLWJGL();
            }
            public final void removeNotify() {
                stopLWJGL();
                super.removeNotify();
            }
        };
        displayParent.setSize(800, 600);
        add(displayParent);
        displayParent.setFocusable(true);
        displayParent.requestFocus();
        displayParent.setIgnoreRepaint(true);
        this.setVisible(true);
    }

}
