package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.tools.SentryGun;
import com.jantox.siege.entities.tools.Tool;

import static org.lwjgl.opengl.GL11.*;

public class SentryGunItem extends Tool {

    Player powner;

    public SentryGunItem(Player owner) {
        super(owner);
        this.powner = owner;
    }

    @Override
    public void onUse(int mouse) {
        Vector3D hold = powner.getCamera().getHoldingPosition();
        hold.x += powner.getCamera().getDirectionVector().x * 1.5;
        hold.y = -2;
        hold.z += powner.getCamera().getDirectionVector().z * 1.5;
        level.spawn(new SentryGun(hold));
    }

    @Override
    public void onRelease() {

    }

    @Override
    public int getRest() {
        return 60;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render() {
        glPushMatrix();

        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw(), 0, 1, 0);
        //glRotatef(-powner.getCamera().getPitch(), 1, 0, 0);
        glScalef(1.25f, 1.25f, 1.25f);

        glTranslatef(0f, -1.5f, -1.5f);
        glCallList(Resources.getModel(14));
        //glRotatef(-90, 0, 1, 0);

        glCallList(Resources.getModel(15));
        glPopMatrix();
    }
}
