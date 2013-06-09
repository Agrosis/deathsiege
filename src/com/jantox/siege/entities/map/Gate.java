package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import static org.lwjgl.opengl.GL11.*;

public class Gate extends Entity {

    int dir = 1;
    int face = 0;

    public Gate(Vector3D pos, int face) {
        super(pos);
        this.face = face;
    }

    @Override
    public void update(float delta) {
        if(dir == 0) {
            if(pos.y > -10.5 && !this.isOpen())
                pos.y -= 0.03f;
        } else {
            if(pos.y < -2 && !this.isClose())
                pos.y += 0.03f;
        }
    }

    public void open() {
        dir = 0;
    }

    public void close() {
        dir = 1;
    }

    public boolean isOpen() {
        return pos.y <= -10.5;
    }

    public boolean isClose() {
        return pos.y >= -2;
    }

    @Override
    public void render() {
        glPushMatrix();

        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);

        if(face == 0) {
            glTranslatef(-3, 0, 0);
            for(int i = 0; i < 8; i++) {
                glTranslatef(2, 0, 0);

                //glCallList(Resources.getModel(8));
            }
        } else if(face == 1) {
            glTranslatef(0, 0, -3);
            for(int i = 0; i < 8; i++) {
                glTranslatef(0, 0, 2);
                glCallList(Resources.getModel(8));
            }
        }
        glPopMatrix();
    }
}
