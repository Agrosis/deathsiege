package com.jantox.siege.level;

import com.jantox.siege.Camera;
import com.jantox.siege.Resources;
import com.jantox.siege.Vector3D;
import com.jantox.siege.SpawnerFactory;
import com.jantox.siege.entities.map.*;
import com.jantox.siege.entities.resources.Tree;
import com.jantox.siege.entities.tools.Bullet;
import com.jantox.siege.entities.tools.Weapons;
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
    private ArrayList<ParticleSystem> particlesys;

    private ArrayList<Quad> floors;
    public ArrayList<Quad> walls;
    public ArrayList<Quad> ramps;

    private SpawnerFactory spawnerFactory;

    private Player player;
    private Camera camera;

    private ControlPoint points[];

    public Gate gates[];

    public static ParticleSystem psys;

    private Skybox skybox;

    public Level(Player player) {
        this.player = player;
        this.camera = player.getCamera();

        this.entities = new ArrayList<Entity>();
        floors = new ArrayList<Quad>();
        floors.add(new Quad(new Vector3D(-115, 0, -115), new Vector3D(115, 0, -115), new Vector3D(115, 0, 115), new Vector3D(-115, 0, 115)));
        //floors.add(new Quad(new Vector3D(-10, 15, -10),new Vector3D(10, 15, -10),new Vector3D(10, 15, 10),new Vector3D(-10, 15, 10)));
        //floors.add(new Quad(new Vector3D(20, 3, 13), new Vector3D(20, 3, 15), new Vector3D(25, 3, 15), new Vector3D(25, 3, 13)));
        //floors.add(new Quad(new Vector3D(23, 3, 13), new Vector3D(25, 3, 13), new Vector3D(25, 3, 5), new Vector3D(23, 3, 5)));

        walls = new ArrayList<Quad>();
        //ramps.add(new Quad(new Vector3D(15, -2, 15),new Vector3D(18, -2, 15),new Vector3D(18, 1, 15),new Vector3D(15, 1, 15)));

        ramps = new ArrayList<Quad>();
        //ramps.add(new Quad(new Vector3D(1, -2, 15), new Vector3D(1, -2, 13), new Vector3D(20,1,13), new Vector3D(20, 1, 15)));

        psys = new ParticleSystem(new Vector3D(5, 0, 5));

        spawnerFactory = new SpawnerFactory(this);

        skybox = new Skybox("", 200);


    }

    public void init() {
        points = new ControlPoint[5];
        points[0] = new ControlPoint(new Vector3D(0, -1.9999999, 0));
        points[1] = new ControlPoint(new Vector3D(-40, -1.9999999, -40));
        points[2] = new ControlPoint(new Vector3D(40, -1.9999999, -40));
        points[3] = new ControlPoint(new Vector3D(40, -1.9999999, 40));
        points[4] = new ControlPoint(new Vector3D(-40, -1.9999999, 40));
        this.spawn(points[0]);
        this.spawn(points[1]);
        this.spawn(points[2]);
        this.spawn(points[3]);
        this.spawn(points[4]);

        gates = new Gate[4];
        gates[0] = new Gate(new Vector3D(-6, -2, -100), 0);
        gates[1] = new Gate(new Vector3D(-6, -2, 100), 0);
        gates[2] = new Gate(new Vector3D(100, -2, -6), 1);
        gates[3] = new Gate(new Vector3D(-100, -2, -6), 1);
        this.spawn(gates[0]);
        this.spawn(gates[1]);
        this.spawn(gates[2]);
        this.spawn(gates[3]);

        this.spawn(new Ladder(new Vector3D(-7.1, 0, -5)));
        this.spawn(new Ladder(new Vector3D(-7.1, -5, -5)));
        this.spawn(new Ladder(new Vector3D(-7.1, 5, -5)));
        this.spawn(new Ladder(new Vector3D(-7.1, 10, -5)));

        this.spawn(new Ammo(new Vector3D(15, -1.5, 15), Weapons.BLASTER, 100));

        this.spawn(new Spawner(new Vector3D(20, 2, 20)));
        this.spawn(new Spawner(new Vector3D(20, 2, -20)));
        this.spawn(new Spawner(new Vector3D(-20, 2, -20)));
        this.spawn(new Spawner(new Vector3D(-20, 2, 20)));


        for(int i = 9; i < 99; i+=3) {
            this.spawn(new Path(new Vector3D(i + 1, 0, 0)));
            if(i == 36) {
                i += 3;
            }
        }

        for(int i = -9; i > -99; i-=3) {
            this.spawn(new Path(new Vector3D(i - 1, 0, 0)));
            if(i == -36) {
                i -= 3;
            }
        }

        for(int i = 9; i < 99; i+=3) {
            this.spawn(new Path(new Vector3D(0, 0, i + 1)));
            if(i == 36) {
                i += 3;
            }
        }

        for(int i = -9; i > -99; i-=3) {
            this.spawn(new Path(new Vector3D(0, 0, i - 1)));
            if(i == -36) {
                i -= 3;
            }
        }

        for(int i = 32; i > -32; i-=3) {
            this.spawn(new Path(new Vector3D(40, 0, i)));
        }

        for(int i = 32; i > -32; i-=3) {
            this.spawn(new Path(new Vector3D(-40, 0, i)));
        }

        for(int i = 32; i > -32; i-=3) {
            this.spawn(new Path(new Vector3D(i, 0, 40)));
        }

        for(int i = 32; i > -32; i-=3) {
            this.spawn(new Path(new Vector3D(i, 0, -40)));
        }

        this.spawn(new Warehouse(new Vector3D(-40, 0, -40)));
        this.spawn(new Warehouse(new Vector3D(40, 0, -40)));
        this.spawn(new Warehouse(new Vector3D(40, 0, 40)));
        this.spawn(new Warehouse(new Vector3D(-40, 0, 40)));
        this.spawn(new Warehouse(new Vector3D(0, 0, 0)));

        /*this.spawnForest(10, new Vector3D(22.5, 0, 22.5), 10);
        this.spawnForest(10, new Vector3D(-22.5, 0, 22.5), 10);
        this.spawnForest(10, new Vector3D(-22.5, 0, -22.5), 10);
        this.spawnForest(10, new Vector3D(22.5, 0, -22.5), 10);*/

        this.spawn(new Endwek(new Vector3D(20, -1, 20), 5));
    }

    public void update(float delta) {
        spawnerFactory.update();
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.update(delta);
            if(e.isExpired()) {
                entities.remove(i);
                e = null;
            }

            if(e instanceof Bullet) {
                Ray bullet = new Ray(e.getPosition(), ((Bullet)e).getDirection());
                for(Entity x : entities) {
                    if(x instanceof Kage) {
                        Sphere z = (Sphere) x.getCollisionMask();
                        if(CollisionSystem.raySphere(bullet, z)) {
                            ((Kage) x).damage(10);
                            Vector3D bp = x.getPosition().copy();
                            bp.y += 1;
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            e.setExpired(true);
                            break;
                        }
                    } else if(x instanceof Spawner) {
                        Vector3D nz = x.getPosition().copy();
                        nz.x += 1;
                        nz.z += 0.5;
                        Sphere z = new Sphere(nz, 2);
                        if(CollisionSystem.raySphere(bullet, z)) {
                            ((Spawner) x).damage(10);
                            Vector3D bp = x.getPosition().copy();
                            bp.y -= 1;
                            bp.z += 0.5;
                            bp.x += 1;
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            psys.addParticle(new Particle(bp.copy(), new ParticleBehavior.DamageParticle()));
                            e.setExpired(true);

                            break;
                        }
                    }
                }
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
        glVertex3f(-275, -2, -275);
        glTexCoord2f(100, 0);
        glVertex3f(275, -2, -275);
        glTexCoord2f(100, 100);
        glVertex3f(275, -2, 275);
        glTexCoord2f(0, 100);
        glVertex3f(-275, -2, 275);
        glEnd();

        glPopMatrix();

        psys.render();

        for(int i = 0; i < entities.size(); i++) {
            // check if we should render it based on facing plane
            /*Vector3D to = player.pos.copy();
            to.subtract(entities.get(i).getPosition());
            to.y = 0;
            to.x = -to.x;
            to.y = -to.y;
            Vector3D cdir = camera.getDirectionVector();
            cdir.x = -cdir.x;
            cdir.z = -cdir.z;
            cdir.y = 0;
            cdir.normalize();
            to.normalize();
            if(cdir.dotProduct(to) >= 0) */
            entities.get(i).render();
        }

        glEnable(GL_TEXTURE_2D);

        // enables multitexturing for lighting
        /*ARBMultitexture.glActiveTextureARB(ARBMultitexture.GL_TEXTURE0_ARB);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(6).getTextureID());
        ARBMultitexture.glActiveTextureARB(ARBMultitexture.GL_TEXTURE1_ARB);
        glBindTexture(GL_TEXTURE_2D, Lighting.generateLightmap(new Vector3D(0, 0, -1)));

        glBegin(GL_QUADS);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE0_ARB, 0, 0);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE1_ARB, 0, 0);
        glVertex3f(0, 0, 0);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE0_ARB, 1, 0);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE1_ARB, 1, 0);
        glVertex3f(1, 0, 0);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE0_ARB, 1, 1);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE1_ARB, 1, 1);
        glVertex3f(1, 1, 0);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE0_ARB, 0, 1);
        ARBMultitexture.glMultiTexCoord2fARB(ARBMultitexture.GL_TEXTURE1_ARB, 0, 1);
        glVertex3f(0, 1, 0);
        glEnd();*/

        glColor3f(0,0,0);

        //ramps.get(0).render();
        //floors.get(2).render();
        //floors.get(3).render();

        this.player.render();


        glPopMatrix();
    }

    public void spawn(Entity e) {
        entities.add(e);
    }

    public void spawnForest(int amount, Vector3D pos, float radius) {
        for(int i = 0; i < amount; i++) {
            Vector3D npos = pos.copy();
            npos.add(new Vector3D(Entity.rand.nextGaussian() * radius, -2, Entity.rand.nextGaussian() * radius));
            this.spawn(new Tree(npos.copy()));
        }
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

}