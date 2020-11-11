package de.hhn.it.vs.distribution.qna.provider.raat.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class QnaServiceServeOneClient extends AbstractServeOneClient {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(QnaServiceServeOneClient.class);

    private final BDQnAService qnAService;

    /**
     * Creates new thread to work on a single client request.
     *
     * @param socket  socket connected with the client
     * @param service service to be used for the request
     * @throws IOException               when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public QnaServiceServeOneClient(Socket socket, Object service) throws IOException, IllegalParameterException {
        super(socket, service);
        qnAService = (BDQnAService) service;
    }

    public enum QnAServiceRequestMethods {
        CREATE_AREA,
        CREATE_QUESTION,
        CREATE_ANSWER,
        GET_AREA_IDS,
        GET_AREA,
        GET_QUESTION_IDS,
        GET_QUESTION,
        GET_ANSWER_IDS,
        GET_ANSWER,
        UPDATE_AREA,
        UPDATE_QUESTION,
        UPDATE_ANSWER
    }

    public enum QnAServiceRequestParameters {
        USER_TOKEN,
        AREA,
        AREA_ID,
        QUESTION,
        QUESTION_ID,
        ANSWER,
        ANSWER_ID
    }


    @Override
    public void run() {
        Request request = null;
        Response response;
        try {
            request = (Request) in.readObject();

            QnAServiceRequestMethods method = QnAServiceRequestMethods.valueOf(request.getMethodToCall());

            switch (method) {
                case CREATE_AREA:
                    response = createArea(request);
                    break;
                case CREATE_QUESTION:
                    response = createQuestion(request);
                    break;
                case CREATE_ANSWER:
                    response = createAnswer(request);
                    break;
                case GET_AREA_IDS:
                    response = getAreaIds(request);
                    break;
                case GET_AREA:
                    response = getArea(request);
                    break;
                case GET_QUESTION_IDS:
                    response = getQuestionIds(request);
                    break;
                case GET_QUESTION:
                    response = getQuestion(request);
                    break;
                case GET_ANSWER_IDS:
                    response = getAnswerIds(request);
                    break;
                case GET_ANSWER:
                    response = getAnswer(request);
                    break;
                case UPDATE_AREA:
                    response = updateArea(request);
                    break;
                case UPDATE_QUESTION:
                    response = updateQuestion(request);
                    break;
                case UPDATE_ANSWER:
                    response = updateAnswer(request);
                    break;
                default:
                    // Create a response with a ServiceNotAvailableException
                    ServiceNotAvailableException noMethodException =
                            new ServiceNotAvailableException("Method with name unknown. " + method);
                    response = new Response(request, noMethodException);
            }
        } catch (Exception e) {
            response = new Response(request, e);
        }

        try {
            out.writeObject(response);
        } catch (IOException e1) {
            logger.error("Problems writing the response: " + e1.getMessage());
        }
    }

    private Response createAnswer(Request request) throws Exception {
        long answerID = qnAService.createAnswer(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.QUESTION_ID.toString()),
                (Answer) request.getParameter(QnAServiceRequestParameters.ANSWER.toString()));
        return new Response(request, answerID);
    }

    private Response updateAnswer(final Request request) throws Exception {
        qnAService.updateAnswer(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.QUESTION_ID.toString()),
                (Answer) request.getParameter(QnAServiceRequestParameters.ANSWER.toString()));
        return new Response(request);
    }

    private Response updateQuestion(final Request request) throws Exception {
        qnAService.updateQuestion(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()),
                (Question) request.getParameter(QnAServiceRequestParameters.QUESTION.toString()));
        return new Response(request);
    }

    private Response updateArea(final Request request) throws Exception {
        qnAService.updateArea(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (Area) request.getParameter(QnAServiceRequestParameters.AREA.toString()));
        return new Response(request);
    }

    private Response getAnswer(final Request request) throws Exception {
        Answer answer = qnAService.getAnswer(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.QUESTION_ID.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.ANSWER_ID.toString()));
        return new Response(request, answer);
    }

    private Response getAnswerIds(final Request request) throws Exception {
        List<Long> answerIds = qnAService.getAnswerIds(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.QUESTION_ID.toString()));
        return new Response(request, answerIds);
    }

    private Response getQuestion(final Request request) throws Exception {
        Question question = qnAService.getQuestion(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.QUESTION_ID.toString()));
        return new Response(request, question);
    }

    private Response getQuestionIds(final Request request) throws Exception {
        List<Long> questionIds = qnAService.getQuestionIds(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()));
        return new Response(request, questionIds);
    }

    private Response getArea(final Request request) throws Exception {
        Area area = qnAService.getArea(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()),
                (long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString()));
        return new Response(request, area);
    }

    private Response getAreaIds(final Request request) throws Exception {
        List<Long> areaIds = qnAService.getAreaIds(
                (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString()));
        return new Response(request, areaIds);
    }

    private Response createQuestion(final Request request) throws Exception{
        Token token = (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString());
        Question question = (Question) request.getParameter( QnAServiceRequestParameters.QUESTION.toString());
        long areaID = (Long) request.getParameter(QnAServiceRequestParameters.AREA_ID.toString());
        long questionID = qnAService.createQuestion(token, areaID, question);
        return new Response(request, questionID);
    }

    private Response createArea(final Request request) throws Exception {
        Token token = (Token) request.getParameter(QnAServiceRequestParameters.USER_TOKEN.toString());
        Area area = (Area) request.getParameter( QnAServiceRequestParameters.AREA.toString());
        long areaID = qnAService.createArea(token, area);
        return new Response(request, areaID);
    }
}
