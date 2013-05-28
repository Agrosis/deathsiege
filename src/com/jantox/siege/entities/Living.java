package com.jantox.siege.entities;

import com.jantox.siege.Vector3D;

public abstract class Living extends Entity {

    protected int health, maxhealth;

    public Living(Vector3D pos, int health) {
        super(pos);
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
    }

}
