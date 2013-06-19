package com.jantox.siege.entities.map;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Shop extends Entity {

    public Shop(Vector3D pos) {
        super(pos);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        //glEnable(GL_TEXTURE_2D);
        glPushMatrix();

        if(this.getPosition().distanceSquared(level.getPlayer().getPosition()) <= 5 * 5) {
            GameInstance.shop = this;
        } else {
            GameInstance.shop = null;
        }

        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glScalef(0.75f, 0.75f, 0.75f);
        glCallList(Resources.getModel(30));

        glPopMatrix();
    }

}
