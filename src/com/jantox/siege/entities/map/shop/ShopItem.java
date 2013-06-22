package com.jantox.siege.entities.map.shop;

import com.jantox.siege.Resources;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class ShopItem {

    private int id, index;
    private Texture texture;

    public ShopItem(int id, int index) {
        this.id = id;
        this.index = index;
        if(id == 0) {
            try {
                texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/extended_time.png")), GL11.GL_NEAREST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void render() {
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(200 + 13 +2, 50 + 49+2);
        glTexCoord2f(108f/256f, 0);
        glVertex2f(200 + 108 + 13+2, 50 + 49+2);
        glTexCoord2f(108f/256f, 14f/32f);
        glVertex2f(200 + 108 + 13 + 2, 50 + 49 + 14+2);
        glTexCoord2f(0, 14f/32f);
        glVertex2f(200 + 13+2, 50 + 49 + 14+2);

        glEnd();
    }

}
