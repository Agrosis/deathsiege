package com.jantox.siege.level;

import com.jantox.siege.*;
import com.jantox.siege.entities.map.*;
import com.jantox.siege.entities.map.ControlPoint;
import com.jantox.siege.entities.resources.Tree;
import com.jantox.siege.entities.tools.Bullet;
import com.jantox.siege.geometry.*;
import com.jantox.siege.entities.*;
import com.jantox.siege.particle.Particle;
import com.jantox.siege.particle.ParticleBehavior;
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

    private Gamemode gamemode;

    public Level(Player player) {
        this.player = player;
        this.camera = player.getCamera();

        multiplayers = new ArrayList<MultiplayerLiving>();

        this.entities = new ArrayList<Entity>();
        floors = new ArrayList<Quad>();
        floors.add(new Quad(new Vector3D(-2000, 0, -2000), new Vector3D(2000, 0, -2000), new Vector3D(2000, 0, 2000), new Vector3D(-2000, 0, 2000)));
        floors.add(new Quad(new Vector3D(-10, 15, -10),new Vector3D(10, 15, -10),new Vector3D(10, 15, 10),new Vector3D(-10, 15, 10)));
        //floors.add(new Quad(new Vector3D(20, 3, 13), new Vector3D(20, 3, 15), new Vector3D(25, 3, 15), new Vector3D(25, 3, 13)));
        //floors.add(new Quad(new Vector3D(23, 3, 13), new Vector3D(25, 3, 13), new Vector3D(25, 3, 5), new Vector3D(23, 3, 5)));

        walls = new ArrayList<Quad>();
        walls.add(new Quad(new Vector3D(155, 0, 160), new Vector3D(-530, 0, 160), new Vector3D(-530, 20, 160), new Vector3D(155, 20, 160)));
        walls.add(new Quad(new Vector3D(155, 0, -160), new Vector3D(-530, 0, -160), new Vector3D(-530, 20, -160), new Vector3D(155, 20, -160)));
        //ramps.add(new Quad(new Vector3D(15, -2, 15),new Vector3D(18, -2, 15),new Vector3D(18, 1, 15),new Vector3D(15, 1, 15)));

        ramps = new ArrayList<Quad>();
        //ramps.add(new Quad(new Vector3D(1, -2, 15), new Vector3D(1, -2, 13), new Vector3D(20,1,13), new Vector3D(20, 1, 15)));

        psys = new ParticleSystem(new Vector3D(5, 0, 5));

        skybox = new Skybox("", 1400);
    }

    public void init() {
        this.gamemode = new Siege(this);
        gamemode.init();
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

                if(e instanceof Bullet) {
                    Ray bullet = new Ray(e.getPosition(), ((Bullet)e).getDirection());
                    for(Entity x : entities) {
                        if(x instanceof Kage || x instanceof Endwek) {
                            Sphere z = (Sphere) x.getCollisionMask();
                            if(CollisionSystem.raySphere(bullet, z)) {
                                ((Living) x).damage(50);
                                e.setExpired(true);
                                break;
                            }
                        } else if(x instanceof Spawner) {
                            Vector3D nz = x.getPosition().copy();
                            nz.x += 1;
                            nz.z += 0.5;
                            Sphere z = new Sphere(nz, 2);
                            if(CollisionSystem.raySphere(bullet, z)) {
                                ((Spawner) x).damage(50);
                                e.setExpired(true);

                                break;
                            }
                        }
                    }
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
        glColor3f(1f, 1f, 1f);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(0).getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-700, -2, -700);
        glTexCoord2f(50, 0);
        glVertex3f(700, -2, -700);
        glTexCoord2f(50, 50);
        glVertex3f(700, -2, 700);
        glTexCoord2f(0, 50);
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
}