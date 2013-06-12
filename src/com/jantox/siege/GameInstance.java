package com.jantox.siege;

import com.jantox.siege.entities.*;
import com.jantox.siege.level.Level;
import com.jantox.siege.net.MultiplayerInstance;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class GameInstance {

    private int width, height;

    private Level level;
    private MultiplayerInstance mpinstance;
    private Timer timer;

    public static boolean multiplayer = false;

    public GameInstance(int w, int h) {
        this.width = w;
        this.height = h;

        Configuration.init();
    }

    public void init() {
        this.initGL();

        level = new Level(new Player(new Camera(new Vector3D(0, 10, 0), width, height)));
        Entity.level = level;
        level.init();

        if(multiplayer) {
            mpinstance = new MultiplayerInstance(level);
        }
    }

    public void initGL() {
        // initialize OpenGL
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60, (float) width / (float) height, 1.0f, 2000.0f);
        glMatrixMode(GL_MODELVIEW);

        glClearDepth(1);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        glShadeModel(GL_SMOOTH);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        timer = new Timer();

        // initialize input methods
        Input.setMouse(width / 2, height / 2);
        Mouse.setGrabbed(true);
    }

    public void start() {
        while(!Display.isCloseRequested()) {
            int delta = timer.getDelta();

            update(delta);
            render(delta);

            timer.update();

            Display.update();
            Display.sync(60);
        }
    }

    public void update(int delta) {
        Input.update();
        level.update(delta);

        if(multiplayer) {
            mpinstance.update();
        }
    }

    public void render(int delta) {
        level.render(delta);

        switch2D();

        glDisable(GL_TEXTURE_2D);
        drawCrossheir();

        switch3D();
    }

    public void switch3D() {
        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
    }

    public void switch2D() {
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0.0, 800, 600, 0.0, -1.0, 10.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glClear(GL_DEPTH_BUFFER_BIT);

    }

    public void drawCrossheir() {
        glColor3f(0.15f, 0.15f, 0.15f);
        glBegin(GL_LINES);
        glVertex2f(399, 294);
        glVertex2f(399, 304);
        glVertex2f(400, 294);
        glVertex2f(400, 304);
        glEnd();
        glBegin(GL_LINES);
        glVertex2f(390, 299);
        glVertex2f(409, 299);
        glVertex2f(390, 300);
        glVertex2f(409, 300);
        glEnd();
        glBegin(GL_LINES);
        glEnd();
    }

}
