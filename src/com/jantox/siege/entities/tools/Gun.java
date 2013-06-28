package com.jantox.siege.entities.tools;

import com.jantox.siege.entities.Entity;
import com.jantox.siege.level.Level;

public abstract class Gun extends Tool {

    protected int fullmag, curmag, space;

    public Gun(Entity owner, Level level, int f, int s) {
        super(owner, level);
        this.fullmag = f;
        this.curmag = this.space = s;
    }

    protected void use() {
        curmag--;
    }

    public int getFullMag() {
        return fullmag;
    }

    public int getCurMag() {
        return curmag;
    }

}
