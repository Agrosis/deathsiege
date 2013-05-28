package com.jantox.siege.entities;

import com.jantox.siege.Camera;
import com.jantox.siege.Input;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.map.Ladder;
import com.jantox.siege.entities.tools.*;
import com.jantox.siege.geometry.AABB;
import com.jantox.siege.geometry.CollisionSystem;
import com.jantox.siege.geometry.Quad;
import com.jantox.siege.geometry.Sphere;
import com.jantox.siege.level.Level;
import org.lwjgl.opengl.GL11;

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

    private boolean running = false;
    private boolean using = false;

    public static double move = 0;

    public Player(Camera camera) {
        super(camera.getCamera(), 100);
        this.camera = camera;

        inventory = new ArrayList<Tool>();

        inventory.add(new Woodaxe(this));
        inventory.add(new Blaster(this));
        inventory.add(new TwinBlaster(this));
        inventory.add(new Crossbow(this));
        inventory.add(new SentryGunItem(this));

        curwep = inventory.get(selected);

        this.mask = new Sphere(pos, 1f);

        gravity = -0.05f;
    }


    @Override
    public void update(float delta) {
        this.pos = camera.getCamera();
        this.mask.update(this.pos);

        if(Input.w || Input.a || Input.s || Input.d) {
            move += 10f;
        }

        if(cursel != Input.curnum-1) {
            if(Input.curnum-1 < inventory.size() && change == 0) {
                change = 1;
                changestat = 0;
            }
        }

        if(weaponRest == 0) {
            if(Input.lmouse) {
                curwep.onUse(0);
                weaponRest = curwep.getRest();
                using = true;
            }
            if(Input.rmouse) {
                curwep.onUse(1);
                weaponRest = curwep.getRest();
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
            } else if(e instanceof Ammo) {
                Ammo a = (Ammo) e;
                if(curwep instanceof Weapon) {
                    if(a.getWeaponType() == ((Weapon)curwep).getWeaponType()) {
                        if(CollisionSystem.sphereSphere(new Sphere(pos.copy(), 2), (Sphere)e.getCollisionMask())) {
                            ((Weapon)curwep).giveAmmo(a.getAmount());
                            e.setExpired(true);
                        }
                    }
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
        boolean rampcheck = false;
        for(Quad ra : level.rramps) {
            if((CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2.05f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                rampcheck = true;
            }
        }

        Quad floor = null;

        if(rampcheck == false) {
        ArrayList<Quad> floors = level.getFloors();
        boolean b = false;
        for(Quad f : floors) {
            if(CollisionSystem.sphereQuad(new Sphere(camera.camera.copy(), 0.4f), f.a, f.b, f.c, f.d, new Vector3D(0, 1, 0)) != null) {
                b= true;
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
                    camera.camera.y = floor.a.y;

                    if(Input.space && gravity == 0) {
                        gravity = 0.41f;
                        camera.camera.y += gravity;
                    }
                } else {
                    if(!rampcheck) {
                        camera.camera.y += gravity;

                        if(gravity != 0) {
                            gravity -= 0.025f;
                            if(gravity < -0.5f) {
                                gravity = -0.5f;
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

        for(Quad ra : level.rramps) {
            Vector3D a = null;
            if((a = CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2.05f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                camera.camera = a.copy();
            }
        }

        this.updateMask();

        /*ArrayList<Quad> ramps = level.getRamps();
        for(Quad ra : ramps) {
            Vector3D a;
            if((a = CollisionSystem.sphereQuad(new Sphere(this.pos, 2f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                camera.camera = a.copy();
            }
        }

        this.updateMask();
*/
        /*if(rcheck == false) {
            ArrayList<Quad> floors = level.getFloors();
            Quad floor = null;
            for(Quad f : floors) {
                if(CollisionSystem.sphereQuad((Sphere)this.getCollisionMask(), f.a, f.b, f.c, f.d, new Vector3D(0, 1, 0)) != null) {
                    floor = f;
                    break;
                }
            }

            if(floor != null) {
                if(gravity != 0) {
                    gravity = 0;
                }
                camera.camera.y = floor.a.y;

                if(Input.space && gravity == 0) {
                    gravity = 0.35f;
                    camera.camera.y += gravity;
                }
            } else {
                camera.camera.y += gravity;

                if(gravity != 0) {
                    gravity -= 0.02f;
                    if(gravity < -0.5f) {
                        gravity = -0.5f;
                    }
                } else {
                    gravity = -0.05f;
                    camera.camera.y += gravity;
                }
            }
        }
        this.updateMask();*/

            /*if(rcheck == false) {
                ArrayList<Quad> floors = level.getFloors();
                Quad floor = null;
                for(Quad f : floors) {
                    if(CollisionSystem.sphereQuad((Sphere)this.getCollisionMask(), f.a, f.b, f.c, f.d, new Vector3D(0, 1, 0)) != null) {
                        floor = f;
                        break;
                    }
                }



                if(floor != null) {
                    if(gravity != 0) {
                        gravity = 0;
                    }
                    camera.camera.y = floor.a.y;

                    if(Input.space && gravity == 0) {
                        gravity = 0.35f;
                        camera.camera.y += gravity;
                    }
                } else {
                    camera.camera.y += gravity;

                    if(gravity != 0) {
                        gravity -= 0.02f;
                        if(gravity < -0.5f) {
                            gravity = -0.5f;
                        }
                    } else {
                        gravity = -0.05f;
                        camera.camera.y += gravity;
                    }
                }
            }*/
        //this.updateMask();

        /*System.out.println("START");

        boolean rampcheck = false;
        Vector3D a = null;
        for(Quad ra : level.rramps) {
            if((a = CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                rampcheck = true;
                System.out.println("ramp check is true");
            }
        }

        this.updateMask();

        if(!rampcheck) {
            camera.camera.y -= 0.02;
            if(camera.camera.y < 0.5) {
                camera.camera.y = 0.5;
            }
        }

        this.updateMask();

        rampcheck = false;
        for(Quad ra : level.rramps) {
            if((a = CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                rampcheck = true;
            }
        }
        if(rampcheck == false) {
            a = null;
        }

        if(a != null) {
            camera.camera = a.copy();
            is = true;
        } else {
            if(is)
                System.out.println("NOPE!");
            is = false;
        }

        this.updateMask();

        System.out.println("END");*/

        // ramp collisions
        // first, check if you are intersecting with any ramps;

        /*System.out.println("STARTING");

        Vector3D inter = null;
        boolean rampcheck = false;
        Quad ramp = null;
        for(Quad ra : level.rramps) {
            if((inter = CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2.05f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                rampcheck = true;
                ramp = ra;
                System.out.println("ramp check is true");
            }
        }

        this.updateMask();

        // if you aren't and your y is not 0.5, you are in air
        if(rampcheck == false) {
            System.out.println("Ramp check is false!");
            /*camera.camera.y -= 0.05f;
            if(camera.camera.y < 2) {
                camera.camera.y = 2;
            //}
            camera.camera.y -= 0.05f;
            if(camera.camera.y < 2) {
                camera.camera.y = 2;
            }

            for(Quad ra : level.rramps) {
                if((inter = CollisionSystem.sphereRamp(new Sphere(camera.camera.copy(), 2.05f), ra.a, ra.b, ra.c, ra.d, ra.getNormal())) != null) {
                    camera.camera = inter.copy();
                    System.out.println("Resolved ramp!");
                }
            }
        } else {
            camera.camera = inter.copy();
            System.out.println("Adjusting position...");
        }

        this.updateMask();

        System.out.println("ENDING");*/
    }

    boolean is = false;

    @Override
    public void render() {
        GL11.glPushMatrix();
        if(!using && !(curwep instanceof SentryGunItem))
            GL11.glTranslatef(0, changestat + (float)Math.sin(Math.toRadians(move))/25, 0);
        //else
          //GL11.glTranslatef(0, changestat + (float)Math.sin(Math.toRadians(move))/25, 0);
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
}
