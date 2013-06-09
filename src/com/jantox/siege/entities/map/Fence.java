package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Fence extends Entity {

    int rot = 0;
    Vector3D gdis;

    public Fence(Vector3D pos, int rot, Vector3D gdis) {
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
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glPushMatrix();
        glTranslatef((float)gdis.x, (float)gdis.y + down, (float)gdis.z);
        glRotatef((rot == 90 ? 0 : 90), 0, 1, 0);
        glScalef(2f,2f,2f);
        glCallList(Resources.getModel(25));
        glPopMatrix();
        glRotatef(90, 1, 0, 0);
        glRotatef(180, 0, 1, 0);
        glRotatef(rot, 0, 0, 1);
        glScalef(0.1f, 0.125f, 0.1f);
        glCallList(Resources.getModel(24));
        glPopMatrix();
    }

}
