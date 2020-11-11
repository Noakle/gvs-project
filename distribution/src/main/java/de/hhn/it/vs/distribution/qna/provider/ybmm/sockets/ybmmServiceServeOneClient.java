package de.hhn.it.vs.distribution.qna.provider.ybmm.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ybmmServiceServeOneClient extends AbstractServeOneClient {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(UserManagementServiceServeOneClient.class);

    BDQnAService qnAService;


    public static final String CREATE_AREA = "qna.createarea";
    public static final String CREATE_QUESTION = "qna.createqetsion";
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
    public static final String PARAM_ANSWER = "param.answer";
    public static final String PARAM_QUESTION_ID = "param.questionid";
    public static final String PARAM_ANSWER_ID = "param.answerid";


    /**
     * Creates new thread to work on a single client request.
     *
     * @param socket  socket connected with the client
     * @param service service to be used for the request
     * @throws IOException               when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public ybmmServiceServeOneClient(Socket socket, Object service) throws IOException, IllegalParameterException {
        super(socket, service);
        qnAService = (WnckQnAService) service;
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

        // terminate the socket and stream connection
        disconnect();

    }


    private Response createArea(final Request request) throws Exception {
        long l = qnAService.createArea(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (Area) request.getParameter(PARAM_AREA));
        return new Response(request, l);

    }

    private Response createQuestion(final Request request) throws Exception {
        long l = qnAService.createQuestion(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID),
                (Question) request.getParameter(PARAM_QUESTION));
        return new Response(request, l);

    }

    private Response createAnswer(final Request request) throws Exception {
        long l = qnAService.createAnswer(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID),
                (long) request.getParameter(PARAM_QUESTION_ID),
                (Answer) request.getParameter(PARAM_ANSWER));
        return new Response(request, l);

    }

    private Response getArea(final Request request) throws Exception {
        Area area = qnAService.getArea(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID)
        );
        return new Response(request, area);

    }

    private Response getAreaIds(final Request request) throws Exception {
        List<Long> list = qnAService.getAreaIds(
                (Token) request.getParameter(PARAM_USER_TOKEN)
        );
        return new Response(request, list);


    }

    private Response getQuestion(final Request request) throws Exception {
        Question question = qnAService.getQuestion(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID),
                (long) request.getParameter(PARAM_QUESTION_ID)
        );
        return new Response(request, question);

    }

    private Response getQuestionIds(final Request request) throws Exception {
        List<Long> list = qnAService.getQuestionIds(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID)
        );
        return new Response(request, list);

    }

    private Response getAnswer(final Request request) throws Exception {
        Answer answer = qnAService.getAnswer(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID),
                (long) request.getParameter(PARAM_QUESTION_ID),
                (long) request.getParameter(PARAM_ANSWER_ID)
        );
        return new Response(request, answer);

    }

    private Response getAnswerIds(final Request request) throws Exception {
        List<Long> list = qnAService.getAnswerIds(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID),
                (long) request.getParameter(PARAM_QUESTION_ID)
        );
        return new Response(request, list);

    }

    private Response updateArea(final Request request) throws Exception {
        qnAService.updateArea(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (Area) request.getParameter(PARAM_AREA)
        );
        return new Response(request);
    }

    private Response updateQuestion(final Request request) throws Exception {
        qnAService.updateQuestion(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID),
                (Question) request.getParameter(PARAM_QUESTION)
        );
        return new Response(request);
    }

    private Response updateAnswer(final Request request) throws Exception {
        qnAService.updateAnswer(
                (Token) request.getParameter(PARAM_USER_TOKEN),
                (long) request.getParameter(PARAM_AREA_ID),
                (long) request.getParameter(PARAM_QUESTION_ID),
                (Answer) request.getParameter(PARAM_ANSWER)
        );
        return new Response(request);
    }

}
