package de.hhn.it.vs.distribution.rbaw.provider.sockets;

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

public class BDrbawServiceViaSockets implements BDUserManagementService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDrbawServiceViaSockets.class);

    private String hostname;
    private int portNumber;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public BDrbawServiceViaSockets(final String hostname, final int portNumber) {
        this.hostname = hostname;
        this.portNumber = portNumber;
    }

    private void connectToService() throws ServiceNotAvailableException {
        try {
            clientSocket = new Socket(hostname, portNumber);
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream((clientSocket.getInputStream()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServiceNotAvailableException("Cannot connect to host " + hostname + " with port "
                    + portNumber + ".", ex);
        }
    }

    private void disconnectFromService() throws ServiceNotAvailableException {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            // Problems disconnecting should not terminate the interaction. So we only log the problem.
            throw new ServiceNotAvailableException("can not end the connection" + ex);
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

    /**
     * Registers the user in the system.
     *
     * @param email    identifies the user
     * @param password authenticates the user to get access to his token. The password has to contain
     *                 at minimum four characters.
     * @param name     nick name of the user to be used in UIs. This nick name has to contain at
     *                 minimum
     *                 two visible characters.
     * @return Token to identify the user
     * @throws IllegalParameterException        if one of the parameters doesn't match the
     *                                          specification
     * @throws UserNameAlreadyAssignedException if the user name is already assigned to another user
     * @throws ServiceNotAvailableException     if the service cannot be provided
     */
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

    /**
     * Login for registered user.
     *
     * @param email    identifies the user
     * @param password authenticates the user
     * @return Token identifying the user
     * @throws IllegalParameterException    if email or password do not match the values in the user
     *                                      management service
     * @throws ServiceNotAvailableException if the service cannot be provided
     */
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

    /**
     * returns user info.
     *
     * @param userToken token identifying the user
     * @return user object related to the given token
     * @throws IllegalParameterException    if a null reference is given
     * @throws InvalidTokenException        if the token is no valid token from this user
     *                                      management service
     * @throws ServiceNotAvailableException if the service cannot be provided
     */
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

    /**
     * returns a list of the registered users.
     *
     * @param userToken token identifying the user
     * @return List of user objects
     * @throws IllegalParameterException    if a null reference is given
     * @throws InvalidTokenException        if the token is no valid token from this user
     *                                      management service
     * @throws ServiceNotAvailableException if the service cannot be provided
     */
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

    /**
     * allows a user to change his name.
     *
     * @param userToken Token identifying the user
     * @param newName   new nick name. Must the same specification as in the register method.
     * @throws IllegalParameterException        if either the token is a null reference or the name
     *                                          does not match the specification.
     * @throws InvalidTokenException            if the token is no valid token from this user
     *                                          management service
     * @throws UserNameAlreadyAssignedException if the name is already used by another user
     * @throws ServiceNotAvailableException     if the service cannot be provided
     */
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
    }
}