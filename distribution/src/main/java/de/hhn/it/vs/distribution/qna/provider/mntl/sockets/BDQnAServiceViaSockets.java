package de.hhn.it.vs.distribution.qna.provider.mntl.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Class Service Via Sockets
 * @author Leibl, Nauendorf
 * @version 2020-11-10
 */
public class BDQnAServiceViaSockets implements BDQnAService {

    //logger for information during use
    private static final org.slf4j.Logger logger =
        org.slf4j.LoggerFactory.getLogger(BDQnAServiceViaSockets.class);

    private String hostName;
    private int portNumber;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;



    public BDQnAServiceViaSockets(final String hostname, final int portNumber) {
        this.hostName = hostname;
        this.portNumber = portNumber;
    }

    /*
    method to connect to the service
     */
    private void connectToService() throws ServiceNotAvailableException {
        try {
            socket = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream((socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ServiceNotAvailableException("Can not connect to host " + hostName + " with port "
                    + portNumber + ".", ex);
        }
    }

    /*
    method to disconnect from the service
     */
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
        logger.error("Unknown exception object in response: " + exceptionFromRemote);
        exceptionFromRemote.printStackTrace();
        throw new ServiceNotAvailableException("Unknown exception received in response object.",
            exceptionFromRemote);
    }

    @Override
    public long createArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(CREATE_AREA);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA, area);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (long) response.getReturnObject();
    }

    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(CREATE_QUESTION);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        request.addParameter(PARAM_QUESTION, question);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (long) response.getReturnObject();
    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(CREATE_ANSWER);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        request.addParameter(PARAM_QUESTION_ID, questionId);
        request.addParameter(PARAM_ANSWER, answer);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (long) response.getReturnObject();
    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(GET_AREA_IDS);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(GET_AREA);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (Area) response.getReturnObject();
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(GET_QUESTION_IDS);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(GET_QUESTION);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        request.addParameter(PARAM_QUESTION_ID, questionId);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (Question) response.getReturnObject();
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(GET_ANSWER_IDS);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        request.addParameter(PARAM_QUESTION_ID, questionId);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(GET_ANSWER);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        request.addParameter(PARAM_QUESTION_ID, questionId);
        request.addParameter(PARAM_ANSWER_ID, answerId);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
        return (Answer) response.getReturnObject();
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(UPDATE_AREA);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA, area);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(UPDATE_QUESTION);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        request.addParameter(PARAM_QUESTION, question);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        Request request = new Request(UPDATE_ANSWER);
        request.addParameter(PARAM_USER_TOKEN, userToken);
        request.addParameter(PARAM_AREA_ID, areaId);
        request.addParameter(PARAM_QUESTION_ID, questionId);
        request.addParameter(PARAM_ANSWER, answer);
        Response response = sendAndGetResponse(request);
        if (response.isException()) {
            rethrowStandardExceptions(response);
        }
    }
}
