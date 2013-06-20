package com.jantox.siege.net;

import com.jantox.siege.Configuration;
import com.jantox.siege.Vector3D;
import com.jantox.siege.entities.Endwek;
import com.jantox.siege.entities.MultiplayerLiving;
import com.jantox.siege.entities.OnlinePlayer;
import com.jantox.siege.level.Level;

import java.io.IOException;
import java.util.ArrayList;

public class MultiplayerInstance {

    private Client client;
    private Level level;

    private ArrayList<OnlinePlayer> players;

    private int ticks = 0;

    public MultiplayerInstance(Level level) {
        this.level = level;
        this.players = new ArrayList<OnlinePlayer>();

        this.client = new Client(Configuration.getProperty("ip"), Integer.valueOf(Configuration.getProperty("port")));
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        ticks++;
        if(ticks % 5 == 0) {
            Packet pos = new Packet(Protocol.POSITION);
            pos.writeFloat((float)level.getPlayer().getPosition().x);
            pos.writeFloat((float)level.getPlayer().getPosition().y);
            pos.writeFloat((float)level.getPlayer().getPosition().z);
            pos.writeFloat(level.getPlayer().getCamera().getPitch());
            pos.writeFloat(level.getPlayer().getCamera().getYaw());

            try {
                client.write(pos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Packet p = client.read();

            if(p != null) {
                if(p.getHeader() == Protocol.PING) {
                    Packet pong = new Packet(Protocol.PONG);
                    client.write(pong);
                } else if(p.getHeader() == Protocol.NEW_CONNECTION) {
                    int uid = p.readInteger();
                    OnlinePlayer player = new OnlinePlayer(uid);
                    this.level.spawn(player);
                    this.players.add(player);
                } else if(p.getHeader() == Protocol.CONNECTION_LEFT) {
                    OnlinePlayer op = this.getPlayerWithID(p.readInteger());
                    this.players.remove(op);
                    this.level.despawn(op);
                } else if(p.getHeader() == Protocol.POSITION) {
                    int id = p.readInteger();
                    float x = p.readFloat();
                    float y = p.readFloat();
                    float z = p.readFloat();
                    float pitch = p.readFloat();
                    float yaw = p.readFloat();

                    OnlinePlayer op = this.getPlayerWithID(id);
                    op.setNextPosition(new Vector3D(x, y, z));
                    op.setOrientation(pitch, yaw);
                } else if(p.getHeader() == Protocol.GATE_OPEN) {
                    if(level != null) {
                        //level.fortress.open(p.read());
                    }
                } else if(p.getHeader() == Protocol.SPAWN) {
                    System.out.println("spawned");

                    int eid = p.readInteger();

                    int monstertype = p.read();

                    float x = p.readFloat();
                    float y = p.readFloat();
                    float z = p.readFloat();

                    if(monstertype == 0) {
                        //Endwek e = new Endwek(new Vector3D(x, y, z), p.read(), eid);
                        //level.spawn(e);
                    }
                } else if(p.getHeader() == Protocol.ENTITY_POSITION) {
                    int eid = p.readInteger();

                    float x = p.readFloat();
                    float y = p.readFloat();
                    float z = p.readFloat();

                    MultiplayerLiving ml = level.getMultiplayerObjectWith(eid);
                    if(ml != null)
                        ml.updatePosition(new Vector3D(x,y,z));
                } else if(p.getHeader() == Protocol.KILL) {
                    int eid = p.readInteger();

                    level.despawnMultiplayer(eid);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public OnlinePlayer getPlayerWithID(int id) {
        for(OnlinePlayer op : players) {
            if(op.getUserID() == id) {
                return op;
            }
        }
        return null;
    }

}
