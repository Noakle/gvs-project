package de.hhn.it.vs.distribution.qna.provider.mrvn;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;
import java.io.IOException;
import java.net.Socket;

public class UserManagementServiceServeOneClient extends AbstractServeOneClient {

  public static final String REGISTER = "usermanagement.register";
  public static final String LOGIN = "usermanagement.login";
  public static final String RESOLVE_USER = "usermanagement.resolveuser";
  public static final String CHANGE_NAME = "usermanagement.changename";
  public static final String GET_USERS = "usermanagement.getusers";
  public static final String PARAM_NAME = "param.name";
  public static final String PARAM_NEW_NAME = "param.newname";
  public static final String PARAM_EMAIL = "param.email";
  public static final String PARAM_SECRET = "param.secret";
  public static final String PARAM_USER_TOKEN = "param.usertoken";

  /**
   * Creates new thread to work on a single client request.
   *
   * @param socket socket connected with the client
   * @param service service to be used for the request
   * @throws IOException when problems with the socket connection occur
   * @throws IllegalParameterException when called with null references
   */
  public UserManagementServiceServeOneClient(Socket socket, Object service) throws IOException, IllegalParameterException {
    super(socket, service);
  }

  /**
   * The thread routine, consisting of reading, translating and responding properly to the request
   * received.
   */
  @Override
  public void run()
  {
    Request req; // store any request in these
    Response res;
    try {
      req = (Request) in.readObject(); // try reading from the socket
    }
    catch (IOException | ClassNotFoundException ex) {
      ex.printStackTrace(); // print whatever went wrong
      return; // no request received, so nothing to do
    }

    String methodName = req.getMethodToCall(); // extract method name to know how to process it

    switch (methodName) { // call the appropriate method
      case REGISTER:
        res = register(req);
        break;
      case LOGIN:
        res = login(req);
        break;
      case RESOLVE_USER:
        res = resolveUser(req);
        break;
      case CHANGE_NAME:
        res = changeName(req);
        break;
      case GET_USERS:
        res = getUsers(req);
        break;
      default:
        // method name sent to this server is invalid, answer with an exception
        ServiceNotAvailableException e = new ServiceNotAvailableException();
        res = new Response(req, e);
        break;
    }
  }

  public Response register(Request r) {
    return null;
  }

  public Response login(Request r) {
    return null;
  }

  public Response resolveUser(Request r) {
    return null;
  }

  public Response changeName(Request r) {
    return null;
  }

  public Response getUsers(Request r) {
    return null;
  }
}
