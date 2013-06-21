package com.jantox.siege;

import com.jantox.siege.entities.*;
import com.jantox.siege.entities.map.Shop;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.level.*;
import com.jantox.siege.net.MultiplayerInstance;
import com.jantox.siege.sfx.AudioController;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class GameInstance {

    public static boolean sniper;
    public static Shop shop = null;

    private int width, height;

    private Level level;
    private MultiplayerInstance mpinstance;
    private Timer timer;

    public static boolean multiplayer = false;

    public static AudioController audio;

    private BitmapFont font;

    public static int cash = 0;
    public static int ccash = 0;

    public GameInstance(int w, int h) {
        this.width = w;
        this.height = h;

        audio = new AudioController();

        Configuration.init();
    }

    public void init() {
        this.initGL();

        level = new Level(new Player(new Camera(new Vector3D(0, 10, 0), width, height)));
        Entity.level = level;
        level.init();

        try {
            font = new BitmapFont(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/number_font_strip10.png")), GL_NEAREST), 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        AL.destroy();
    }

    public void update(int delta) {
        Input.update();
        level.update(delta);
        audio.update();

        if(cash < ccash) {
            cash += 3;
            if(cash >= ccash)
                cash = ccash;
        } else if(cash > ccash) {
            cash -= 3;
            if(cash <= ccash)
                cash = ccash;
        }

        if(multiplayer) {
            mpinstance.update();
        }
    }

    public void render(int delta) {
        level.render(delta);

        switch2D();

        glDisable(GL_TEXTURE_2D);
        drawCrossheir();

        if(GameInstance.shop != null) {
            GameInstance.shop.renderShop();
        }

        if (sniper) {
            glColor3f(0,0,0);
            glBegin(GL_QUADS);
            glVertex2f(0, 0);
            glVertex2f(100, 0);
            glVertex2f(100, 600);
            glVertex2f(0, 600);

            glVertex2f(700, 0);
            glVertex2f(800, 0);
            glVertex2f(800, 600);
            glVertex2f(700, 600);
            glEnd();

            glColor3f(1,1,1);
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, Resources.getTexture(9).getTextureID());

            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(100, 0);
            glTexCoord2f(1, 0);
            glVertex2f(700, 0);
            glTexCoord2f(1,1);
            glVertex2f(700, 600);
            glTexCoord2f(0, 1);
            glVertex2f(100, 600);
            glEnd();
        }

        font.drawText(":" + String.valueOf(cash), 20, 20, 1, new Vector3D(1,1,0));

        glColor3f(1,1,1);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(11).getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(10, 540 + 5);
        glTexCoord2f(108f/128, 0);
        glVertex2f(10 + 108, 540 + 5);
        glTexCoord2f(108f/128, 48/64f);
        glVertex2f(10 + 108, 540 + 48 + 5);
        glTexCoord2f(0, 48/64f);
        glVertex2f(10, 540 + 48 + 5);
        glEnd();

        if(((com.jantox.siege.level.Siege)level.getGameMode()).getBreakTime() > -1) {
            glBindTexture(GL_TEXTURE_2D, Resources.getTexture(12).getTextureID());
            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(10, 510 + 5);
            glTexCoord2f(118f/128, 0);
            glVertex2f(10 + 118, 510 + 5);
            glTexCoord2f(118f/128, 18/32f);
            glVertex2f(10 + 118, 510 + 18 + 5);
            glTexCoord2f(0, 18/32f);
            glVertex2f(10, 510 + 18 + 5);
            glEnd();

            font.drawText(String.valueOf((int)((com.jantox.siege.level.Siege)level.getGameMode()).getBreakTime()), 115, 511, 0.75f, new Vector3D(0.75,0.75,0.75));
        }

        font.drawText(String.valueOf(((com.jantox.siege.level.Siege)level.getGameMode()).getWave()+1), 95, 539, 1.5f, new Vector3D(0.85,0.85,0.85));

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
        glDisable(GL_DEPTH_TEST);

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

    public void setDisplayMode(int width, int height, boolean fullscreen) {
        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width) &&
                (Display.getDisplayMode().getHeight() == height) &&
                (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;

                for (int i=0;i<modes.length;i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
                                (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width,height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
                return;
            }

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
        }
    }

}
