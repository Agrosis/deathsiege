package com.jantox.siege.entities.resources;

import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

public abstract class Resource extends Entity {

    public static final int AVAILABLE = 0;
    public static final int NOT_AVAILABLE = 1;

    protected int status;
    protected float progress;
    protected float maxProgress;
    protected float growthRate;

    public Resource(Vector3D pos) {
        super(pos, null);

        status = AVAILABLE;
        growthRate = 0.25f;
        progress = 300;
        maxProgress = 300;
    }

    @Override
    public void update(float delta) {
        if(this.status == NOT_AVAILABLE) {
            if(progress < maxProgress) {
                progress += growthRate;
                if(progress >= maxProgress) {
                    progress = maxProgress;
                    status = AVAILABLE;
                }
            }
        }
    }

    public int getStatus() {
        return status;
    }

    public float getProgress() {
        return progress;
    }

    public void useResource(float rate) {
        this.progress -= rate;
        //Level.psys.addParticle(new Particle(pos.copy(), new ParticleBehavior.CutParticle()));
        //Level.psys.addParticle(new Particle(pos.copy(), new ParticleBehavior.CutParticle()));
        //Level.psys.addParticle(new Particle(pos.copy(), new ParticleBehavior.CutParticle()));
        if(this.status == AVAILABLE && progress <= 0) {
            this.status = NOT_AVAILABLE;
            progress = 0;
        }
    }

}
