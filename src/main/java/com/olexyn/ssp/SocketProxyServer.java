package com.olexyn.ssp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class SocketProxyServer {

    public int localPort;
    ServerSocket socketServer;
    public String localHost;
    public String remoteHost;
    public int remotePort;

    SocketProxyServer(int localPort, String remoteHost, int remotePort) throws IOException {
        this.localPort = localPort;
        socketServer = new ServerSocket(localPort);
        this.localHost = socketServer.getInetAddress().getHostName();
        this.remoteHost = remoteHost;
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }


    public void start() throws IOException {

        while (true) {

            Tunnel tunnel = new Tunnel(socketServer.accept() , new Socket(remoteHost, remotePort));

            new Thread(new ToClient(tunnel)).start();
            new Thread(new ToRemote(tunnel)).start();
        }
    }
}


class Tunnel{

    public Socket clientFacingSocket;
    public Socket remoteFacingSocket;
    Map<String,String> hostTranslation= new HashMap<>();

    Tunnel(Socket clientFacingSocket, Socket remoteFacingSocket){
        this.clientFacingSocket = clientFacingSocket;
        this.remoteFacingSocket = remoteFacingSocket;
        String _localHost = remoteFacingSocket.getLocalAddress().getHostName();
        String _remoteHost =remoteFacingSocket.getInetAddress().getHostName();
        hostTranslation.put(_localHost, _remoteHost );
    }
}