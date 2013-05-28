package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;

public abstract class CollisionMask {

    protected Vector3D position;

    public CollisionMask(Vector3D pos) {
        this.position = pos;
    }

    public void update(Vector3D newpos) {
        this.position = newpos.copy();
    }

    public Vector3D getPosition() {
        return position;
    }

}
