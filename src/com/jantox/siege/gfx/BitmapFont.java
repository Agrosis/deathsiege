package com.jantox.siege.gfx;

import static org.lwjgl.opengl.GL11.*;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import org.newdawn.slick.opengl.Texture;

public class BitmapFont {

    public static final Vector3D BLUE = new Vector3D(0, 0, 1);
    public static final Vector3D RED = new Vector3D(1, 0, 0);
    public static final Vector3D GREEN = new Vector3D(0, 1, 0);
    public static final Vector3D YELLOW = new Vector3D(1, 1, 0);
    public static final Vector3D LIGHT_GRAY = new Vector3D(0.75, 0.75, 0.75);
    public static final Vector3D DARK_GRAY = new Vector3D(0.25, 0.25, 0.25);

    private Texture texture;
    private int width, height;

    public BitmapFont(Texture texture, int width, int height) {
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    public void drawText(String s, int x, int y, float scale, Vector3D color, boolean rightpad, float separation) {
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glPushMatrix();
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());

        if(separation == -1) {
            separation = width*scale;
        }

        if(rightpad) {
            x -= (s.length() * width) + (s.length() * separation);
        }

        glTranslatef(x, y, 0);
        glScalef(scale, scale, scale);

        float unit = (float) width / (float) texture.getImageWidth();

        glColor3f((float) color.x, (float) color.y, (float) color.z);

        int dx = -1;
        for(int i = 0; i < s.length(); i++) {
            int index = s.charAt(i) - ' ';
            dx++;
            if(s.charAt(i) == '\n') {
                dx = -1;
                glTranslatef(0, height-1, 0);
            } else {
                float xtc = (float) index * unit;

                glBegin(GL_QUADS);
                glTexCoord2f(xtc, 0);
                glVertex2f(dx * separation, 0);
                glTexCoord2f(xtc + unit, 0);
                glVertex2f(width+dx * separation, 0);
                glTexCoord2f(xtc + unit, 1);
                glVertex2f(width+dx * separation, height);
                glTexCoord2f(xtc, 1);
                glVertex2f(dx * separation, height);
                glEnd();
            }
        }

        glDisable(GL_TEXTURE_2D);
        glPopMatrix();
    }

}
