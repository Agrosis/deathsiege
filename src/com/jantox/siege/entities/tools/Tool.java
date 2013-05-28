package com.jantox.siege.entities.tools;

import com.jantox.siege.entities.Entity;

public abstract class Tool extends Entity {

    protected Entity owner;

    protected int damage;

    public Tool(Entity owner) {
        super(owner.getPosition().copy());
    }

    public abstract void onUse(int mouse);

    public abstract void onRelease();

    public abstract int getRest();

}
