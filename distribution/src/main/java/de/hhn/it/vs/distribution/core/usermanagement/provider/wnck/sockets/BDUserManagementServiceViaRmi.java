package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient.*;
import static de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient.PARAM_NEW_NAME;

public class BDUserManagementServiceViaRmi implements BDUserManagementService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDUserManagementServiceViaSockets.class);

    private String hostname;
    private int portNumber;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public BDUserManagementServiceViaRmi(final String hostname, final int portNumber) {
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

    private Response sendAndGetResponse(final Request request) throws ServiceNotAvailableException {
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


    private void rethrowStandardExceptions(final Response response) throws
            IllegalParameterException, ServiceNotAvailableException, InvalidTokenException {
        Exception exceptionFromRemote = response.getExceptionObject();

        // check all acceptable exception types and rethrow
        if (exceptionFromRemote instanceof IllegalParameterException) {
            throw (IllegalParameterException) exceptionFromRemote;
        }

        if (exceptionFromRemote instanceof InvalidTokenException) {
            throw (InvalidTokenException) exceptionFromRemote;
        }

        if (exceptionFromRemote instanceof ServiceNotAvailableException) {
            throw (ServiceNotAvailableException) exceptionFromRemote;
        }

        rethrowUnexpectedException(exceptionFromRemote);
    }

    private void rethrowUnexpectedException(final Exception exceptionFromRemote) throws
            ServiceNotAvailableException {
        // if we reach this part, than the exception is an exception we do not expect!
        logger.error("WTF - Unknown exception object in response: " + exceptionFromRemote);
        exceptionFromRemote.printStackTrace();
        throw new ServiceNotAvailableException("Unknown exception received in response object.",
                exceptionFromRemote);
    }


    @Override
    public Token register(final String email, final String password, final String name) throws
            IllegalParameterException, UserNameAlreadyAssignedException,
            ServiceNotAvailableException {
        Request request = new Request(REGISTER)
                .addParameter(PARAM_EMAIL, email)
                .addParameter(PARAM_SECRET, password)
                .addParameter(PARAM_NAME, name);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

            if (exceptionFromRemote instanceof IllegalParameterException) {
                throw (IllegalParameterException) exceptionFromRemote;
            }
            if (exceptionFromRemote instanceof UserNameAlreadyAssignedException) {
                throw (UserNameAlreadyAssignedException) exceptionFromRemote;
            }
            if (exceptionFromRemote instanceof ServiceNotAvailableException) {
                throw (ServiceNotAvailableException) exceptionFromRemote;
            }

            rethrowUnexpectedException(exceptionFromRemote);
        }
        return (Token) response.getReturnObject();
    }

    @Override
    public Token login(final String email, final String password) throws IllegalParameterException,
            ServiceNotAvailableException {
        Request request = new Request(LOGIN)
                .addParameter(PARAM_EMAIL, email)
                .addParameter(PARAM_SECRET, password);
        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

            if (exceptionFromRemote instanceof IllegalParameterException) {
                throw (IllegalParameterException) exceptionFromRemote;
            }
            if (exceptionFromRemote instanceof ServiceNotAvailableException) {
                throw (ServiceNotAvailableException) exceptionFromRemote;
            }

            rethrowUnexpectedException(exceptionFromRemote);
        }
        return (Token) response.getReturnObject();
    }

    @Override
    public User resolveUser(final Token userToken) throws IllegalParameterException,
            InvalidTokenException, ServiceNotAvailableException {
        Request request = new Request(RESOLVE_USER).addParameter(PARAM_USER_TOKEN, userToken);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            rethrowStandardExceptions(response);
        }


        return (User) response.getReturnObject();
    }

    @Override
    public List<User> getUsers(final Token userToken) throws IllegalParameterException,
            InvalidTokenException, ServiceNotAvailableException {
        Request request = new Request(GET_USERS)
                .addParameter(PARAM_USER_TOKEN, userToken);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            rethrowStandardExceptions(response);
        }


        return (List<User>) response.getReturnObject();
    }

    @Override
    public void changeName(final Token userToken, final String newName) throws
            IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException,
            ServiceNotAvailableException {

        Request request = new Request(CHANGE_NAME)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_NEW_NAME, newName);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

            if (exceptionFromRemote instanceof UserNameAlreadyAssignedException) {
                throw (UserNameAlreadyAssignedException) exceptionFromRemote;
            }


            rethrowStandardExceptions(response);
        }


        return;

    }

}
