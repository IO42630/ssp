package com.olexyn.ssp;

import java.io.IOException;


public class App {
    public static void main(String... args) throws IOException {
        SocketProxyServer socketProxyServer = new SocketProxyServer(9098, "google.ch", 80);
        socketProxyServer.start();
    }
}
