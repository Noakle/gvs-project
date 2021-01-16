package de.hhn.it.vs.distribution.qna.provider.pyle.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static de.hhn.it.vs.distribution.qna.provider.pyle.sockets.PyleServiceServeOneClient.*;

public class BDPyleServiceViaSockets implements BDQnAService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDPyleServiceViaSockets.class);

    private String hostName;
    private int portNummer;
    private Socket clientSocket;
   // private PrintWriter out;
    //private BufferedReader in;
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
            outObj = new ObjectOutputStream(clientSocket.getOutputStream());
            inObj = new ObjectInputStream(clientSocket.getInputStream());
        }catch(Exception ex)
        {
            ex.printStackTrace();
            throw new ServiceNotAvailableException("Cannot connect to host " + hostName + " with port "
                    + portNummer + ".", ex);
        }
    }

    public void stopConnectionService() throws ServiceNotAvailableException {
        try {
            inObj.close();
            outObj.close();
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


    @Override
    public long createArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Request request = new Request(CREATE_AREA);
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
        return (long) response.getReturnObject();

    }

    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Request request = new Request(CREATE_QUESTION);
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
        return (long) response.getReturnObject();
    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Request request = new Request(CREATE_ANSWERE);
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
        return (long) response.getReturnObject();

    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Request request = new Request(GET_AREA_IDS);
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
        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Request request = new Request(GET_AREA);
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
        return (Area) response.getReturnObject();
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        return null;
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

    }
}
