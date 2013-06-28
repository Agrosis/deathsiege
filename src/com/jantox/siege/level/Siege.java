package com.jantox.siege.level;

import com.jantox.siege.GameInstance;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.*;
import com.jantox.siege.entities.map.*;
import com.jantox.siege.entities.map.shop.Shop;
import com.jantox.siege.sfx.AudioController;

public class Siege extends Gamemode {

    private Fortress fortress;
    private ControlPoint points[];

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

        points = new ControlPoint[5];
    }

    public int getWave() {
        return wave;
    }

    @Override
    public void init() {
        points[0] = new ControlPoint(new Vector3D(0, -1.9999999, 0), level);
        points[1] = new ControlPoint(new Vector3D(70, -1.9999999, -70), level);
        points[2] = new ControlPoint(new Vector3D(-70, -1.9999999, -70), level);
        points[3] = new ControlPoint(new Vector3D(-70, -1.9999999, 70), level);
        points[4] = new ControlPoint(new Vector3D(70, -1.9999999, 70), level);

        level.spawn(new Spawner(new Vector3D(70, -16, -70), level));
        level.spawn(new Spawner(new Vector3D(-70, -16, -70), level));
        level.spawn(new Spawner(new Vector3D(70, -16, 70), level));
        level.spawn(new Spawner(new Vector3D(-70, -16, 70), level));

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

        level.spawn(new Helicopter(new Vector3D(50, 0, 50), level));

        level.spawn(new Ladder(new Vector3D(-7.1, 0, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, -5, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, 5, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, 10, -5)));

        level.spawn((level.fortress = this.fortress = new Fortress(level)));
        spawnpoint = this.getRandomSpawnPoint();
        MONSTERS_LEFT = 5;

        /*for(int i = 9; i < 160; i+=9) {
            level.spawn(new Path(new Vector3D(i + 9, 0, 0)));
        }

        for(int i = -9; i > -160; i-=9) {
            level.spawn(new Path(new Vector3D(i - 9, 0, 0)));
        }

        for(int i = 9; i < 160; i+=9) {
            level.spawn(new Path(new Vector3D(0, 0, i + 5)));
        }

        for(int i = -9; i > -160; i-=9) {
            level.spawn(new Path(new Vector3D(0, 0, i - 5)));
        }

        /*for(int i = -100; i < 100; i+= 5) {
            if(i == 0) {
                i += 5;
            }
            level.spawn(new Path(new Vector3D(i, 0, 100)));
            level.spawn(new Path(new Vector3D(100, 0, i)));
            level.spawn(new Path(new Vector3D(i, 0, -100)));
            level.spawn(new Path(new Vector3D(-100, 0, i)));
        }*/

        /*for(int i = 32; i > -32; i-=5) {
            level.spawn(new Path(new Vector3D(40, 0, i)));
        }

        for(int i = 32; i > -32; i-=5) {
            level.spawn(new Path(new Vector3D(-40, 0, i)));
        }

        for(int i = 32; i > -32; i-=5) {
            level.spawn(new Path(new Vector3D(i, 0, 40)));
        }

        for(int i = 32; i > -32; i-=5) {
            level.spawn(new Path(new Vector3D(i, 0, -40)));
        }*/

        level.spawn(new Decoration(new Vector3D(0, 0, 0), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));

        level.spawn(new Decoration(new Vector3D(-100, 0, 100), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 22));
        level.spawn(new Shop(new Vector3D(-105, -1, 105), level, Shop.SHOP.WEAPONS));

        //level.spawn(new Shop(new Vector3D(105, -1, 105), level, Shop.SHOP.SPECIALS));
        //level.spawn(new Decoration(new Vector3D(90, 0, 90), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 23));

        lastsec = System.currentTimeMillis();

        fortress.open();
    }

    public void nextWave() {
        wave++;

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
                            level.spawn(new Kage(spawnpoint.getCloseTo(8), level, Entity.rand.nextInt(2), points[Entity.rand.nextInt(5)]));
                        } else {
                            level.spawn(new Endwek(spawnpoint.getCloseTo(8), level, points[Entity.rand.nextInt(5)], false));
                        }
                    }
                }
            }

            if(MONSTERS_LEFT <= 0) {
                nextWave();
            }
        }
    }

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
}
