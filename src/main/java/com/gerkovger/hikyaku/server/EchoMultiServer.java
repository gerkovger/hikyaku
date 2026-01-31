package com.gerkovger.hikyaku.server;

import com.gerger.hikyaku.api.MsgType;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoMultiServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new EchoClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private DataInputStream in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());

                while (true) {
                    short magic = in.readShort();
                    if (magic != 0x4252) throw new IOException("Bad magic");

                    byte version = in.readByte();
                    byte type = in.readByte();
                    short flags = in.readShort();
                    short headerLen = in.readShort();
                    int frameLen = in.readInt();
                    long correlationId = in.readLong();

                    int payloadLen = frameLen - headerLen;
                    if (payloadLen < 0)
                        throw new IOException(
                                "Bad lengths: frameLen=" + frameLen + " headerLen=" + headerLen);

                    byte[] payload = in.readNBytes(payloadLen);
                    if (payload.length != payloadLen)
                        throw new EOFException(); // truncated frame / connection closed

                    MsgType mt = MsgType.of(type);
                    String body = new String(payload, java.nio.charset.StandardCharsets.UTF_8);

                    System.out.println(mt + ", " + body);
                }

            } catch (EOFException e) {
                System.out.println("Connection closed.");
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
    }

}