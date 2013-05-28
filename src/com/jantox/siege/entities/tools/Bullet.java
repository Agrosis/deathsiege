package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.geometry.Sphere;

import static org.lwjgl.opengl.GL11.*;

public class Bullet extends Entity {

    private Vector3D vel;
    private int life;

    Vector3D rotation;

    Vector3D color;

    public Bullet(Vector3D pos, Vector3D vel, Vector3D rotation) {
        super(pos);
        this.vel = vel;
        life = 3;

        this.rotation = rotation;

        int r = rand.nextInt(2);
        if(r == 0) {
            color = new Vector3D(1, 1, 0);
        } else if(r == 1) {
            color = new Vector3D(205/255f, 40/255f, 0);
        }
        color = new Vector3D(1, 1, 0);

        /*pos.x += rand.nextGaussian() / 50;
        pos.y += rand.nextGaussian() / 50;
        pos.z += rand.nextGaussian() / 50;*/

        this.mask = new Sphere(pos, 0.5f);
    }

    @Override
    public void update(float delta) {
        //this.pos.add(vel);
        mask.update(pos);
        life--;
        if(life <= 0)
            this.expired = true;
    }

    @Override
    public void render() {
        /*glPushMatrix();
        glColor3f((float)color.x, (float)color.y, (float)color.z);
        glTranslatef((float) pos.x, (float) pos.y, (float) pos.z);
        glScalef(0.5f, 0.5f, 0.5f);
        glRotatef(Entity.level.getPlayer().getCamera().getYaw(), 0, 1, 0);
        glRotatef(-Entity.level.getPlayer().getCamera().getPitch(), 1, 0, 0);

        glTranslatef((float)rotation.x, (float)rotation.y, (float)rotation.z - 4);

        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(3).getTextureID());

        glBegin (GL_QUADS);
            glTexCoord2f(0f, 0f);
        glVertex3f (-1, -1, 0);
            glTexCoord2f(1f, 0f);
        glVertex3f (1, -1, 0);
            glTexCoord2f(1f, 1f);
        glVertex3f (1, 1, 0);
            glTexCoord2f(0f, 1f);
        glVertex3f (-1, 1, 0);
        glEnd();

        glPopMatrix();*/
    }

    public Vector3D getDirection() {
        Vector3D dir = vel.copy();
        dir.normalize();
        return dir;
    }
}
