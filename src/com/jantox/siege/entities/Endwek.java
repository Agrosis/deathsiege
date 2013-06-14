package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.SpawnerFactory;
import com.jantox.siege.Vector3D;
import com.jantox.siege.ai.AISet;
import com.jantox.siege.geometry.Sphere;
import org.lwjgl.opengl.GL11;

public class Endwek extends MultiplayerLiving {

    float gravity = 0;
    float viewangle = 0f;

    private AISet ai;

    private Entity target;
    private boolean giant = false;

    public Endwek(Vector3D pos, int cid, int eid) {
        super(pos, 100, eid);

        gravity = 0.2f;

        if(eid == 1) {
            giant = true;
        }

        target = level.getControlPoint(cid);

        this.mask = new Sphere(pos, 2);
        this.viewangle = (float)this.pos.angleXZ(target.getPosition());
    }

    @Override
    public void update(float delta) {
        mask.update(pos);

        if(health <= 0) {
            this.expired = true;
            SpawnerFactory.monstersleft --;
        }

        Vector3D cam = target.getPosition();
        Vector3D vel = new Vector3D(cam.x - pos.x, cam.y - pos.y, cam.z - pos.z);
        vel.normalize();
        vel.y = 0;

        if(!giant)
            vel.divide(13);
        else
            vel.divide(3);

        pos.add(vel);

        double ang = this.pos.angleXZ(target.getPosition());
        if(viewangle < ang) {
            viewangle += 1f;
            if(viewangle > ang)
                viewangle = (float) ang;
        } else if(viewangle > ang) {
            viewangle -= 1f;
            if(viewangle < ang)
                viewangle = (float) ang;
        }
    }

    int arm = 0;
    int dir = 0;

    int swing = 0;

    @Override
    public void render() {
        if(dir == 0) {
            arm += 2;
            if(arm > 30) {
                arm = 30;
                dir = 1;
            }
        } else {
            arm -= 2;
            if(arm < -30) {
                arm = -30;
                dir = 0;
            }
        }

        swing++;

        GL11.glPushMatrix();
        GL11.glColor3f(1.0f, 0f, 0f);

        pos.y = 0;
        GL11.glTranslatef((float)pos.x, (float)pos.y - 1f, (float)pos.z);
        if(giant) {
            GL11.glTranslatef(0, 7.5f, 0);
        }

        if(giant)
            GL11.glScalef(9f, 9f, 9f);
        GL11.glRotatef(-viewangle-90, 0, 1, 0);
        GL11.glCallList(Resources.getModel(18));
        GL11.glCallList(Resources.getModel(26));

        GL11.glPushMatrix();
        GL11.glCallList(Resources.getModel(19));
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        //GL11.glTranslatef(0, -0.5f, 0);
        GL11.glRotatef(-arm, 1, 0, 0);
        GL11.glCallList(Resources.getModel(20));
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        //GL11.glTranslatef(0, -0.5f, 0);
        GL11.glRotatef(arm, 1, 0, 0);
        GL11.glCallList(Resources.getModel(21));
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

}
