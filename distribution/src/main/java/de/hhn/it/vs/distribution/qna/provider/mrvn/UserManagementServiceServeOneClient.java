package de.hhn.it.vs.distribution.qna.provider.mrvn;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.qna.provider.mrvn.data.Database;
import de.hhn.it.vs.distribution.qna.provider.mrvn.data.DbStub;
import de.hhn.it.vs.distribution.qna.provider.mrvn.data.NoSuchRegisteredUserException;
import de.hhn.it.vs.distribution.qna.provider.mrvn.data.User;
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
  public static final String PARAM_PASSWORD = "param.password";
  public static final String PARAM_USER_TOKEN = "param.usertoken";
  public static final String SUCCESS = "Operation performed successfully";

  public final Database database;

  /**
   * Creates new thread to work on a single client request.
   *
   * @param socket socket connected with the client
   * @param service service to be used for the request
   * @throws IOException when problems with the socket connection occur
   * @throws IllegalParameterException when called with null references
   */
  public UserManagementServiceServeOneClient(Socket socket, Object service) throws IOException,
          IllegalParameterException {
    super(socket, service);
    database = null;
  }

  public UserManagementServiceServeOneClient(Socket socket, Object service, DbStub db)
          throws IOException, IllegalParameterException {
    super(socket, service);
    this.database = db;
  }

  /**
   * The thread routine, consisting of reading, translating and responding properly to the request
   * received.
   */
  @Override
  public void run() {
    Request req; // store any request in these
    Response res;
    try {
      req = (Request) in.readObject(); // try reading from the socket
    } catch (IOException | ClassNotFoundException ex) {
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

  /**
   * Registers a user into the database according to the request.
   *
   * @param r Request instance that triggered this routine
   * @return the response message, containing either a succes message or an exception
   */
  public Response register(Request r) {
    User u;
    try {
      u = new User((String) r.getParameter(PARAM_EMAIL), (String) r.getParameter(PARAM_PASSWORD),
              (String) r.getParameter(PARAM_NAME));
    } catch (IllegalParameterException e) {
      return new Response(r, e);
    }
    ((DbStub) database).addUser(u);
    return new Response(r, SUCCESS);
  }

  public Response login(Request r) { return null; }

  public Response resolveUser(Request r) {
    return null;
  }

  public Response changeName(Request r) {
    User u;
    try {
      u = database.getUser((String) r.getParameter(PARAM_NAME));
      u.setName((String) r.getParameter(PARAM_NEW_NAME));
    } catch (IllegalParameterException e) {
      return new Response(r, e);
    } catch (NullPointerException e) {
      return new Response(r, new NoSuchRegisteredUserException());
    }
    return new Response(r, SUCCESS);
  }

  public Response getUsers(Request r) {
    return new Response(r, database.getUserList());
  }
}
