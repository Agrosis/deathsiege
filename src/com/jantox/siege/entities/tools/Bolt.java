package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;

import static org.lwjgl.opengl.GL11.*;

public class Bolt extends Entity {

    private Vector3D vel;
    private int life;

    Vector3D rotation;

    float vely = 2f;

    public Bolt(Vector3D pos, Vector3D velx, Vector3D rotation) {
        super(pos);
        this.vel = velx;
        life = 50;

        this.rotation = rotation;

        /*Vector3D ppos = level.getPlayer().getPosition();
        Vector3D cam = level.getPlayer().getCamera().getDirectionVector();
        cam.multiply(2);
        ppos.add(cam);

        vel = ppos.copy();
        vel.subtract(pos);
        vel.normalize();
        //vel.multiply(0.5);
       */

        vely = (float)vel.y;

        this.mask = new Sphere(pos, 0.5f);
    }

    @Override
    public void update(float delta) {
        this.pos.add(vel);
        vel.y -= 0.008f;
        mask.update(pos);
        if(pos.y <= -2) {
            this.expired = true;
        }
    }

    @Override
    public void render() {
        glDisable(GL_TEXTURE_2D);
        glPushMatrix();
        glColor3f(0, 0, 0);
        glTranslatef((float) pos.x, (float) pos.y, (float) pos.z);
        glRotatef(Entity.level.getPlayer().getCamera().getYaw(), 0, 1, 0);

        double length = vel.length();
        double vy = vel.y;

        glRotatef((float)Math.toDegrees(Math.asin(vy / length)), 1, 0, 0);
        glScalef(0.5f, 0.5f, 0.5f);
        glTranslatef((float)rotation.x, (float)rotation.y, (float)rotation.z);
        glCallList(Resources.getModel(17));
        glPopMatrix();
    }
}
