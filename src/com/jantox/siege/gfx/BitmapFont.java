package com.jantox.siege.gfx;

import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;

public class BitmapFont {

    private Texture texture;
    private int width, height;

    public BitmapFont(Texture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public void drawText(String s, int x, int y) {
        glEnable(GL_TEXTURE_2D);

        float diff = (1f / 768f) * 8;

        glScalef(2, 2, 0);

        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if(c >= 0x20 && c <= 0x7D) {
                c -= 0x20;


                float xtpos = diff * c;

                glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
                glBegin(GL_QUADS);
                glTexCoord2f(xtpos, 0);
                glVertex2f(x + i * width, y);
                glTexCoord2f(xtpos + diff, 0);
                glVertex2f(x + i * width + width, y);
                glTexCoord2f(xtpos + diff, 1);
                glVertex2f(x + i * width + width, y + height);
                glTexCoord2f(xtpos, 1);
                glVertex2f(x + i * width, y + height);
                glEnd();
            }
        }

        glDisable(GL_TEXTURE_2D);
    }

}
