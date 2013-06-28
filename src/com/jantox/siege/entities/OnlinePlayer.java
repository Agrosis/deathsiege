package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.level.Level;
import com.jantox.siege.net.Interpolation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class OnlinePlayer extends Entity {

    private int uid;

    // this.pos is the interpolated position
    double postick = 0;
    public Vector3D prev_pos = new Vector3D();
    public Vector3D next_pos = new Vector3D();

    private float pitch, opitch, npitch;
    private float yaw, oyaw, nyaw;

    public OnlinePlayer(int userid, Level level) {
        super(new Vector3D(), level);
        this.uid = userid;
        pitch = 0;
        yaw = 0;
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

        float yawdisp = nyaw - oyaw;
        float pitchdisp = npitch - opitch;
        yawdisp /= 5;
        pitchdisp /= 5;
        yawdisp *= postick;

        if(opitch != npitch)
            this.pitch = opitch + pitchdisp;
        if(oyaw != nyaw)
            this.yaw = oyaw + yawdisp;

        double ydisp = next_pos.y - prev_pos.y;
        ydisp /= 5;
        ydisp *= postick;

        this.pos.y = this.prev_pos.y + ydisp;

        glPushMatrix();
        glTranslatef((float)pos.x, (float)pos.y-1.5f, (float)pos.z);
        glRotatef(yaw+180, 0, 1, 0);
        glColor3f(1, 1, 1);

        GL11.glPushMatrix();
        glTranslatef(0, 2, 0);
        glRotatef(pitch, 1, 0, 0);
        glTranslatef(0, -2, 0);
        GL11.glCallList(Resources.getModel(18));
        GL11.glPopMatrix();

        GL11.glCallList(Resources.getModel(26));

        GL11.glPushMatrix();
        GL11.glCallList(Resources.getModel(19));
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        //GL11.glTranslatef(0, -0.5f, 0);
        GL11.glCallList(Resources.getModel(20));
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        //GL11.glTranslatef(0, -0.5f, 0);
        GL11.glCallList(Resources.getModel(21));
        GL11.glPopMatrix();

        glPopMatrix();
    }

    public int getUserID() {
        return uid;
    }


    public void setOrientation(float pitch, float yaw) {
        this.opitch = this.npitch;
        this.oyaw = this.nyaw;

        this.npitch = pitch;
        this.nyaw = yaw;
    }
}
