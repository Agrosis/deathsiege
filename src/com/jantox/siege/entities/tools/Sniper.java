package com.jantox.siege.entities.tools;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Input;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Sniper extends Tool {

    private Player powner;

    private boolean use = false;

    private Vector3D holdpos;
    private Vector3D scopepos;

    Vector3D sdir;
    double length;

    public Sniper(Player owner) {
        super(owner);
        this.powner = owner;

        holdpos = new Vector3D(-2, -1,-0.8);
        scopepos = new Vector3D(0,-0.3,0);

        sdir = scopepos.copy();
        sdir.subtract(holdpos);
        length = sdir.length();
        sdir.normalize();
        sdir.divide(10);
    }

    double addlen;

    boolean r = false;

    boolean scoped;

    public void update(float delta) {
        if(scoped) {
            if(addlen < length) {
                holdpos.add(sdir);
                addlen += sdir.length();
                if(addlen >= length) {
                    addlen = length;

                    System.out.println("changed it!");
                    glMatrixMode(GL_PROJECTION);
                    glLoadIdentity();
                    gluPerspective(15, (float) 800 / (float) 600, 1.0f, 2000.0f);
                    glMatrixMode(GL_MODELVIEW);
                    GameInstance.sniper = true;
                }
            }
        } else {
            if(addlen > 0) {
                holdpos.subtract(sdir);
                addlen -= sdir.length();
                if(addlen < 0) {
                    addlen = 0;
                }
            }
        }

        if(Input.rmouse && r == false) {
            r = true;
            if(scoped) {
                scoped = false;
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                gluPerspective(60, (float) 800 / (float) 600, 1.0f, 2000.0f);
                glMatrixMode(GL_MODELVIEW);
                GameInstance.sniper = false;
            } else {
                scoped = true;
            }
        }

        if(!Input.rmouse) {
            r = false;
        }
    }

    @Override
    public void onUse(int mouse) {
        use = true;
        if(mouse == 0) {
            Vector3D dir = powner.getCamera().getDirectionVector().copy();
            Vector3D add = new Vector3D(rand.nextGaussian()/50, rand.nextGaussian()/50, rand.nextGaussian()/50);

            if(!GameInstance.sniper) {
                dir.add(add);
            }
            level.addProjectile(new Projectile(Projectile.SNIPER, powner.getCamera().getHoldingPosition(), dir));

            level.getPlayer().getCamera().setPitchRecoil(0.5f);

            GameInstance.audio.playSound(0);
        }
    }

    @Override
    public void onRelease() {
        use = false;
    }

    @Override
    public int getRest() {
        return 45;
    }

    @Override
    public void render() {
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw() - 90, 0, 1, 0);
        glRotatef(powner.getCamera().getPitch(), 0, 0, 1);


        glScalef(1.5f, 1.5f, 1.5f);

        //sp.multiply(slen);
        glTranslatef((float)holdpos.x, (float)holdpos.y, (float)holdpos.z);
        //sp.normalize();
        /*glScalef(1.5f, 1.5f, 1.5f);

        glColor3f(0.25f,0.25f,0.25f);*/

        if(!GameInstance.sniper)
            glCallList(Resources.getModel(12));
        glPopMatrix();
        glEnable(GL_DEPTH_TEST);
    }
}

