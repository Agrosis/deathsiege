package com.jantox.siege.entities.tools;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Input;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Fists extends Tool {

    private Player powner;

    private boolean use = false;

    int ticks = 0;

    int arm = -1;
    float acvel = 0;
    float action = 0;

    public Fists(Player owner, Level level) {
        super(owner, level);
        this.powner = owner;
    }

    public void update(float delta) {
        ticks++;

        if(arm != -1) {
            acvel -= 0.2f;
            action += acvel;
            if(action <= 0) {
                acvel = 0;
                action = 0;

                arm = -1;
            }
        }
    }

    @Override
    public void onUse(int mouse) {
        use = true;
        if(arm == -1) {
            arm = mouse;
            acvel = 2;
            System.out.println("HI");
        }
    }

    @Override
    public void onRelease() {
        use = false;

    }

    @Override
    public int getRest() {
        return 1;
    }

    @Override
    public void render() {
        glDisable(GL_DEPTH_TEST);

        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw() + 90, 0, 1, 0);
        glRotatef(-powner.getCamera().getPitch(), 0, 0, 1);

        glScalef(0.5f, 0.5f, 0.5f);
        if(arm == 1)
            glTranslatef(action, 0, 0);
        glTranslatef(0f, -3.5f, 5f);

        glColor3f(239/255f, 208/255f, 207/255f);

        //glRotatef(-10, 0, 1, 0);

        glCallList(Resources.getModel(38));
        if(arm == 1)
            glTranslatef(-action, 0, 0);

        if(arm == 0)
            glTranslatef(action, 0, 0);
        glTranslatef(0f, 0f, -10f);
        glCallList(Resources.getModel(38));


        glPopMatrix();

        glEnable(GL_DEPTH_TEST);
    }

}
