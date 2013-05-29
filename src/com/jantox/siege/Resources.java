package com.jantox.siege;

import com.jantox.siege.models.ObjectLoader;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Resources {

    public static Resources resources = new Resources();

    private ArrayList<Texture> textures;
    private ArrayList<Integer> models;

    public Resources() {
        textures = new ArrayList<Texture>();
        models = new ArrayList<Integer>();

        this.load();
    }

    public void load() {
        try {
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_grass2.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_smoke_particle.png")), GL11.GL_LINEAR));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/control_point_green.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/Gradient_Round.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/inner_gradient.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/outer_gradient.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/stone.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_tree.png")), GL11.GL_NEAREST));
            textures.add(TextureLoader.getTexture("PNG", new FileInputStream(new File("textures/texture_spawner.png")), GL11.GL_NEAREST));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectLoader objloader = new ObjectLoader();
        models.add(objloader.loadOBJModel("models/blast.obj"));
        models.add(objloader.loadOBJModel("models/gem.obj"));
        models.add(objloader.loadOBJModel("models/building2HDD.obj"));
        models.add(objloader.loadOBJModel("models/cube.obj"));
        models.add(objloader.loadOBJModel("models/blaster.obj"));
        models.add(objloader.loadOBJModel("models/tree2.obj"));
        models.add(objloader.loadOBJModel("models/woodaxe.obj"));
        models.add(objloader.loadOBJModel("models/ramp.obj"));
        models.add(objloader.loadOBJModel("models/gatepart.obj"));
        models.add(objloader.loadOBJModel("models/stump.obj"));
        models.add(objloader.loadOBJModel("models/log.obj"));
        models.add(objloader.loadOBJModel("models/ladder.obj"));
        models.add(objloader.loadOBJModel("models/fence.obj"));
        models.add(objloader.loadOBJModel("models/ammo.obj"));
        models.add(objloader.loadOBJModel("models/sentry_bottom.obj"));
        models.add(objloader.loadOBJModel("models/sentry_top.obj"));
        models.add(objloader.loadOBJModel("models/crossbow.obj"));
        models.add(objloader.loadOBJModel("models/bolt.obj"));
        models.add(objloader.loadOBJModel("models/zombie_head.obj"));
        models.add(objloader.loadOBJModel("models/zombie_arms.obj"));
        models.add(objloader.loadOBJModel("models/zombie_feet.obj"));
    }

    public Texture get(int id) {
        return textures.get(id);
    }

    public Integer getMdl(int id) {
        return models.get(id);
    }

    public static Texture getTexture(int id) {
        return resources.get(id);
    }

    public static Integer getModel(int id) {
        return resources.getMdl(id);
    }

}
