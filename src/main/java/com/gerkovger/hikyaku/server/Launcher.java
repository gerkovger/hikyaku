package com.gerkovger.hikyaku.server;

import java.io.IOException;

public class Launcher {

    static void main() throws IOException {
        var server = new EchoMultiServer();
        server.start(6666);
    }

}
