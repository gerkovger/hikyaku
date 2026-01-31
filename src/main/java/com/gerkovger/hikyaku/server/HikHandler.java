package com.gerkovger.hikyaku.server;


import com.gerger.hikyaku.api.HikMessageFactory;
import com.gerger.hikyaku.api.channel.ChannelType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class HikHandler extends Thread {

    private static final AtomicInteger sessionCounter = new AtomicInteger(0);

    private final String connectionId = "CONN-" + sessionCounter.incrementAndGet();

    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;

    public HikHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try {
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());



            while (true) {
                var msg = HikMessageFactory.readNextMessage(in);
                var type = msg.getMessageType();
                var corid = msg.getCorrelationId();
                var body = msg.payloadAsString();
                switch (type) {
                    case PING -> {
                        System.out.println(msg.getCorrelationId() + ": PING -> PONG");
                    }
                    case CONNECT -> {
                        var bb = ByteBuffer.wrap(msg.getPayload());
                        var nameLen = msg.getPayload().length - 1;
                        var channelType = ChannelType.of(bb.get());
                        var channelName = new byte[nameLen];
                        bb.get(channelName);
                        System.out.println("Connection request for " + channelType + " named " + new String(channelName));
                    }
                    default -> {
                        System.out.println(msg.getCorrelationId() + ": " + msg.getMessageType() + ", " + msg.payloadAsString());
                    }
                }
            }

        } catch (EOFException e) {
            System.out.println("Connection closed for " + connectionId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {
            }
            try {
                clientSocket.close();
            } catch (Exception ignored) {
            }
        }
    }

    public String getConnectionId() {
        return connectionId;
    }
}
