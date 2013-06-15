package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Fortress extends Entity {

    float gate[];
    boolean status[];

    public Fortress() {
        super(new Vector3D());

        gate = new float[4];
        for(int i = 0; i < 4; i++) {
            gate[i] = 0;
        }
        status = new boolean[4];
    }

    int ticks = 0;

    @Override
    public void update(float delta) {

    }

    public void open(int i) {
        status[i] = true;
    }

    public boolean isOpen(int i) {
        return status[i] && gate[i] <= -70;
    }

    public void close(int i) {
        status[i] = false;
    }

    public boolean isClosing(int i) {
        return !status[i] && gate[i] < 0;
    }

    public boolean isClosed(int i) {
        return !status[i] && gate[i] >= 0;
    }

    @Override
    public void render() {
        for(int i = 0; i < 4; i++) {
            if(status[i]) {
                if(gate[i] > -70)
                    gate[i] -= 0.07;
                if(gate[i] < -70)
                    gate[i] = -70;
            } else {
                if(gate[i] < 0)
                    gate[i] += 0.07;
                if(gate[i] > 0)
                    gate[i] = 0;
            }
        }

        glPushMatrix();
        /*glDisable(GL_TEXTURE_2D);

        glPushMatrix();
        glTranslatef(165, -4 + gate[0], 0);
        glRotatef(90, 0, 1, 0);
        glScalef(3.1f, 4f, 3.1f);
        glCallList(Resources.getModel(25));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);

        glPushMatrix();
        glTranslatef(165, -4 + gate[1], 77);
        glRotatef(90, 0, 1, 0);
        glScalef(3.1f, 4f, 3.1f);
        glCallList(Resources.getModel(25));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);

        glPushMatrix();
        glTranslatef(165, -4 + gate[2], -77);
        glRotatef(90, 0, 1, 0);
        glScalef(3.1f, 4f, 3.1f);
        glCallList(Resources.getModel(25));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);*/

        glTranslatef(0, -7, 0);
        glRotatef(-90, 1, 0, 0);
        glScalef(0.35f, 0.35f, 0.35f);
        glCallList(Resources.getModel(24));
        glPopMatrix();
    }

    public boolean isOpening(int i) {
        return status[i] && gate[i] < 0;
    }
}
