package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Fortress extends Entity {

    int rot = 0;
    Vector3D gdis;

    public Fortress(Vector3D pos, int rot, Vector3D gdis) {
        super(pos);
        this.rot = rot;
        this.gdis = gdis;
    }

    @Override
    public void update(float delta) {

    }

    float down = 0f;

    @Override
    public void render() {
        down -= 0.05f;

        glPushMatrix();

        glDisable(GL_TEXTURE_2D);
        glColor3f(0.2f, 0.2f, 0.2f);
        glPushMatrix();
        glTranslatef(263, -4, 0);
        glRotatef(90, 0, 1, 0);
        glScalef(4.1f, 5f, 4.1f);
        glCallList(Resources.getModel(25));
        glPopMatrix();

        glPushMatrix();
        glTranslatef(-263, -4, 0);
        glRotatef(90, 0, 1, 0);
        glScalef(4.1f, 5f, 4.1f);
        glCallList(Resources.getModel(25));
        glPopMatrix();

        glPushMatrix();
        glTranslatef(0, -4, 263);
        glScalef(4.1f, 5f, 4.1f);
        glCallList(Resources.getModel(25));
        glPopMatrix();

        glPushMatrix();
        glTranslatef(0, -4, -263);
        glScalef(4.1f, 5f, 4.1f);
        glCallList(Resources.getModel(25));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);

        glTranslatef(0, -3, 0);
        glRotatef(-90, 1, 0, 0);
        glScalef(0.4f, 0.4f, 0.35f);
        glCallList(Resources.getModel(24));
        glPopMatrix();
    }

}
