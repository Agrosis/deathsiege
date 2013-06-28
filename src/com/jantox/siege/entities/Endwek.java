package com.jantox.siege.entities;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.ai.AISet;
import com.jantox.siege.entities.map.ControlPoint;
import com.jantox.siege.geometry.CollisionSystem;
import com.jantox.siege.geometry.Ray;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import com.jantox.siege.level.Siege;
import org.lwjgl.opengl.GL11;

public class Endwek extends Living {

    float gravity = 0;
    float viewangle = 0f;

    private AISet ai;

    private Entity target, oldtarget;
    private boolean giant;

    public Endwek(Vector3D pos, Level level, Entity target, boolean giant) {
        super(pos, level, 100);

        gravity = 0.2f;

        this.target = target;

        this.giant = giant;

        this.mask = new Sphere(pos, 2);
        this.viewangle = (float)this.pos.angleXZ(target.getPosition());
    }

    int attack = 0;

    @Override
    public void update(float delta) {
        mask.update(pos);

        if(health <= 0) {
            if(!expired) {
                Siege.MONSTERS_LEFT--;
            }
            this.expired = true;
        }

        if(this.pos.distanceSquared(level.getPlayer().getPosition().copy()) <= 18 * 18) {
            if(target instanceof ControlPoint) {
                oldtarget = target;
                target = level.getPlayer();
            }
        } else {
            if(oldtarget != null)
                target = oldtarget;
        }

        Vector3D cam = target.getPosition();
        Vector3D vel = new Vector3D(cam.x - pos.x, cam.y - pos.y, cam.z - pos.z);
        vel.normalize();
        vel.y = 0;
        if(giant)
            vel.divide(7);
        else
            vel.divide(7);

        if(pos.distanceSquared(target.getPosition()) >= 4) {
            pos.add(vel);
        }

        double ang = this.pos.angleXZ(target.getPosition());
        if(viewangle < ang) {
            viewangle += 1f;
            if(viewangle > ang)
                viewangle = (float) ang;
        } else if(viewangle > ang) {
            viewangle -= 1f;
            if(viewangle < ang)
                viewangle = (float) ang;
        }

        if(pos.distanceSquared(target.getPosition()) <= 5 * 5) {
            if(target instanceof ControlPoint) {
                ((ControlPoint)target).attack();
                this.setExpired(true);
            }
            if(pos.distanceSquared(target.getPosition()) <= 2 * 2) {
                attack++;
                if(attack == 40) {
                    ((Player)target).damage(1);
                    attack = 0;
                    GameInstance.audio.playSound(4);
                }
            }
        }
    }

    int arm = 0;
    int dir = 0;

    int swing = 360;
    int sdir = 0;

    @Override
    public void render() {
        if(dir == 0) {
            arm += 2;
            if(arm > 30) {
                arm = 30;
                dir = 1;
            }
        } else {
            arm -= 2;
            if(arm < -30) {
                arm = -30;
                dir = 0;
            }
        }

        if(sdir == 0) {
            swing-=3;
            if(swing <= 270) {
                sdir = 1;
            }
        } else {
            swing+=3;
            if(swing >= 360) {
                sdir = 0;
            }
        }


        GL11.glPushMatrix();
        GL11.glColor3f(1.0f, 0f, 0f);

        pos.y = 0;
        GL11.glTranslatef((float)pos.x, (float)pos.y - 1f, (float)pos.z);
        if(giant)
            GL11.glTranslatef(0, 11, 0);

        if(giant)
            GL11.glScalef(12,12,12);

        GL11.glRotatef(-viewangle-90, 0, 1, 0);
        GL11.glCallList(Resources.getModel(18));
        GL11.glCallList(Resources.getModel(26));

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 1.5f, 0);
        GL11.glRotatef(swing, 1, 0, 0);
        GL11.glTranslatef(0, -1.5f, 0);
        GL11.glCallList(Resources.getModel(19));
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(-arm, 1, 0, 0);
        GL11.glCallList(Resources.getModel(20));
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(arm, 1, 0, 0);
        GL11.glCallList(Resources.getModel(21));
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }

    public boolean isHeadshot(Ray bullet) {
        Vector3D np = pos.copy();
        np.y += 1.3f;
        if(CollisionSystem.raySphere(bullet, new Sphere(np, 0.5f), null)) {
            this.expired = true;
            Siege.MONSTERS_LEFT --;
            GameInstance.ccash += 50;
            return true;
        }

        return false;
    }
}
