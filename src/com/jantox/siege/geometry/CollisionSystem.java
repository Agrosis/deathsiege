package com.jantox.siege.geometry;

import com.jantox.siege.Vector3D;

public class CollisionSystem {

    public static boolean aaBBAABB(AABB a, AABB b) {
        if (Math.abs(a.position.x - b.position.x) > (a.r[0] + a.r[0])) return false;
        if (Math.abs(a.position.y - b.position.y) > (a.r[1] + a.r[1])) return false;
        if (Math.abs(a.position.z - b.position.z) > (a.r[2] + a.r[2])) return false;

        return true;
    }

    public static float sqDistPointAABB(final Vector3D p, final AABB aabb) {
        float sqDist = 0.0f;
        float v;
        float minX, minY, minZ, maxX, maxY, maxZ;

        // get the minX, maxX, minY, maxY and minZ, maxZ points of the AABB
        minX = (float) (aabb.position.x - aabb.r[0]);
        maxX = (float) (aabb.position.x + aabb.r[0]);

        minY = (float) (aabb.position.y - aabb.r[1]);
        maxY = (float) (aabb.position.y + aabb.r[1]);

        minZ = (float) (aabb.position.z - aabb.r[2]);
        maxZ = (float) (aabb.position.z + aabb.r[2]);

        // test the bounds against the points X axis
        v = (float) p.x;

        if (v < minX) sqDist += (minX - v) * (minX - v);
        if (v > maxX) sqDist += (v - maxX) * (v - maxX);

        // test the bounds against the points Y axis
        v = (float) p.y;

        if (v < minY) sqDist += (minY - v) * (minY - v);
        if (v > maxY) sqDist += (v - maxY) * (v - maxY);

        // test the bounds against the points Z axis
        v = (float) p.z;

        if (v < minZ) sqDist += (minZ - v) * (minZ - v);
        if (v > maxZ) sqDist += (v - maxZ) * (v - maxZ);

        return sqDist;
    }

    public static boolean sphereAABB(Sphere a , AABB aabb) {
        float sqDist = sqDistPointAABB(a.getPosition(), aabb);
        float r = a.getRadius();

        return sqDist <= r * r;
    }

    public static boolean sphereSphere(Sphere a, Sphere b) {
        if(a.getPosition().distanceSquared(b.getPosition()) <= ((a.getRadius() + b.getRadius()) * (a.getRadius() + b.getRadius())))
            return true;
        return false;
    }

    public static boolean raySphere(Ray r, Sphere s) {
        Vector3D rs = r.getPosition().copy();
        Vector3D rd = r.getDirection().copy();
        Vector3D sp = s.getPosition().copy();

        rd.normalize();
        double b = 2 * (rd.x * (rs.x - sp.x) + rd.y * (rs.y - sp.y) + rd.z * (rs.z - sp.z));
        double c = (rs.x - sp.x) * (rs.x - sp.x) + (rs.y - sp.y) * (rs.y - sp.y) + (rs.z - sp.z) * (rs.z - sp.z) - s.getRadius() * s.getRadius();

        double disc = b * b - 4 * c;
        if(disc < 0)
            return false;

        // intersection points on the ray
        //double t1 = (-b + Math.sqrt(disc)) / 2;
        //double t2 = (-b - Math.sqrt(disc)) / 2;

        return true;
    }

    public static boolean rayQuad(Ray r, Vector3D a, Vector3D b, Vector3D c, Vector3D d, Vector3D n, Vector3D col) {
        Vector3D ti = rayPlane(r, a, n, col);
        if(ti != null) {
            if(pointTriangle(ti, a, b, d)) {
                return true;
            }
            if(pointTriangle(ti, b, d, c)) {
                return true;
            }
        }

        return false;
    }

