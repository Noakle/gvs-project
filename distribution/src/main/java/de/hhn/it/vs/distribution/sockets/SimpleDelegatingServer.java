package de.hhn.it.vs.distribution.sockets;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by wnck on 18/11/2016.
 */

public class SimpleDelegatingServer extends Thread {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(SimpleDelegatingServer.class);

    private static final int STANDARD_PORT = 1099;

    private int portNumber;
    private ServerSocket serverSocket;
    private Class<? extends AbstractServeOneClient> clazz;
    private Object service;

    /**
     * the SimpleDelegatingServer gets configured with a service object and an additional class to
     * receive incoming connections and to delegate them to a new instance of the additional class
     * using the socket and the service object.
     *
     * @param portNumber port on which the ServerSocket will wait for connections
     * @param service    BDCCService object implementing the service. This will be passed through to
     *                   the new instance of the call handling thread class.
     * @param clazz      Class which implements the handling of one call using the service object
     *                   which
     *                   a new instantiated object receives together with the socket representing the
     *                   connection.
     * @throws IOException if any socket problem occurs
     */
    public SimpleDelegatingServer(final int portNumber, Object
            service, Class<? extends AbstractServeOneClient>
                                          clazz) throws
            IOException {
        this.portNumber = portNumber;
        serverSocket = new ServerSocket(portNumber);
        this.service = service;
        this.clazz = clazz;
    }

    /**
     * For details please ee the other contructor. This constructor just uses a default port number.
     *
     * @param service Service to be used during delegation
     * @param type    Class of delegated thread
     * @throws IOException if any socket problem occurs
     */
    public SimpleDelegatingServer(Object service, Class<? extends AbstractServeOneClient>
            type) throws
            IOException {
        this(STANDARD_PORT, service, type);
    }

    /**
     * Goes into an endless loop accepting connections and delegating them to a separate thread.
     * this call blocks the control flow.
     *
     * @throws Exception Since this is just a finger exercise, whenever, whatever fails, we are out
     */
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
