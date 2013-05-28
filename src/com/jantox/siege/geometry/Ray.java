package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;

public class Ray extends CollisionMask {

    private Vector3D dir;

    public Ray(Vector3D pos, Vector3D dir) {
        super(pos);
        this.dir = dir.copy();
        dir.normalize();
    }

    public Vector3D getDirection() {
        return dir;
    }

    public Vector3D getPointOnRay(double d) {
        Vector3D pos = position.copy();
        Vector3D cdir = dir.copy();
        cdir.normalize();
        cdir.multiply(d);
        pos.add(cdir);
        return pos;
    }

}
