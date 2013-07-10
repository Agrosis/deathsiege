package com.jantox.siege.entities;

import com.jantox.siege.*;
import com.jantox.siege.entities.map.Ladder;
import com.jantox.siege.entities.tools.*;
import com.jantox.siege.geometry.AABB;
import com.jantox.siege.geometry.CollisionSystem;
import com.jantox.siege.geometry.Quad;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

public class Player extends Living {

    private Camera camera;

    private ArrayList<Tool> inventory;
    private int selected = 0;

    public Tool curwep;
    private int cursel = 0;

    private int change = 0;
    public static float changestat = 0;

    int weaponRest = 0;

    public float stamina = 300;

    private boolean running = false;
    private boolean using = false;

    public static double move = 0;

    public Player(Camera camera, Level level) {
        super(camera.getCamera(), level, 100);
        this.camera = camera;

        inventory = new ArrayList<Tool>();
        inventory.add(new Fists(this, level));
        inventory.add(new Battleaxe(this));
        //inventory.add(new Crossbow(this, level));
        inventory.add(new Minigun(this, level));
        inventory.add(new Sniper(this, level));

        curwep = inventory.get(selected);

        this.mask = new Sphere(pos, 1.7f);
        gravity = -0.05f;
    }

    public void addItem(Tool t) {
        inventory.add(t);
    }

    @Override
    public void update(float delta) {
        this.pos = camera.getCamera();
        this.mask.update(this.pos);

        boolean m = false;

        move += 10f;

        if(Input.w || Input.a || Input.s || Input.d) {
            m = true;
        }

        if(cursel != Input.curnum-1) {
            if(Input.curnum-1 < inventory.size() && change == 0) {
                change = 1;
                changestat = 0;
                GameInstance.audio.playSound(6);
            }
        }

        if(cursel == 0) {
            ix = iy = 0;
        } else if(cursel == 1) {
            ix = 1;
            iy = 0;
        } else if(cursel == 2) {
            ix = 0;
            iy = 1;
        } else if(cursel == 3) {
            ix = iy = 1;
        }

        if(weaponRest == 0) {
            if(Input.lmouse) {
                curwep.onUse(0);
                weaponRest = curwep.getRest();
                using = true;
            }
            if(Input.rmouse) {
                curwep.onUse(1);
                //weaponRest = curwep.getRest();
                using = true;
            }
            if(using) {
                if(!Input.lmouse && !Input.rmouse) {
                    curwep.onRelease();
                    using = false;
                }
            }
        }

        curwep.update(delta);

        ArrayList<Entity> entities = level.getEntities();
        Ladder r = null;
        for(Entity e: entities) {
            if(e instanceof Ladder) {
                if(CollisionSystem.sphereAABB((Sphere)this.mask, (AABB)e.getCollisionMask())) {
                    r = (Ladder) e;
                    break;
                }
            }
        }

        if(weaponRest > 0)
            weaponRest --;

        if(change == 1) {
            changestat -= 0.1f;
            if(changestat <= -1) {
                change = 2;
                changestat = -1;
                if(Input.curnum > inventory.size())
                    Input.curnum = inventory.size();
                curwep = inventory.get(Input.curnum-1);
                cursel = Input.curnum-1;
            }
        } else if(change == 2) {
            changestat += 0.1f;
            if(changestat >= 0) {
                changestat = 0;
                change = 0;
            }
        }
    }

