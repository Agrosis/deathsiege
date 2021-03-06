package com.jantox.siege;

import com.jantox.siege.gfx.BitmapFont;
import com.jantox.siege.models.DSMLoader;
import com.jantox.siege.models.ObjectLoader;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Resources {

    public static Resources resources = new Resources();

    private ArrayList<Texture> textures;
    private ArrayList<Integer> models;
    private Map<String, BitmapFont> fonts;

    public Resources() {
        textures = new ArrayList<Texture>();
        models = new ArrayList<Integer>();
        fonts = new HashMap<String, BitmapFont>();

        this.load();
    }

    public void load() {
        try {
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_grass.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_smoke_particle.png")), GL11.GL_LINEAR));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/control_point_green.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/Gradient_Round.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/stone.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_tree.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/sniper.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/shop_menu.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/brickwall.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_locked.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/inventory.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/inventory_selector.png")), GL11.GL_NEAREST));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectLoader objloader = new ObjectLoader();
        models.add(objloader.loadOBJModel("models/blast.obj"));
        models.add(objloader.loadOBJModel("models/gem.obj"));
        models.add(objloader.loadOBJModel("models/building2HDD.obj"));
        models.add(objloader.loadOBJModel("models/cube.obj"));
        models.add(objloader.loadOBJModel("models/shotgun.obj"));
        models.add(objloader.loadOBJModel("models/tree2.obj"));
        models.add(objloader.loadOBJModel("models/woodaxe.obj"));
        models.add(objloader.loadOBJModel("models/ramp.obj"));
        models.add(objloader.loadOBJModel("models/sword.obj"));
        models.add(objloader.loadOBJModel("models/catapult2.obj"));
        models.add(objloader.loadOBJModel("models/catapult_arm.obj"));
        models.add(objloader.loadOBJModel("models/ladder.obj"));
        models.add(objloader.loadOBJModel("models/sniper.obj"));
        models.add(objloader.loadOBJModel("models/ammo.obj"));
        models.add(objloader.loadOBJModel("models/sentry_bottom.obj"));
        models.add(objloader.loadOBJModel("models/sentry_top.obj"));
        models.add(objloader.loadOBJModel("models/crossbow.obj"));
        models.add(objloader.loadOBJModel("models/bolt.obj"));
        models.add(objloader.loadOBJModel("models/zombie_head.obj"));
        models.add(objloader.loadOBJModel("models/zombie_arms.obj"));
        models.add(objloader.loadOBJModel("models/zfeet_r.obj"));
        models.add(objloader.loadOBJModel("models/zfeet_l.obj"));
        models.add(objloader.loadOBJModel("models/building3.obj"));
        models.add(objloader.loadOBJModel("models/building4.obj"));
        models.add(objloader.loadOBJModel("models/fortress.obj"));
        models.add(objloader.loadOBJModel("models/gatepost.obj"));
        models.add(objloader.loadOBJModel("models/zombie_body.obj"));
        models.add(objloader.loadOBJModel("models/helicopter_body2.obj"));
        models.add(objloader.loadOBJModel("models/helicopter_wing.obj"));
        models.add(objloader.loadOBJModel("models/blastbullet.obj")); // 29
        models.add(objloader.loadOBJModel("models/shop.obj"));
        models.add(objloader.loadOBJModel("models/intervention.obj"));
        models.add(objloader.loadOBJModel("models/spawner.obj"));
        models.add(objloader.loadOBJModel("models/spawner2.obj"));
        models.add(objloader.loadOBJModel("models/spawner3.obj"));
        models.add(objloader.loadOBJModel("models/minigun_handle.obj")); //35
        models.add(objloader.loadOBJModel("models/rock.obj")); //36
        models.add(objloader.loadOBJModel("models/minigun.obj")); //37
        models.add(objloader.loadOBJModel("models/fists.obj"));

        try {
            fonts.put("terminal", new BitmapFont(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/fonts/terminal.png")), GL11.GL_NEAREST), 16, 16));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Texture get(int id) {
        return textures.get(id);
    }

    public Integer getMdl(int id) {
        return models.get(id);
    }

    public BitmapFont getFnt(String s) {
        return fonts.get(s);
    }

    public static Texture getTexture(int id) {
        return resources.get(id);
    }

    public static Integer getModel(int id) {
        return resources.getMdl(id);
    }

    public static BitmapFont getFont(String key) {
        return resources.getFnt(key);
    }

}
