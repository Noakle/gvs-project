package de.hhn.it.vs.distribution.sockets;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;

public class informatikxServiceSocketServer extends Thread {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(SimpleDelegatingServer.class);



    private int portNumber;
    private ServerSocket serverSocket;
    private Class<? extends AbstractServeOneClient> clazz;
    private Object service;

    public informatikxServiceSocketServer (int portNumber, Object service,
                                           Class<? extends AbstractServeOneClient> clazz) throws
            IOException {
        this.portNumber = portNumber;
        serverSocket = new ServerSocket(portNumber);
        this.service = service;
        this.clazz = clazz;
    }

    public void foreverAcceptAndDelegate() throws Exception {
        start();
    }


    public void run() {
        try {
            logger.info("ready to go ...");
            while (true) {
                logger.info("Wait for a call ...");
                Socket clientSocket = serverSocket.accept();
                logger.info("got connection from " + clientSocket);
                Constructor<?> constructor = clazz.getConstructor(Socket.class, Object.class);
                Object object = constructor.newInstance(clientSocket, service);
                logger.info("ServeOneClient of class {} launched", clazz.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Problem in service socket wrapper {}. Terminating SimpleDelegatingServer.",
                    clazz.getName());
        }
    }
}
