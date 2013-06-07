package com.jantox.siege.entities;

import com.jantox.siege.Vector3D;
import static org.lwjgl.opengl.GL11.*;

public class OnlinePlayer extends Entity {

    private int uid;

    public OnlinePlayer(int userid) {
        super(new Vector3D());
        this.uid = userid;
    }

    @Override
    public void update(float delta) {

    }

    public void render() {
        glPushMatrix();
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glColor3f(1, 1, 1);
        glBegin(GL_QUADS);
        glVertex3f(0, 0, 0);
        glVertex3f(1, 0, 0);
        glVertex3f(1, 1, 0);
        glVertex3f(0, 1, 0);
        glEnd();
        glPopMatrix();
    }

    public int getUserID() {
        return uid;
    }

}
