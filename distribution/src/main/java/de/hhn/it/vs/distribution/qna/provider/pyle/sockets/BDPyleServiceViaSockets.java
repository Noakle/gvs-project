package de.hhn.it.vs.distribution.qna.provider.pyle.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient.*;
import static de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient.PARAM_NAME;

public class BDPyleServiceViaSockets implements BDUserManagementService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDPyleServiceViaSockets.class);

    private String hostName;
    private int portNummer;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private ObjectOutputStream outObj;
    private ObjectInputStream inObj;
    public BDPyleServiceViaSockets(String hostName, int portNummer )
    {
            this.hostName = hostName;
            this.portNummer = portNummer;
    }
       /*
       *Start die Verbindung mit dem Server
       * @ip, ip adresse des Servers
       * @port, die Port nummer
        */
    public void startConnectionService(String ip, int port) throws ServiceNotAvailableException{
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }catch(Exception ex)
        {
            ex.printStackTrace();
            throw new ServiceNotAvailableException("Cannot connect to host " + hostName + " with port "
                    + portNummer + ".", ex);
        }
    }

    public void stopConnectionService() throws ServiceNotAvailableException {
        try {
            in.close();
            out.close();
            clientSocket.close();
        }catch (Exception ex)
        {
            throw  new ServiceNotAvailableException("conot end the connection" + ex);
        }
    }

    private Response sendAndGetResponse(final Request request) throws ServiceNotAvailableException {
        // send request and wait for the response
        try {
            startConnectionService(hostName,portNummer);
            logger.debug("sending request " + request);
            outObj.writeObject(request);
            logger.debug("wait for response ...");
            Response response = (Response) inObj.readObject();
            logger.debug("Got response " + response);
            stopConnectionService();
            if (!response.equals(null))
            {
                return response;
            }else {
                //throw new ServiceNotAvailableException("Message Lost: ");
                rethrowStandardExceptions(response);
            }
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
     * Registers a user in the system.
     *
     * @param email    identifies the user
     * @param password authenticates the user to get access to his token. The password has to contain
     *                 at minimum four characters.
     * @param name     nick name of the user to be used in UIs. This nick name has to contain at
     *                 minimum
     *                 two visible characters.
     * @return Token identifying the user
     * @throws IllegalParameterException        if one of the parameters doesn't match the
     *                                          specification
     * @throws UserNameAlreadyAssignedException if the nick name is already assigned to another user
     * @throws ServiceNotAvailableException     if the service cannot be provided
     */
    @Override
    public Token register(String email, String password, String name) throws IllegalParameterException, UserNameAlreadyAssignedException, ServiceNotAvailableException {
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
     * Login for a registered user, e.g. from another device or after restart of a client
     *
     * @param email    identifies the user
     * @param password authenticates the user
     * @return Token identifying the user
     * @throws IllegalParameterException    if email or password do not match the values in the user
     *                                      management service
     * @throws ServiceNotAvailableException if the service cannot be provided
     */
    @Override
    public Token login(String email, String password) throws IllegalParameterException, ServiceNotAvailableException {
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
     * returns user info related to the given token.
     *
     * @param userToken token identifying the user
     * @return User object related to the given token
     * @throws IllegalParameterException    if a null reference is given
     * @throws InvalidTokenException        if the token is no valid token from this user
     *                                      management service
     * @throws ServiceNotAvailableException if the service cannot be provided
     */
    @Override
    public User resolveUser(Token userToken) throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
        return null;
    }

    /**
     * Returns a list of registered users. This method is for admin or debug purposes.
     *
     * @param userToken token identifying the user
     * @return List of User objects
     * @throws IllegalParameterException    if a null reference is given
     * @throws InvalidTokenException        if the token is no valid token from this user
     *                                      management service
     * @throws ServiceNotAvailableException if the service cannot be provided
     */
    @Override
    public List<User> getUsers(Token userToken) throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
        return null;
    }

    /**
     * Allows the user to change his nick name.
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
    public void changeName(Token userToken, String newName) throws IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException, ServiceNotAvailableException {

    }
}
