package com.jantox.siege.entities.tools;

import com.jantox.siege.entities.Entity;
import com.jantox.siege.level.Level;

public abstract class Tool extends Entity {

    protected Entity owner;

    protected int damage;

    public Tool(Entity owner, Level level) {
        super(owner.getPosition().copy(), level);
    }

    public abstract void onUse(int mouse);

    public abstract void onRelease();

    public abstract int getRest();

}
