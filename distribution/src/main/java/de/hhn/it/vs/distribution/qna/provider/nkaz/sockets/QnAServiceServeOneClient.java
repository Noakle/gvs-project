package de.hhn.it.vs.distribution.qna.provider.nkaz.sockets;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient;
import de.hhn.it.vs.distribution.sockets.AbstractServeOneClient;

import java.io.IOException;
import java.net.Socket;

public class QnAServiceServeOneClient extends AbstractServeOneClient {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(QnAServiceServeOneClient.class);

  private final BDQnAService qnAService;

  public enum QnAServiceRequestMethods {
    CREATE_AREA,
    CREATE_QUESTION,
    CREATE_ANSWER,
    GET_AREA_IDS,
    GET_AREA,
    GET_QUESTION_IDS,
    GET_QUESTION,
    GET_ANSWER_IDS,
    GET_ANSWER,
    UPDATE_AREA,
    UPDATE_QUESTION,
    UPDATE_ANSWER;
  }

  public enum QnAServiceRequestParameters {
    USER_TOKEN,
    AREA,
    AREA_ID,
    QUESTION,
    QUESTION_ID,
    ANSWER,
    ANSWER_ID;
  }

  /**
   * Creates new thread to work on a single client request.
   *
   * @param socket socket connected with the client
   * @param service service to be used for the request
   * @throws IOException when problems with the socket connection occur
   * @throws IllegalParameterException when called with null references
   */
  public QnAServiceServeOneClient(Socket socket, Object service)
      throws IOException, IllegalParameterException {
    super(socket, service);
    qnAService = (BDQnAService) service;
  }
}
