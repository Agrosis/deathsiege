package com.jantox.siege.entities;

import com.jantox.siege.Vector3D;
import com.jantox.siege.level.Level;
import com.jantox.siege.net.Interpolation;

public abstract class MultiplayerLiving extends Living {

    protected long entityid;
    protected Vector3D prevpos;
    protected Vector3D nextpos;

    protected double postick;

    public MultiplayerLiving(Vector3D pos, Level level, int health, long eid) {
        super(pos, level, health);
        this.entityid = eid;

        prevpos = nextpos = new Vector3D();
    }

    public int getEntityID() {
        return (int)entityid;
    }

    public void setEntityID(int eid) {
        this.entityid = eid;
    }

    public void updateMultiplayer() {
        if(this.health <= 0)
            this.expired = true;

        postick++;
        if(nextpos.x != prevpos.x && nextpos.z != prevpos.z) {
            double disp = nextpos.x - prevpos.x;
            disp /= 5;
            disp *= postick;

            this.pos.x = prevpos.x + disp;
            this.pos.z = Interpolation.linear_interpolate(prevpos, nextpos, prevpos.x + disp);
        }
    }

    public void updatePosition(Vector3D np) {
        this.prevpos = nextpos.copy();
        nextpos = np.copy();
        postick = 0;
    }

}
