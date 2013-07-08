package com.jantox.siege;

public class Matrix4 {

    double vals[][];

    public Matrix4() {
        vals = new double[4][4];
        this.reset();
    }

    public void reset() {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                vals[i][j] = 0;
            }
        }
    }

    public void set(double val, int x, int y) {
        vals[y][x] = val;
    }

    public double get(int x, int y) {
        return vals[y][x];
    }

    public Vector3D multiplyVectorBy(Vector3D a) {
        System.out.println("yo");
        a.debug();
        Vector3D res = new Vector3D();
        a.w = 1;

        for(int i = 0; i < 4; i++) {
            Vector3D b = new Vector3D(get(i, 0), get(i, 1), get(i, 2), get(i, 3));

            /*if(i == 0)
                res.x = (long)Math.floor(a.dotProduct(b) + 0.5d);
            else if(i == 1)
                res.y = (long)Math.floor(a.dotProduct(b) + 0.5d);
            else if(i == 2)
                res.z = (long)Math.floor(a.dotProduct(b) + 0.5d);*/

            if(i == 0)
                res.x = a.dotProduct(b);
            else if(i == 1)
                res.y = a.dotProduct(b);
            else if(i == 2)
                res.z = a.dotProduct(b);
        }

        System.out.println(res.x);

        return res;
    }

    public void debug() {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                System.out.print(vals[j][i] + " ");
            }
            System.out.println();
        }
    }

    public static Matrix4 createTranslationMatrix(Vector3D translate) {
        Matrix4 t = new Matrix4();

        t.set(1, 0, 0);
        t.set(1, 1, 1);
        t.set(1, 2, 2);
        t.set(1, 3, 3);

        t.set(translate.x, 0, 3);
        t.set(translate.y, 1, 3);
        t.set(translate.z, 2, 3);

        return t;
    }

    public static Matrix4 createScaleMatrix(Vector3D scale) {
        Matrix4 t = new Matrix4();

        t.set(scale.x, 0, 0);
        t.set(scale.y, 1, 1);
        t.set(scale.z, 2, 2);
        t.set(1, 3, 3);

        return t;
    }

    public static Matrix4 createRotationMatrix(double angle, int axis) {
        Matrix4 r = new Matrix4();

        angle = Math.toRadians(angle);

        double a = Math.cos(angle);
        double b = -Math.sin(angle);
        double c = Math.sin(angle);

        r.set(1,3,3);
        if(axis == 0) {
            r.set(1, 0, 0);
            r.set(a, 1, 1);
            r.set(b, 2, 1);
            r.set(c, 1, 2);
            r.set(a, 2, 2);
            r.set(1, 3,3);
        } else if(axis == 1) {
            r.set(a, 0, 0);
            r.set(b, 2, 0);
            r.set(c, 0, 2);
            r.set(a, 2, 2);
            r.set(1, 1,1);
        } else if(axis == 2) {
            r.set(a, 0, 0);
            r.set(b, 1, 0);
            r.set(c, 0, 1);
            r.set(a, 1, 1);
            r.set(1, 2,2);
        }

        return r;
    }

}
