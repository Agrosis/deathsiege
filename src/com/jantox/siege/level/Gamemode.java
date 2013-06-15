package com.jantox.siege.level;

public abstract class Gamemode {

    protected Level level;

    public Gamemode(Level level) {
        this.level = level;
    }

    public abstract void init();
    public abstract void update();

}
