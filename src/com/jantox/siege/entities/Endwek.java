package com.jantox.siege.entities;

import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.resources.Gem;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.glRotatef;

public class Endwek extends Living {

    float gravity = 0;

    private ControlPoint target;

    public Endwek(Vector3D pos, float version) {
        super(pos, (int)(25 * version));

        gravity = 0.2f;

        target = level.getRandomControlPoint();

        this.mask = new Sphere(pos, version * 2);
    }

    @Override
    public void update(float delta) {
        mask.update(pos);

        if(this.pos.distanceSquared(target.getPosition()) <= 5 * 5) {
            this.expired = true;
        }
    }

    int arm;

    @Override
    public void render() {
        arm += 1;

        GL11.glPushMatrix();

        GL11.glTranslatef((float)pos.x, (float)pos.y - 0.2f, (float)pos.z);

        GL11.glScalef(0.9f, 0.9f, 0.9f);

        GL11.glCallList(Resources.getModel(18));
        GL11.glPushMatrix();
        GL11.glRotatef((float)Math.cos(arm) * 10, 1, 1, 0);
        GL11.glCallList(Resources.getModel(19));
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef((float)Math.cos(arm) * 4, 1, 0, 0);
        GL11.glCallList(Resources.getModel(20));
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }
}
