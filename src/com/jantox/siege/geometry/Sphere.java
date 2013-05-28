package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;

public class Sphere extends CollisionMask {

    private float radius;

    public Sphere(Vector3D pos, float radius) {
        super(pos);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

}
