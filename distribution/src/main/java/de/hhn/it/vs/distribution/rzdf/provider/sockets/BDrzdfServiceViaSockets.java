package de.hhn.it.vs.distribution.rzdf.provider.sockets;

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

import static de.hhn.it.vs.distribution.rzdf.provider.sockets.rzdfServiceServeOneClient.*;

/**
 * Created by David Flaig and Rick Zolnierek on 11.11.2020
 */
public class BDrzdfServiceViaSockets implements BDQnAService{

    private static final org.slf4j.Logger logger =
    org.slf4j.LoggerFactory.getLogger(BDrzdfServiceViaSockets.class);

    private String hostName;
    private int portNumber;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public BDrzdfServiceViaSockets(final String hostName, final int portNumber){

        this.hostName = hostName;
        this.portNumber = portNumber;

    }

    private void connectToService() throws ServiceNotAvailableException {
        try {
            socket = new Socket(hostName, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream((socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ServiceNotAvailableException("Cannot connect to host " + hostName + " with port "
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

    public long createArea(Token userToken, Area area) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(CREATE_AREA)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA, area);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (long) response.getReturnObject();

    }

    public long createQuestion(Token userToken, long areaId, Question question) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(CREATE_QUESTION)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId)
            .addParameter(PARAM_QUESTION, question);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (long) response.getReturnObject();

    }

    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(CREATE_ANSWER)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId)
            .addParameter(PARAM_QUESTION_ID, questionId)
            .addParameter(PARAM_ANSWER, answer);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (long) response.getReturnObject();

    }

    public List<Long> getAreaIds(Token userToken) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(GET_AREA_IDS)
            .addParameter(PARAM_USER_TOKEN, userToken);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (List<Long>) response.getReturnObject();

    }

    public Area getArea(Token userToken, long areaId) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(GET_AREA)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (Area) response.getReturnObject();

    }

    public List<Long> getQuestionIds(Token userToken, long areaId) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(GET_QUESTION_IDS)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (List<Long>) response.getReturnObject();

    }

    public Question getQuestion(Token userToken, long areaId, long questionId) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(GET_QUESTION)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId)
            .addParameter(PARAM_QUESTION_ID, questionId);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (Question) response.getReturnObject();

    }

    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(GET_ANSWER_IDS)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId)
            .addParameter(PARAM_QUESTION_ID, questionId);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (List<Long>) response.getReturnObject();

    }

    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(GET_ANSWER)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId)
            .addParameter(PARAM_QUESTION_ID, questionId)
            .addParameter(PARAM_ANSWER_ID, answerId);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        return (Answer) response.getReturnObject();

    }

    public void updateArea(Token userToken, Area area) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(UPDATE_AREA)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA, area);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        

    }

    public void updateQuestion(Token userToken, long areaId, Question question) throws 
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
            Request request = new Request(UPDATE_QUESTION)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId)
            .addParameter(PARAM_QUESTION, question);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        
    }

    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws  
        ServiceNotAvailableException, IllegalParameterException, InvalidTokenException{
            Request request = new Request(UPDATE_ANSWER)
            .addParameter(PARAM_USER_TOKEN, userToken)
            .addParameter(PARAM_AREA_ID, areaId)
            .addParameter(PARAM_QUESTION_ID, questionId)
            .addParameter(PARAM_ANSWER, answer);
            Response response = sendAndGetResponse(request);
        if (response.isException()) {
            logger.error(response.getExceptionObject().getMessage());
            rethrowStandardExceptions(response);
        }
        

    }

        
}
