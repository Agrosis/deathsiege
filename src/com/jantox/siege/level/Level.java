package com.jantox.siege.level;

import com.jantox.siege.*;
import com.jantox.siege.entities.map.*;
import com.jantox.siege.entities.tools.Projectile;
import com.jantox.siege.geometry.*;
import com.jantox.siege.entities.*;
import com.jantox.siege.particle.ParticleSystem;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Level {

    private ArrayList<Entity> entities;
    private ArrayList<MultiplayerLiving> multiplayers;

    private ArrayList<Quad> floors;
    public ArrayList<Quad> walls;
    public ArrayList<Quad> ramps;

    private Skybox skybox;
    private Player player;
    private Camera camera;
    public Fortress fortress;

    public static ParticleSystem psys;

    private ArrayList<Projectile> projectiles;

    private Gamemode gamemode;

    public Level(Player player) {
        this.player = player;
        this.camera = player.getCamera();

        projectiles = new ArrayList<Projectile>();

        multiplayers = new ArrayList<MultiplayerLiving>();

        this.entities = new ArrayList<Entity>();
        floors = new ArrayList<Quad>();
        floors.add(new Quad(new Vector3D(-2000, 0, -2000), new Vector3D(2000, 0, -2000), new Vector3D(2000, 0, 2000), new Vector3D(-2000, 0, 2000)));
        floors.add(new Quad(new Vector3D(-10, 15, -10),new Vector3D(10, 15, -10),new Vector3D(10, 15, 10),new Vector3D(-10, 15, 10)));
        //floors.add(new Quad(new Vector3D(20, 3, 13), new Vector3D(20, 3, 15), new Vector3D(25, 3, 15), new Vector3D(25, 3, 13)));
        //floors.add(new Quad(new Vector3D(23, 3, 13), new Vector3D(25, 3, 13), new Vector3D(25, 3, 5), new Vector3D(23, 3, 5)));

        walls = new ArrayList<Quad>();
        walls.add(new Quad(new Vector3D(-250, 0, -222), new Vector3D(250, 0, -222), new Vector3D(250, 20, -222), new Vector3D(-250, 20,-222)));
        walls.add(new Quad(new Vector3D(-250, 0, 222), new Vector3D(250, 0, 222), new Vector3D(250, 20, 222), new Vector3D(-250, 20,222)));
        walls.add(new Quad(new Vector3D(-222, 0, 250), new Vector3D(-222, 0, -250), new Vector3D(-222, 20, -250), new Vector3D(-222, 20,250)));
        walls.add(new Quad(new Vector3D(222, 0, 250), new Vector3D(222, 0, -250), new Vector3D(222, 20, -250), new Vector3D(222, 20,250)));
        //ramps.add(new Quad(new Vector3D(15, -2, 15),new Vector3D(18, -2, 15),new Vector3D(18, 1, 15),new Vector3D(15, 1, 15)));

        ramps = new ArrayList<Quad>();
        //ramps.add(new Quad(new Vector3D(1, -2, 15), new Vector3D(1, -2, 13), new Vector3D(20,1,13), new Vector3D(20, 1, 15)));

        psys = new ParticleSystem(new Vector3D(5, 0, 5));

        skybox = new Skybox("", 1400);
    }

    public void init() {
        this.gamemode = new Siege(this);
        gamemode.init();

        this.spawn(new Spawner(new Vector3D(20, -16f, 20)));
        //this.spawn(new Endwek(new Vector3D(50, 0, 50), player, false));
    }

    public void update(float delta) {
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);

            if(!(e instanceof MultiplayerLiving) || !GameInstance.multiplayer) {
                e.update(delta);

                if(e.isExpired()) {
                    entities.remove(i);
                    continue;
                }
            } else {
                if(e.isExpired()) {
                    entities.remove(i);
                    e = null;
                    continue;
                }
                ((MultiplayerLiving)e).updateMultiplayer();
            }
        }

        for(int i = 0;i < projectiles.size(); i++) {
            if(projectiles.get(i).expired == false) {
                projectiles.get(i).use(entities);
                projectiles.remove(i);
            }
        }

        camera.update(delta);
        player.updateMask();
        player.updatePosition();

        this.player.update(3);
        psys.update(delta);

        skybox.update();
        gamemode.update();
    }

    public void render(float delta) {
        glClearColor(0.5f,0.5f,0.5f,0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();

        glPushMatrix();

        camera.applyRotation();
        skybox.render();
        camera.applyTranslation();

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        //glEnable(GL_BLEND);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glPushMatrix();
        glColor3f(0.5f,0.5f,0.5f);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(0).getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-700, -2, -700);
        glTexCoord2f(400, 0);
        glVertex3f(700, -2, -700);
        glTexCoord2f(400, 400);
        glVertex3f(700, -2, 700);
        glTexCoord2f(0, 400);
        glVertex3f(-700, -2, 700);
        glEnd();

        glPopMatrix();

        psys.render();

        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).render();
        }

        this.player.render();

        glPopMatrix();
    }

    public void spawn(Entity e) {
        if(GameInstance.multiplayer && e instanceof MultiplayerLiving) {
            multiplayers.add((MultiplayerLiving)e);
        }
        entities.add(e);
    }

    public void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Entity getClosestMonster(Entity e, float radius) {
        double dist = 1000000000;
        Entity r = null;

        for(Entity t : entities) {
            if(t instanceof Kage) {
                if(e.getPosition().distanceSquared(t.getPosition()) <= radius) {
                    if(e.getPosition().distanceSquared(t.getPosition()) < dist) {
                        dist = e.getPosition().distanceSquared(t.getPosition());
                        r = t;
                    }
                }
            }
        }

        return r;
    }

    public ArrayList<Quad> getFloors() {
        return floors;
    }

    public ArrayList<Quad> getWalls() {
        return walls;
    }

    public void despawn(Entity e) {
        entities.remove(e);
    }

    public void despawnMultiplayer(int eid) {
        for(int i = 0; i < this.multiplayers.size(); i++) {
            if(this.multiplayers.get(i).getEntityID() == eid) {
                this.multiplayers.remove(i);
            }
        }
    }

    public ArrayList<MultiplayerLiving> getMultiplayerLivings() {
        return multiplayers;
    }

    public MultiplayerLiving getMultiplayerObjectWith(int eid) {
        for(MultiplayerLiving ml : multiplayers) {
            if(ml.getEntityID() == eid) {
                return ml;
            }
        }
        return null;
    }

    public void removeAllMonsters() {
        for(int i = 0; i < entities.size(); i++) {
            if(entities.get(i) instanceof Kage || entities.get(i) instanceof Endwek) {
                entities.get(i).setExpired(true);
            }
        }
    }

    public Gamemode getGameMode() {
        return gamemode;
    }
}