package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.entities.resources.Gem;
import com.jantox.siege.entities.tools.Fury;
import com.jantox.siege.entities.tools.Rock;
import com.jantox.siege.level.Level;
import com.jantox.siege.Vector3D;
import com.jantox.siege.geometry.AABB;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Guardian extends Living {

    int ticks;
    int v = 0;

    public Guardian(Vector3D pos, Level level) {
        super(pos, level, 150);

        this.mask = new AABB(new Vector3D(pos.x + 1f, pos.y - 1f, pos.z + 0.5f), 1f, 0.5f, 0.25f);
        ticks = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        ticks++;
        if(ticks == 60) {
            ticks = 0;
            v++;
            if(v == 3) {
                v = 0;
            }


            //level.spawn(new Fury(this.pos.copy(), level, new Vector3D(0.25f, 0.25f, 0)));
        }

        //if(ticks % 1 == 0) {
            //Level.psys.addParticle(new Particle(new Vector3D(pos.x + 1,pos.y,pos.z + 0.5), new ParticleBehavior.DirtParticle()));
       // }

        if(pos.y < -2f) {
            if(ticks % 1 == 0) {
                Level.psys.addParticle(new Particle(new Vector3D(pos.x + 1,-2,pos.z + 0.5), level, new ParticleBehavior.DirtParticle()));
            }
            pos.y += 0.02f;
            if(pos.y > 0.0f)
                pos.y = 0.0;
        }

        if(this.ticks % 3 == 0)
            Level.psys.addParticle(new Particle(new Vector3D(pos.x,pos.y+rand.nextInt(10),pos.z), level, new ParticleBehavior.SpawnerParticle()));
        //Level.psys.addParticle(new Particle(new Vector3D(pos.x + 1,pos.y - 1,pos.z + 0.5), new ParticleBehavior.SpawnerParticle()));
    }

    @Override
    public void render() {
        glPushMatrix();

        if(health < 0) {
            health = 0;
            if(!expired) {
                expired = true;
                for(int i = 0; i < 20; i++) {
                    Vector3D rock = new Vector3D(pos.x, pos.y + rand.nextInt(10) + 15, pos.z);
                    level.spawn(new Rock(rock, level, new Vector3D(rand.nextGaussian(), 1, rand.nextGaussian())));
                }

            }
        }

        glEnable(GL_TEXTURE_2D);
        glColor3f(1.0f, 1.0f, 1.0f);
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glScalef(2f, 2f, 2f);

        glCallList(Resources.getModel(32+v));

        glPopMatrix();
    }

    public void attack() {
        health-=5;
    }
}
