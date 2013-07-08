package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.*;
import com.jantox.siege.entities.resources.Log;
import com.jantox.siege.entities.resources.Resource;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Sword extends Tool {

    private Player powner;
    private boolean use = false;

    int status;

    public Sword(Player owner) {
        super(owner, null);
        this.powner = owner;
        status = 0;
    }

    public void update(float delta) {

    }

    @Override
    public void onUse(int mouse) {
        use = true;
    }

    @Override
    public void onRelease() {
        use = false;
    }

    @Override
    public int getRest() {
        return 5;
    }

    @Override
    public void render() {
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_TEXTURE_2D);
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();

        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        //glScalef(0.5f, 0.5f, 0.5f);
        glRotatef(powner.getCamera().getYaw() - 90, 0, 1, 0);
        glRotatef(powner.getCamera().getPitch(), 0, 0, 1);

        //glScalef(0.5f, 0.5f, 0.5f);

        glRotatef(30, 0, 1, 0);

        glTranslatef(-0.25f, -1.15f, -0.75f);
        glCallList(Resources.getModel(8));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
    }
}
