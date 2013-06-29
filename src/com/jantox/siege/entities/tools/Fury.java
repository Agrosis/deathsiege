package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;

import static org.lwjgl.opengl.GL11.*;

public class Fury extends Entity {

    private Vector3D vel;
    private int life;

    Vector3D rotation;

    float vely = 2f;

    Vector3D color;

    public Fury(Vector3D pos, Level level, Vector3D vel) {
        super(pos, level);
        this.vel = vel;
        life = 200;

        color = new Vector3D(rand.nextInt(255)/255f, rand.nextInt(255)/255f, rand.nextInt(255)/255f);

        this.rotation = rotation;

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

        Level.psys.addParticle(new Particle(this.pos.getCloseTo(0.1f), level, new ParticleBehavior.Fury(color)));
        Level.psys.addParticle(new Particle(this.pos.getCloseTo(0.1f), level, new ParticleBehavior.Fury(color)));
        Level.psys.addParticle(new Particle(this.pos.getCloseTo(0.1f), level, new ParticleBehavior.Fury(color)));
    }

    @Override
    public void render() {

    }

}
