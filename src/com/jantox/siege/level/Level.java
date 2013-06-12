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

    private ArrayList<ParticleSystem> particlesys;

    private ArrayList<Quad> floors;
    public ArrayList<Quad> walls;
    public ArrayList<Quad> ramps;

    private SpawnerFactory spawnerFactory;

    private Player player;
    private Camera camera;

    private ControlPoint points[];

    public static ParticleSystem psys;

    private Skybox skybox;

    public Fortress fortress;

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
        //ramps.add(new Quad(new Vector3D(15, -2, 15),new Vector3D(18, -2, 15),new Vector3D(18, 1, 15),new Vector3D(15, 1, 15)));

        ramps = new ArrayList<Quad>();
        //ramps.add(new Quad(new Vector3D(1, -2, 15), new Vector3D(1, -2, 13), new Vector3D(20,1,13), new Vector3D(20, 1, 15)));

        psys = new ParticleSystem(new Vector3D(5, 0, 5));

        spawnerFactory = new SpawnerFactory(this);

        skybox = new Skybox("", 1400);
    }

    public void init() {
        points = new ControlPoint[5];
        points[0] = new ControlPoint(new Vector3D(0, -1.9999999, 0));
        points[1] = new ControlPoint(new Vector3D(-40, -1.9999999, -40));
        points[2] = new ControlPoint(new Vector3D(40, -1.9999999, -40));
        points[3] = new ControlPoint(new Vector3D(40, -1.9999999, 40));
        points[4] = new ControlPoint(new Vector3D(-40, -1.9999999, 40));

        /*this.spawn(new Spawner(new Vector3D(85, 1.7f, 85)));
        this.spawn(new Spawner(new Vector3D(85, 1.7f, -85)));
        this.spawn(new Spawner(new Vector3D(-85, 1.7f, -85)));
        this.spawn(new Spawner(new Vector3D(-85, 1.7f, 85)));*/

        this.spawn((fortress = new Fortress()));
        spawnerFactory.fortress = fortress;

        for(int i = 9; i < 190; i+=5) {
            this.spawn(new Path(new Vector3D(i + 1, 0, 0)));
        }

        for(int i = -9; i > -190; i-=5) {
            this.spawn(new Path(new Vector3D(i - 1, 0, 0)));
        }

        for(int i = 9; i < 190; i+=5) {
            this.spawn(new Path(new Vector3D(0, 0, i + 1)));
        }

        for(int i = -9; i > -190; i-=5) {
            this.spawn(new Path(new Vector3D(0, 0, i - 1)));
        }

        this.spawn(new Decoration(new Vector3D(0, 0, 0), new Vector3D(0.14, 0.14, 0.14), new Vector3D(-90, 0, 0), 2));

        this.spawn(new Decoration(new Vector3D(140, 0, 140), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 23));
        this.spawn(new Decoration(new Vector3D(-140, 0, 140), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 270, 0), 23));
        this.spawn(new Decoration(new Vector3D(-140, 0, -140), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 180, 0), 23));
        this.spawn(new Decoration(new Vector3D(140, 0, -140), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 90, 0), 23));

        this.spawn(new Decoration(new Vector3D(50, 0, 50), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 22));
        this.spawn(new Decoration(new Vector3D(-50, 0, 50), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 270, 0), 22));
        this.spawn(new Decoration(new Vector3D(-50, 0, -50), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 180, 0), 22));
        this.spawn(new Decoration(new Vector3D(50, 0, -50), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 90, 0), 22));

        /*this.spawn(new Decoration(new Vector3D(0, 0, 0), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
        this.spawn(new Decoration(new Vector3D(-40, 0, -40), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
        this.spawn(new Decoration(new Vector3D(40, 0, -40), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
        this.spawn(new Decoration(new Vector3D(40, 0, 40), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
        this.spawn(new Decoration(new Vector3D(-40, 0, 40), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));

        this.spawn(new Decoration(new Vector3D(80, 0, 80), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 23));
        this.spawn(new Decoration(new Vector3D(-80, 0, 80), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 270, 0), 23));

        this.spawnForest(10, new Vector3D(98, 0, 98), 10);
        this.spawnForest(10, new Vector3D(-98, 0, 98), 10);
        this.spawnForest(10, new Vector3D(-98, 0, -98), 10);
        this.spawnForest(10, new Vector3D(98, 0, -98), 10);*/
    }

    public void update(float delta) {
        spawnerFactory.update();
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
        glVertex3f(-300, -2, -300);
        glTexCoord2f(50, 0);
        glVertex3f(300, -2, -300);
        glTexCoord2f(50, 50);
        glVertex3f(300, -2, 300);
        glTexCoord2f(0, 50);
        glVertex3f(-300, -2, 300);
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

    public ControlPoint getRandomControlPoint() {
        return points[Entity.rand.nextInt(5)];
    }

    public void despawn(Entity e) {
        entities.remove(e);
    }

    public Entity getControlPoint(int cid) {
        return this.points[cid];
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