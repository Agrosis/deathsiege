package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

/**
 * Created with IntelliJ IDEA.
 * User: agro
 * Date: 5/3/13
 * Time: 9:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Shot extends Entity {

    Vector3D rot;

    public int life = 0;

    public Shot(Vector3D pos, Vector3D rot) {
        super(pos);
        this.rot = rot;
        life = 3;
    }

    @Override
    public void update(float delta) {
        life--;
    }

    @Override
    public void render() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void render(Vector3D posi, Vector3D roti) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);

        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        glColor3f(1, 1, 0);
        glTranslatef((float) posi.x, (float) posi.y, (float) posi.z);
        glScalef(0.5f, 0.5f, 0.5f);
        glRotatef(Entity.level.getPlayer().getCamera().getYaw(), 0, 1, 0);
        glRotatef(-Entity.level.getPlayer().getCamera().getPitch(), 1, 0, 0);

        glTranslatef((float)roti.x, (float)roti.y, (float)roti.z - 4);

        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(3).getTextureID());

        glBegin (GL_QUADS);
        glTexCoord2f(0f, 0f);
        glVertex3f (-1, -1, 0);
        glTexCoord2f(1f, 0f);
        glVertex3f (1, -1, 0);
        glTexCoord2f(1f, 1f);
        glVertex3f (1, 1, 0);
        glTexCoord2f(0f, 1f);
        glVertex3f (-1, 1, 0);
        glEnd();

        glPopMatrix();
    }

}
