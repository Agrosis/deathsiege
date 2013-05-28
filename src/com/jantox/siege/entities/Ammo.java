package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.geometry.Sphere;
import org.lwjgl.opengl.GL11;

public class Ammo extends Entity {

    int type;
    int amount;

    public Ammo(Vector3D pos, int type, int amount) {
        super(pos);
        this.amount = amount;
        this.type = type;

        this.mask = new Sphere(pos, 1);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) pos.x, (float) pos.y - 0.3f, (float) pos.z);
        GL11.glRotatef(90, 1, 0, 0);
        GL11.glScalef(0.2f, 0.2f, 0.2f);
        GL11.glCallList(Resources.getModel(4));
        GL11.glPopMatrix();
    }

    public int getWeaponType() {
        return 0;
    }

    public int getAmount() {
        return amount;
    }
}
