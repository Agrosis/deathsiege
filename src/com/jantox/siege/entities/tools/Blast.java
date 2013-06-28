package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;

import static org.lwjgl.opengl.GL11.*;

public class Blast extends Entity {

    private Vector3D vel;
    private int life;

    Vector3D rotation;

    float vely = 2f;

    public Blast(Vector3D pos, Level level, Vector3D velx, Vector3D rotation) {
        super(pos, level);
        this.vel = velx;
        life = 0;

        this.rotation = rotation;

        vely = (float)vel.y;

        vel.multiply(2);

        this.mask = new Sphere(pos, 0.5f);
    }

    @Override
    public void update(float delta) {
        this.pos.add(vel);
        mask.update(pos);

        life ++;
        if(life >= 30)
            this.expired = true;
    }

    @Override
    public void render() {
        glDisable(GL_TEXTURE_2D);
        glPushMatrix();
        glColor3f(0, 0, 0);
        glTranslatef((float) pos.x, (float) pos.y, (float) pos.z);
        glRotatef(level.getPlayer().getCamera().getYaw(), 0, 1, 0);

        double length = vel.length();
        double vy = vel.y;

        glRotatef((float)Math.toDegrees(Math.asin(vy / length)), 1, 0, 0);
        glScalef(0.0125f, 0.0125f, 0.0125f);
        glTranslatef((float)rotation.x+0.25f, (float)rotation.y, (float)rotation.z);
        glCallList(Resources.getModel(29));
        glPopMatrix();
    }

}
