package com.jantox.siege.entities.resources;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;

public class Log extends Entity {

    private int value;
    private Vector3D color;

    float gravity = 0.005f;

    float angle = 0f;

    public Log(Vector3D pos) {
        super(pos);

        this.velocity = new Vector3D(Entity.rand.nextGaussian()/50, Math.abs(Entity.rand.nextGaussian())/5f, Entity.rand.nextGaussian()/50);

        this.affectGravity = true;
        angle = Entity.rand.nextInt(360);
    }

    boolean onground = false;

    @Override
    public void update(float delta) {
        Vector3D ppos = Entity.level.getPlayer().getCamera().getCamera().copy();
        ppos.y -= 0.5f;

        if(this.onground)
            if(this.pos.distanceSquared(ppos) <= 4 * 4) {
                Vector3D vel = new Vector3D(ppos.x - pos.x,  ppos.y - pos.y, ppos.z - pos.z);
                vel.normalize();
                vel.divide(2.5);
                pos.add(vel);

                if(this.pos.distanceSquared(ppos) <= 3) {
                    this.expired = true;
                    this.pos = level.getPlayer().getCamera().getCamera().copy();

                }
            }

        angle += 4f;
        if(angle >= 360)
            angle -= 360;


        if(onground)
            pos.y = Math.cos(Math.toRadians(angle)*4) / 25 - 1.75;

        if(!onground) {
            pos.add(velocity);
            if(affectedByGravity()) {
                velocity.y += gravity;
                gravity -= 0.001f;
            }
            if(this.pos.y < -1.75) {
                this.pos.y = -1.75;
                onground = true;
            }
        }
    }

    @Override
    public void render() {
        glPushMatrix();
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glRotatef(angle, 0, 1, 0);
        glScalef(0.45f, 0.45f, 0.45f);
        glCallList(Resources.getModel(10));
        glPopMatrix();
    }
}
