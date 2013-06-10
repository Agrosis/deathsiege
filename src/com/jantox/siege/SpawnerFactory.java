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

    Fortress fortress;
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

    public void update() {
        if(System.currentTimeMillis() - lastms >= 1000) {
            lastms = System.currentTimeMillis();
            seconds++;
            spawn = true;
        }


    }

}
