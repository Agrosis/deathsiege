package com.jantox.siege.models;

import static org.lwjgl.opengl.GL11.*;

public class DSMLoader {

    public DSMLoader() {

    }

    public int loadDSMModel(String modelloc) {
        int mid = glGenLists(1);
        glNewList(mid, GL_COMPILE);

        return mid;
    }

}
