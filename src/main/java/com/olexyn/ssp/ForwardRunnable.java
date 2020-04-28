package com.olexyn.ssp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;


class ToRemote implements Runnable {

    Tunnel tunnel;
    InputStream in;
    OutputStream out;

    ToRemote(Tunnel tunnel) throws IOException {
        this.tunnel = tunnel;
        in = tunnel.clientFacingSocket.getInputStream();
        out = tunnel.remoteFacingSocket.getOutputStream();
    }


    @Override
    public void run() {
        try {
            int bytes_read;
            byte[] reply = new byte[4096];
            while ((bytes_read = in.read(reply)) != -1) {
                String localHost = tunnel.remoteFacingSocket.getLocalAddress().getHostName();
                reply = Tools.injectHost(reply, tunnel.hostTranslation.get(localHost));
                System.out.println("ToRemote\n"+new String(reply, StandardCharsets.UTF_8));
                out.write(reply, 0, bytes_read);
                out.flush();
            }
        } catch (IOException ignored) {}
    }
}


class ToClient implements Runnable {

    Tunnel tunnel;
    InputStream inFromRemote;
    OutputStream outToClient;

    ToClient(Tunnel tunnel) throws IOException {
        this.tunnel = tunnel;
        inFromRemote = tunnel.remoteFacingSocket.getInputStream();
        outToClient = tunnel.clientFacingSocket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            int bytes_read;
            byte[] buffer = new byte[4096];
            while ((bytes_read = inFromRemote.read(buffer)) != -1) {
                String replyString = new String(buffer, StandardCharsets.UTF_8);



                outToClient.write(buffer, 0, bytes_read);
                System.out.println("ToClient\n"+new String(buffer, StandardCharsets.UTF_8));



                outToClient.flush();
            }
        } catch (IOException ignored) {}
    }
}