    // this function checks if a ray intersects with a plane
    public static Vector3D rayPlane(Ray r, Vector3D a, Vector3D n, Vector3D col) {
        double d = a.dotProduct(n);

        if(n.dotProduct(r.getDirection()) == 0)
            return null;

        double t = (d - n.dotProduct(r.getPosition())) / (n.dotProduct(r.getDirection()));
        if(t < 0)
            return null;

        if(col != null) {
            col.x = t;

        }

        return r.getPointOnRay(t);
    }

    // rayplane(float nx,float ny,float nz,float xs,float ys,
    // float zs,float xd,float yd,float zd,coordinate p1,coordinate p2,coordinate p3,coordinate p4,float* dist,coordinate* point)
    // rayplane(-vn.x,-vn.y,-vn.z,sp.x,sp.y,sp.z,vn.x,vn.y,vn.z,p1,p2,p3,p4,&dist1)
    public static Vector3D sphereQuad(Sphere s, Vector3D a, Vector3D b, Vector3D c, Vector3D d, Vector3D n) {
        Ray r1 = new Ray(s.getPosition(), n);
        Ray r2 = new Ray(s.getPosition(), n.getInverse());

        Vector3D resp = s.getPosition().copy();

        Vector3D d1 = new Vector3D(), d2 = new Vector3D();
        // checks for both sides
        if(rayQuad(r1, a, b, c, d, n.getInverse(), d1) || rayQuad(r2, a, b, c, d, n, d2)) {
            if(d1.x > s.getRadius() || d2.x > s.getRadius()) {
                return null;
            }
            if(d1.x > 0) {
                resp.x = resp.x - n.x * (s.getRadius() - d1.x);
                resp.y = resp.y - n.y * (s.getRadius() - d1.x);
                resp.z = resp.z - n.z * (s.getRadius() - d1.x);
            } else {
                resp.x = resp.x + n.x * (s.getRadius() - d2.x);
                resp.y = resp.y + n.y * (s.getRadius() - d2.x);
                resp.z = resp.z + n.z * (s.getRadius() - d2.x);
            }
        } else {
            return null;
        }

        return resp;
    }

    public static Vector3D sphereRamp(Sphere s, Vector3D a, Vector3D b, Vector3D c, Vector3D d, Vector3D n) {
        Ray r1 = new Ray(s.getPosition(), n);
        s.position.y -= 1f;

        Vector3D resp = s.getPosition().copy();

        Vector3D d1 = new Vector3D();
        // checks for both sides
        if(rayQuad(r1, a, b, c, d, n.getInverse(), d1)) {
            if(d1.x > s.getRadius()) {
                return null;
            }
            if(d1.x > 0) {
                Vector3D i = r1.getPointOnRay(d1.x);
                resp.y = i.y + s.getRadius() - 0.05;
            }
        } else {
            return null;
        }

        return resp;
    }

    // this function checks if a point contained within the supporting plane of a triangle
    // is contained within the triangle
    //
    public static boolean pointTriangle(Vector3D p, Vector3D a, Vector3D b, Vector3D c) {
        double ab = a.distance(b);
        double bc = b.distance(c);
        double ac = c.distance(a);

        float total_area = areaTriangle(new Vector3D(ab, bc, ac));

        double pa = p.distance(a);
        double pb = p.distance(b);
        double pc = p.distance(c);

        float at1 = areaTriangle(new Vector3D(ab, pb, pa));
        float at2 = areaTriangle(new Vector3D(pb, pc, bc));
        float at3 = areaTriangle(new Vector3D(pa, ac, pc));

        if(Math.abs(total_area - at1 - at2 - at3) <= 0.5)
            return true;

        //System.out.println((at1 + at2 + at3) + " : " + total_area);

        /*if(at1 + at2 + at3 == total_area)
            return true;
*/
        return false;
    }

    // area of a triangle using herons formula
    public static float areaTriangle(Vector3D sides) {
        double smp = (sides.x + sides.y + sides.z) / 2;

        return (float) Math.sqrt(smp * (smp - sides.x) * (smp - sides.y) * (smp - sides.z));
    }

}