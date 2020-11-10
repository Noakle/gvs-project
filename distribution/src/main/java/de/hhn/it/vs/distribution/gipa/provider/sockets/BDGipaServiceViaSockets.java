package de.hhn.it.vs.distribution.gipa.provider.sockets;

import java.io.IOException;
import java.net.Socket;

public class BDGipaServiceViaSockets implements BDGipaService{
    private final String hostname;
    private final int port;
    private Socket clientSocket;

    BDGipaServiceViaSockets(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }


    @Override
    public void verbindungAufbauen() {
        try {
            clientSocket = new Socket(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void verbindungAbbauen() {
        try {
            if(clientSocket != null){
                clientSocket.close();
                clientSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
