package com.gerkovger.hikyaku.server;

import java.io.IOException;
import java.net.ServerSocket;

public class HikBroker {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            var handler = new HikHandler(serverSocket.accept());
            handler.start();
            System.out.println("New handler created with id: " + handler.getConnectionId());
        }

    }

    public void stop() throws IOException {
        serverSocket.close();
    }


}