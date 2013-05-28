package com.jantox.siege.models;

import com.jantox.siege.Vector3D;
import com.jantox.siege.geometry.Quad;

public class ModelFace {

    private Vector3D[] vertexes;
    private Vector3D[] textures;
    private int material;

    private boolean textured;

    public ModelFace(Vector3D[] vertex, Vector3D[] textures, int mat) {
        this.vertexes = vertex;
        this.textures = textures;
        this.material = mat;

        if(textures == null)
            textured = false;
        else
            textured = true;
    }

    public int getVertexAmount() {
        return vertexes.length;
    }

    public Vector3D getVertex(int i) {
        return vertexes[i];
    }

    public int getMaterial() {
        return material;
    }

    public boolean isTextured() {
        return textured;
    }

    public Vector3D getTextureCoordinate(int i) {
        return textures[i];
    }

    public Quad toQuad() {
        return new Quad(vertexes[0], vertexes[1], vertexes[2], vertexes[3]);
    }

}
