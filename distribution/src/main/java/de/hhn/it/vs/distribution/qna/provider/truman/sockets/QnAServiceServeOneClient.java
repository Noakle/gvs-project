package de.hhn.it.vs.distribution.qna.provider.truman.sockets;

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

public class QnAServiceServeOneClient extends AbstractServeOneClient {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDQnAServiceViaSockets.class);

    public static final String CREATE_AREA = "qna.createarea";
    public static final String CREATE_QUESTION = "qna.createquestion";
    public static final String CREATE_ANSWER = "qna.createanswer";
    public static final String GET_AREA_IDS = "qna.getareaids";
    public static final String GET_AREA = "qna.getarea";
    public static final String GET_QUESTION_IDS = "qna.getquestionids";
    public static final String GET_QUESTION = "qna.getquestion";
    public static final String GET_ANSWER_IDS = "qna.getanswerids";
    public static final String GET_ANSWER = "qna.getanswer";
    public static final String UPDATE_AREA = "qna.updatearea";
    public static final String UPDATE_QUESTION = "qna.updatequestion";
    public static final String UPDATE_ANSWER = "qna.updateanswer";

    public static final String PARAM_USER_TOKEN = "param.usertoken";
    public static final String PARAM_AREA = "param.area";
    public static final String PARAM_AREA_ID = "param.areaid";
    public static final String PARAM_QUESTION = "param.question";
    public static final String PARAM_QUESTION_ID = "param.quesionid";
    public static final String PARAM_ANSWER = "param.answer";
    public static final String PARAM_ANSWER_ID = "param.answerid";

    BDQnAService bdQnAService;



    /**
     * Creates new thread to work on a single client request.
     *
     * @param socket  socket connected with the client
     * @param service service to be used for the request
     * @throws IOException               when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public QnAServiceServeOneClient(Socket socket, Object service) throws IOException, IllegalParameterException {
        super(socket, service);
        bdQnAService = (BDQnAService) service;
    }

    @Override
    public void run() {
        Request request = null;
        Response response = null;
        try {
            request = (Request) in.readObject();

            String methodCall = request.getMethodToCall();

            switch (methodCall) {
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
                    ServiceNotAvailableException noMethodException = new ServiceNotAvailableException
                            ("Method with name unknown. " + methodCall);
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

    private Response createArea(final Request request) throws Exception {
        long  areaID = bdQnAService.createArea((Token) request.getParameter(PARAM_USER_TOKEN),
                (Area) request.getParameter(PARAM_AREA));
        return new Response(request, areaID);
    }

    private Response createQuestion(final Request request) throws Exception {
        long questionID = bdQnAService.createQuestion((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID),
                (Question) request.getParameter(PARAM_QUESTION));
        return new Response(request, questionID);
    }

    private Response createAnswer(final Request request) throws Exception {
        long  answerID = bdQnAService.createAnswer((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID),
                (Long) request.getParameter(PARAM_QUESTION_ID),
                (Answer) request.getParameter(PARAM_ANSWER));
        return new Response(request, answerID);
    }

    private Response getAreaIds(final Request request) throws Exception {
        List<Long> areaID = bdQnAService.getAreaIds((Token) request.getParameter(PARAM_USER_TOKEN));
        return new Response(request, areaID);
    }

    private Response getArea(final Request request) throws Exception {
        Area  area = bdQnAService.getArea((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID));
        return new Response(request, area);
    }

    private Response getQuestionIds(final Request request) throws Exception {
        List<Long> questionIds = bdQnAService.getQuestionIds((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID));
        return new Response(request, questionIds);
    }

    private Response getQuestion(final Request request) throws Exception {
        Question question = bdQnAService.getQuestion((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID),
                (Long) request.getParameter(PARAM_QUESTION_ID));
        return new Response(request, question);
    }

    private Response getAnswerIds(final Request request) throws Exception {
        List<Long>  answerIds = bdQnAService.getAnswerIds((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID),
                (Long) request.getParameter(PARAM_QUESTION_ID));
        return new Response(request, answerIds);
    }

    private Response getAnswer(final Request request) throws Exception {
        Answer  answer = bdQnAService.getAnswer((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID),
                (Long) request.getParameter(PARAM_QUESTION_ID),
                (Long) request.getParameter(PARAM_ANSWER_ID));
        return new Response(request, answer);
    }

    private Response updateArea(final Request request) throws Exception {
        bdQnAService.updateArea((Token) request.getParameter(PARAM_USER_TOKEN),
                (Area) request.getParameter(PARAM_AREA));
        return new Response(request);
    }

    private Response updateQuestion(final Request request) throws Exception {
        bdQnAService.updateQuestion((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID),
                (Question) request.getParameter(PARAM_QUESTION));
        return new Response(request);
    }

    private Response updateAnswer(final Request request) throws Exception {
        bdQnAService.updateAnswer((Token) request.getParameter(PARAM_USER_TOKEN),
                (Long) request.getParameter(PARAM_AREA_ID),
                (Long) request.getParameter(PARAM_QUESTION_ID),
                (Answer) request.getParameter(PARAM_ANSWER));
        return new Response(request);
    }



}
