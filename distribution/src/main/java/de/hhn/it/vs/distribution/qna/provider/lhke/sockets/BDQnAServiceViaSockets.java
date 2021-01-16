package de.hhn.it.vs.distribution.qna.provider.lhke.sockets;

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

import static de.hhn.it.vs.distribution.qna.provider.lhke.sockets.QnAServiceServeOneClient.*;

public class BDQnAServiceViaSockets implements BDQnAService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDQnAServiceViaSockets.class);

    private String hostname;
    private int portNumber;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public BDQnAServiceViaSockets(final String hostname, final int portNumber) {
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
    public long createArea(final Token userToken, final Area area) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {
        Request request = new Request(CREATE_AREA)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA, area);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (long) response.getReturnObject();
    }

    @Override
    public long createQuestion(final Token userToken, final long areaId, final Question question) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(CREATE_QUESTION)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION, question);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (long) response.getReturnObject();
    }

    @Override
    public long createAnswer(final Token userToken, final long areaId, final long questionId, final Answer answer) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(CREATE_ANSWER)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId)
                .addParameter(PARAM_ANSWER, answer);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (long) response.getReturnObject();
    }

    @Override
    public List<Long> getAreaIds(final Token userToken) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(GET_ANSWER_IDS)
                .addParameter(PARAM_USER_TOKEN, userToken);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Area getArea(final Token userToken, final long areaId) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(GET_AREA)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (Area) response.getReturnObject();
    }

    @Override
    public List<Long> getQuestionIds(final Token userToken, final long areaId) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(GET_QUESTION_IDS)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Question getQuestion(final Token userToken, final long areaId, final long questionId) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(GET_QUESTION)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (Question) response.getReturnObject();
    }

    @Override
    public List<Long> getAnswerIds(final Token userToken, final long areaId, final long questionId) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(GET_ANSWER_IDS)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (List<Long>) response.getReturnObject();
    }

    @Override
    public Answer getAnswer(final Token userToken, final long areaId, final long questionId, final long answerId) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(GET_ANSWER)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, answerId)
                .addParameter(PARAM_QUESTION_ID, questionId)
                .addParameter(PARAM_ANSWER_ID, answerId);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return (Answer) response.getReturnObject();
    }

    @Override
    public void updateArea(final Token userToken, final Area area) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(UPDATE_AREA)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA, area);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return;
    }

    @Override
    public void updateQuestion(final Token userToken, final long areaId, final Question question) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(UPDATE_QUESTION)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION, question);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return;
    }

    @Override
    public void updateAnswer(final Token userToken, final long areaId, final long questionId, final Answer answer) throws
            ServiceNotAvailableException, IllegalParameterException,
            InvalidTokenException {

        Request request = new Request(UPDATE_ANSWER)
                .addParameter(PARAM_USER_TOKEN, userToken)
                .addParameter(PARAM_AREA_ID, areaId)
                .addParameter(PARAM_QUESTION_ID, questionId)
                .addParameter(PARAM_ANSWER, answer);

        Response response = sendAndGetResponse(request);

        if (response.isException()) {
            Exception exceptionFromRemote = response.getExceptionObject();

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
        return;
    }
}
