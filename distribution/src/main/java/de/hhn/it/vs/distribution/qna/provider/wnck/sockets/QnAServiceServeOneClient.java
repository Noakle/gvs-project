package de.hhn.it.vs.distribution.qna.provider.wnck.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
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
          org.slf4j.LoggerFactory.getLogger(QnAServiceServeOneClient.class);

  private BDQnAService qnAService;

  public static final String CREATE_AREA = "qna.area.create";
  public static final String CREATE_QUESTION = "qna.question.create";
  public static final String CREATE_ANSWER = "qna.answer.create";
  public static final String GET_AREA_IDS = "qna.areaids.get";
  public static final String GET_QUESTION_IDS = "qna.questionids.get";
  public static final String GET_ANSWER_IDS = "qna.answerids.get";
  public static final String GET_AREA = "qna.area.get";
  public static final String GET_QUESTION = "qna.question.get";
  public static final String GET_ANSWER = "qna.answer.get";
  public static final String UPDATE_AREA = "qna.area.update";
  public static final String UPDATE_QUESTION = "qna.question.update";
  public static final String UPDATE_ANSWER = "qna.answer.update";
  public static final String USER_TOKEN = "userToken";
  public static final String TITLE = "title";
  public static final String DESCRIPTION = "description";
  public static final String AREA = "area";
  public static final String QUESTION = "question";
  public static final String ANSWER = "answer";
  public static final String AREA_ID = "areaId";
  public static final String QUESTION_ID = "questionId";
  public static final String ANSWER_ID = "answerId";


  /**
   * Creates new thread to work on a single client request.
   *
   * @param socket  socket connected with the client
   * @param service service to be used for the request
   * @throws IOException               when problems with the socket connection occur
   * @throws IllegalParameterException when called with null references
   */
  public QnAServiceServeOneClient(final Socket socket, final Object service) throws IOException,
          IllegalParameterException {
    super(socket, service);
    qnAService = (BDQnAService) service;
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
          // Create a response with a serviceNotAvailableException
          ServiceNotAvailableException noMethodException = new ServiceNotAvailableException
                  ("Method with name unknown. " + methodCall);
          response = new Response(request, noMethodException);

      }
    } catch (Exception e) {
      response = new Response(request, e);
    }

    try {
      out.writeObject(response);
    } catch (IOException e) {
      logger.error("Problem writing response: " + e.getMessage());
    }

  }

  private Response createArea(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    long id = (long) qnAService.createArea((Token) request.getParameter(USER_TOKEN),
            (Area) request.getParameter(AREA));
    return new Response(request, id);
  }

  private Response createQuestion(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    long id = (long) qnAService.createQuestion(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID),
            (Question) request.getParameter(QUESTION));
    return new Response(request, id);
  }

  private Response createAnswer(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    long id = (long) qnAService.createAnswer(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID),
            (long) request.getParameter(QUESTION_ID),
            (Answer) request.getParameter(ANSWER)
    );
    return new Response(request, id);
  }

  private Response getAreaIds(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    List<Long> idList = qnAService.getAreaIds(
            (Token) request.getParameter(USER_TOKEN)
    );
    return new Response(request, idList);
  }

  private Response getQuestionIds(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    List<Long> idList = qnAService.getQuestionIds(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID)
    );
    return new Response(request, idList);
  }

  private Response getAnswerIds(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    List<Long> idList = qnAService.getAnswerIds(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID),
            (long) request.getParameter(QUESTION_ID)
    );
    return new Response(request, idList);
  }

  private Response getArea(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    Area area = qnAService.getArea(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID)
    );
    return new Response(request, area);
  }

  private Response getQuestion(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    Question question = qnAService.getQuestion(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID),
            (long) request.getParameter(QUESTION_ID)
    );
    return new Response(request, question);
  }

  private Response getAnswer(final Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    Answer answer = qnAService.getAnswer(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID),
            (long) request.getParameter(QUESTION_ID),
            (long) request.getParameter(ANSWER_ID)
    );
    return new Response(request, answer);
  }

  private Response updateArea(Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    qnAService.updateArea(
            (Token) request.getParameter(USER_TOKEN),
            (Area) request.getParameter(AREA)
    );
    return new Response(request);
  }

  private Response updateQuestion(Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    qnAService.updateQuestion(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID),
            (Question) request.getParameter(QUESTION)
    );
    return new Response(request);
  }

  private Response updateAnswer(Request request) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    qnAService.updateAnswer(
            (Token) request.getParameter(USER_TOKEN),
            (long) request.getParameter(AREA_ID),
            (long) request.getParameter(QUESTION_ID),
            (Answer) request.getParameter(ANSWER)
    );
    return new Response(request);
  }

}
