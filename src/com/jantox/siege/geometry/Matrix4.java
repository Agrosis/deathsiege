package com.jantox.siege.geometry;

public class Matrix4 {

    private float matrix[][];

    public Matrix4() {
        matrix = new float[4][4];
        this.reset();
    }

    public float get(int x, int y) {
        return matrix[x][y];
    }

    public void reset() {
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                matrix[x][y] = 0;
            }
        }
    }

    public void add(Matrix4 matb) {
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                matrix[x][y] += matb.get(x, y);
            }
        }
    }

    public void subtract(Matrix4 matb) {
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                matrix[x][y] -= matb.get(x, y);
            }
        }
    }

    public void multiply(Matrix4 matb) {
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                matrix[x][y] -= matb.get(x, y);
            }
        }
    }

}
