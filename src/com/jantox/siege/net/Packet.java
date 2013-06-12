package com.jantox.siege.net;

import java.nio.ByteBuffer;

public class Packet {

    public static final int MAX_PACKET_SIZE = 256;

    private ByteBuffer buffer;
    private int size;

    public Packet(int header) {
        buffer = ByteBuffer.allocate(Packet.MAX_PACKET_SIZE);
        buffer.clear();
        buffer.put((byte)0x00);
        buffer.put((byte)header);
        size++;
    }

    public Packet(ByteBuffer buffer, int size) {
        this.buffer = buffer;
        this.size = size;
        this.buffer.get(); // increment header
    }

    public int getHeader() {
        return buffer.get(0);
    }

    public ByteBuffer getData() {
        buffer.put(0, (byte)this.getSize());
        buffer.flip();
        return buffer;
    }

    public void write(byte b) {
        buffer.put(b);
        size++;
    }

    public byte read() {
        return buffer.get();
    }

    public void writeInteger(int i) {
        buffer.putInt(i);
        size += 4;
    }

    public void writeShort(short s) {
        buffer.putShort(s);
        size += 2;
    }

    public void writeFloat(float f) {
        buffer.putFloat(f);
        size += 4;
    }

    public float readFloat() {
        return buffer.getFloat();
    }

    public void writeLong(long l) {
        buffer.putLong(l);
        size += 8;
    }

    public int getSize() {
        return size;
    }

    public void debug() {

    }

    public int readInteger() {
        return buffer.getInt();
    }
}

