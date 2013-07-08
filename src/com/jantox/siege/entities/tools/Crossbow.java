package com.jantox.siege.entities.tools;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Input;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;
import com.jantox.siege.level.Level;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Crossbow extends Tool {

    private Player powner;

    private boolean use = false;

    private Vector3D holdpos;
    private Vector3D scopepos;

    Vector3D sdir;
    double length;

    int fov = 68;

    public Crossbow(Player owner, Level level) {
        super(owner, level);
        this.powner = owner;

        holdpos = new Vector3D(-2, -2.1f,-1.8);
        scopepos = new Vector3D(-1.5,-0.9,0);

        sdir = scopepos.copy();
        sdir.subtract(holdpos);
        length = sdir.length();
        sdir.normalize();
        sdir.divide(10);
    }

    double addlen;

    public void update(float delta) {
        if(Input.rmouse) {
            if(addlen < length) {
                holdpos.add(sdir);
                addlen += sdir.length();

                if(addlen >= length) {
                    addlen = length;
                }
            }

            if(fov > 40) {
                fov--;
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                gluPerspective(fov, (float) 1066 / (float) 600, 1.0f, 2000.0f);
                glMatrixMode(GL_MODELVIEW);
            }
        } else {
            if(addlen > 0) {
                holdpos.subtract(sdir);
                addlen -= sdir.length();
                if(addlen < 0) {
                    addlen = 0;
                }
            }

            if(fov < 68) {
                fov++;
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                gluPerspective(fov, (float) 1066 / (float) 600, 1.0f, 2000.0f);
                glMatrixMode(GL_MODELVIEW);
            }
        }
    }

    @Override
    public void onUse(int mouse) {
        use = true;
        if(mouse == 0) {
            Vector3D dir = powner.getCamera().getDirectionVector();
            GameInstance.audio.playSound(2);
            level.spawn(new Bolt(powner.getCamera().getHoldingPosition(), level, dir, new Vector3D(0.5f, -0.75f, -0.2f)));
        }
    }

    @Override
    public void onRelease() {
        use = false;
    }

    @Override
    public int getRest() {
        return 10;
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw() - 90, 0, 1, 0);
        glRotatef(powner.getCamera().getPitch(), 0, 0, 1);

        glScalef(0.35f, 0.35f, 0.35f);
        glTranslatef((float)holdpos.x, (float)holdpos.y, (float)holdpos.z);
        glColor3f(0.25f,0.25f,0.25f);

        glCallList(Resources.getModel(16));
        glPopMatrix();
    }
}
