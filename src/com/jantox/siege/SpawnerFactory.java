package com.jantox.siege;

import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Spawner;
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

    public SpawnerFactory(Level l) {
        this.level = l;
        lastms = System.currentTimeMillis();
    }

    int sptime = 0;
    int spawnswitch = 0;

    public void update() {
        if(System.currentTimeMillis() - lastms >= 1000) {
            lastms = System.currentTimeMillis();
            seconds++;
            spawnswitch = 0;
            if(fortress.isOpen(gate))
                sptime++;
            spawn = true;
        }

        if(seconds == 4) {
            seconds++;
            for(int i = 0; i < 30; i++) {
                level.spawn(new Endwek(new Vector3D(rand.nextInt(300) - 150, 0, rand.nextInt(300) - 150),rand.nextInt(4),4));
            }
        }

        /*if(spawnswitch >= 20) {
            level.spawn(new Spawner(new Vector3D(rand.nextInt(300) - 150, 1.7f, rand.nextInt(300) - 150)));
        }

        if(!fortress.isOpening(gate)) {
            fortress.open(gate);
        }

        if(fortress.isOpen(gate)) {
            if(seconds >= 3) {
                seconds = 0;
                level.spawn(new Endwek(this.getPlace(gate),rand.nextInt(4),4));
            }

            if(sptime >= 30) {
                sptime = 0;
                fortress.close(gate);
                gate = rand.nextInt(4);
                fortress.open(gate);
            }
        }*/
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
