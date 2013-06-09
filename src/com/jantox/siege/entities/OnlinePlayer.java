package com.jantox.siege.entities;

import com.jantox.siege.Vector3D;
import com.jantox.siege.net.Interpolation;

import static org.lwjgl.opengl.GL11.*;

public class OnlinePlayer extends Entity {

    private int uid;

    // this.pos is the interpolated position
    double postick = 0;
    public Vector3D prev_pos = new Vector3D();
    public Vector3D next_pos = new Vector3D();

    public OnlinePlayer(int userid) {
        super(new Vector3D());
        this.uid = userid;
    }

    public void setNextPosition(Vector3D np) {
        this.prev_pos = next_pos.copy();
        next_pos = np.copy();
        postick = 0;
    }

    @Override
    public void update(float delta) {
        postick += 1;
    }

    public void render() {
        if(next_pos.x != prev_pos.x && next_pos.z != prev_pos.z) {
            double disp = next_pos.x - prev_pos.x;
            disp /= 5;
            disp *= postick;

            this.pos.x = prev_pos.x + disp;
            this.pos.z = Interpolation.linear_interpolate(prev_pos, next_pos, prev_pos.x + disp);
        }

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
