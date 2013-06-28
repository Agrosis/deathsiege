package com.jantox.siege.particle;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.level.Level;

import static org.lwjgl.opengl.GL11.*;

public class Particle extends Entity {

    private Vector3D velocity;
    private float angle;
    private int direction;

    private int life, maxlife;
    private boolean billboard = false;

    private int texture;
    private Vector3D color;
    private Vector3D scale;
    private float alpha;

    private ParticleBehavior behavior;

    public Particle(Vector3D pos, Level l, ParticleBehavior behavior) {
        super(pos, l);
        this.behavior = behavior;

        this.velocity = new Vector3D();
        this.color = new Vector3D();
        scale = new Vector3D(0.015f, 0.015f, 0.015f);
        maxlife = 50;
        gravity = 0;
        alpha = 1;
        texture = -1;

        if(rand.nextInt() % 2 == 0)
            direction = 1;
        else
            direction = -1;

        this.behavior.init(this);
    }

    public void update(float delta) {
        behavior.update(this);

        life++;
        if(life > maxlife)
            this.expired = true;

        pos.x += velocity.x;
        pos.y += velocity.y;
        pos.z += velocity.z;

        if(affectGravity) {
            velocity.y += gravity;
            gravity -= 0.04f;
        }
    }

    public void render() {
        glDisable(GL_TEXTURE_2D);
        glPushMatrix();

        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);

        if(!billboard)
            glRotatef(angle, 0, 1, 0);
        else {
            double ang = this.pos.angleXZ(level.getPlayer().getCamera().getCamera());
            glRotatef((float)-ang+90, 0, 1, 0);
            //ang = this.pos.angleXY(Entity.level.getPlayer().getCamera().getCamera());
            //glRotatef((float)ang, 1, 0, 0);
        }

        //glTranslatef((float)-pos.x, (float)-pos.y, (float)-pos.z);
        glScalef((float) scale.x, (float)scale.y, (float)scale.z);
        glColor4f((float)color.x, (float)color.y, (float)color.z, alpha);
        //glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);

        if(texture != -1) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);
            glBindTexture(GL_TEXTURE_2D, Resources.getTexture(texture).getTextureID());
        }

        glBegin (GL_QUADS);
        if(texture != -1)
            glTexCoord2f(0f, 0f);
        glVertex3f (-1, -1, 0);
        if(texture != -1)
            glTexCoord2f(1f, 0f);
        glVertex3f (1, -1, 0);
        if(texture != -1)
            glTexCoord2f(1f, 1f);
        glVertex3f (1, 1, 0);
        if(texture != -1)
            glTexCoord2f(0f, 1f);
        glVertex3f (-1, 1, 0);
        glEnd();

        glPopMatrix();

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);
    }

    public int getLife() {
        return life;
    }

    public void setLife(int l) {
        this.life = l;
    }

    public Vector3D getColor() {
        return color;
    }

    public void setColor(Vector3D color) {
        this.color = color.copy();
    }

    public Vector3D getScale() {
        return scale;
    }

    public void setScale(Vector3D f) {
        this.scale = f;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3D vel) {
        this.velocity = vel;
    }

    public void rotate(float a) {
        this.angle += a * direction;
        if(angle < 0)
            angle += 360;
        if(angle >= 360)
            angle -= 360;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float f) {
        this.angle = f;
    }

    public int getMaxLife() {
        return maxlife;
    }

    public void setMaxLife(int l) {
        this.maxlife = l;
    }

    public float getAlpha() {
        return alpha;
    }

    public void fade(float f) {
        alpha += f;
        if(alpha < 0)
            alpha = 0;
        else if(alpha > 1)
            alpha = 1;
    }

    public void setTexture(int tid) {
        this.texture = tid;
    }

    public void setBillboarded(boolean b) {
        this.billboard = b;
    }

}
