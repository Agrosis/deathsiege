package com.jantox.siege.level;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Skybox {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int NORTH = 2;
    public static final int EAST = 3;
    public static final int WEST = 4;
    public static final int SOUTH = 5;

    private Texture skytextures[];
    private float radius;

    public Skybox(String sdir, float radius) {
        skytextures = new Texture[6];
        this.radius = radius;

        try {
            skytextures[UP] = TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/skybox/u.png")), GL_NEAREST);
            skytextures[DOWN] = TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/skybox/d.png")), GL_NEAREST);
            skytextures[NORTH] = TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/skybox/n.png")), GL_NEAREST);
            skytextures[EAST] = TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/skybox/e.png")), GL_NEAREST);
            skytextures[WEST] = TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/skybox/w.png")), GL_NEAREST);
            skytextures[SOUTH] = TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/skybox/s.png")), GL_NEAREST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

    }

    public void render() {
        glEnable(GL_TEXTURE_2D);
        //glColor3f(1, 0, 0);
        // up
        glBindTexture(GL_TEXTURE_2D, skytextures[UP].getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-radius/2, radius/2, -radius/2);
        glTexCoord2f(0, 1);
        glVertex3f(-radius/2, radius/2, radius/2);
        glTexCoord2f(1, 1);
        glVertex3f(radius/2, radius/2, radius/2);
        glTexCoord2f(1, 0);
        glVertex3f(radius/2, radius/2, -radius/2);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, skytextures[EAST].getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(radius/2, radius/2, radius/2);
        glTexCoord2f(1, 0);
        glVertex3f(radius/2, radius/2, -radius/2);
        glTexCoord2f(1, 1);
        glVertex3f(radius/2, -radius/2, -radius/2);
        glTexCoord2f(0, 1);
        glVertex3f(radius/2, -radius/2, radius/2);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, skytextures[WEST].getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-radius/2, radius/2, -radius/2);
        glTexCoord2f(1, 0);
        glVertex3f(-radius/2, radius/2, radius/2);
        glTexCoord2f(1, 1);
        glVertex3f(-radius/2, -radius/2, radius/2);
        glTexCoord2f(0, 1);
        glVertex3f(-radius/2, -radius/2, -radius/2);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, skytextures[NORTH].getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-radius/2, radius/2, radius/2);
        glTexCoord2f(1, 0);
        glVertex3f(radius/2, radius/2, radius/2);
        glTexCoord2f(1, 1);
        glVertex3f(radius/2, -radius/2, radius/2);
        glTexCoord2f(0, 1);
        glVertex3f(-radius/2, -radius/2, radius/2);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, skytextures[SOUTH].getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(radius/2, radius/2, -radius/2);
        glTexCoord2f(1, 0);
        glVertex3f(-radius/2, radius/2, -radius/2);
        glTexCoord2f(1, 1);
        glVertex3f(-radius/2, -radius/2, -radius/2);
        glTexCoord2f(0, 1);
        glVertex3f(radius/2, -radius/2, -radius/2);

        glEnd();
    }

}
