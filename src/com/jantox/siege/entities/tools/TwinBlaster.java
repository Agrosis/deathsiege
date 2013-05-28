package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class TwinBlaster extends Tool {

    private Player powner;

    public TwinBlaster(Player owner) {
        super(owner);
        this.powner = owner;
    }

    public void update(float delta) {

    }

    @Override
    public void onUse(int mouse) {
        if(mouse == 1)
            Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(2.5f, -1.5f, -0.25f + 1.25)));
        else
            Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(-2.5f, -1.5f, -0.25f + 1.25)));

    }

    @Override
    public void onRelease() {

    }

    @Override
    public int getRest() {
        return 3;
    }

    @Override
    public void render() {
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw(), 0, 1, 0);
        glRotatef(-powner.getCamera().getPitch() + 90, 1, 0, 0);
        glScalef(0.25f, 0.25f, 0.25f);

        glColor3f(0,0,0);

        glTranslatef(4.25f, -0.15f, 2f);
        glCallList(Resources.getModel(4));
        glTranslatef(-8.5f, 0f, 0f);
        glCallList(Resources.getModel(4));
        glPopMatrix();
    }
}
