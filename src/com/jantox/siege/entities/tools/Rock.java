package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;

import static org.lwjgl.opengl.GL11.*;

public class Rock extends Entity {

    private Vector3D vel;
    private int life;

    Vector3D rotation;

    public Rock(Vector3D pos, Level level, Vector3D velx) {
        super(pos, level);
        this.vel = velx;

        rotation = new Vector3D();

        this.mask = new Sphere(pos, 0.5f);
    }

    @Override
    public void update(float delta) {
        this.pos.add(vel);
        if(vel.y > 0)
            vel.y -= 0.01f;
        else
            vel.y -= 0.03;
        mask.update(pos);

        if(pos.y <= -2) {
            this.expired = true;
            for(int i = 0; i < 1; i++) {
                level.spawn(new Particle(this.pos.getCloseTo(6f), level, new ParticleBehavior.DirtSmokeParticle()));
            }
        }

        rotation.add(new Vector3D(rand.nextInt(10), rand.nextInt(10), rand.nextInt(10)));
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        glColor3f(1, 1, 1);
        glTranslatef((float) pos.x, (float) pos.y, (float) pos.z);

        glRotatef((float) rotation.x, 1, 0 ,0);
        glRotatef((float) rotation.y, 0, 1, 0);
        glRotatef((float) rotation.z, 0, 0, 1);
        glCallList(Resources.getModel(36));
        glPopMatrix();
    }

}
