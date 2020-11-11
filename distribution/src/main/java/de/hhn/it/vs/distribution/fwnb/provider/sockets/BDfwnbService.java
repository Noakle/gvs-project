package de.hhn.it.vs.distribution.fwnb.provider.sockets;

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
import java.util.List;


public class BDfwnbService extends AbstractServeOneClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(BDfwnbService.class);

  WnckQnAService qnAService;

  public static final String CREATE_AREA = "qna.createarea";
  public static final String CREATE_QUESTION = "qna.createquestion";
  public static final String CREATE_ANSWER = "qna.createanswer";
  public static final String GET_AREA_IDS = "qna.getareaids";
  public static final String GET_AREA = "qna.getarea";
  public static final String GET_QUESTION_IDS = "qna.getquestionsids";
  public static final String GET_QUESTION = "qna.getquestions";
  public static final String GET_ANSWER_IDS = "qna.getanswerids";
  public static final String GET_ANSWER = "qna.getanswer";
  public static final String UPDATE_AREA = "qna.updatearea";
  public static final String UPDATE_QUESTION = "qna.updatequestion";
  public static final String UPDATE_ANSWER = "qna.updateanswer";

  public static final String PARAM_USER_TOKEN = "param.usertoken";
  public static final String PARAM_AREA = "param.area";
  public static final String PARAM_AREA_ID = "param.areaid";
  public static final String PARAM_QUESTION = "param.question";
  public static final String PARAM_QUESTION_ID = "param.questionid";
  public static final String PARAM_ANSWER = "param.answer";
  public static final String PARAM_ANSWER_ID = "param.amswerid";

  /**
   * Creates new thread to work on a single client request.
   *
   * @param socket socket connected with the client
   * @param service service to be used for the request
   * @throws IOException when problems with the socket connection occur
   * @throws IllegalParameterException when called with null references
   */
  public BDfwnbService(final Socket socket, final Object service) throws IOException, IllegalParameterException {
    super(socket, service);
    qnAService = (WnckQnAService) service;
  }

  @Override
  public void run() {
    Request request = null;
    Response response = null;
    try {
      request = (Request) in.readObject();

      String methodToCall = request.getMethodToCall();
      switch (methodToCall) {
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
          ServiceNotAvailableException noMethodException = new ServiceNotAvailableException(
                  "Unknown method name: " + methodToCall);
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

  private Response updateAnswer(Request request) throws Exception {
    qnAService.updateAnswer(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID),
            (long) request.getParameter(PARAM_QUESTION_ID),
            (Answer) request.getParameter(PARAM_ANSWER)
    );
    return new Response(request);
  }

  private Response updateQuestion(Request request) throws Exception {
    qnAService.updateQuestion(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID),
            (Question) request.getParameter(PARAM_QUESTION)
    );
    return new Response(request);
  }

  private Response updateArea(Request request) throws Exception {
    qnAService.updateArea(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Area) request.getParameter(PARAM_AREA)
    );
    return new Response(request);
  }

  private Response getAnswer(Request request) throws Exception {
    Answer ans = null;
    ans = qnAService.getAnswer(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID),
            (long) request.getParameter(PARAM_QUESTION_ID),
            (long) request.getParameter(PARAM_ANSWER_ID)
    );
    Response r = new Response(request);
    r.setReturnObject(ans);

    return r;
  }

  private Response getAnswerIds(Request request) throws Exception {
    List<Long> l = null;
    l = qnAService.getAnswerIds(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID),
            (long) request.getParameter(PARAM_QUESTION_ID)
    );
    Response r = new Response(request);
    r.setReturnObject(l);

    return r;
  }

  private Response getQuestion(Request request) throws Exception {
    Question q = null;
    q = qnAService.getQuestion(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID),
            (long) request.getParameter(PARAM_QUESTION_ID)
    );
    Response r = new Response(request);
    r.setReturnObject(q);

    return r;
  }

  private Response getQuestionIds(Request request) throws Exception {
    List<Long> l = null;
    l = qnAService.getQuestionIds(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID)
    );
    Response r = new Response(request);
    r.setReturnObject(l);

    return r;
  }

  private Response getArea(Request request) throws Exception {
    Area ar = null;

    ar = qnAService.getArea(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID)
    );
    Response r = new Response(request);
    r.setReturnObject(ar);

    return r;
  }

  private Response getAreaIds(Request request) throws Exception {
    List<Long> l = null;
    l = qnAService.getAreaIds(
            (Token) request.getParameter(PARAM_USER_TOKEN)
    );
    Response r = new Response(request);
    r.setReturnObject(l);

    return r;

  }

  private Response createAnswer(Request request) throws Exception {
    long t;

    t = qnAService.createAnswer(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID),
            (long) request.getParameter(PARAM_QUESTION_ID),
            (Answer) request.getParameter(PARAM_ANSWER));
    Response r = new Response(request);
    r.setReturnObject(t);

    return r;
  }

  private Response createQuestion(Request request) throws Exception {
    long t;

    t = qnAService.createQuestion(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (long) request.getParameter(PARAM_AREA_ID),
            (Question) request.getParameter(PARAM_QUESTION));
    Response r = new Response(request);
    r.setReturnObject(t);
    return r;
  }

  private Response createArea(Request request) throws Exception {
    long t;

    t = qnAService.createArea(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Area) request.getParameter(PARAM_AREA));
    Response r = new Response(request);
    r.setReturnObject(t);
    return r;
  }
}
