package de.hhn.it.vs.distribution.qna.provider.raat.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.qna.provider.nkaz.sockets.QnAServiceServeOneClient;
import de.hhn.it.vs.distribution.qna.provider.raat.sockets.QnaServiceServeOneClient.QnAServiceRequestMethods;
import de.hhn.it.vs.distribution.qna.provider.raat.sockets.QnaServiceServeOneClient.QnAServiceRequestParameters;

import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class BDqnaServiceViaSockets implements BDQnAService {

  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(BDqnaServiceViaSockets.class);

  private final String hostname;
  private final int portNumber;
  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;

  /**
   * constructor
   *
   * @param hostname
   * @param portNumber
   */
  public BDqnaServiceViaSockets(final String hostname, final int portNumber) {
    this.hostname = hostname;
    this.portNumber = portNumber;
  }

  /**
   * connects to the service
   *
   * @throws ServiceNotAvailableException if there's no connection
   */
  private void connect() throws ServiceNotAvailableException {
    try {
      socket = new Socket(hostname, portNumber);
      out = new ObjectOutputStream(socket.getOutputStream());
      in = new ObjectInputStream((socket.getInputStream()));
    } catch (IOException ex) {
      ex.printStackTrace();
      throw new ServiceNotAvailableException(
          "Cannot connect to host " + hostname + " with port " + portNumber + ".", ex);
    }
  }

  /**
   * disconnect from the server
   */
  private void disconnect() {
    try {
      if (out != null) {
        out.close();
        out = null;
      }
      if (in != null) {
        in.close();
        in = null;
      }

      if (socket != null) {
        socket.close();
        socket = null;
      }
    } catch (IOException ex) {
      // Problems disconnecting should not terminate the interaction. So we only log the problem.
      logger.warn("Problems disconnecting from service: " + ex.getMessage());
    }
  }

  /**
   * send the request and receive the respond
   *
   * @param request which will be send
   * @return the respond
   * @throws ServiceNotAvailableException if there's no connection
   */

  private Response sendAndResponse(final Request request) throws ServiceNotAvailableException {
    // send request and wait for the response
    try {
      connect();
      logger.debug("sending request " + request);
      out.writeObject(request);
      logger.debug("wait for response ...");
      Response response = (Response) in.readObject();
      logger.debug("Got response " + response);
      disconnect();
      return response;
    } catch (Exception ex) {
      throw new ServiceNotAvailableException("Communication problem: " + ex.getMessage(), ex);
    }
  }

  private void rethrowStandardExceptions(final Response response)
      throws IllegalParameterException, ServiceNotAvailableException, InvalidTokenException {
    Exception exceptionFromRemote = response.getExceptionObject();

    // check all acceptable exception types and rethrow
    if (exceptionFromRemote instanceof ServiceNotAvailableException) {
      throw (ServiceNotAvailableException) exceptionFromRemote;
    }
    if (exceptionFromRemote instanceof IllegalParameterException) {
      throw (IllegalParameterException) exceptionFromRemote;
    }
    if (exceptionFromRemote instanceof InvalidTokenException) {
      throw (InvalidTokenException) exceptionFromRemote;
    }

    rethrowUnexpectedException(exceptionFromRemote);
  }

  private void rethrowUnexpectedException(final Exception exceptionFromRemote)
      throws ServiceNotAvailableException {
    // if we reach this part, than the exception is an exception we do not expect!
    logger.error("WTF - Unknown exception object in response: " + exceptionFromRemote);
    exceptionFromRemote.printStackTrace();
    throw new ServiceNotAvailableException(
        "Unknown exception received in response object.", exceptionFromRemote);
  }

  @Override
  public long createArea(Token userToken, Area area)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnaServiceServeOneClient.QnAServiceRequestMethods.CREATE_AREA.toString());
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA.toString(), area);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Long) response.getReturnObject();
  }

  @Override
  public long createQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request =
        new Request(QnAServiceRequestMethods.CREATE_QUESTION.toString());
    request
        .addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(),
            userToken)
        .addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(),
            areaId)
        .addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.QUESTION.toString(),
            question);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Long) response.getReturnObject();
  }

  @Override
  public long createAnswer(Token userToken, long areaId, long questionId, Answer answer)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.CREATE_ANSWER.toString());
    request
            .addParameter(QnAServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken)
            .addParameter(QnAServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId)
            .addParameter(QnAServiceServeOneClient.QnAServiceRequestParameters.QUESTION_ID.toString(), questionId)
            .addParameter(QnAServiceServeOneClient.QnAServiceRequestParameters.ANSWER.toString(), answer);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Long) response.getReturnObject();
  }

  @Override
  public List<Long> getAreaIds(Token userToken)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.GET_AREA_IDS.toString());
    request.addParameter(QnAServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (List<Long>) response.getReturnObject();
  }

  @Override
  public Area getArea(Token userToken, long areaId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.GET_AREA.toString());
    request
            .addParameter(QnAServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken)
            .addParameter(QnAServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Area) response.getReturnObject();
  }

  @Override
  public List<Long> getQuestionIds(Token userToken, long areaId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.GET_QUESTION_IDS.toString());
    request
            .addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken)
            .addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (List<Long>) response.getReturnObject();
  }

  @Override
  public Question getQuestion(Token userToken, long areaId, long questionId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnaServiceServeOneClient.QnAServiceRequestMethods.GET_QUESTION.toString());
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.QUESTION_ID.toString(), questionId);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Question) response.getReturnObject();
  }

  @Override
  public List<Long> getAnswerIds(Token userToken, long areaId, long questionId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.GET_ANSWER_IDS.toString());
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.QUESTION_ID.toString(), questionId);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (List<Long>) response.getReturnObject();
  }

  @Override
  public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.GET_ANSWER.toString());
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.QUESTION_ID.toString(), questionId);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.ANSWER_ID.toString(), answerId);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Answer) response.getReturnObject();
  }

  @Override
  public void updateArea(Token userToken, Area area)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnaServiceServeOneClient.QnAServiceRequestMethods.UPDATE_AREA.toString());
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA.toString(), area);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }
  }

  @Override
  public void updateQuestion(Token userToken, long areaId, Question question)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.UPDATE_QUESTION.toString());
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.QUESTION.toString(), question);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }
  }

  @Override
  public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request(QnAServiceServeOneClient.QnAServiceRequestMethods.UPDATE_ANSWER.toString());
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.USER_TOKEN.toString(), userToken);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.AREA_ID.toString(), areaId);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.QUESTION_ID.toString(), questionId);
    request.addParameter(QnaServiceServeOneClient.QnAServiceRequestParameters.ANSWER.toString(), answer);

    Response response = sendAndResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }
  }
}



