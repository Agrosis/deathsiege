package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.entities.resources.Gem;
import com.jantox.siege.level.Level;
import com.jantox.siege.Vector3D;
import com.jantox.siege.geometry.AABB;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Spawner extends Living {

    int tid;
    private Texture[] texture;

    int ticks;

    public Spawner(Vector3D pos, Level level) {
        super(pos, level, 150);

        this.mask = new AABB(new Vector3D(pos.x + 1f, pos.y - 1f, pos.z + 0.5f), 1f, 0.5f, 0.25f);

        ticks = 0;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        ticks++;

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

        if(expired) {
            //SpawnerFactory.ACTIVE_SPAWNERS --;
        }

        if(expired) {
            for(int i = 0; i < 10; i++) {
                level.spawn(new Gem(this.pos.copy(), level));
            }
        }

        //Level.psys.addParticle(new Particle(new Vector3D(pos.x + 1,pos.y - 1,pos.z + 0.5), new ParticleBehavior.SpawnerParticle()));
        //Level.psys.addParticle(new Particle(new Vector3D(pos.x + 1,pos.y - 1,pos.z + 0.5), new ParticleBehavior.SpawnerParticle()));
    }

    @Override
    public void render() {
        glPushMatrix();

        if(health < 0) {
            health = 0;
        }

        glEnable(GL_TEXTURE_2D);
        glColor3f(1.0f, 1.0f, 1.0f);
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glScalef(2f, 2f, 2f);

        glCallList(Resources.getModel(32));

        glPopMatrix();
    }
}
