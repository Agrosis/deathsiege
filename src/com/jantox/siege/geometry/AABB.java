package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;

public class AABB extends CollisionMask {

    float r[];

    public AABB(Vector3D pos, float hx, float hy, float hz) {
        super(pos);
        r = new float[3];
        this.r[0] = hx;
        this.r[1] = hy;
        this.r[2] = hz;
    }

    public float getHalfWidth() {
        return r[0];
    }

    public float getHalfHeight() {
        return r[1];
    }

    public float getHalfZ() {
        return r[2];
    }

}
