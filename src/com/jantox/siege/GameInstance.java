package com.jantox.siege;

import com.jantox.siege.entities.*;
import com.jantox.siege.entities.map.shop.Shop;
import com.jantox.siege.entities.tools.Gun;
import com.jantox.siege.gamejolt.GameJolt;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.gfx.Intent;
import com.jantox.siege.gfx.Notification;
import com.jantox.siege.level.*;
import com.jantox.siege.level.Siege;
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
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class GameInstance {

    public static boolean sniper;
    public static Shop shop = null;
    public static boolean paused = false;

    private int width, height;

    public static int intent = -1;

    private Level level;
    private MultiplayerInstance mpinstance;
    private Timer timer;

    public static boolean debug = false;

    public static boolean multiplayer = false;

    public static ArrayList<Notification> notifications;
    public static ArrayList<Intent> intents;

    public static AudioController audio;

    public static int cash = 0;
    public static int ccash = 0;

    public static GameJolt gamejolt;

    private MainMenu mainmenu;

    public static float snsc = 0;

    public GameInstance(int w, int h) {
        mainmenu = new MainMenu();

        this.width = w;
        this.height = h;

        audio = new AudioController();

        Configuration.init();

        gamejolt = new GameJolt();
        curms = System.currentTimeMillis();

        notifications = new ArrayList<Notification>();

    }

    public void init() {
        this.initGL();

        level = new Level();
        level.init(new Player(new Camera(new Vector3D(0, 10, 0), width, height), level));

        if(multiplayer) {
            mpinstance = new MultiplayerInstance(level);
        }

        intents = new ArrayList<Intent>();

        intents.add(new Intent(0, "Press E to control catapult"));
        intents.add(new Intent(1, "Press E to leave catapult"));
    }

    public void initGL() {
        // initialize OpenGL
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(68, (float) width / (float) height, 1.0f, 2000.0f);
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

            Input.update();
            if(!paused)
                update(delta);
            render(delta);

            timer.update();

            if(System.currentTimeMillis() - curms >= 30000) {
                curms = System.currentTimeMillis();
                gamejolt.ping();
            }

            if(Statistics.MONSTERS_KILLED == 10) {
                gamejolt.addTrophy(2465);
            }

            if(ccash >= 5000) {
                if(!gamejolt.hasTrophy(2468)) {
                    gamejolt.addTrophy(2468);
                }
            }

            Display.update();
            Display.sync(60);
        }

        AL.destroy();
    }

    public void update(int delta) {
        level.update(delta);
        audio.update();

        mainmenu.update();

        if(intent != -1) {
            intents.get(intent).update();
        }

        if(cash < ccash) {
            cash += 3;
            if(cash >= ccash)
                cash = ccash;
        } else if(cash > ccash) {
            cash -= 3;
            if(cash <= ccash)
                cash = ccash;
        }

        if(notifications.size() > 0) {
            if(notifications.get(0).isExpired())
                notifications.remove(0);
            else
                notifications.get(0).update();
        }

        if(multiplayer) {
            mpinstance.update();
        }
    }

    long curms;

    public void render(int delta) {
        level.render(delta);
        mainmenu.render();

        switch2D();

        glDisable(GL_TEXTURE_2D);
        glScalef(800f/1066f, 1, 1);

        drawCrossheir();

        if(notifications.size() > 0) {
            notifications.get(0).render();
        }

        if(intent != -1) {
            intents.get(intent).render();
        }

        if(GameInstance.shop != null) {
            GameInstance.shop.renderShop();
        }

        if (sniper) {
            glColor3f(0,0,0);
            glBegin(GL_QUADS);
            glVertex2f(0, 0);
            glVertex2f(533-300 * snsc, 0);
            glVertex2f(533-300 * snsc, 600);
            glVertex2f(0, 600);

            glVertex2f(533+300 * snsc, 0);
            glVertex2f(1066, 0);
            glVertex2f(1066, 600);
            glVertex2f(533+300 * snsc, 600);

            glVertex2f(1066, 300 - 300 * snsc);
            glVertex2f(0, 300 - 300 * snsc);
            glVertex2f(0, 0);
            glVertex2f(1066, 0);

            glVertex2f(1066, 300 + 300 * snsc);
            glVertex2f(0, 300 + 300 * snsc);
            glVertex2f(0, 600);
            glVertex2f(1066, 600);

            glEnd();

            glColor3f(1,1,1);
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, Resources.getTexture(6).getTextureID());

            snsc += 0.08f;
            if(snsc > 1) {
                snsc = 1;
            }

            glBegin(GL_QUADS);
            glTexCoord2f(0, 0);
            glVertex2f(533-300 * snsc, 300 - 300 * snsc);
            glTexCoord2f(1, 0);
            glVertex2f(533+300* snsc, 300 - 300 * snsc);
            glTexCoord2f(1,1);
            glVertex2f(533+300* snsc, 300 + 300 * snsc);
            glTexCoord2f(0, 1);
            glVertex2f(533-300* snsc, 300 + 300 * snsc);
            glEnd();
        } else {
            snsc = 0;
        }

        if(!paused) {
            if(level.getPlayer().getTool() instanceof Gun) {
                Gun gun = (Gun) level.getPlayer().getTool();

                int cur = gun.getCurMag();
                int ful = gun.getFullMag();
                Resources.getFont("terminal").drawText(cur + "/" + ful, 1066-20, 540, 3f, new Vector3D(0.75, 0.75, 0.75), true, 8);
            }

            //Resources.getFont("terminal").drawText("Money: $" + String.valueOf(cash), 1066+20, 15, 2, new Vector3D(1,1,0), true,  8);
            if(((com.jantox.siege.level.Siege)level.getGameMode()).getBreakTime() == -1) {
                Resources.getFont("terminal").drawText("Enemies Left: " + String.valueOf(((com.jantox.siege.level.Siege)level.getGameMode()).getEnemiesLeft()), 22, 55, 1f, BitmapFont.LIGHT_GRAY, false, 8);
            } else {
                Resources.getFont("terminal").drawText("Next Wave: " + String.valueOf((int)(((Siege) level.getGameMode()).getBreakTime())), 22, 55, 1f, BitmapFont.LIGHT_GRAY, false, 8);
            }
            Resources.getFont("terminal").drawText("Wave " + String.valueOf(((com.jantox.siege.level.Siege)level.getGameMode()).getWave()+1), 15, 15, 2.5f, BitmapFont.LIGHT_GRAY, false, 8);

            if(GameInstance.debug) {
                glColor4f(0.1f, 0.1f, 0.1f, 0.5f);
                glBegin(GL_QUADS);
                glVertex2f(4, 495);
                glVertex2f(195, 495);
                glVertex2f(195, 595);
                glVertex2f(4, 595);
                glEnd();

                Vector3D pos = level.getPlayer().getCamera().getCamera();
                float pitch = level.getPlayer().getCamera().getPitch();
                float yaw = level.getPlayer().getCamera().getYaw();
                Resources.getFont("terminal").drawText("FPS: " + timer.getFrameFps() + "\nX: " + pos.x + "\nY: " + pos.y + "\nZ: " + pos.z + "\nYaw: " + yaw + "\nPitch: " + pitch, 10, 500, 1, BitmapFont.RED, false, 8);
            }
        }

        if(paused) {
            String p = "Game Paused";
            String ctp = "(Click to continue)";
            Resources.getFont("terminal").drawText(p, 533 - (int)(p.length() / 2f) * 40, 250, 5, BitmapFont.WHITE, false, 8);
            Resources.getFont("terminal").drawText(ctp, 533 - (int)(ctp.length() / 2f) * 16, 320, 2, BitmapFont.WHITE, false, 8);

            glColor4f(0.1f, 0.1f, 0.1f, 0.25f);
            glBegin(GL_QUADS);
            glVertex2f(0, 0);
            glVertex2f(1066, 0);
            glVertex2f(1066, 600);
            glVertex2f(0, 600);
            glEnd();
        }

        glColor3f(1, 0, 0);
        for(int i = 0; i < 4; i++) {
            glBegin(GL_QUADS);
            glVertex2f(10 + i * 23, 590);
            glVertex2f(23 + i * 23, 590);
            glVertex2f(23 + i * 23, 500);
            glVertex2f(10 + i * 23, 500);
            glEnd();
        }

        glColor3f(0, 1, 0);
        for(int i = 0; i < 4; i++) {

            int health = level.getGuardian(i).getHealth();

            glBegin(GL_QUADS);
            glVertex2f(10 + i * 23, 590);
            glVertex2f(23 + i * 23, 590);
            glVertex2f(23 + i * 23, 590-health);
            glVertex2f(10 + i * 23, 590-health);
            glEnd();
        }

        level.getPlayer().renderInventory();

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
        glVertex2f(532, 294);
        glVertex2f(532, 304);
        glVertex2f(533, 294);
        glVertex2f(533, 304);
        glEnd();
        glBegin(GL_LINES);
        glVertex2f(523, 299);
        glVertex2f(541, 299);
        glVertex2f(523, 300);
        glVertex2f(541, 300);
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
