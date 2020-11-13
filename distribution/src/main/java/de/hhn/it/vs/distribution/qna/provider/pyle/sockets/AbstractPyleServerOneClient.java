package de.hhn.it.vs.distribution.qna.provider.pyle.sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractPyleServerOneClient  extends Thread  {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(AbstractPyleServerOneClient.class);

    protected ServerSocket serverSocket;
    protected Socket clientSocket;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;

    public AbstractPyleServerOneClient(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
        this.start();
    }


    public void stopConnectionService() throws IOException {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        }catch (Exception ex)
        {
            throw  new IOException("conot end the connection" + ex.getMessage());
        }
    }

}
