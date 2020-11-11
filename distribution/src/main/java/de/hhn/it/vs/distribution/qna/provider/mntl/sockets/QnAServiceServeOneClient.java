package de.hhn.it.vs.distribution.qna.provider.mntl.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
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

/**
 * Class Service Serve One Client
 * @author Leibl, Nauendorf
 * @version 2020-11-10
 */
public class QnAServiceServeOneClient extends AbstractServeOneClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(QnAServiceServeOneClient.class);


  public static final String CREATE_ANSWER = "managment.create_answer";
  public static final String CREATE_AREA = "managment.create_area";
  public static final String CREATE_QUESTION = "managment.create_question";

  public static final String PARAM_USER_TOKEN = "param.usertoken";
  public static final String PARAM_AREA_ID = "param.area_id";
  public static final String PARAM_QUESTION_ID ="param.question_id";
  public static final String PARAM_QUESTION = "param.question";
  public static final String PARAM_ANSWER = "param.answer";
  public static final String PARAM_AREA = "param.area";
  public static final String PARAM_ANSWER_ID = "param.answer_id";

  public static final String GET_AREA_IDS = "get.area_ids";
  public static final String GET_AREA = "get.area";
  public static final String GET_QUESTION = "get.question";
  public static final String GET_ANSWER = "get.answer";
  public static final String GET_QUESTION_IDS = "get.question_ids";
  public static final String GET_ANSWER_IDS = "get.answer_ids";

  public static final String UPDATE_AREA = "update.area";
  public static final String UPDATE_QUESTION = "update.question";
  public static final String UPDATE_ANSWER = "update.answer";

  WnckQnAService qnAService;

  public QnAServiceServeOneClient(final Socket socket, final Object service) throws IOException, IllegalParameterException {
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


