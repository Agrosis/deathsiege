package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.entities.resources.Gem;
import com.jantox.siege.level.Level;
import com.jantox.siege.Vector3D;
import com.jantox.siege.geometry.AABB;
import com.jantox.siege.SpawnerFactory;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

public class Spawner extends Living {

    int tid;
    private Texture[] texture;

    int ticks;

    public Spawner(Vector3D pos) {
        super(pos, 150);
        pos.y -= 3;

        this.mask = new AABB(new Vector3D(pos.x + 1f, pos.y - 1f, pos.z + 0.5f), 1f, 0.5f, 0.25f);

        ticks = 0;

        tid = glGenLists(1);

        glNewList(tid, GL_COMPILE);

        glEndList();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        ticks++;
        if(ticks % 500 == 0) {
            level.spawn(new Kage(this.pos.copy().getCloseTo(1), 1, 0));
        }

        if(pos.y < 0) {
            pos.y += 0.005f;
            if(pos.y > 0)
                pos.y = 0;
        }

        if(expired) {
            SpawnerFactory.ACTIVE_SPAWNERS --;
        }

        if(expired) {
            for(int i = 0; i < 10; i++) {
                level.spawn(new Gem(this.pos.copy()));
            }
        }

        Level.psys.addParticle(new Particle(new Vector3D(pos.x + 1,pos.y - 1,pos.z + 0.5), new ParticleBehavior.SpawnerParticle()));
        Level.psys.addParticle(new Particle(new Vector3D(pos.x + 1,pos.y - 1,pos.z + 0.5), new ParticleBehavior.SpawnerParticle()));
    }

    @Override
    public void render() {
        glPushMatrix();

        if(health < 0) {
            health = 0;
        }

        glEnable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(8).getTextureID());
        glColor3f(1.0f, 1.0f, 1.0f);
        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f,0.0f);
        glVertex3f(0.0f,-1,0.0f);
        glTexCoord2f(0.0f,0.25f);
        glVertex3f(0f,-1f,1f);
        glTexCoord2f(0.5f,0.25f);
        glVertex3f(2f,-1f,1f);
        glTexCoord2f(0.5f,0.0f);
        glVertex3f(2f,-1,0.0f);
        glEnd();

        glBegin(GL_QUADS);
        glTexCoord2f(0.0f,0.25f);
        glVertex3f(0.0f,-1,0.0f);
        glTexCoord2f(0.0f,0.5f);
        glVertex3f(2f,-1f,0f);
        glTexCoord2f(0.5f,0.5f);
        glVertex3f(2f,-2f,0f);
        glTexCoord2f(0.5f,0.25f);
        glVertex3f(0f,-2,0.0f);
        glEnd();

        glBegin(GL_QUADS);
        glTexCoord2f(0.0f,0.25f);
        glVertex3f(0.0f,-1,1.0f);
        glTexCoord2f(0.0f,0.5f);
        glVertex3f(2f,-1f,1f);
        glTexCoord2f(0.5f,0.5f);
        glVertex3f(2f,-2f,1f);
        glTexCoord2f(0.5f,0.25f);
        glVertex3f(0f,-2,1.0f);
        glEnd();

        glBegin(GL_QUADS);
        glTexCoord2f(0.25f,0.5f);
        glVertex3f(2f,-1f,1f);
        glTexCoord2f(0.0f,0.5f);
        glVertex3f(2f,-1f,0f);
        glTexCoord2f(0.f,0.75f);
        glVertex3f(2f,-2f,0f);
        glTexCoord2f(0.25f,0.75f);
        glVertex3f(2f,-2f,1f);
        glEnd();

        glBegin(GL_QUADS);
        glTexCoord2f(0.25f,0.5f);
        glVertex3f(0f,-1f,1f);
        glTexCoord2f(0.0f,0.5f);
        glVertex3f(0f,-1f,0f);
        glTexCoord2f(0.f,0.75f);
        glVertex3f(0f,-2f,0f);
        glTexCoord2f(0.25f,0.75f);
        glVertex3f(0f,-2f,1f);
        glEnd();

        glPopMatrix();
    }
}
