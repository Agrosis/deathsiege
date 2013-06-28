package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.geometry.AABB;

import static org.lwjgl.opengl.GL11.*;


public class Ladder extends Entity {


    public Ladder(Vector3D pos) {
        super(pos, null);
        this.mask = new AABB(this.pos, 1f, 10, 2f);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        glPushMatrix();
        glTranslatef((float)pos.x, (float)pos.y + 5, (float)pos.z);
        glScalef(2,2,2);
        glCallList(Resources.getModel(11));
        glPopMatrix();
    }
}
