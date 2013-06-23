package com.jantox.siege.entities.map.shop;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Input;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.level.Siege;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Shop extends Entity {

    public static enum SHOP { WEAPONS, DEFENSE, SPECIALS };

    private int index;

    private ArrayList<ShopItem> items;
    private BitmapFont font;

    public Shop(Vector3D pos, SHOP shoptype) {
        super(pos);

        items = new ArrayList<ShopItem>();

        if(shoptype == SHOP.SPECIALS) {
            items.add(new ShopItem(ShopItem.ITEM.EXTENDED_TIME, 0));
            items.add(new ShopItem(ShopItem.ITEM.SENTRY_GUN, 1));
        }

        font = Resources.getFont("terminal");
    }

    int sbreak = 0;

    @Override
    public void update(float delta) {
        if(Input.down && sbreak <= 0 && index < 7) {
            sbreak = 10;
            index++;

            GameInstance.audio.playSound(7);
        } else if(Input.up && sbreak <= 0 && index > 0) {
            sbreak = 10;
            index--;

            GameInstance.audio.playSound(7);
        } else if(Input.enter && sbreak <= 0) {
            sbreak = 10;
            this.buyItem();
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
        glColor3f(1,1,1);



        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(7).getTextureID());
        glBegin(GL_QUADS);

        glTexCoord2f(0, 0);
        glVertex2f(200, 50);
        glTexCoord2f(400f/512f, 0);
        glVertex2f(600, 50);
        glTexCoord2f(400f/512f, 500f/512f);
        glVertex2f(600, 550);
        glTexCoord2f(0, 500f/512f);
        glVertex2f(200, 550);

        glEnd();
        glDisable(GL_TEXTURE_2D);



        glPushMatrix();

        glTranslatef(0, index * 55, 0);
        glBegin(GL_LINES);
        glVertex2f(200 + 12, 50 + 48);
        glVertex2f(200 + 12 + 377, 50 + 48);

        glVertex2f(200 + 12 + 377, 50 + 48);
        glVertex2f(200 + 12 + 377, 50 + 48 + 56);

        glVertex2f(200 + 12 + 377, 50 + 48 + 56);
        glVertex2f(200 + 12, 50 + 48 + 56);

        glVertex2f(200 + 12, 50 + 48 + 56);
        glVertex2f(200 + 12, 50 + 48);
        glEnd();
        glPopMatrix();

        for(int i = 0; i < items.size(); i++) {
            items.get(i).render();
        }

        glPushMatrix();
        font.drawText("$1000", 520, 235/2, 1f, new Vector3D(1,1,1), false, 8);
        font.drawText("$2500", 520, 345/2, 1f, new Vector3D(1,1,1), false, 8);
        /*font.drawText("$200", 500, 455/2, 1f, new Vector3D(1,1,1), false, 8);
        font.drawText("$3000", 500, 565/2, 1f, new Vector3D(1,1,1), false, 8);
        font.drawText("$4000", 500, 675/2, 1f, new Vector3D(1,1,1), false, 8);*/
        glPopMatrix();
    }

    public void buyItem() {
        ShopItem si = this.items.get(index);

        if(GameInstance.ccash >= si.getCost()) {
            if(si.getItemType() == ShopItem.ITEM.EXTENDED_TIME) { // extended time
                if(((Siege)level.getGameMode()).canExtendTime()) {
                    ((Siege)level.getGameMode()).addBreakTime(15);
                    GameInstance.ccash -= si.getCost();
                    GameInstance.audio.playSound(8);
                } else {
                    GameInstance.audio.playSound(9);
                }
            }
        } else {
            GameInstance.audio.playSound(9);
        }
    }

    public int getIndex() {
        return index;
    }

}
