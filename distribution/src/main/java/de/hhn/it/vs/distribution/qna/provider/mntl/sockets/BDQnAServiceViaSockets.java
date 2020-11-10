package de.hhn.it.vs.distribution.qna.provider.mntl.sockets;

import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class Service Via Sockets
 * @author Leibl, Nauendorf
 * @version 2020-11-10
 */
public class BDQnAServiceViaSockets {

    //logger for information during use
    private static final org.slf4j.Logger logger =
        org.slf4j.LoggerFactory.getLogger(BDQnAServiceViaSockets.class);

    private String hostName;
    private int portNumber;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;



    public BDQnAServiceViaSockets(final String hostname, final int portNumber) {
        this.hostName = hostname;
        this.portNumber = portNumber;
    }

    /*
    method to connect to the service
     */
    private void connect() throws ServiceNotAvailableException {
        try {
            socket = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream((socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ServiceNotAvailableException("Can not connect to host " + hostName + " with port "
                    + portNumber + ".", ex);
        }
    }

    /*
    method to disconnect from the service
     */
    private void disconnect() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException ex) {
            // Problems disconnecting should not terminate the interaction. So we only log the problem.
            logger.warn("Problems disconnecting from service: " + ex.getMessage());
        }
    }

}
