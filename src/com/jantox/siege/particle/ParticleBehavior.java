package com.jantox.siege.particle;

import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

public abstract class ParticleBehavior {

    public abstract void init(Particle p);
    public abstract void update(Particle p);

    public static class SpawnerParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.setScale(new Vector3D(0.025,0.025,0.025));
            p.setColor(new Vector3D(0.5f, 0, 0));
            p.setVelocity(new Vector3D(Entity.rand.nextGaussian()/10, 0.5/10f, Entity.rand.nextGaussian()/10));
        }

        @Override
        public void update(Particle p) {
            p.rotate(5);
        }

    }

    public static class DirtParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.setScale(new Vector3D(0.025,0.025,0.025));
            p.setColor(new Vector3D(1f,0, 0));
            p.setVelocity(new Vector3D(Entity.rand.nextGaussian()/5, 0.000005f, Entity.rand.nextGaussian()/5));
            p.setGravity(true, 0.1f);
            p.setBillboarded(true);
        }

        @Override
        public void update(Particle p) {

        }
    }

    public static class SmokeParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.pos.add(new Vector3D(Entity.rand.nextGaussian()*0.15, 0, Entity.rand.nextGaussian()*0.15));
            p.setColor(new Vector3D(1f, 1f, 1f));
            p.setVelocity(new Vector3D(Entity.rand.nextGaussian()/350, 0.04f, Entity.rand.nextGaussian()/350));
            p.setTexture(1);
            p.setMaxLife(Entity.rand.nextInt(250));
            p.setScale(new Vector3D(0.35f, 0.35f, 0.35f));
            p.setBillboarded(true);
        }

        @Override
        public void update(Particle p) {
            p.fade(-0.01f);
        }

    }

    public static class KageParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.pos.add(new Vector3D(Entity.rand.nextGaussian()*0.5, -1.5, Entity.rand.nextGaussian()*0.5));
            p.setColor(new Vector3D(159/255f, 0f, 1f));
            p.setVelocity(new Vector3D(Entity.rand.nextGaussian()/350, (int)Entity.rand.nextGaussian() * 0.08f, Entity.rand.nextGaussian()/350));
            p.setMaxLife(Entity.rand.nextInt(175));
            p.setScale(new Vector3D(0.05f, 0.05f, 0.05f));
            p.setBillboarded(true);
        }

        @Override
        public void update(Particle p) {
            //p.fade(-0.01f);
        }

    }

    public static class CutParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.setGravity(true, 0.01f);
            p.setColor(new Vector3D(1f, 0f, 0));
            p.setVelocity(new Vector3D(Entity.rand.nextGaussian()/25, Math.abs(Entity.rand.nextGaussian())/10f, Entity.rand.nextGaussian()/25));
            p.setMaxLife(200);
        }

        @Override
        public void update(Particle p) {

        }

    }

    public static class BlasterShot extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.setColor(new Vector3D(127/255f, 0f, 0));
            p.setBillboarded(true);
            p.setTexture(3);
            p.setScale(new Vector3D(0.05, 0.05, 0.05f));
            p.setMaxLife(50);
        }

        @Override
        public void update(Particle p) {

        }

    }

    public static class DamageParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.setGravity(true, 0.01f);
            p.setColor(new Vector3D(1f, 0f, 0));
            p.setBillboarded(true);
            p.setScale(new Vector3D(0.05, 0.05, 0.05f));
            p.setVelocity(new Vector3D(Entity.rand.nextGaussian()/25, Math.abs(Entity.rand.nextGaussian())/10f, Entity.rand.nextGaussian()/25));
            p.setMaxLife(200);
        }

        @Override
        public void update(Particle p) {

        }

    }

    public static class InnerFireParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.setColor(new Vector3D(1f, 0f, 0));
            p.setBillboarded(true);
            p.setScale(new Vector3D(0.1, 0.1, 0.1));
            p.setMaxLife(50);
            p.setTexture(4);
        }

        @Override
        public void update(Particle p) {

        }

    }

    public static class OuterFireParticle extends ParticleBehavior {

        @Override
        public void init(Particle p) {
            p.setColor(new Vector3D(205/255f, 140/255f, 0));
            p.setBillboarded(true);
            p.setScale(new Vector3D(0.1, 0.1, 0.1f));
            p.setMaxLife(50);
            p.setTexture(4);
        }

        @Override
        public void update(Particle p) {

        }

    }

    public static class Fury extends ParticleBehavior {

        private Vector3D color;

        public Fury(Vector3D c) {
            this.color = c;
        }

        @Override
        public void init(Particle p) {
            p.setColor(color);
            p.setBillboarded(true);
            p.setScale(new Vector3D(0.3, 0.3, 0.3f));
            p.setMaxLife(50);
            p.setVelocity(new Vector3D(0,-Math.abs(Entity.rand.nextGaussian())/100f, 0));
        }

        @Override
        public void update(Particle p) {

        }

    }
}
