package com.jantox.siege.entities.tools;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Player;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Crossbow extends Tool {

    private Player powner;

    private float rev_angle = 0;
    private float rev_speed = 0;

    private boolean use = false;

    private int ammo = 0;

    public Crossbow(Player owner) {
        super(owner);
        this.powner = owner;
    }

    public void update(float delta) {

    }

    @Override
    public void onUse(int mouse) {
        use = true;
        if(mouse == 0) {
            Vector3D dir = powner.getCamera().getDirectionVector();
            GameInstance.audio.playSound(2);
            Entity.level.spawn(new Bolt(powner.getCamera().getHoldingPosition(), dir, new Vector3D(0.5f, -0.75f, -0.2f)));
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
        glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        Vector3D hold = powner.getCamera().getHoldingPosition();
        glTranslatef((float) hold.x, (float) hold.y, (float) hold.z);
        glRotatef(powner.getCamera().getYaw() - 90, 0, 1, 0);
        glRotatef(powner.getCamera().getPitch(), 0, 0, 1);

        glScalef(0.35f, 0.35f, 0.35f);
        glTranslatef(-2f, -1.8f, -1.1f);
        glColor3f(0.25f,0.25f,0.25f);

        glCallList(Resources.getModel(16));
        glPopMatrix();
    }
}
