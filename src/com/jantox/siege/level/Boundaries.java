package com.jantox.siege.level;

import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Helicopter;
import com.jantox.siege.entities.map.Decoration;
import com.jantox.siege.entities.map.Fortress;

public class Boundaries extends Gamemode {

    public Boundaries(Level level) {
        super(level);
    }

    @Override
    public void init() {
        level.spawn(new Helicopter(new Vector3D(100, 4, -100)));

        level.spawn((level.fortress = new Fortress()));

        level.spawn(new Decoration(new Vector3D(-400, 0, 0), new Vector3D(0.3, 0.11, 0.11), new Vector3D(-90, 0, 0), 2));
    }

    @Override
    public void update() {

    }
}
