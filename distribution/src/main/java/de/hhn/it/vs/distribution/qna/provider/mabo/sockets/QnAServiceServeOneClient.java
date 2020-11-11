package de.hhn.it.vs.distribution.qna.provider.mabo.sockets;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;

import java.io.IOException;
import java.net.Socket;

public class QnAServiceServeOneClient extends AbstractServeOneClient {

  public static final String CREATE_AREA = "qna.createarea";
  public static final String CREATE_QUESTION = "qna.createquestion";
  public static final String CREATE_ANSWER = "qna.createanswere";
  public static final String GET_AREA_IDS = "qna.getareaids";
  public static final String GET_AREA = "qna.getarea";
  public static final String GET_QUESTION_IDS = "qna.getquestionsids";
  public static final String GET_QUESTION = "qna.getquestions";
  public static final String GET_ANSWER_IDS = "qna.getanswerids";
  public static final String GET_ANSWER = "qna.getanswere";
  public static final String UPDATE_AREA = "qna.updatearea";
  public static final String UPDATE_QUESTION = "qna.updatequestion";
  public static final String UPDATE_ANSWER = "qna.updateanswere";

  public static final String PARAM_USER_TOKEN = "param.usertoken";
  public static final String PARAM_AREA = "param.area";
  public static final String PARAM_AREA_ID = "param.areaid";
  public static final String PARAM_QUESTION = "param.question";
  public static final String PARAM_QUESTION_ID = "param.questionid";
  public static final String PARAM_ANSWER = "param.answere";
  public static final String PARAM_ANSWER_ID = "param.amswereid";

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
  }
}
