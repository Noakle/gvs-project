package de.hhn.it.vs.distribution.qna.provider.truman.sockets;

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

import static de.hhn.it.vs.distribution.qna.provider.truman.sockets.QnAServiceServeOneClient.*;

public class BDQnAServiceViaSockets implements BDQnAService {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDQnAServiceViaSockets.class);

    private final String hostname;
    private final int portNumber;
    private Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;


    public BDQnAServiceViaSockets(String hostname, int portNumber){
            this.hostname = hostname;
            this.portNumber = portNumber;
    }

    public void connect() throws ServiceNotAvailableException {
        logger.info("Attempting to connect to " + hostname + " via Port " + portNumber + "...");
        try {
            socket = new Socket(hostname, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            logger.info("Successfully connected!");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServiceNotAvailableException("Cannot connect to host " + hostname +
                    " with port " + portNumber + ".", e);
        }
    }

    public void disconnect(){
        try {
            if(in != null) {
                in.close();
                in = null;
            }
            if(out != null) {
                out.close();
                out = null;
            }

            if(socket != null) {
                socket.close();
                socket = null;
            }
            logger.info("Disconnected from service.");
        } catch (IOException e) {
            logger.warn("Problems disconnecting from service: " + e.getMessage());
        }
    }

    private Response sendAndGetResponse(final Request request) throws ServiceNotAvailableException {
        // send request and wait for the response
        try {
            connect();
            logger.debug("sending request " + request);
            out.writeObject(request);
            logger.debug("wait for response ...");
            Response response = (Response) in.readObject();
            logger.debug("Got response " + response);
            disconnect();
            return response;
        } catch (Exception ex) {
            throw new ServiceNotAvailableException("Communication problem: " + ex.getMessage(), ex);
        }
    }

    private void checkResponseExceptions(Exception exceptionFromRemote) throws IllegalParameterException,
            ServiceNotAvailableException, InvalidTokenException {

            if (exceptionFromRemote instanceof IllegalParameterException) {
                throw (IllegalParameterException) exceptionFromRemote;
            }
            if (exceptionFromRemote instanceof ServiceNotAvailableException) {
                throw (ServiceNotAvailableException) exceptionFromRemote;
            }
            if (exceptionFromRemote instanceof InvalidTokenException) {
                throw (InvalidTokenException) exceptionFromRemote;
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
    public long createArea(Token userToken, Area area) throws ServiceNotAvailableException,
            IllegalParameterException, InvalidTokenException {

        Request request = new Request(CREATE_AREA)
                        .addParameter(PARAM_USER_TOKEN, userToken)
                        .addParameter(PARAM_AREA, area);
        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (long) response.getReturnObject();
    }

    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException,
            IllegalParameterException, InvalidTokenException {

            Request request = new Request(CREATE_QUESTION)
                    .addParameter(PARAM_USER_TOKEN, userToken)
                    .addParameter(PARAM_AREA_ID, areaId)
                    .addParameter(PARAM_QUESTION, question);
            Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

            return (long) response.getReturnObject();
    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws
            ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

        Request request = new Request(CREATE_ANSWER)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId)
                .addParameter(PARAM_ANSWER, answer);
        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (long) response.getReturnObject();
    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(GET_AREA_IDS)
                .addParameter(PARAM_USER_TOKEN, userToken);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, 
            IllegalParameterException, InvalidTokenException {

        Request request = new Request(GET_AREA)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (Area) response.getReturnObject();
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, 
            IllegalParameterException, InvalidTokenException {
        
        Request request = new Request(GET_QUESTION_IDS)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, 
            IllegalParameterException, InvalidTokenException {

        Request request = new Request(GET_QUESTION)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (Question) response.getReturnObject();
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, 
            IllegalParameterException, InvalidTokenException {

        Request request = new Request(GET_ANSWER_IDS)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws 
            ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        
        Request request = new Request(GET_ANSWER)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId)
                .addParameter(PARAM_ANSWER_ID, answerId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }

        return (Answer) response.getReturnObject();
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(UPDATE_AREA)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA, area);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }
    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, 
            IllegalParameterException, InvalidTokenException {

        Request request = new Request(UPDATE_QUESTION)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION, question);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }
    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws 
            ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

        Request request = new Request(UPDATE_ANSWER)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId)
                .addParameter(PARAM_ANSWER, answer);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            checkResponseExceptions(response.getExceptionObject());
        }
    }
}
