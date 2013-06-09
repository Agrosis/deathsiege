package com.jantox.siege.net;

import com.jantox.siege.Vector3D;

public class Interpolation {

    public static double linear_interpolate(Vector3D a, Vector3D b, double xpos) {
        // use the x value
        double z = a.z + ((b.z - a.z) * (xpos - a.x) / (b.x - a.x));

        return z;
    }

}
