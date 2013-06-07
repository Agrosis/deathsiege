package com.jantox.siege.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

    public static final int MAX_PACKET_SIZE = 256;

    private SocketChannel socket;
    private String host;
    private int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        socket = SocketChannel.open();
        socket.configureBlocking(false);
        socket.connect(new InetSocketAddress(host, port));

        while(!socket.finishConnect()) {

        }
    }

    public void close() throws IOException {
        socket.close();
    }

    public Packet read() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Packet.MAX_PACKET_SIZE);
        int bytes = socket.read(buffer);

        if(bytes <= 0)
            return null;

        buffer.flip();
        return new Packet(buffer, bytes);
    }

    public void write(Packet p) throws IOException {
        ByteBuffer buffer = p.getData();

        socket.write(buffer);
    }

}
