package de.hhn.it.vs.distribution.lane.provider;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;
import java.io.IOException;
import java.net.Socket;

public class LaneServiceServeClient extends AbstractServeOneClient {

  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(LaneServiceServeClient.class);

  /**
   * Creates new thread to work on a single client request.
   *
   * @param socket  socket connected with the client
   * @param service service to be used for the request
   * @throws IOException               when problems with the socket connection occur
   * @throws IllegalParameterException when called with null references
   */
  public LaneServiceServeClient(Socket socket, Object service)
      throws IOException, IllegalParameterException {
    super(socket, service);
  }

  @Override
  public void run() {
    Request request = null;
    Response response = null;

    try {
      request = (Request) in.readObject();

      String methodCall = request.getMethodToCall();

      switch (methodCall) {
        default:
          // Create a response with a ServiceNotAvailableException
          ServiceNotAvailableException noMethodException = new ServiceNotAvailableException
              ("Method with name unknown. " + methodCall);
          response = new Response(request, noMethodException);

      }

    } catch (Exception e) {

    }

    try {
      out.writeObject(response);
    } catch (IOException e1) {
      logger.error("Problems writing the response: " + e1.getMessage());
    }

    disconnect();

  }
}
