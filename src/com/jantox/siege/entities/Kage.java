package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Statistics;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.resources.Gem;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import com.jantox.siege.level.Siege;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glRotatef;

public class Kage extends Living {

    float viewangle = 0f;
    float version;

    float gravity = 0;

    private Entity target;

    public Kage(Vector3D pos, Level level, float version, Entity target) {
        super(pos, level, (int)(25 * version));
        this.version = version;

        this.target = target;

        pos.y -= version * 2;

        gravity = 0.2f;

        this.mask = new Sphere(pos, version * 2);
        this.viewangle = (float)this.pos.angleXZ(target.getPosition());
    }

    @Override
    public void update(float delta) {
        mask.update(pos);

        if(pos.distanceSquared(target.getPosition()) <= 5 * 5) {
            ((Guardian)target).attack();
            this.setExpired(true);
        }

        Vector3D cam = target.getPosition();

        Vector3D vel = new Vector3D(cam.x - pos.x, cam.y - pos.y, cam.z - pos.z);
        vel.normalize();
        vel.divide(5);

        //Level.psys.addParticle(new Particle(this.pos.copy(), new ParticleBehavior.KageParticle()));
        //Level.psys.addParticle(new Particle(this.pos.copy(), new ParticleBehavior.KageParticle()));

        vel.y = 0;

        if(gravity == 0) {
            gravity = 0.6f;
        } else {
            gravity -= 0.02;
        }

        pos.y += gravity;
        if(pos.y < -2) {
            pos.y = -2;
            gravity = 0;
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

        if(viewangle != (float)ang) {
            vel.divide(2);
        }

        if(this.health < 0) {
            if(!expired) {
                Siege.MONSTERS_LEFT--;
                Statistics.MONSTERS_KILLED++;
                for(int i = 0; i < 10; i++) {
                    level.spawn(new Gem(this.pos.copy(), level));
                }
            }
            this.expired = true;
        }

        pos.add(vel);
    }

    @Override
    public void render() {
        GL11.glPushMatrix();
        //GL11.glColor3f(0.5f, 0, 0);

        GL11.glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        GL11.glScalef(version, version, version);
        glRotatef((float)-viewangle+90, 0, 1, 0);

        GL11.glCallList(Resources.getModel(3));
        GL11.glPopMatrix();

        /*if(rider != null) {
            rider.render();
        }*/
    }
}
