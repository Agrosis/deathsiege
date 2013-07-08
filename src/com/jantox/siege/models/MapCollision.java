package com.jantox.siege.models;

import com.jantox.siege.Vector3D;
import com.jantox.siege.geometry.Quad;
import com.jantox.siege.level.Level;

import java.util.ArrayList;

public class MapCollision {

    private ArrayList<Quad> floors;
    private ArrayList<Quad> walls;
    private ArrayList<Quad> ramps;

    public MapCollision() {
        floors = new ArrayList<Quad>();
        walls = new ArrayList<Quad>();
        ramps = new ArrayList<Quad>();
    }

    public void addPlane(Quad q) {
        /*Vector3D qn = q.getNormal();
        if(qn.x == 1 || qn.x == -1 || qn.z == 1 || qn.z == -1)
            Level.walls.add(q);
        else if(qn.y == 1 || qn.y == -1)
            Level.floors.add(q);
        else
            Level.ramps.add(q);*/
        Level.walls.add(q);
    }

    public ArrayList<Quad> getFloors() {
        return floors;
    }

    public ArrayList<Quad> getWalls() {
        return walls;
    }

    public ArrayList<Quad> getRamps() {
        return ramps;
    }

}
