package com.jantox.siege.entities.tools;

import com.jantox.siege.GameInstance;
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

public class Shotgun extends Tool {

    private Player powner;

    private boolean use = false;

    private ArrayList<Shot> blasts;

    public Shotgun(Player owner) {
        super(owner);
        this.powner = owner;
        blasts = new ArrayList<Shot>();
    }

    public void update(float delta) {

    }

    @Override
    public void onUse(int mouse) {
        use = true;
        if(mouse == 0) {
            Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(1f, -1.5f, 1.5f)));
            //Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(1f, -1.5f, 1.5f)));
            Vector3D hp = powner.getCamera().getHoldingPosition().copy();
            hp.x += rand.nextGaussian();
            hp.y += rand.nextGaussian();
            hp.z += rand.nextGaussian();

            GameInstance.audio.playSound(0);
            this.blasts.add(new Shot(hp,  new Vector3D(1f, -1.5f, 1.5f)));
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
        for(int i = 0; i < blasts.size(); i++) {
            Shot s = blasts.get(i);
            if(s.life <= 0) {
                blasts.remove(i);
            }
            glDisable(GL_DEPTH_TEST);
            s.update(4);
            Vector3D v = powner.getCamera().getHoldingPosition();
            v.y += .25f;
            s.render(v,  new Vector3D(1f + rand.nextGaussian() / 30, -1.5f + rand.nextGaussian() / 30, 1.5f + rand.nextGaussian() / 30));
            glEnable(GL_DEPTH_TEST);
        }

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw() + 180, 0, 1, 0);
        glRotatef(powner.getCamera().getPitch(), 1, 0, 0);

        glScalef(1.5f, 1.5f, 1.5f);
        glTranslatef(-0.75f, -1f, 1.25f);
        glColor3f(0.25f,0.25f,0.25f);

        glCallList(Resources.getModel(4));
        glPopMatrix();
        glEnable(GL_DEPTH_TEST);
    }
}
