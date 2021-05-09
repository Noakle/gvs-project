package de.hhn.it.vs.distribution.lupa.provider.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static de.hhn.it.vs.distribution.lupa.provider.sockets.ComponentServiceServeOneClient.REGISTER;

public class BDComponentServiceViaSockets implements BDUserManagementService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDUserManagementServiceViaSockets.class);

    ObjectOutputStream out;
    ObjectInputStream in;
    Socket socket;
    private String hostname;
    private int portNumber;

    public BDComponentServiceViaSockets(String hostname, int port) {
        this.hostname = hostname;
        this.portNumber = port;
    }

    @Override
    public Token register(String email, String password, String name) throws  UserNameAlreadyAssignedException,
            ServiceNotAvailableException, IOException,  IllegalParameterException, ServiceNotAvailableException {
        initializeSocket(hostname, portNumber);
        Request request = new Request(REGISTER);
        request.addParameter("param.email", email);
        request.addParameter("param.secret", password);
        request.addParameter("param.name", name);

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
        shutDown();
        return (Token) response.getReturnObject();
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
    public Token login(String email, String password) throws IllegalParameterException, ServiceNotAvailableException {
        return null;
    }

    @Override
    public User resolveUser(Token userToken) throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
        return null;
    }

    @Override
    public List<User> getUsers(Token userToken) throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
        return null;
    }

    @Override
    public void changeName(Token userToken, String newName) throws IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException, ServiceNotAvailableException {

    }

    private void initializeSocket(String hostname, int port) throws IOException {
        // Connect to the server
        socket = new Socket(hostname, port);
        System.out.println(socket);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    private void getRessource(String path) throws IOException {
       // out.println("GET " + path + " HTTP/1.0");
       // out.println();

        // Read data from the server until we finish reading the document
        String line = in.readLine();
        while( line != null )
        {
            System.out.println( line );
            line = in.readLine();
        }

        shutDown();
    }

    private void shutDown() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    private Response sendAndGetResponse(Request request) throws IOException {
        // send request and wait for the response
        try {
            out.writeObject(request);
            logger.debug("wait for response ...");
            Response response = (Response) in.readObject();
            logger.debug("Got response " + response);
            return response;
        } catch (Exception ex) {
            throw new IOException("Communication problem: " + ex.getMessage(), ex);
        }
    }
}

/*
* Erzeugen Sie eine Implementierung Ihrer BD<component>ServiceFassade.
* Nennen Sie die Implementierung bitte BD<component>ServiceViaSockets.
* - Diese Implementierung sollte einen Konstruktor haben mit Hostname und Portnummer als Parameter
* - Schreiben Sie Methoden, die die Verbindung auf- und abbauen
* - In jeder Fassadenmethode müssen Sie
*   - Das Request-Objekt anlegen und konfigurieren
*   - Das Request-Objekt versenden und das Response-Objekt empfangen
*   - Das Response-Objekt analysieren und entweder den Rückgabewert weitergeben oder die im Response-Objekt enthaltene Exceptionneu aufwerfen.
*   - Andere Exceptions auf die ServiceNotAvailableExceptionmappen.
* */