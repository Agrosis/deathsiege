package com.jantox.siege.net;

public class Protocol {

    public static int LOGIN = 0x01;

    public static int NEW_CONNECTION = 0x02;
    public static int CONNECTION_LEFT = 0x03;

    public static int PING = 0x04;
    public static int PONG = 0x05;

    public static int POSITION = 0x06;

    public static int GATE_OPEN = 0x07;
    public static int GATE_CLOSE = 0x08;

    // values are different for each kind of enemy
    public static int SPAWN = 0x09;

}
