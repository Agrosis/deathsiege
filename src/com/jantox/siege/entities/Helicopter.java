package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.level.Level;

import static org.lwjgl.opengl.GL11.*;

public class Helicopter extends Entity {

    float angle = 0;

    Vector3D target;

    public Helicopter(Vector3D pos, Level level) {
        super(pos, level);

        target = level.getPlayer().getPosition();
    }

    @Override
    public void update(float delta) {
        angle += 15;

        //this.pos.x --;
    }

    @Override
    public void render() {
        glPushMatrix();

        glTranslatef((float)pos.x, (float)pos.y + 60 + (float)Math.cos(angle/100) * 2, (float)pos.z);

        glScalef(8,8,8);

        glCallList(Resources.getModel(27));

        glRotatef(angle, 0, 1, 0);
        glCallList(Resources.getModel(28));

        glPopMatrix();
    }
}
