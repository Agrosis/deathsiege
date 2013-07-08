package com.jantox.siege.level;

import com.jantox.siege.*;
import com.jantox.siege.entities.map.*;
import com.jantox.siege.entities.map.shop.Shop;
import com.jantox.siege.entities.tools.Projectile;
import com.jantox.siege.geometry.*;
import com.jantox.siege.entities.*;
import com.jantox.siege.models.CollisionLoader;
import com.jantox.siege.models.MapCollision;
import com.jantox.siege.particle.ParticleSystem;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Level {

    private ArrayList<Entity> entities;
    private ArrayList<MultiplayerLiving> multiplayers;

    public static ArrayList<Quad> floors;
    public static ArrayList<Quad> walls;
    public static ArrayList<Quad> ramps;

    private Skybox skybox;
    private Player player;
    private Camera camera;
    public Fortress fortress;

    public static ParticleSystem psys;

    private ArrayList<Projectile> projectiles;

    private Gamemode gamemode;

    public Level() {
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


        /*Vector3D a = new Vector3D(0, 0, 0);
        Vector3D b = new Vector3D(4, 0, 0);
        Vector3D c = new Vector3D(4, 4, 0);
        Vector3D d = new Vector3D(0, 4, 0);

        Matrix4 tr = Matrix4.createRotationMatrix(90, 1);

        a = tr.multiplyVectorBy(a);
        b = tr.multiplyVectorBy(b);
        c = tr.multiplyVectorBy(c);
        d = tr.multiplyVectorBy(d);

        tr = Matrix4.createTranslationMatrix(new Vector3D(20, 0, 20));

        a = tr.multiplyVectorBy(a);
        b = tr.multiplyVectorBy(b);
        c = tr.multiplyVectorBy(c);
        d = tr.multiplyVectorBy(d);

        a.debug();b.debug();c.debug();d.debug();


        walls.add(new Quad(a,b,c,d));*/

        skybox = new Skybox("", 1400);
    }

    public void init(Player p) {
        this.player = p;
        this.camera = player.getCamera();

        this.gamemode = new Siege(this);
        gamemode.init();
        //this.spawn(new Endwek(new Vector3D(50, 0, 50), player, false));

        CollisionLoader cl = new CollisionLoader();
        Matrix4 scale = Matrix4.createScaleMatrix(new Vector3D(0.11,0.11,0.11));
        Matrix4 rotate = Matrix4.createRotationMatrix(90, 0);
        Matrix4 translate = Matrix4.createTranslationMatrix(new Vector3D(0,-2,0));
        MapCollision mc = cl.loadOBJModel("models/building2HDD.obj", translate, rotate, scale);
    }

    public void update(float delta) {
        boolean shop = false;
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

            if(e instanceof Shop) {
                if(player.getPosition().distanceSquared(e.getPosition()) <= 5*5) {
                    GameInstance.shop = (Shop) e;
                    shop = true;
                }
            }
        }

        if(shop == false) {
            GameInstance.shop = null;
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
        glColor3f(124f/255f,252f/255f,0f/255f);
        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(0).getTextureID());
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex3f(-700, -2, -700);
        glTexCoord2f(75, 0);
        glVertex3f(700, -2, -700);
        glTexCoord2f(75,75);
        glVertex3f(700, -2, 700);
        glTexCoord2f(0, 75);
        glVertex3f(-700, -2, 700);
        glEnd();

        glPopMatrix();

        glPushMatrix();

        glColor3f(0,0,0);
        //glTranslatef(20, 0, 20);
        //glRotatef(90, 0, 1, 0);
        for(Quad q : walls) {
            q.render();
            //q.debug();
        }
        glPopMatrix();


        for(int i = 0; i < entities.size(); i++) {
            entities.get(i).render();
        }

        psys.render();

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

    public Guardian getGuardian(int i) {
        return ((Siege)gamemode).getGuardian(i);
    }

    public Gamemode getGameMode() {
        return gamemode;
    }
}