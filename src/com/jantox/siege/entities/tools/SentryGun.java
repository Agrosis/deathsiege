package com.jantox.siege.entities.tools;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;

import static org.lwjgl.opengl.GL11.*;

public class SentryGun extends Entity {

    float top_angle = 0f;
    float angle = 10f;

    int direction = 1;

    int step = 0;

    public SentryGun(Vector3D pos, Level level) {
        super(pos, level);
    }

    int sm = 0;

    @Override
    public void update(float delta) {
        step++;
        sm++;

        if(step == 5) {
            step = 0;
            Entity monster = level.getClosestMonster(this, 600);
            if(monster != null) {
                angle = (float) this.pos.angleXZ(monster.getPosition());
                Vector3D vel = this.pos.copy();
                vel.subtract(monster.getPosition());
                vel.normalize();

                Vector3D bp = this.pos.copy();
                bp.y += 0.5;
                //Bullet b = new Bullet(bp, vel, new Vector3D());
                //level.spawn(b);
            }
        }

        if(sm == 30) {
            sm = 0;
            Vector3D sp = pos.copy();
            sp.y += 2;
            sp.x += rand.nextGaussian() / 50;
            sp.z += rand.nextGaussian() / 50;
            Particle p = new Particle(sp, level, new ParticleBehavior.SmokeParticle());
            p.setScale(new Vector3D(0.3, 0.3, 0.3));
            p.setMaxLife(rand.nextInt(60) + 60);
            Level.psys.addParticle(p);

            sp = pos.copy();
            sp.y += 2;
            sp.x += rand.nextGaussian() / 50;
            sp.z += rand.nextGaussian() / 50;
            p = new Particle(sp, level, new ParticleBehavior.SmokeParticle());
            p.setScale(new Vector3D(0.3, 0.3, 0.3));
            p.setMaxLife(rand.nextInt(60) + 60);
            Level.psys.addParticle(p);
        }

        float speed = Math.abs(angle - top_angle) * 0.05f;
        if(speed > 2)
            speed = 2;
        if(speed < 0.1)
            speed = 0.1f;


        if(top_angle < angle) {
            top_angle += speed;
            if(top_angle >= angle) {
                top_angle = angle;
                angle = (float)rand.nextGaussian() * 360;
                direction *= -1;
            }
        } else if(top_angle > angle) {
                top_angle -= speed;
                if(top_angle <= angle) {
                    top_angle = angle;
                    angle = (float)rand.nextGaussian() * 360;
                    direction *= -1;
                }
        } else if(top_angle == angle) {
            angle = (float)rand.nextGaussian() * 360;
            direction *= -1;
        }
    }

    @Override
    public void render() {
        glPushMatrix();

        glColor3f(0.35f, 0.35f, 0.35f);

        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glScalef(1.6f,1.6f,1.6f);
        glCallList(Resources.getModel(14));
        glRotatef(-top_angle-90, 0, 1, 0);
        glCallList(Resources.getModel(15));
        glPopMatrix();
    }

}
