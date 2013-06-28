package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Path extends Entity {

    public Path(Vector3D pos) {
        super(pos, null);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        glTranslatef((float)pos.x + 1.5f, (float)pos.y-1.999f, (float)pos.z + 1.5f);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(4).getTextureID());
        glScalef(9, 9, 9);
        glColor3f(1f, 1f, 1f);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-1, 0, -1);
        glTexCoord2f(1, 0);
        glVertex3f(0, 0, -1);
        glTexCoord2f(1, 1);
        glVertex3f(0, 0, 0);
        glTexCoord2f(0, 1);
        glVertex3f(-1, 0, 0);
        glEnd();
        glPopMatrix();
    }

}
