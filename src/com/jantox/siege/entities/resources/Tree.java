package com.jantox.siege.entities.resources;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;

import static org.lwjgl.opengl.GL11.*;

public class Tree extends Resource {

    public Tree(Vector3D pos) {
        super(pos);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        glColor3f(1f, 1f, 1f);
        glTranslatef((float)pos.x, (float)pos.y-0.35f, (float)pos.z);
        if(this.getStatus() == Resource.AVAILABLE)
            glCallList(Resources.getModel(5));
        else
            glCallList(Resources.getModel(9));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
    }
}
