package com.jantox.siege;

import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Kage;
import com.jantox.siege.entities.Spawner;
import com.jantox.siege.entities.map.Gate;
import com.jantox.siege.level.Level;

import java.util.Random;

public class SpawnerFactory {

    public static int ACTIVE_SPAWNERS = 0;
    public static int ACTIVE_MONSTERS = 0;

    private Level level;

    private long seconds = 0L;
    private long lastms = 0L;
    private boolean spawn = false;

    private Random rand = new Random(System.currentTimeMillis());

    int place = 0;
    Gate cg;

    public SpawnerFactory(Level l) {
        this.level = l;
        lastms = System.currentTimeMillis();
    }

    public void update() {
        if(System.currentTimeMillis() - lastms >= 1000) {
            lastms = System.currentTimeMillis();
            seconds++;
            spawn = true;
        }

        if(seconds % 0.5 == 0 && seconds != 0 && spawn) {
            spawn = false;

            if(cg != null) {
                if(cg.isOpen()) {
                    if(place == 0)
                        level.spawn(new Kage(new Vector3D(0, 0f, -100), 1, 0));
                    if(place == 1)
                        level.spawn(new Kage(new Vector3D(0, 0f, 100), 1, 0));
                    if(place == 2)
                        level.spawn(new Kage(new Vector3D(100, 0f, 0), 1, 0));
                    if(place == 3)
                        level.spawn(new Kage(new Vector3D(-100, 0f, 0), 1, 0));
                }
            }
        }
        if(seconds % 30 == 0 || cg == null) {
            if(cg != null) {
                cg.close();
            }
            place = rand.nextInt(4);
            cg = Entity.level.gates[place];
            cg.open();
        }
    }

}
