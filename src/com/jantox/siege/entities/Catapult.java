package com.jantox.siege.entities;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Input;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.tools.Rock;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;

import static org.lwjgl.opengl.GL11.*;

public class Catapult extends Entity {

    float angle = 0;
    float arm_angle = 0;
    float aavel = 5;

    int dir = 0;
    int breaktime = 0;

    boolean in = false;

    public Catapult(Vector3D pos, Level level) {
        super(pos, level);
    }

    @Override
    public void update(float delta) {
        if(breaktime == 0) {
            arm_angle += aavel;

            if(dir == 0)
                aavel -= 0.1f;

            if(arm_angle >= 70) {
                arm_angle = 70;
                aavel = -0.5f;
                dir = 1;

                Vector3D rock = new Vector3D(pos.x, pos.y + 5, pos.z);

                Vector3D vel = new Vector3D(Math.cos(Math.toRadians(-angle+90)), 0.75, Math.sin(Math.toRadians(-angle+90)));

                level.spawn(new Rock(rock, level, vel));
            }

            if(arm_angle <= -10) {
                breaktime = -1;
            }
        } else {
            breaktime--;
        }

        if(!Input.e) {
            e = false;
        }

        if(level.getPlayer().getCamera().getPlayerPos().distanceSquared(this.pos) <= 10 * 10) {
            if(GameInstance.intent == -1)
                GameInstance.intent = 0;

            if(in == false) {
                if(GameInstance.intent == 0)
                    if(Input.e && !e) {
                        e = true;
                        in = true;
                        level.getPlayer().getCamera().setFocus(this);
                        GameInstance.intent = 1;
                    }
            } else {
                if(GameInstance.intent == 1)
                    if(Input.e && !e) {
                        e = true;
                        in = false;
                        level.getPlayer().getCamera().setFocus(null);
                        //level.getPlayer().getCamera().setCamera(new Vector3D(0,0,0));
                        GameInstance.intent = -1;
                    }
            }
        } else {
            if(GameInstance.intent == 0) {
                GameInstance.intent = -1;
                GameInstance.intents.get(0).off();
            }
        }

        if(Input.lmouse) {
            if(dir == 1 && arm_angle <= -10) {
                arm_angle = -10;
                dir = 0;
                breaktime = 0;
                aavel = 5;
            }
        }
    }

    private boolean e = false;

    @Override
    public void render() {
        glPushMatrix();
        glEnable(GL_TEXTURE_2D);

        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);

        glScalef(2,2,2);

        //glCallList(Resources.getModel(27));

        glRotatef(angle, 0, 1, 0);
        glCallList(Resources.getModel(9));

        glTranslatef(0, 0, 0.48f);
        glRotatef(arm_angle, 1, 0, 0);
        glTranslatef(0, 0, -0.48f);
        glCallList(Resources.getModel(10));

        glPopMatrix();
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
