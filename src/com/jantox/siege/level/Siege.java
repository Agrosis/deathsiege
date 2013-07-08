package com.jantox.siege.level;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.*;
import com.jantox.siege.entities.map.*;
import com.jantox.siege.entities.map.shop.Shop;
import com.jantox.siege.gfx.Notification;

public class Siege extends Gamemode {

    private Fortress fortress;
    private Guardian points[];

    public static int wave = 0;
    public static int MONSTERS_LEFT = 0;

    private long lastsec;
    private long seconds;
    private long breaktime = -1;

    private boolean bosswave = false;
    private Vector3D spawnpoint;
    private boolean spawned = false;

    private boolean extended = false;

    public Siege(Level level) {
        super(level);

        points = new Guardian[4];
    }

    public int getWave() {
        return wave;
    }

    @Override
    public void init() {
        points[0] = new Guardian(new Vector3D(70, -16, -70), level);
        points[1] = new Guardian(new Vector3D(-70, -16, -70), level);
        points[2] = new Guardian(new Vector3D(-70, -16, 70), level);
        points[3] = new Guardian(new Vector3D(70, -16, 70), level);

        level.spawn(points[0]);
        level.spawn(points[1]);
        level.spawn(points[2]);
        level.spawn(points[3]);

        /*level.spawn(new Path(new Vector3D(50, 0, -50)));
        level.spawn(new Path(new Vector3D(-50, 0, -50)));
        level.spawn(new Path(new Vector3D(50, 0, 50)));
        level.spawn(new Path(new Vector3D(-50, 0, 50)));
        */

        level.spawn(new Path(new Vector3D(70, 0, 70), new Vector3D(20, 20, 20)));
        level.spawn(new Path(new Vector3D(-70, 0, 70), new Vector3D(20, 20, 20)));
        level.spawn(new Path(new Vector3D(-70, 0, -70), new Vector3D(20, 20, 20)));
        level.spawn(new Path(new Vector3D(70, 0, -70), new Vector3D(20, 20, 20)));

        for(int i = -60; i < 60; i+=10) {
            level.spawn(new Path(new Vector3D(65, 0, i), new Vector3D(10, 10, 10)));
            level.spawn(new Path(new Vector3D(-75, 0, i), new Vector3D(10, 10, 10)));
        }

        for(int i = -60; i < 60; i+=10) {
            level.spawn(new Path(new Vector3D(i, 0, 65), new Vector3D(10, 10, 10)));
            level.spawn(new Path(new Vector3D(i, 0, -75), new Vector3D(10, 10, 10)));
        }

        //level.spawn(points[0]);
        //level.spawn(points[1]);
       // level.spawn(points[2]);
        //level.spawn(points[3]);
        //level.spawn(points[4]);

        Catapult cp = new Catapult(new Vector3D(20, -1, 20), level);
        level.spawn(cp);

        //level.getPlayer().getCamera().setFocus(cp);

        level.spawn(new Ladder(new Vector3D(-7.1, 0, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, -5, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, 5, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, 10, -5)));

        level.spawn(new Endwek(new Vector3D(20, 0, 20), level, points[0], false));

        level.spawn((level.fortress = this.fortress = new Fortress(level)));
        spawnpoint = this.getRandomSpawnPoint();
        MONSTERS_LEFT = 5;

        level.spawn(new Decoration(new Vector3D(0, 0, 0), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));

        level.spawn(new Decoration(new Vector3D(-100, 0, 100), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 22));
        level.spawn(new Shop(new Vector3D(-105, -1, 105), level, Shop.SHOP.WEAPONS));

        level.spawn(new Shop(new Vector3D(0, -1, -10), level, Shop.SHOP.SPECIALS)); // 125, 125
        level.spawn(new Decoration(new Vector3D(110, 0.001, 110), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 23));

        lastsec = System.currentTimeMillis();

        fortress.open();
    }

    public void nextWave() {
        wave++;

        if(wave == 1) {
            GameInstance.gamejolt.addTrophy(2464);
        } else if(wave == 2) {
            GameInstance.notifications.add(new Notification("Weapons Shop Available!", "You can now buy weapons!", 600));
        } else if(wave == 4) {
            GameInstance.notifications.add(new Notification("Specials Shop Available!", "You can now buy special items!", 600));
        }


        fortress.close();
        level.removeAllMonsters();
        breaktime = 30;

        if(wave % 5 == 0 && wave != 0) {
            MONSTERS_LEFT = (int) Math.ceil(wave / 10d) * 10;
            bosswave = true;
        } else {
            MONSTERS_LEFT = wave * 2 + 5;
        }
    }

    @Override
    public void update() {
        if(System.currentTimeMillis() - lastsec >= 1000) {
            lastsec = System.currentTimeMillis();
            spawned = true;

            seconds++;
            colors = true;

            if(breaktime > -1) {
                breaktime--;
                if(breaktime <= 5 && breaktime > -1) {
                    GameInstance.audio.playSound(10);
                }
                if(breaktime == -1) {
                    fortress.open();
                    extended = false;
                    GameInstance.audio.playSound(11);
                }
            }
        }

        if(breaktime == -1) {
            if(seconds >= 30) {
                seconds = 0;
                this.spawnpoint = this.getRandomSpawnPoint();
            }

            if(fortress.isOpen() && MONSTERS_LEFT > 0) {
                if(seconds % 3 == 0 && spawned) {
                    spawned = false;
                    for(int i = 0; i < 1; i++) {
                        if(Entity.rand.nextInt(2) % 2 != 0) {
                            level.spawn(new Kage(spawnpoint.getCloseTo(8), level, Entity.rand.nextInt(2), points[Entity.rand.nextInt(4)]));
                        } else {
                            level.spawn(new Endwek(spawnpoint.getCloseTo(8), level, points[Entity.rand.nextInt(4)], false));
                        }
                    }
                }
            }

            if(MONSTERS_LEFT <= 0) {
                nextWave();
            }
        }

        if(seconds % 1 == 0 && colors) {
            colors = false;
            //level.spawn(new Fury(new Vector3D(Entity.rand.nextInt(200)-100, 2, Entity.rand.nextInt(200)-100), level, new Vector3D(Entity.rand.nextGaussian(), 1.7f, Entity.rand.nextGaussian())));
        }
    }

    boolean colors = true;

    public Vector3D getRandomSpawnPoint() {
        int r = Entity.rand.nextInt(4);

        if(r == 1) {
            return new Vector3D(220, 0, 0);
        } else if(r == 3) {
            return new Vector3D(-220, 0, 0);
        } else if(r == 2) {
            return new Vector3D(0, 0, 220);
        } else if(r == 0) {
            return new Vector3D(0, 0, -220);
        }
        return null;
    }

    public float getBreakTime() {
        return breaktime;
    }

    public void addBreakTime(int i) {
        breaktime += i;
        extended = true;
    }

    public boolean canExtendTime() {
        return breaktime > -1 && extended == false;
    }

    public int getEnemiesLeft() {
        return MONSTERS_LEFT;
    }

    public Guardian getGuardian(int i) {
        return points[i];
    }
}
