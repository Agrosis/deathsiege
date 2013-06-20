package com.jantox.siege.entities.map;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Input;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.gfx.BitmapFont;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Shop extends Entity {

    private int index;
    private BitmapFont font;

    public Shop(Vector3D pos) {
        super(pos);

        try {
            font = new BitmapFont(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/number_font_strip10.png")), GL_NEAREST), 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int sbreak = 0;

    @Override
    public void update(float delta) {
        if(Input.down && sbreak <= 0) {
            sbreak = 10;
            index++;
        }

        if(sbreak > 0) {
            sbreak--;
        }

        if(this.getPosition().distanceSquared(level.getPlayer().getPosition()) <= 5 * 5) {
            GameInstance.shop = this;
        } else {
            GameInstance.shop = null;
        }
    }

    @Override
    public void render() {
        //glEnable(GL_TEXTURE_2D);
        glPushMatrix();

        glTranslatef((float)pos.x, (float)pos.y, (float)pos.z);
        glScalef(0.75f, 0.75f, 0.75f);
        glCallList(Resources.getModel(30));

        glPopMatrix();
    }

    public void renderShop() {
        glTranslatef(0, index * 56, 0);

        glBegin(GL_LINES);
        glVertex2f(200 + 12, 50 + 48);
        glVertex2f(200 + 12 + 377, 50 + 48);

        glVertex2f(200 + 12 + 377, 50 + 48);
        glVertex2f(200 + 12 + 377, 50 + 48 + 55);

        glVertex2f(200 + 12 + 377, 50 + 48 + 55);
        glVertex2f(200 + 12, 50 + 48 + 55);

        glVertex2f(200 + 12, 50 + 48 + 55);
        glVertex2f(200 + 12, 50 + 48);
        glEnd();

        glTranslatef(0, -index * 56, 0);

        glColor3f(1,1,1);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(10).getTextureID());
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(200, 50);
        glTexCoord2f(399f/512f, 0);
        glVertex2f(600, 50);
        glTexCoord2f(399f/512f, 499f/512f);
        glVertex2f(600, 550);
        glTexCoord2f(0, 499f/512f);
        glVertex2f(200, 550);

        glEnd();
        glDisable(GL_TEXTURE_2D);


        font.drawText(":1000", 1000, 235, 0.5f, new Vector3D(1,1,1));
        font.drawText(":500", 1000, 345, 0.5f, new Vector3D(1,1,1));
        font.drawText(":200", 1000, 455, 0.5f, new Vector3D(1,1,1));
        font.drawText(":3000", 1000, 565, 0.5f, new Vector3D(1,1,1));
        font.drawText(":4000", 1000, 675, 0.5f, new Vector3D(1,1,1));
    }

    public int getIndex() {
        return index;
    }

}
