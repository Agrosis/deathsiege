package com.jantox.siege;

import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Kage;
import com.jantox.siege.entities.map.Fortress;
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

    public Fortress fortress;
    int gate;

    int wave = 0;
    int tospawn = 0;
    public static int monstersleft;
    int breaktime = 0;

    public SpawnerFactory(Level l) {
        this.level = l;
        lastms = System.currentTimeMillis();
        tospawn = monstersleft = wave * 5 + 3;
    }

    int sptime = 0;

    public void update() {
        if(System.currentTimeMillis() - lastms >= 1000) {
            lastms = System.currentTimeMillis();
            seconds++;
            sptime++;
            spawn = true;
        }

        if(!fortress.isOpening(gate)) {
            fortress.open(gate);
        }

        if(fortress.isOpen(gate)) {
            if(seconds >= 3) {
                seconds = 0;
                level.spawn(new Endwek(this.getPlace(gate), 3,4));
            }

            if(sptime >= 30) {
                fortress.close(gate);
                gate = rand.nextInt(4);
                fortress.open(gate);
            }
        }
    }

    public Vector3D getPlace(int i) {
        if(i == 1) {
            return new Vector3D(198, 0, 0);
        } else if(i == 3) {
            return new Vector3D(-198, 0, 0);
        } else if(i == 2) {
            return new Vector3D(0, 0, 198);
        } else if(i == 0) {
            return new Vector3D(0, 0, -198);
        }
        return null;
    }

}
