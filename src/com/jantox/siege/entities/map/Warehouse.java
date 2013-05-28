package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;

public class Warehouse extends Entity {

    public Warehouse(Vector3D pos) {
        super(pos);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        glPushMatrix();
        //glColor3f(0.75f, 0.75f, 0.75f);

        glTranslatef((float)pos.x, (float)pos.y-2, (float)pos.z);

        glRotatef(-90, 1, 0, 0);

        glScalef(0.11f, 0.11f, 0.11f);
        //glScalef(7, 7, 7);
        glCallList(Resources.getModel(2));
        glPopMatrix();
    }
}
