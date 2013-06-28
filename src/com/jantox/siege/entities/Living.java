package com.jantox.siege.entities;

import com.jantox.siege.Vector3D;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;

public abstract class Living extends Entity {

    protected int health, maxhealth;

    public Living(Vector3D pos, Level level, int health) {
        super(pos, level);
        this.health = this.maxhealth = health;
    }

    public void update(float delta) {
        if(this.isDead())
            this.expired = true;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxhealth;
    }

    public void damage(float d) {
        this.health -= d;
        if(health < 0)
            maxhealth = 0;

        Level.psys.addParticle(new Particle(this.pos.copy(), level, new ParticleBehavior.DamageParticle()));
        Level.psys.addParticle(new Particle(this.pos.copy(), level, new ParticleBehavior.DamageParticle()));
        Level.psys.addParticle(new Particle(this.pos.copy(), level, new ParticleBehavior.DamageParticle()));
        Level.psys.addParticle(new Particle(this.pos.copy(), level, new ParticleBehavior.DamageParticle()));
        Level.psys.addParticle(new Particle(this.pos.copy(), level, new ParticleBehavior.DamageParticle()));
    }

}
