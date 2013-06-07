package com.jantox.siege.entities.tools;

import com.jantox.siege.Input;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class TwinBlaster extends Tool {

    private Player powner;

    private float rev_angle[];
    private float rev_speed[];
    private boolean use[];

    private ArrayList<Shot> blasts;

    public TwinBlaster(Player owner) {
        super(owner);
        this.powner = owner;

        blasts = new ArrayList<Shot>();

        rev_angle = new float[2];
        rev_speed = new float[2];
        use = new boolean[2];
    }

    public void update(float delta) {
        if(Input.rmouse) {
            use[1] = true;
            if(rev_speed[1] >= 4) {
                Vector3D hp = powner.getCamera().getHoldingPosition().copy();
                this.blasts.add(new Shot(hp,  new Vector3D(2f, -1.5f, 1.5f)));
                Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(2.5f, -1.5f, -0.25f + 1.25)));
            }
        }
        if(Input.lmouse) {
            use[0] = true;
            if(rev_speed[0] >= 4) {
                Vector3D hp = powner.getCamera().getHoldingPosition().copy();
                this.blasts.add(new Shot(hp,  new Vector3D(-2f, -1.5f, 1.5f)));
                Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(-2.5f, -1.5f, -0.25f + 1.25)));
            }
        }

        if(!Input.rmouse) {
            use[1] = false;
        }
        if(!Input.lmouse) {
            use[0] = false;
        }

        for(int i = 0; i < 2; i++) {
            if(use[i]) {
                if(rev_speed[i] < 5)
                    rev_speed[i] += 0.15f;

                if(rev_speed[i] > 5)
                    rev_speed[i] = 5;
            } else {
                if(rev_speed[i] > 0)
                    rev_speed[i] -= 0.035f;

                if(rev_speed[i] < 0)
                    rev_speed[i] = 0;
            }
            rev_angle[i] += rev_speed[i];
            if(rev_angle[i] > 360)
                rev_angle[i] -= 360;
            if(rev_angle[i] < 0)
                rev_angle[i] += 360;
        }
    }

    @Override
    public void onUse(int mouse) {

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
        for(int i = 0; i < blasts.size(); i++) {
            Shot s = blasts.get(i);
            if(s.life <= 0) {
                blasts.remove(i);
            }
            glDisable(GL_DEPTH_TEST);
            s.update(4);
            s.render(powner.getCamera().getHoldingPosition(),  new Vector3D(1f + rand.nextGaussian() / 30, -1.5f + rand.nextGaussian() / 30, 1.5f + rand.nextGaussian() / 30));
            glEnable(GL_DEPTH_TEST);
        }

        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw(), 0, 1, 0);
        glRotatef(-powner.getCamera().getPitch() + 90, 1, 0, 0);
        glScalef(0.25f, 0.25f, 0.25f);

        glColor3f(0,0,0);

        glTranslatef(4.25f, -0.15f, 2f);
        glRotatef(rev_angle[1], 0, 1, 0);
        glCallList(Resources.getModel(4));
        glRotatef(-rev_angle[1], 0, 1, 0);
        glTranslatef(-8.5f, 0f, 0f);
        glRotatef(-rev_angle[0], 0, 1, 0);
        glCallList(Resources.getModel(4));
        glPopMatrix();
    }
}
