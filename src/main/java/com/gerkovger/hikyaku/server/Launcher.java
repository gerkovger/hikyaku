package com.gerkovger.hikyaku.server;

import java.io.IOException;

public class Launcher {

    static void main() throws IOException {
        var server = new HikBroker();
        server.start(6666);
    }

}
