package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.level.Level;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Fortress extends Entity {

    float gate[];
    boolean status[];

    public Fortress(Level level) {
        super(new Vector3D(), level);

        gate = new float[4];
        for(int i = 0; i < 4; i++) {
            gate[i] = 0;
        }
        status = new boolean[4];
    }

    int ticks = 0;

    @Override
    public void update(float delta) {
        for(int i = 0; i < 4; i++) {
            if(status[i]) {
                if(gate[i] > -70)
                    gate[i] -= 0.14;
                if(gate[i] < -70)
                    gate[i] = -70;
            } else {
                if(gate[i] < 0)
                    gate[i] += 0.14;
                if(gate[i] > 0)
                    gate[i] = 0;
            }
        }
    }

    public void open() {
        status[0] = true;
        status[1] = true;
        status[2] = true;
        status[3] = true;
    }

    public void close() {
        status[0] = false;
        status[1] = false;
        status[2] = false;
        status[3] = false;
    }

    @Override
    public void render() {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);

        // east
        glPushMatrix();
        glTranslatef(225, -4 + gate[1], 0);
        glRotatef(90, 0, 1, 0);
        glScalef(3.6f, 5f, 3.6f);
        glCallList(Resources.getModel(25));
        glPopMatrix();

        // west
        glPushMatrix();
        glTranslatef(-225, -4 + gate[3], 0);
        glRotatef(90, 0, 1, 0);
        glScalef(3.6f, 5f, 3.6f);
        glCallList(Resources.getModel(25));
        glPopMatrix();

        // north
        glPushMatrix();
        glTranslatef(0, -4 + gate[0], -225);
        //glRotatef(90, 0, 1, 0);
        glScalef(3.6f, 5f, 3.6f);
        glCallList(Resources.getModel(25));
        glPopMatrix();

        // south
        glPushMatrix();
        glTranslatef(0, -4 + gate[2], 225);
        //glRotatef(90, 0, 1, 0);
        glScalef(3.6f, 5f, 3.6f);
        glCallList(Resources.getModel(25));
        glPopMatrix();

        // stone wall
        glTranslatef(0, -7, 0);
        glRotatef(-90, 1, 0, 0);
        glScalef(0.35f, 0.35f, 0.35f);
        glCallList(Resources.getModel(24));
        glPopMatrix();
    }

    public boolean isOpening(int i) {
        return status[i] && gate[i] < 0;
    }

    public boolean isOpen() {
        return status[0] == true && gate[0] == -70;
    }
}
