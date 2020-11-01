package de.hhn.it.vs.distribution.mabo.provider.sockets;

import static de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient.CHANGE_NAME;
import static de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient.PARAM_NEW_NAME;
import static de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient.PARAM_USER_TOKEN;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BDmaboServiceViaSockets {

  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(BDmaboServiceViaSockets.class);


  private final String hostname;
  private final int portNumber;
  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  /**
   * Creates a new instance of BDmaboServiceViaSockets
   *
   * @param hostname   Hostname of the Service
   * @param portNumber Port of the Service
   */
  public BDmaboServiceViaSockets(String hostname, int portNumber) {
    this.hostname = hostname;
    this.portNumber = portNumber;
  }

  private void connectToService() throws ServiceNotAvailableException {
    try {
      socket = new Socket(hostname, portNumber);
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream((socket.getInputStream()));
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new ServiceNotAvailableException("Cannot connect to host " + hostname + " with port "
          + portNumber + ".", ex);
    }
  }

  private void disconnectFromService() {
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

  public void createArea(Token userToken, Area area){
    // TODO: Create every Methode

    /*Request request = new Request(CHANGE_NAME)
        .addParameter(PARAM_USER_TOKEN, userToken)
        .addParameter(PARAM_NEW_NAME, newName);

    Response response = sendAndGetResponse(request);

    if (response.isException()) {
      Exception exceptionFromRemote = response.getExceptionObject();

      if (exceptionFromRemote instanceof UserNameAlreadyAssignedException) {
        throw (UserNameAlreadyAssignedException) exceptionFromRemote;
      }


      rethrowStandardExceptions(response);
    }*/


    return;
  }
}
