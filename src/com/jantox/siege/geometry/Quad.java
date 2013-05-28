package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;

public class Quad {

    public Vector3D a, b, c, d;

    public Quad(Vector3D a, Vector3D b, Vector3D c,Vector3D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Vector3D getNormal() {
        Vector3D f = b.copy();
        f.subtract(a);
        f.normalize();
        Vector3D g = d.copy();
        g.subtract(a);
        g.normalize();
        Vector3D n = f.crossProduct(g);
        n.normalize();
        return n;
    }
}
