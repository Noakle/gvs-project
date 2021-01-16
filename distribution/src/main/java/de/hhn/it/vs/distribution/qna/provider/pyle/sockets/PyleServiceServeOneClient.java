package de.hhn.it.vs.distribution.qna.provider.pyle.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;

public class PyleServiceServeOneClient extends AbstractPyleServerOneClient {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(PyleServiceServeOneClient.class);

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

    public PyleServiceServeOneClient(int port) throws IOException {
        super(port);
    }

    public void run()
    {
        Request request = null;
        Response response = null;
        try
        {
           request= (Request) in.readObject();
            String methodCall = request.getMethodToCall();
            if (methodCall.equals(REGISTER))
            {
                response = register(request);
                stopConnectionService();
            }
            if (methodCall.equals(LOGIN))
            {
                response = login(request);
                stopConnectionService();
            }
            if (!methodCall.equals(REGISTER)||(!methodCall.equals(LOGIN)))
            {
                try{}catch (Exception ex)
                {
                    ServiceNotAvailableException noMethodException = new ServiceNotAvailableException
                            ("Method with name unknown. " + methodCall);
                    response = new Response(request, noMethodException);
                    stopConnectionService();
                }
            }

        }catch (Exception ex)
        {

            logger.debug("request or response or both problem"+ ex.toString());
        }
        try {
            out.writeObject(response);
        } catch (IOException e1) {
            logger.error("Problems writing the response: " + e1.getMessage());
        }
    }

    /*private Response resolveUser(final Request request) throws Exception {
        User user = userManagementService.resolveUser((Token) request.getParameter
                (PARAM_USER_TOKEN));
        return new Response(request, user);
    }*/

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

   /* private Response changeName(final Request request) throws Exception {
        userManagementService.changeName((Token) request.getParameter(PARAM_USER_TOKEN),
                (String) request.getParameter(PARAM_NEW_NAME));
        return new Response(request);
    }*/

   /* private Response getUsers(final Request request) throws Exception {
        List<User> users = userManagementService.getUsers((Token) request.getParameter
                (PARAM_USER_TOKEN));
        return new Response(request, users);
    }*/
}
