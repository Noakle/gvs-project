package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Created by wnck on 27.04.17.
 */

public class UserManagementServiceServeOneClient extends AbstractServeOneClient {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(UserManagementServiceServeOneClient.class);

    BDUserManagementService userManagementService;

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
     * @param socket  socket connected with the client
     * @param service service to be used for the request
     * @throws IOException               when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public UserManagementServiceServeOneClient(final Socket socket, final Object service) throws
            IOException, IllegalParameterException {
        super(socket, service);
        userManagementService = (BDUserManagementService) service;
    }

    @Override
    public void run() {
        Request request = null;
        Response response = null;
        try {
            request = (Request) in.readObject();

            String methodCall = request.getMethodToCall();

            switch (methodCall) {
                case REGISTER:
                    response = register(request);
                    break;
                case LOGIN:
                    response = login(request);
                    break;
                case RESOLVE_USER:
                    response = resolveUser(request);
                    break;
                case CHANGE_NAME:
                    response = changeName(request);
                    break;
                case GET_USERS:
                    response = getUsers(request);
                    break;
                default:
                    // Create a response with a ServiceNotAvailableException
                    ServiceNotAvailableException noMethodException = new ServiceNotAvailableException
                            ("Method with name unknown. " + methodCall);
                    response = new Response(request, noMethodException);
            }
        } catch (Exception e) {
            response = new Response(request, e);
        }

        try {
            out.writeObject(response);
        } catch (IOException e1) {
            logger.error("Problems writing the response: " + e1.getMessage());
        }

    }

    private Response resolveUser(final Request request) throws Exception {
        User user = userManagementService.resolveUser((Token) request.getParameter
                (PARAM_USER_TOKEN));
        return new Response(request, user);
    }

    private Response register(final Request request) throws Exception {
        Token token = userManagementService.register(
                (String) request.getParameter(PARAM_EMAIL),
                (String) request.getParameter(PARAM_SECRET),
                (String) request.getParameter(PARAM_NAME));
        return new Response(request, token);
    }

    private Response login(final Request request) throws Exception {
        Token token = userManagementService.login(((String) request.getParameter(PARAM_EMAIL)),
                (String) request.getParameter(PARAM_SECRET));
        return new Response(request, token);
    }

    private Response changeName(final Request request) throws Exception {
        userManagementService.changeName((Token) request.getParameter(PARAM_USER_TOKEN),
                (String) request.getParameter(PARAM_NEW_NAME));
        return new Response(request);
    }

    private Response getUsers(final Request request) throws Exception {
        List<User> users = userManagementService.getUsers((Token) request.getParameter
                (PARAM_USER_TOKEN));
        return new Response(request, users);
    }
}
