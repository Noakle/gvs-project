package de.hhn.it.vs.distribution.qna.provider.team_rocket.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class QnAServiceServeOneClient extends AbstractServeOneClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(QnAServiceServeOneClient.class);

  BDQnAService qnaService;

  public static final String CREATE_AREA = "qna.createArea";
  public static final String CREATE_QUESTION = "qna.createQuestion";
  public static final String CREATE_ANSWER = "qna.createAnswer";
  public static final String GET_AREA_IDS = "qna.getAreaIds";
  public static final String GET_AREA = "qna.getArea";
  public static final String GET_QUESTION_IDS = "qna.getQuestionIds";
  public static final String GET_QUESTION = "qna.getQuestion";
  public static final String GET_ANSWER_IDS = "qna.getAnswerId";
  public static final String GET_ANSWER = "qna.getAnswer";
  public static final String UPDATE_AREA = "qna.updateArea";
  public static final String UPDATE_QUESTION = "qna.updateQuestion";
  public static final String UPDATE_ANSWER = "qna.updateAnswer";
  public static final String PARAM_USER_TOKEN = "param.userToken";
  public static final String PARAM_AREA_ID = "param.areaId";
  public static final String PARAM_AREA = "param.area";
  public static final String PARAM_QUESTION_ID = "param.questionId";
  public static final String PARAM_QUESTION = "param.question";
  public static final String PARAM_ANSWER_ID = "param.answerId";
  public static final String PARAM_ANSWER = "param.answer";


  /**
   * Creates new thread to work on a single client request.
   *
   * @param socket  socket connected with the client
   * @param service service to be used for the request
   * @throws IOException               when problems with the socket connection occur
   * @throws IllegalParameterException when called with null references
   */
  public QnAServiceServeOneClient(final Socket socket, final Object service) throws
          IOException, IllegalParameterException {
    super(socket, service);
    qnaService = (BDQnAService) service;
  }

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
                  ("unknown Method: " + methodCall);
          response = new Response(request, noMethodException);
      }
    } catch (Exception exception) {
      response = new Response(request, exception);
    }

    try {
      out.writeObject(response);
    } catch (IOException ioException) {
      logger.error("Problems writing the response: " + ioException.getMessage());
    }
  }

  private Response createArea(final Request request) throws Exception {
    Long areaId = qnaService.createArea(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Area) request.getParameter(PARAM_AREA));

    return new Response(request, areaId);
  }

  private Response createQuestion(final Request request) throws Exception {
    Long questionId = qnaService.createQuestion(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID),
            (Question) request.getParameter(PARAM_QUESTION));

    return new Response(request, questionId);
  }

  private Response createAnswer(final Request request) throws Exception {
    Long answerId = qnaService.createAnswer(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID),
            (Long) request.getParameter(PARAM_QUESTION_ID),
            (Answer) request.getParameter(PARAM_ANSWER));

    return new Response(request, answerId);
  }

  private Response getAreaIds(final Request request) throws Exception {
    List<Long> areaIds = qnaService.getAreaIds((Token) request.getParameter(PARAM_USER_TOKEN));

    return new Response(request, areaIds);
  }

  private Response getArea(final Request request) throws Exception {
    Area area = qnaService.getArea(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID));

    return new Response(request, area);
  }

  private Response getQuestionIds(final Request request) throws Exception {
    List<Long> questionIds = qnaService.getQuestionIds(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID));

    return new Response(request, questionIds);
  }

  private Response getQuestion(final Request request) throws Exception {
    Question question = qnaService.getQuestion(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID),
            (Long) request.getParameter(PARAM_QUESTION_ID));

    return new Response(request, question);
  }

  private Response getAnswerIds(final Request request) throws Exception {
    List<Long> answerIds = qnaService.getAnswerIds(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID),
            (Long) request.getParameter(PARAM_QUESTION_ID));

    return new Response(request, answerIds);
  }

  private Response getAnswer(final Request request) throws Exception {
    Answer answer = qnaService.getAnswer(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID),
            (Long) request.getParameter(PARAM_QUESTION_ID),
            (Long) request.getParameter(PARAM_ANSWER_ID));

    return new Response(request, answer);
  }

  private Response updateArea(final Request request) throws Exception {
    qnaService.updateArea(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Area) request.getParameter(PARAM_AREA));

    return new Response(request);
  }

  private Response updateQuestion(final Request request) throws Exception {
    qnaService.updateQuestion(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID),
            (Question) request.getParameter(PARAM_QUESTION));

    return new Response(request);
  }

  private Response updateAnswer(final Request request) throws Exception {
    qnaService.updateAnswer(
            (Token) request.getParameter(PARAM_USER_TOKEN),
            (Long) request.getParameter(PARAM_AREA_ID),
            (Long) request.getParameter(PARAM_QUESTION_ID),
            (Answer) request.getParameter(PARAM_ANSWER));

    return new Response(request);
  }

}
