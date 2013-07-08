package com.jantox.siege.entities.map.shop;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.gfx.BitmapFont;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class ShopItem {

    public static enum ITEM { EXTENDED_TIME, SENTRY_GUN, AMMO_REFILL, THE_ORIGINAL, THE_SHOTGUN, FIREWORKS, CROSSBOW, BATTLEAXE, CATAPULT };

    private ITEM type;
    private Texture texture;
    private int cost;

    private String name, desc;
    private int wavelock;

    public ShopItem(ITEM type) {
        this.type = type;
        if(type == ITEM.EXTENDED_TIME) {
            this.cost = 1000;
            name = "Extended Time";
            desc = "Adds an extra 30 seconds\nto the break time.";
            wavelock = 1;
        } else if(type == ITEM.SENTRY_GUN) {
            this.cost = 1300;
            name = "Sentry Gun";
            wavelock = 15;
            desc = "An automated defense mechanism.";
        } else if(type == ITEM.AMMO_REFILL) {
            this.cost = 500;
            name = "Ammo Refill";
            wavelock = 3;
            desc = "Refills your current weapon's ammo.";
        } else if(type == ITEM.THE_ORIGINAL) {
            this.cost = 2000;
            name = "The Original";
            wavelock = 15;
            desc = "A long ranged and medium accuracy\nsniper with quickscoping.";
        } else if(type == ITEM.THE_SHOTGUN) {
            this.cost = 800;
            name = "The Shotgun";
            wavelock = 15;
            desc = "A quick 2-round shotgun with\nlow range and high damage.";
        } else if(type == ITEM.FIREWORKS) {
            this.cost = 200;
            name = "Fireworks";
            wavelock = 3;
            desc = "Fireworks will scatter across the\nmap for one minute! From Agro. :)";
        } else if(type == ITEM.CROSSBOW) {
            this.cost = 1000;
            name = "The Fatal Crossbow";
            wavelock = 5;
            desc = "A deadly crossbow that will kill\non impact from close range.";
        } else if(type == ITEM.BATTLEAXE) {
            this.cost = 1200;
            name = "The Artic Axe";
            wavelock = 5;
            desc = "A fast one-handed battle axe.";
        }
    }

    public int getWaveLock() {
        return wavelock;
    }

    public void render(boolean lock, int index) {
        glPushMatrix();
        glTranslatef(0, index * 55, 0);

        Resources.getFont("terminal").drawText(name, 533-200 + 13 + 4, 50 + 49 + 4, 1, BitmapFont.DARK_GRAY, false,  8);
        Resources.getFont("terminal").drawText(desc, 533-200 + 13 + 4, 50 + 49 + 18, 1, BitmapFont.YELLOW, false,  8);

        if(lock) {
            glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
            glBegin(GL_QUADS);
            glVertex2f(533-200 + 13, 50 + 49);
            glVertex2f(533+175 + 13, 50 + 49);
            glVertex2f(533+175 + 13, 50 + 49 + 3 + 50);
            glVertex2f(533-200 + 13, 50 + 49 + 3 + 50);
            glEnd();

            Resources.getFont("terminal").drawText("Wave " + wavelock, 533-100 + 13 + 4 + 200, 50 + 49 + 4 + 13, 1, BitmapFont.DARK_GRAY, false,  8);
        } else {
            Resources.getFont("terminal").drawText("$" + cost, 533-100 + 13 + 4 + 200, 50 + 49 + 4 + 13, 1, BitmapFont.GREEN, false,  8);
        }

        glPopMatrix();
    }

    public ITEM getItemType() {
        return type;
    }

    public int getCost() {
        return cost;
    }

}
