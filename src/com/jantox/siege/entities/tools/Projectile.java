package com.jantox.siege.entities.tools;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Kage;
import com.jantox.siege.entities.Living;
import com.jantox.siege.geometry.CollisionSystem;
import com.jantox.siege.geometry.Ray;
import com.jantox.siege.geometry.Sphere;

import java.util.ArrayList;

public class Projectile {

    public static int SHOTGUN = 0;
    public static int SNIPER = 1;

    private Vector3D src, dir;

    private int damage;
    private float range;

    public boolean expired = false;

    public Projectile(int type, Vector3D src, Vector3D dir) {
        this.src = src;
        this.dir = dir;
        if(type == SHOTGUN) {
            damage = 800;
            range = 20;
        } else if(type == SNIPER) {
            range = 500;
            damage = 10000;
        }
    }

    public void use(ArrayList<Entity> entities) {
        expired = true;
        Living res = null;
        double dist = 1000000000;

        Ray bullet = new Ray(src.copy(), dir.copy());
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if(e instanceof Endwek || e instanceof Kage) {
                Living l = (Living) e;

                Vector3D cr = new Vector3D();
                if(CollisionSystem.raySphere(bullet, (Sphere)l.getCollisionMask(), cr)) {
                    if(cr.x < dist) {
                        dist = cr.x;
                        res = l;
                    }
                    if(cr.y < dist) {
                        dist = cr.y;
                        res = l;
                    }
                }

                if(e instanceof Endwek) {
                    if(((Endwek)e).isHeadshot(bullet)) {
                        GameInstance.audio.playSound(5);
                        l.damage(1000);
                    }
                }
            }
        }

        if(dist <= range * 2)
            if(res != null) {
                if((int)dist == 0)
                    dist = 1;
                res.damage(damage/(int)dist);
            }
    }

}
