package de.hhn.it.vs.distribution.fdkh.provider.socket;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;

import java.io.IOException;
import java.net.Socket;

public class fdkhServiceServeOneClient extends AbstractServeOneClient {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(fdkhServiceServeOneClient.class);

    /**
     * Creates new thread to work on a single client request.
     *
     * @param socket  socket connected with the client
     * @param service service to be used for the request
     * @throws IOException               when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public fdkhServiceServeOneClient(Socket socket, Object service) throws IOException, IllegalParameterException {
        super(socket, service);
    }
}
