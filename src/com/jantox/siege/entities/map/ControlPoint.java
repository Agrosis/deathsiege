package com.jantox.siege.entities.map;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;

public class ControlPoint extends Entity {

    int health;

    public ControlPoint(Vector3D pos) {
        super(pos);

        this.health = 10;
    }

    int lastspawn = 0;

    @Override
    public void update(float delta) {
        if(health <= 0) {
            lastspawn++;
            if(lastspawn == 60 * 4) {
                lastspawn = 0;
                level.spawn(new Endwek(this.pos.copy(), level.getPlayer(), false));
            }
        }
    }

    public void attack() {
        health--;
        if(health < 0)
            health = 0;
    }

    @Override
    public void render() {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);

        glColor4f(1,1,1, 1);

        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(2).getTextureID());

        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glScalef(5,5, 5);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-1, 0, -1);
        glTexCoord2f(1, 0);
        glVertex3f(1, 0, -1);
        glTexCoord2f(1, 1);
        glVertex3f(1, 0, 1);
        glTexCoord2f(0, 1);
        glVertex3f(-1, 0, 1);
        glEnd();

        glEnable(GL_DEPTH_TEST);

        //glCallList(Resources.getModel(2));
        glPopMatrix();
    }
}
