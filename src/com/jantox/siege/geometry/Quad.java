package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;
import org.lwjgl.opengl.GL11;

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

    public void render() {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex3f((float)a.x, (float)a.y, (float)a.z);
        GL11.glVertex3f((float)b.x, (float)b.y, (float)b.z);
        GL11.glVertex3f((float)c.x, (float)c.y, (float)c.z);
        GL11.glVertex3f((float)d.x, (float)d.y, (float)d.z);
        GL11.glEnd();
    }

}
