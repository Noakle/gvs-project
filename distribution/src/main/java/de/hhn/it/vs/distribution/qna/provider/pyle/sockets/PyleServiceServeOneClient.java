package de.hhn.it.vs.distribution.qna.provider.pyle.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.net.Socket;

public class PyleServiceServeOneClient extends AbstractServeOneClient {

    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(PyleServiceServeOneClient.class);

    WnckQnAService qnAService;

    public static final String CREATE_AREA = "qna.createarea";
    public static final String CREATE_QUESTION = "qna.createquestion";
    public static final String CREATE_ANSWERE = "qna.createanswere";
    public static final String GET_AREA_IDS = "qna.getareaids";
    public static final String GET_AREA = "qna.getarea";
    public static final String GET_QUESTION_IDS = "qna.getquestionsids";
    public static final String GET_QUESTION = "qna.getquestions";
    public static final String GET_ANSWER_IDS = "qna.getanswerids";
    public static final String GET_ANSWER = "qna.getanswere";
    public static final String UPDATE_AREA = "qna.updatearea";
    public static final String UPDATE_QUESTION = "qna.updatequestion";
    public static final String UPDATE_ANSWERE = "qna.updateanswere";

    public static final String PARAM_USER_TOKEN = "param.usertoken";
    public static final String PARAM_AREA = "param.area";
    public static final String PARAM_AREA_ID = "param.areaid";
    public static final String PARAM_QUESTION = "param.question";
    public static final String PARAM_QUESTION_ID = "param.questionid";
    public static final String PARAM_ANSWERE = "param.answere";
    public static final String PARAM_ANSWERE_ID = "param.amswereid";

    /**
     * Creates new thread to work on a single client request.
     *
     * @param socket  socket connected with the client
     * @param service service to be used for the request
     * @throws IOException               when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public PyleServiceServeOneClient(Socket socket, Object service) throws IOException, IllegalParameterException {
        super(socket, service);
        this.qnAService = (WnckQnAService)service;
    }


    public void run()
    {
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
                case CREATE_ANSWERE:
                    response = createAnswer(request);
                    break;
                /*case CHANGE_NAME:
                    response = changeName(request);
                    break;
                case GET_USERS:
                    response = getUsers(request);
                    break;*/
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

    private Response createAnswer(Request request) throws Exception{
        long createanswer = qnAService.createAnswer((Token) request.getParameter(PARAM_USER_TOKEN),
                                                   (long)request.getParameter(PARAM_AREA_ID),
                                                    (long)request.getParameter(PARAM_QUESTION_ID),
                                                       (Answer) request.getParameter(PARAM_ANSWERE));

        return  new Response(request, createanswer);
    }

    private Response createQuestion(Request request) throws Exception {
        long createquestion = qnAService.createQuestion((Token) request.getParameter(PARAM_USER_TOKEN),
                (long)request.getParameter(PARAM_AREA_ID),
                (Question) request.getParameter(PARAM_QUESTION));
        return new Response(request, createquestion);
    }

    private Response createArea(Request request) throws Exception {
        long areat = qnAService.createArea((Token) request.getParameter(PARAM_USER_TOKEN),
                (Area) request.getParameter(PARAM_AREA));
        return  new Response(request, areat);
    }

    /*private Response resolveUser(final Request request) throws Exception {
        User user = userManagementService.resolveUser((Token) request.getParameter
                (PARAM_USER_TOKEN));
        return new Response(request, user);
    }*/


}
