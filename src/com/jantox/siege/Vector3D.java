package com.jantox.siege;

import java.util.Random;

public class Vector3D {

    public double x, y, z;
    public static Random rand = new Random(2423);

    public Vector3D() {
        this.x = this.y = this.z = 0;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double dotProduct(Vector3D b) {
        return this.x * b.x + this.y * b.y + this.z * b.z;
    }

    public double angleXZ(Vector3D b) {
        double dx = this.x - b.x;
        double dz = this.z - b.z;

        return Math.toDegrees(Math.atan2(dz, dx));
    }

    public double angleXY(Vector3D b) {
        double dx = this.x - b.x;
        double dy = this.y - b.y;

        return Math.toDegrees(Math.atan2(dy, dx));
    }

    public Vector3D crossProduct(Vector3D b) {
        Vector3D n = new Vector3D();

        n.x = y * b.z - z * b.y;
        n.y = z * b.x - x * b.z;
        n.z = x * b.y - y * b.x;

        return n;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void add(Vector3D b) {
        this.x += b.x;
        this.y += b.y;
        this.z += b.z;
    }

    public void subtract(Vector3D b) {
        this.x -= b.x;
        this.y -= b.y;
        this.z -= b.z;
    }

    public void multiply(double d) {
        this.x *= d;
        this.y *= d;
        this.z *= d;
    }

    public void divide(double d) {
        this.x /= d;
        this.y /= d;
        this.z /= d;
    }

    public void normalize() {
        double len = this.length();
        this.x /= len;
        this.y /= len;
        this.z /= len;
    }

    public Vector3D copy() {
        return new Vector3D(x, y, z);
    }

    public double distance(Vector3D b) {
        return Math.sqrt(distanceSquared(b));
    }

    public double distanceSquared(Vector3D b) {
        return (x - b.x) * (x - b.x) + (y - b.y) * (y - b.y) + (z - b.z) * (z - b.z);
    }

    public void debug() {
        System.out.println(x + ", " + y + ", " + z);
    }

    public void debug(String tag) {
        System.out.println(tag + ": " + x + ", " + y + ", " + z);
    }

    public Vector3D getCloseTo(float radius) {
        return new Vector3D(x + rand.nextGaussian() * radius,y + rand.nextGaussian() * radius,z + rand.nextGaussian() * radius);
    }

    public Vector3D getInverse() {
        return new Vector3D(-x, -y, -z);
    }
}
