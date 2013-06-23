package com.jantox.siege.level;

import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.Entity;
import com.jantox.siege.entities.Helicopter;
import com.jantox.siege.entities.Kage;
import com.jantox.siege.entities.map.*;
import com.jantox.siege.entities.map.shop.Shop;

public class Siege extends Gamemode {

    private Fortress fortress;
    private ControlPoint points[];

    private int wave = 0;

    public static float GATE_SPEED = 0.05f;
    public static int MONSTERS_LEFT = 0;

    private int GATES_OPEN = 1;
    private int MONSTER_TYPE = 1;
    private int SPAWN_DELAY = 3;
    private int SPAWN_NUMBER = 1;

    private long lastsec;
    private long seconds;
    private long breaktime = -1;

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
        points[0] = new ControlPoint(new Vector3D(0, -1.9999999, 0));
        points[1] = new ControlPoint(new Vector3D(50, -1.9999999, -50));
        points[2] = new ControlPoint(new Vector3D(-50, -1.9999999, -50));
        points[3] = new ControlPoint(new Vector3D(-50, -1.9999999, 50));
        points[4] = new ControlPoint(new Vector3D(50, -1.9999999, 50));
        level.spawn(points[0]);
        level.spawn(points[1]);
        level.spawn(points[2]);
        level.spawn(points[3]);
        level.spawn(points[4]);

        level.spawn(new Helicopter(new Vector3D(50, 0, 50)));

        level.spawn(new Ladder(new Vector3D(-7.1, 0, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, -5, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, 5, -5)));
        level.spawn(new Ladder(new Vector3D(-7.1, 10, -5)));

        level.spawn((level.fortress = this.fortress = new Fortress()));
        spawnpoint = this.getRandomSpawnPoint();
        MONSTERS_LEFT = 5;

        for(int i = 9; i < 160; i+=5) {
            level.spawn(new Path(new Vector3D(i + 1, 0, 0)));
            if(i == 36) {
                i += 5;
            }
        }

        for(int i = -9; i > -160; i-=5) {
            level.spawn(new Path(new Vector3D(i - 1, 0, 0)));
            if(i == -36) {
                i -= 5;
            }
        }

        for(int i = 9; i < 160; i+=5) {
            level.spawn(new Path(new Vector3D(0, 0, i + 1)));
            if(i == 36) {
                i += 5;
            }
        }

        for(int i = -9; i > -160; i-=5) {
            level.spawn(new Path(new Vector3D(0, 0, i - 1)));
            if(i == -36) {
                i -= 5;
            }
        }

        for(int i = -100; i < 100; i+= 5) {
            if(i == 0) {
                i += 5;
            }
            level.spawn(new Path(new Vector3D(i, 0, 100)));
            level.spawn(new Path(new Vector3D(100, 0, i)));
            level.spawn(new Path(new Vector3D(i, 0, -100)));
            level.spawn(new Path(new Vector3D(-100, 0, i)));
        }

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
        level.spawn(new Decoration(new Vector3D(-50, 0, -50), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
        level.spawn(new Decoration(new Vector3D(50, 0, -50), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
        level.spawn(new Decoration(new Vector3D(50, 0, 50), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
        level.spawn(new Decoration(new Vector3D(-50, 0, 50), new Vector3D(0.11, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));

        level.spawn(new Decoration(new Vector3D(-80, 0, 80), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 22));
        level.spawn(new Shop(new Vector3D(105, -1, 105), Shop.SHOP.SPECIALS));
        level.spawn(new Decoration(new Vector3D(90, 0, 90), new Vector3D(0.08, 0.08, 0.08), new Vector3D(-90, 0, 0), 23));

        lastsec = System.currentTimeMillis();

        fortress.open();
    }

    public void nextWave() {
        System.out.println("Wave " + (++wave) + " is complete!");

        breaktime = 30;

        fortress.close();

        MONSTERS_LEFT = wave * 2 + 5;

        if(wave % 7 == 0) {
            SPAWN_NUMBER++;
            SPAWN_DELAY ++;
        }
        if(wave % 10 == 0) {
            //GATES_OPEN ++;
            //SPAWN_DELAY ++;
        }

        level.removeAllMonsters();
    }

    @Override
    public void update() {
        if(System.currentTimeMillis() - lastsec >= 1000) {
            lastsec = System.currentTimeMillis();
            spawned = true;

            seconds++;

            if(breaktime > -1) {
                breaktime--;
                if(breaktime == -1) {
                    fortress.open();
                    extended = false;
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
                    for(int i = 0; i < SPAWN_NUMBER; i++) {
                        if(Entity.rand.nextInt(2) % 2 != 0) {
                            level.spawn(new Kage(spawnpoint.getCloseTo(8), Entity.rand.nextInt(2), points[Entity.rand.nextInt(5)]));
                        } else {
                            level.spawn(new Endwek(spawnpoint.getCloseTo(8), points[Entity.rand.nextInt(5)], false));
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
