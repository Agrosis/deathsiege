package com.jantox.siege;

import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;
import com.jantox.siege.level.Level;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class GameInstance {

    private int width, height;

    private Level level;
    private Timer timer;

    public GameInstance(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public void init() {
        // initialize OpenGL
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60, (float) width / (float) height, 1.0f, 200.0f);
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

        level = new Level(new Player(new Camera(new Vector3D(0, 10, 0), width, height)));
        Entity.level = level;
        level.init();
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

    public void stop() {

    }

    public void update(int delta) {
        Input.update();
        level.update(delta);
    }

    public void render(int delta) {
        level.render(delta);

        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0.0, 800, 600, 0.0, -1.0, 10.0);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glClear(GL_DEPTH_BUFFER_BIT);

        glDisable(GL_TEXTURE_2D);
        drawCrossheir();
        drawHealthbar();

        glColor3f(1f, 1f, 1f);

        glMatrixMode(GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
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

    float i = 100;

    public void drawHealthbar() {
        i -= 0.05f;
        glColor3f(0, 1, 0);
        glBegin(GL_QUADS);
        glVertex2f(20, 20);
        glVertex2f(20 + i * 3, 20);
        glVertex2f(20 + i * 3, 40);
        glVertex2f(20, 40);
        glEnd();

        glColor3f(1, 0, 0);
        glBegin(GL_QUADS);
        glVertex2f(20, 20);
        glVertex2f(320, 20);
        glVertex2f(320, 40);
        glVertex2f(20, 40);
        glEnd();

        glColor3f(0, 0, 0);
        glBegin(GL_QUADS);
        glVertex2f(18, 18);
        glVertex2f(322, 18);
        glVertex2f(322, 42);
        glVertex2f(18, 42);
        glEnd();

        // stamina
        glColor3f(1, 1, 0);
        glBegin(GL_QUADS);
        glVertex2f(780, 20);
        glVertex2f(780 - level.getPlayer().stamina, 20);
        glVertex2f(780 - level.getPlayer().stamina, 40);
        glVertex2f(780, 40);
        glEnd();

        glColor3f(1, 0, 0);
        glBegin(GL_QUADS);
        glVertex2f(480, 20);
        glVertex2f(800-20, 20);
        glVertex2f(800-20, 40);
        glVertex2f(480, 40);
        glEnd();

        glColor3f(0, 0, 0);
        glBegin(GL_QUADS);
        glVertex2f(478, 18);
        glVertex2f(800-20+2, 18);
        glVertex2f(800-20+2, 42);
        glVertex2f(478, 42);
        glEnd();
    }

}
