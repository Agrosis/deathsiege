package com.jantox.siege.entities.map.shop;

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

    public static enum ITEM { EXTENDED_TIME, SENTRY_GUN, AMMO_REFILL, THE_ORIGINAL, THE_SHOTGUN, FIREWORKS };

    private ITEM type;
    private int index;
    private Texture texture;
    private int cost;

    private String name, desc;

    public ShopItem(ITEM type, int index) {
        this.type = type;
        this.index = index;
        if(type == ITEM.EXTENDED_TIME) {
            this.cost = 1000;
            name = "Extended Time";
            desc = "Adds an extra 30 seconds\nto the break time.";
        } else if(type == ITEM.SENTRY_GUN) {
            this.cost = 1300;
            name = "Sentry Gun";
            desc = "An automated defense mechanism.";
        } else if(type == ITEM.AMMO_REFILL) {
            this.cost = 500;
            name = "Ammo Refill";
            desc = "Refills your current weapon's ammo.";
        } else if(type == ITEM.THE_ORIGINAL) {
            this.cost = 1000;
            name = "The Original";
            desc = "A long ranged and medium accuracy\nsniper with quickscoping.";
        } else if(type == ITEM.THE_SHOTGUN) {
            this.cost = 800;
            name = "The Shotgun";
            desc = "A quick 2-round shotgun with\nlow range and high damage.";
        } else if(type == ITEM.FIREWORKS) {
            this.cost = 200;
            name = "Fireworks";
            desc = "Fireworks will scatter across the\nmap for one minute! From Agro. :)";
        }
    }

    public void render() {
        glPushMatrix();
        glTranslatef(0, index * 55, 0);

        Resources.getFont("terminal").drawText(name, 200 + 13 + 4, 50 + 49 + 4, 1, BitmapFont.DARK_GRAY, false,  8);
        Resources.getFont("terminal").drawText(desc, 200 + 13 + 4, 50 + 49 + 18, 1, BitmapFont.YELLOW, false,  8);

        glPopMatrix();
    }

    public ITEM getItemType() {
        return type;
    }

    public int getCost() {
        return cost;
    }
}
