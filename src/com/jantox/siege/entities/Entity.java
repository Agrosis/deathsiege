package com.jantox.siege.entities;

import com.jantox.siege.level.Level;
import com.jantox.siege.Vector3D;
import com.jantox.siege.geometry.CollisionMask;

import java.util.Random;

public abstract class Entity {

    protected Level level;

    public Vector3D pos;
    public Vector3D velocity;

    protected boolean affectGravity = false;
    protected float gravity = 0.0f;

    protected CollisionMask mask;
    protected boolean expired = false;

    public static Random rand = new Random(System.currentTimeMillis());

    public long entityid;

    public Entity(Vector3D pos, Level level) {
        this.pos = pos;
        this.level = level;
        this.velocity = new Vector3D();
    }

    public abstract void update(float delta);
    public abstract void render();

    public void updateEnvironment() {
        pos.y += velocity.y;
        if(affectedByGravity()) {
            velocity.y += gravity;
            gravity -= 0.001f;
        }
        if(this.pos.y < -1.75)
            this.pos.y = -1.75;
    }

    public Vector3D getPosition() {
        return pos;
    }

    public void setPosition(Vector3D b) {
        this.pos = b.copy();
    }

    public CollisionMask getCollisionMask() {
        return mask;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean b) {
        this.expired = b;
    }

    public float getGravity() {
        return gravity;
    }

    public boolean affectedByGravity() {
        return affectGravity;
    }

    public void setGravity(boolean b, float f) {
        this.affectGravity = b;
        this.gravity = f;
    }


}
