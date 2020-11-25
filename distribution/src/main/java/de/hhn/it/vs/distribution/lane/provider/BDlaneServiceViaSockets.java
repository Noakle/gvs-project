package de.hhn.it.vs.distribution.lane.provider;

import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BDlaneServiceViaSockets implements BDlaneService {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(BDlaneServiceViaSockets.class);



  private String hostname;
  private int portNumber;
  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  public BDlaneServiceViaSockets(final String hostname, final int portNumber) {
    this.hostname = hostname;
    this.portNumber = portNumber;
  }

  @Override
  public void connectToService() throws ServiceNotAvailableException {
    try {
      socket = new Socket(hostname, portNumber);
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream(socket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      throw new ServiceNotAvailableException(
          "Cannot connect to host " + hostname + " with port " + portNumber + ".", e);
    }

  }

  @Override
  public void disconnectFromService() {
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
      logger.warn("Problems disconnecting from service: " + ex.getMessage());
    }
  }

  @Override
  public Response sendAndGetResponse(Request request) throws ServiceNotAvailableException {
    // send request and wait for the response
    try {
      connectToService();
      logger.debug("sending request " + request);
      out.writeObject(request);
      logger.debug("wait for response ...");
      Response response = (Response) in.readObject();
      logger.debug("Got response " + response);
      disconnectFromService();
      return response;
    } catch (Exception ex) {
      throw new ServiceNotAvailableException("Communication problem: " + ex.getMessage(), ex);
    }

  }

}
