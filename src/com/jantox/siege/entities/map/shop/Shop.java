package com.jantox.siege.entities.map.shop;

import com.jantox.siege.*;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.tools.Sword;
import com.jantox.siege.entities.tools.Crossbow;
import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.level.Level;
import com.jantox.siege.level.Siege;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Shop extends Entity {

    public static enum SHOP { WEAPONS, DEFENSE, SPECIALS, GUNSHOP };

    private int index;

    private ArrayList<ShopItem> items;
    private BitmapFont font;

    private int base = -600;
    private String name;

    private int startindex = 0;

    public Shop(Vector3D pos, Level level, SHOP shoptype) {
        super(pos, level);

        items = new ArrayList<ShopItem>();
        startindex = 0;

        if(shoptype == SHOP.SPECIALS) {
            name = "Armor, Specials and Upgrades";
            items.add(new ShopItem(ShopItem.ITEM.EXTENDED_TIME));
            items.add(new ShopItem(ShopItem.ITEM.AMMO_REFILL));
            items.add(new ShopItem(ShopItem.ITEM.SENTRY_GUN));
            items.add(new ShopItem(ShopItem.ITEM.FIREWORKS));
        } else if(shoptype == SHOP.WEAPONS) {
            name = "Weapons";
            items.add(new ShopItem(ShopItem.ITEM.CROSSBOW));
            items.add(new ShopItem(ShopItem.ITEM.BATTLEAXE));
            items.add(new ShopItem(ShopItem.ITEM.THE_ORIGINAL));
            items.add(new ShopItem(ShopItem.ITEM.THE_SHOTGUN));
        }

        font = Resources.getFont("terminal");
    }

    int sbreak = 0;

    @Override
    public void update(float delta) {
        if(GameInstance.shop == this) {
            if(Input.down && sbreak <= 0) {
                sbreak = 10;

                if(index == 7) {
                    startindex++;
                } else {
                    if(index < items.size()-1)
                        index++;
                }

                GameInstance.audio.playSound(7);
            } else if(Input.up && sbreak <= 0) {
                sbreak = 10;

                if(index > 0)
                    index--;
                else if(index == 0) {
                    if(startindex > 0)
                        startindex--;
                }

                GameInstance.audio.playSound(7);
            } else if(Input.enter && sbreak <= 0) {
                sbreak = 10;
                this.buyItem();
            }

            if(sbreak > 0) {
                sbreak--;
            }

            if(base < 0)
                base +=75;
        } else {
            if(base > -600)
                base -=75;
        }
    }

    @Override
    public void render() {
        //glEnable(GL_TEXTURE_2D);
        glPushMatrix();

        glTranslatef((float)pos.x, (float)pos.y-0.25f, (float)pos.z);
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
        glVertex2f(533 - 200 + base, 50);
        glTexCoord2f(400f/512f, 0);
        glVertex2f(533 + 200 + base, 50);
        glTexCoord2f(400f/512f, 500f/512f);
        glVertex2f(533 + 200 + base, 550);
        glTexCoord2f(0, 500f/512f);
        glVertex2f(533 - 200 + base, 550);

        glEnd();
        glDisable(GL_TEXTURE_2D);


        if(base == 0) {
            glPushMatrix();

            glTranslatef(0, index * 55, 0);
            glBegin(GL_LINES);
            glVertex2f(333 + 12, 50 + 48);
            glVertex2f(333 + 12 + 377, 50 + 48);

            glVertex2f(333 + 12 + 377, 50 + 48);
            glVertex2f(333 + 12 + 377, 50 + 48 + 56);

            glVertex2f(333 + 12 + 377, 50 + 48 + 56);
            glVertex2f(333 + 12, 50 + 48 + 56);

            glVertex2f(333 + 12, 50 + 48 + 56);
            glVertex2f(333 + 12, 50 + 48);
            glEnd();
            glPopMatrix();

            for(int i = startindex; i < startindex+8; i++) {
                if(i >= items.size())
                    break;
                boolean lock = true;
                if(((Siege)level.getGameMode()).getWave() + 1 >= items.get(i).getWaveLock())
                    lock = false;
                items.get(i).render(lock, i-startindex);
            }

            font.drawText(name, 533 - (int)((name.length() / 2f) * 8), 50 + 20,1, new Vector3D(1,1,1), false, 8);

            glPushMatrix();
            //font.drawText("$1000", 520, 235/2, 1f, new Vector3D(1,1,1), false, 8);
           // font.drawText("$2500", 520, 345/2, 1f, new Vector3D(1,1,1), false, 8);
            /*font.drawText("$200", 500, 455/2, 1f, new Vector3D(1,1,1), false, 8);
            font.drawText("$3000", 500, 565/2, 1f, new Vector3D(1,1,1), false, 8);
            font.drawText("$4000", 500, 675/2, 1f, new Vector3D(1,1,1), false, 8);*/
            glPopMatrix();
        }
    }

    public void buyItem() {
        ShopItem si = this.items.get(index);

        if(GameInstance.ccash >= si.getCost() && ((Siege)level.getGameMode()).getWave() >= si.getWaveLock()) {
            if(Statistics.ITEMS_BOUGHT == 5) {
                GameInstance.gamejolt.addTrophy(2466);
            }

            GameInstance.ccash -= si.getCost();
            Statistics.ITEMS_BOUGHT++;
            if(si.getItemType() == ShopItem.ITEM.EXTENDED_TIME) { // extended time
                if(((Siege)level.getGameMode()).canExtendTime()) {
                    ((Siege)level.getGameMode()).addBreakTime(15);
                    GameInstance.audio.playSound(8);
                } else {
                    GameInstance.audio.playSound(9);
                }
            } else if(si.getItemType() == ShopItem.ITEM.BATTLEAXE) {
                level.getPlayer().addItem(new Sword(level.getPlayer()));
            } else if(si.getItemType() == ShopItem.ITEM.CROSSBOW) {
                level.getPlayer().addItem(new Crossbow(level.getPlayer(), level));
            }
        } else {
            GameInstance.audio.playSound(9);
        }
    }

    public int getIndex() {
        return index;
    }

}
