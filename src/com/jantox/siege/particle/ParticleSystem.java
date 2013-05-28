package com.jantox.siege.particle;

import com.jantox.siege.Vector3D;

import java.util.ArrayList;

public class ParticleSystem {

    private Vector3D pos;

    private ArrayList<Particle> particles;

    public ParticleSystem(Vector3D pos) {
        this.pos = pos;
        particles = new ArrayList<Particle>();
    }

    public void update(float delta) {
        for(int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            if(p.isExpired())
                particles.remove(i);
            else
                p.update(delta);;
        }
    }

    public void render() {
        for(int i = 0; i < particles.size(); i++) {
            particles.get(i).render();
        }
    }

    public void addParticle(Particle p) {
        this.particles.add(p);
    }

}
