package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.*;
import com.jantox.siege.entities.resources.Log;
import com.jantox.siege.entities.resources.Resource;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Battleaxe extends Tool {

    private Player powner;
    private boolean use = false;

    private float aback = 0;
    private float abackvel = 0;
    private float down = 0;

    int status;

    public Battleaxe(Player owner) {
        super(owner, null);
        this.powner = owner;
        status = 0;
    }

    public void update(float delta) {
        if(status == 1) {
            aback += abackvel;
            if(aback <= -50) {
                status = 2;
                aback = -50;
                abackvel = 3;
            }
        } else if(status == 2) {
            aback += abackvel;
            if(aback >= 0) {
                aback = 0;
                status = 0;
            }
        }
    }

    @Override
    public void onUse(int mouse) {
        use = true;
        if(status == 0) {
            abackvel = -3;
            aback = 0;
            status = 1;
            down = 0;
        }
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


        glTranslatef(-0.5f, -1f, -0.5f);
        glScalef(0.5f, 0.5f, 0.5f);

        glRotatef(30, 0, 1, 0);

        if(status != 0) {
            glTranslatef(0, -1, 0);
            glRotatef(-aback, 0, 0, 1);
            glTranslatef(0, 1, 0);
        }

        //glTranslatef(3.5f, 0f, 0f);
        glCallList(Resources.getModel(6));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
    }
}
