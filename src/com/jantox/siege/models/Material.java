package com.jantox.siege.models;

import com.jantox.siege.Vector3D;
import org.newdawn.slick.opengl.Texture;

public class Material {

    public String mtlname;
    public Vector3D ambient, diffuse, specular;
    public Texture texture;

    public Material(String name, Vector3D a, Vector3D b, Vector3D c, Texture t) {
        this.mtlname = name;
        this.ambient = a;
        this.diffuse = b;
        this.specular = c;
        this.texture = t;
    }

    public boolean isTextured() {
        return texture != null;
    }

}
