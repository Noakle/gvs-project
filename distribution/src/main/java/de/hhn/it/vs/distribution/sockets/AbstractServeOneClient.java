package de.hhn.it.vs.distribution.sockets;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.helper.CheckingHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by wnck on 18/11/2016.
 */

public abstract class AbstractServeOneClient extends Thread {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(AbstractServeOneClient.class);

    protected final Socket socket;
    protected final ObjectOutputStream out;
    protected final ObjectInputStream in;

    private Object service;

    /**
     * Creates new thread to work on a single client request.
     * @param socket socket connected with the client
     * @param service service to be used for the request
     * @throws IOException when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public AbstractServeOneClient(final Socket socket, Object service) throws IOException,
            IllegalParameterException {
        CheckingHelper.assertThatIsNotNull(socket, "socket");
        CheckingHelper.assertThatIsNotNull(service, "service");
        // Build IO streams
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.service = service;
        this.start();
    }

    /**
     * Disconnects / closes streams and socket.
     */
    public void disconnect() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            logger.warn("Problems during disconnect: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
