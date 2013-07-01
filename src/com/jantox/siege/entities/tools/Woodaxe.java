package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.*;
import com.jantox.siege.entities.resources.Log;
import com.jantox.siege.entities.resources.Resource;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Woodaxe extends Tool {

    private Player powner;
    private boolean use = false;

    public Woodaxe(Player owner) {
        super(owner, null);
        this.powner = owner;
    }

    public void update(float delta) {

    }

    @Override
    public void onUse(int mouse) {
        use = true;
        /*ArrayList<Entity> entities = Entity.level.getEntities();
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if(e instanceof Resource) {
                Resource t = (Resource) e;
                if(t.getPosition().distanceSquared(Entity.level.getPlayer().getCamera().getCamera()) <= 5 * 5) {
                    t.useResource(50);

                    if(t.getStatus() == Resource.NOT_AVAILABLE && t.getProgress() == 0) {
                        for(int j = 0; j < rand.nextInt(3) + 1; j++) {
                            Vector3D tpos = t.pos.copy();
                            tpos.y += 2;
                            Entity.level.spawn(new Log(tpos));
                        }
                    }
                }
            }
        }*/
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

        //glTranslatef(3.5f, 0f, 0f);
        glCallList(Resources.getModel(6));
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
    }
}
