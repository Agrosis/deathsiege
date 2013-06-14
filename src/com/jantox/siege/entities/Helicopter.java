package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import static org.lwjgl.opengl.GL11.*;

public class Helicopter extends Entity {

    float angle = 0;

    Vector3D target;

    public Helicopter(Vector3D pos) {
        super(pos);

        target = level.getPlayer().getPosition();
    }

    @Override
    public void update(float delta) {
        angle += 15;

        this.pos.x --;
    }

    @Override
    public void render() {
        glPushMatrix();

        float vangle = (float)this.pos.angleXZ(target);
        vangle += 180 + 90;
        System.out.println(vangle);

        vangle = 0;

        glTranslatef((float)pos.x, (float)pos.y + 80, (float)pos.z);

        glScalef(5, 5, 5);

        glRotatef(vangle, 0, 1, 0);
        glCallList(Resources.getModel(27));

        glRotatef(angle, 0, 1, 0);
        glCallList(Resources.getModel(28));

        glPopMatrix();
    }
}
