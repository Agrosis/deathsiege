package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import static org.lwjgl.opengl.GL11.*;

public class Decoration extends Entity {

    private Vector3D scale, rotation;
    private int modelid;

    public Decoration(Vector3D pos, Vector3D scale, Vector3D rotation, int modelid) {
        super(pos, null);
        this.scale = scale;
        this.rotation = rotation;
        this.modelid = modelid;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        glPushMatrix();
        glTranslatef((float)pos.x, (float)pos.y-2, (float)pos.z);
        glRotatef((float)rotation.x, 1, 0, 0);
        glRotatef((float)rotation.y, 0, 0, 1);
        //glRotatef((float)rotation.z, 0, 0, 1);

        glScalef((float)scale.x, (float)scale.y, (float)scale.z);
        glCallList(Resources.getModel(modelid));
        glPopMatrix();
    }
}
