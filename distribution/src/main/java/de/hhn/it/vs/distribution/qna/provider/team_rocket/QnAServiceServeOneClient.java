package de.hhn.it.vs.distribution.qna.provider.team_rocket;

import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.net.Socket;

public class QnAServiceServeOneClient extends AbstractServeOneClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(QnAServiceServeOneClient.class);

  BDQnAService qnaService;

  public static final String CREATE_AREA = "";
  public static final String CREATE_QUESTION = "";
  public static final String CREATE_ANSWER = "";
  public static final String GET_AREA_IDS = "";
  public static final String GET_AREA = "";
  public static final String GET_QUESTION_IDS = "";
  public static final String GET_QUESTION = "";
  public static final String GET_ANSWER_IDS = "";
  public static final String GET_ANSWER = "";
  public static final String UPDATE_AREA = "";
  public static final String UPDATE_QUESTION = "";
  public static final String UPDATE_ANSWER = "";

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

    } catch (Exception exception) {

    }
  }
}
