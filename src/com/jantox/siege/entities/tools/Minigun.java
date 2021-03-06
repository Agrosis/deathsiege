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

public class Minigun extends Tool {

    private Player powner;

    private float rev_angle = 0;
    private float rev_speed = 0;

    private boolean use = false;

    private ArrayList<Shot> blasts;

    private int ammo = 100;

    int ticks = 0;

    public Minigun(Player owner, Level level) {
        super(owner, level);
        this.powner = owner;
        blasts = new ArrayList<Shot>();
    }

    public void update(float delta) {
        ticks++;

        if(use) {
            if(rev_speed < 8)
                rev_speed += 0.15f;

            if(rev_speed > 8)
                rev_speed = 8;
        } else {
            if(rev_speed > 0) {
                rev_speed -= 0.035f;
            }

            if(rev_speed < 0)
                rev_speed = 0;
        }
        rev_angle += rev_speed;
        if(rev_angle > 360)
            rev_angle -= 360;
        if(rev_angle < 0)
            rev_angle += 360;
    }

    @Override
    public void onUse(int mouse) {
        use = true;
        if(mouse == 0)
            if(rev_speed >= 8) {
                if(ammo > 0) {
                    ammo --;
                    //Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(1f, -1.5f, 1.5f)));
                    //Entity.level.spawn(new Bullet(powner.getCamera().getHoldingPosition(), powner.getCamera().getDirectionVector(), new Vector3D(1f, -1.5f, 1.5f)));
                    Vector3D hp = powner.getCamera().getHoldingPosition().copy();
                    hp.x += rand.nextGaussian();
                    hp.y += rand.nextGaussian();
                    hp.z += rand.nextGaussian();
                    this.blasts.add(new Shot(hp, level, new Vector3D(1f, -1.5f, 1.5f)));

                    GameInstance.audio.playSound(12);
                }
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
        glDisable(GL_DEPTH_TEST);
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

        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw() + 90, 0, 1, 0);
        glRotatef(-powner.getCamera().getPitch(), 0, 0, 1);

        glScalef(0.5f, 0.5f, 0.5f);
        glTranslatef(3f, -3.5f, 2.3f);
        glColor3f(0.25f,0.25f,0.25f);

        //glRotatef(-10, 0, 1, 0);

        glRotatef(-rev_angle, 1, 0, 0);

        glCallList(Resources.getModel(37));

        glRotatef(rev_angle, 1, 0, 0);

        glCallList(Resources.getModel(35));


        glPopMatrix();

        glEnable(GL_DEPTH_TEST);
    }

}
