package de.hhn.it.vs.distribution.qna.provider.mrvn;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.net.Socket;

public class QnAServiceServeOneClient extends AbstractServeOneClient {
  private static final org.slf4j.Logger logger =
    org.slf4j.LoggerFactory.getLogger(QnAServiceServeOneClient.class);

  BDQnAService qnaService;

  public static final String CREATE_AREA = "qna.createarea";
  public static final String CREATE_QUESTION = "qna.createquestion";
  public static final String CREATE_ANSWER = "qna.createanswer";
  public static final String GET_AREA_IDS = "qna.getareaids";
  public static final String GET_AREA = "qna.getarea";
  public static final String GET_QUESTION_IDS = "qna.getquestionids";
  public static final String GET_QUESTION = "qna.getquestion";
  public static final String GET_ANSWERS_IDS = "qna.getanswerids";
  public static final String GET_ANSWER = "qna.getanswer";
  public static final String UPDATE_AREA = "qna.updatearea";
  public static final String UPDATE_QUESTION = "qna.updatequestion";
  public static final String UPDATE_ANSWER = "qna.updateanswer";
  public static final String PARAM_AREA = "param.area";
  public static final String PARAM_AREAID = "param.areaid";
  public static final String PARAM_QUESTION = "param.question";
  public static final String PARAM_QUESTIONID = "param.questionid";
  public static final String PARAM_ANSWER = "param.answer";
  public static final String PARAM_ANSWERID = "param.answerid";
  public static final String PARAM_USER_TOKEN = "param.usertoken";

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

  @Override
  public void run() {
    Request request = null;
    Response response = null;
    try {
      request = (Request) in.readObject();

      String methodCall = request.getMethodToCall();

      switch (methodCall) {
        case GET_AREA:
          response = getArea(request);
          break;
        case GET_QUESTION:
          response = getQuestion(request);
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

  private Response getArea(final Request request) throws Exception {
    Area area = qnaService.getArea(
      (Token) request.getParameter(PARAM_USER_TOKEN),
      (long) request.getParameter(PARAM_AREAID));
    return new Response(request, area);
  }

  private Response getQuestion(final Request request) throws Exception {
    Question question = qnaService.getQuestion(
      (Token) request.getParameter(PARAM_USER_TOKEN),
      (long) request.getParameter(PARAM_AREAID),
      (long) request.getParameter(PARAM_QUESTIONID));
    return new Response(request, question);
  }
}