    public void updatePosition() {
        if(!camera.isThirdPerson()) {
            boolean rampcheck = false;
            for(Quad ra : level.ramps) {
                if((CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                    rampcheck = true;
                }
            }

            Quad floor = null;

            if(rampcheck == false) {
                ArrayList<Quad> floors = level.getFloors();
                for(Quad f : floors) {
                    if(CollisionSystem.sphereQuad(new Sphere(camera.camera.copy(), 1f), f.a, f.b, f.c, f.d, new Vector3D(0, 1, 0)) != null) {
                        floor = f;
                        break;
                    }
                }
            }

            ArrayList<Entity> entities = level.getEntities();
            Ladder r = null;
            for(Entity e: entities) {
                if(e instanceof Ladder) {
                    if(CollisionSystem.sphereAABB((Sphere)this.mask, (AABB)e.getCollisionMask())) {
                        r = (Ladder) e;
                        break;
                    }
                }
            }

            if(r == null) {
                if(floor != null) {
                    if(gravity != 0) {
                        gravity = 0;
                    }
                    camera.camera.y = floor.a.y + 0.75;

                    if(Input.space && gravity == 0) {
                        gravity = 0.41f;
                        camera.camera.y += gravity;
                    }
                } else {
                    if(!rampcheck) {
                        camera.camera.y += gravity;

                        if(gravity != 0) {
                            gravity -= 0.025f;
                            if(gravity < -0.65f) {
                                gravity = -0.65f;
                            }
                        } else {
                            gravity = -0.05f;
                            camera.camera.y += gravity;
                        }
                    }
                }
            } else {
                if(Input.space) {
                    camera.camera.y += 0.2;
                } else if(Input.shift) {
                    camera.camera.y -= 0.2;
                }
            }

            this.updateMask();

            for(Quad ra : level.ramps) {
                Vector3D a = null;
                if((a = CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                    camera.camera = a.copy();
                }
            }

            this.updateMask();

            // walls code
            ArrayList<Quad> ramps = level.getWalls();
            Vector3D vel = new Vector3D();
            for(Quad ra : ramps) {
                Vector3D a;
                if((a = CollisionSystem.sphereQuad(new Sphere(this.pos, 2f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                    //camera.camera = a.copy();
                    Vector3D xv = a.copy();
                    xv.subtract(camera.camera);

                    vel.add(xv);
                }
            }
            camera.camera.add(vel);
        }
    }

    boolean is = false;

    @Override
    public void render() {
        GL11.glPushMatrix();
        if(!using && !(curwep instanceof SentryGunItem))
            GL11.glTranslatef(0, changestat + (float)Math.sin(Math.toRadians(move))/25, 0);
        //else
        //GL11.glTranslatef(0, changestat + (float)Math.sin(Math.toRadians(move))/25, 0);
        if(!camera.isThirdPerson())
            curwep.render();
        GL11.glPopMatrix();
    }

    public Camera getCamera() {
        return camera;
    }

    public void updateMask() {
        this.pos = camera.getCamera();
        this.mask.update(this.pos);
    }

    public Tool getTool() {
        return this.curwep;
    }

    int ix = 0, iy = 0;

    public void renderInventory() {
        if(Input.up) {
            iy --;
            if(iy < 0)
                iy = 0;

            if(ix == 0 && iy == 0) {
                Input.curnum = 1;
            } else if(ix == 1 && iy == 0) {
                Input.curnum = 2;
            } else if(ix == 1 && iy == 1) {
                Input.curnum = 4;
            } else if(ix == 0 && iy == 1) {
                Input.curnum = 3;
            }
        }
        if(Input.down) {
            iy ++;
            if(iy > 1)
                iy = 1;

            if(ix == 0 && iy == 0) {
                Input.curnum = 1;
            } else if(ix == 1 && iy == 0) {
                Input.curnum = 2;
            } else if(ix == 1 && iy == 1) {
                Input.curnum = 4;
            } else if(ix == 0 && iy == 1) {
                Input.curnum = 3;
            }
        }
        if(Input.left) {
            ix --;
            if(ix < 0)
                ix = 0;

            if(ix == 0 && iy == 0) {
                Input.curnum = 1;
            } else if(ix == 1 && iy == 0) {
                Input.curnum = 2;
            } else if(ix == 1 && iy == 1) {
                Input.curnum = 4;
            } else if(ix == 0 && iy == 1) {
                Input.curnum = 3;
            }
        }
        if(Input.right) {
            ix++;
            if(ix > 1)
                ix = 1;

            if(ix == 0 && iy == 0) {
                Input.curnum = 1;
            } else if(ix == 1 && iy == 0) {
                Input.curnum = 2;
            } else if(ix == 1 && iy == 1) {
                Input.curnum = 4;
            } else if(ix == 0 && iy == 1) {
                Input.curnum = 3;
            }
        }

        glEnable(GL_TEXTURE_2D);
        glColor3f(1,1,1);
        GL11.glTranslatef(986-15, 15, 0);

        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(10).getTextureID());

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);
        glTexCoord2f(80/128f, 0);
        glVertex2f(80, 0);
        glTexCoord2f(80/128f, 80/128f);
        glVertex2f(80, 80);
        glTexCoord2f(0, 80/128f);
        glVertex2f(0, 80);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, Resources.getTexture(11).getTextureID());

        if(ix == 1) {
            glTranslatef(40, 0, 0);
        }
        if(iy == 1) {
            glTranslatef(0, 40, 0);
        }

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);
        glTexCoord2f(40/64f, 0);
        glVertex2f(40, 0);
        glTexCoord2f(40/64f, 40/64f);
        glVertex2f(40, 40);
        glTexCoord2f(0, 40/64f);
        glVertex2f(0, 40);
        glEnd();
    }
}