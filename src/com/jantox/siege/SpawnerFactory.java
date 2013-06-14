package com.jantox.siege;

import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Kage;
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

    public static int monstersleft = 0;

    int sptime = 0;
    int wave = 0;

    int mspawn = 0;

    int breaktime = -1;

    public SpawnerFactory(Level l) {
        this.level = l;
        lastms = System.currentTimeMillis();

        gate = 0;

        this.nextWave();
    }

    public void nextWave() {
        wave++;
        monstersleft = wave * 5 + 2;

        gate = rand.nextInt(3);

        for(int i = 0; i < wave; i++) {
            level.spawn(new Spawner(new Vector3D(140 - rand.nextInt(525), 1.7f, 140 - rand.nextInt(280))));
        }
    }

    public void update() {
        if(System.currentTimeMillis() - lastms >= 1000) {
            lastms = System.currentTimeMillis();
            seconds++;
            mspawn ++;
            if(fortress.isOpen(gate))
                sptime++;
            spawn = true;

            if(breaktime > 0) {
                breaktime --;
            }
            if(breaktime == 0) {
                this.nextWave();
                breaktime = -1;
            }
        }

        if(monstersleft <= 0 && breaktime == -1) {
            breaktime = 30;
            System.out.println("Wave " + wave + " is complete!");
            System.out.println("Break time on!");
            fortress.close(gate);
        }

        if(breaktime == -1) {
            if(!fortress.isOpening(gate)) {
                fortress.open(gate);
            }

            if(fortress.isOpen(gate)) {
                if(seconds == 20) {
                    seconds = 0;
                }

                if(mspawn >= 5) {
                    mspawn = 0;
                    if(rand.nextInt() % 2 == 0)
                        level.spawn(new Endwek(this.getPlace(gate),rand.nextInt(5),0));
                    else
                        level.spawn(new Kage(this.getPlace(gate),rand.nextInt(5),1));
                }

                if(sptime >= 30) {
                    sptime = 0;
                    fortress.close(gate);
                    gate = rand.nextInt(3);
                    fortress.open(gate);
                }
            }
        }
    }

    public Vector3D getPlace(int i) {
        if(i == 0) {
            return new Vector3D(180, 0, 0);
        }  else if(i == 1) {
            return new Vector3D(180, 0, 77);
        } else if(i == 2) {
            return new Vector3D(180, 0, -77);
        }
        return null;
    }

}
