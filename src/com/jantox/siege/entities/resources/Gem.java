package com.jantox.siege.entities.resources;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.sfx.AudioController;

import static org.lwjgl.opengl.GL11.*;

public class Gem extends Entity {

    private int value;
    private Vector3D color;

    float gravity = 0.005f;

    float angle = 0f;

    public Gem(Vector3D pos) {
        super(pos);

        color = new Vector3D(1, 1, 0);

        //if(Entity.rand.nextInt(60) < 30)
        //    color = new Vector3D((float)206/255, (float)32/255, (float)41/255);
        //else if(Entity.rand.nextInt(90) < 60)
         //   color = new Vector3D((float)80/255, (float)200/255, (float)120/255);
      // else if(Entity.rand.nextInt(60) < 60)
            color = new Vector3D(1, 1, 0);

        this.velocity = new Vector3D(Entity.rand.nextGaussian()/50, Math.abs(Entity.rand.nextGaussian())/10f, Entity.rand.nextGaussian()/50);

        this.affectGravity = true;
        angle = Entity.rand.nextInt(360);
    }

    boolean onground = false;

    @Override
    public void update(float delta) {
        Vector3D ppos = Entity.level.getPlayer().getCamera().getCamera().copy();
        ppos.y -= 0.5f;

        if(this.pos.distanceSquared(ppos) <= 5 * 5) {
            Vector3D vel = new Vector3D(ppos.x - pos.x,  ppos.y - pos.y, ppos.z - pos.z);
            vel.normalize();
            vel.divide(2.5);
            pos.add(vel);

            if(this.pos.distanceSquared(ppos) <= 3) {
                this.expired = true;
                this.pos = level.getPlayer().getCamera().getCamera().copy();
                GameInstance.audio.playSound(3);
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
        glDisable(GL_TEXTURE_2D);
        glPushMatrix();
        //glScalef(0.25f, 0.25f, 0.25f);
        glColor3f((float)color.x, (float)color.y, (float)color.z);
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glRotatef(angle, 1, 1, 0);
        glScalef(0.25f, 0.25f, 0.25f);
        glCallList(Resources.getModel(1));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
    }
}
