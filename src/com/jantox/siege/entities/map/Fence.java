package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Fence extends Entity {

    int rot = 0;

    public Fence(Vector3D pos, int rot) {
        super(pos);
        this.rot = rot;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        glPushMatrix();
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glRotatef(rot, 0, 1, 0);
        glCallList(Resources.getModel(12));
        glPopMatrix();
    }

}
