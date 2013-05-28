package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.resources.Gem;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glRotatef;

public class Kage extends Living {

    float viewangle = 0f;

    float counter = 0;
    float version;

    float gravity = 0;

    private ControlPoint target;

    public Kage(Vector3D pos, float version, float angle) {
        super(pos, (int)(25 * version));
        this.version = version;

        pos.y -= version * 2;

        gravity = 0.2f;

        counter = rand.nextInt(256);

        target = level.getRandomControlPoint();

        this.mask = new Sphere(pos, version * 2);
        this.viewangle = (float)this.pos.angleXZ(target.getPosition());
    }

    @Override
    public void update(float delta) {
        counter += 0.1f;
        mask.update(pos);

        if(this.pos.distanceSquared(target.getPosition()) <= 5 * 5) {
            this.expired = true;
        }

        Vector3D cam = target.getPosition();

        Vector3D vel = new Vector3D(cam.x - pos.x, cam.y - pos.y, cam.z - pos.z);
        vel.normalize();
        vel.divide(7);

        //Level.psys.addParticle(new Particle(this.pos.copy(), new ParticleBehavior.KageParticle()));
        //Level.psys.addParticle(new Particle(this.pos.copy(), new ParticleBehavior.KageParticle()));

        vel.y = 0;

        if(gravity == 0) {
            gravity = (version * 0.2f);
        } else {
            gravity -= 0.01;
        }

        pos.y += gravity;
        if(pos.y < -2) {
            pos.y = -2;
            gravity = 0;
        }

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

        if(viewangle != (float)ang) {
            vel.divide(2);
        }

        if(this.health < 0) {
            if(!expired) {
                for(int i = 0; i < 10; i++) {
                    level.spawn(new Gem(this.pos.copy()));
                }
            }
            this.expired = true;
        }

        pos.add(vel);
    }

    @Override
    public void render() {
        GL11.glPushMatrix();
        //GL11.glColor3f(0.5f, 0, 0);

        GL11.glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        GL11.glScalef(version, version, version);
        glRotatef((float)-viewangle+90, 0, 1, 0);

        GL11.glCallList(Resources.getModel(3));
        GL11.glPopMatrix();
    }
}
