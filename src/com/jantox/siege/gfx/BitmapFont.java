package com.jantox.siege.gfx;

import static org.lwjgl.opengl.GL11.*;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import org.newdawn.slick.opengl.Texture;

public class BitmapFont {

    private Texture texture;
    private int width, height;

    public BitmapFont(Texture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public void drawText(String s, int x, int y, float scale, Vector3D color) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        glScalef(scale, scale, scale);

        glColor3f((float)color.x, (float)color.y, (float)color.z);
        for(int i = 0; i < s.length(); i++) {
            int index = s.charAt(i) - '0';
            index *= 16;

            glBegin(GL_QUADS);
            glTexCoord2f(index/256f, 0);
            glVertex2f(x + i * 18, y);
            glTexCoord2f(index/256f + 16f/256f, 0);
            glVertex2f(x + 32 + i * 18, y);
            glTexCoord2f(index/256f + 16f/256f, 1);
            glVertex2f(x + 32 + i * 18, y + 32);
            glTexCoord2f(index/256f, 1);
            glVertex2f(x + i * 18, y + 32);
            glEnd();
        }

        glDisable(GL_TEXTURE_2D);
        glPopMatrix();
    }

}
