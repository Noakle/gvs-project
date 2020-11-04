package de.hhn.it.vs.distribution.qna.provider.nkaz.sockets;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class BDQnAServiceViaSockets implements BDQnAService {

  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(BDQnAServiceViaSockets.class);

  private final String hostname;
  private final int portNumber;
  private Socket socket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  /*


  In jeder Fassadenmethode müssen Sie
  o Das Request-Objekt anlegen und konfigurieren
  o Das Request-Objekt versenden und das Response-Objekt empfangen
  o Das Response-Objekt analysieren und entweder den Rückgabewert weitergeben
  oder die im Response-Objekt enthaltene Exception neu aufwerfen.
  o Andere Exceptions auf die ServiceNotAvailableException mappen.

     */

  public BDQnAServiceViaSockets(final String hostname, final int portNumber) {
    this.hostname = hostname;
    this.portNumber = portNumber;
  }

  private void connectToService() throws ServiceNotAvailableException {
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

  private void disconnectFromService() {
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

  private Response sendAndGetResponse(final Request request) throws ServiceNotAvailableException {
    // send request and wait for the response
    try {
      connectToService();
      logger.debug("sending request " + request);
      out.writeObject(request);
      logger.debug("wait for response ...");
      Response response = (Response) in.readObject();
      logger.debug("Got response " + response);
      disconnectFromService();
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
    Request request = new Request("CREATE_AREA");
    request.addParameter("PARAM_USER", userToken);
    request.addParameter("PARAM_AREA", area);

    Response response = sendAndGetResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Long) response.getReturnObject();
  }

  @Override
  public long createQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request("CREATE_QUESTION");
    request
        .addParameter("PARAM_USER", userToken)
        .addParameter("PARAM_AREA_ID", areaId)
        .addParameter("PARAM_QUESTION", question);

    Response response = sendAndGetResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Long) response.getReturnObject();
  }

  @Override
  public long createAnswer(Token userToken, long areaId, long questionId, Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request("CREATE_ANSWER");
    request
        .addParameter("PARAM_USER", userToken)
        .addParameter("PARAM_AREA_ID", areaId)
        .addParameter("PARAM_QUESTION_ID", questionId)
        .addParameter("PARAM_ANSWER", answer);

    Response response = sendAndGetResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Long) response.getReturnObject();
  }

  @Override
  public List<Long> getAreaIds(Token userToken)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request("GET_AREA_IDS");
    request.addParameter("PARAM_USER", userToken);

    Response response = sendAndGetResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }
    
    return (List<Long>) response.getReturnObject();
  }

  @Override
  public Area getArea(Token userToken, long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request("GET_AREA");
    request.addParameter("PARAM_USER", userToken).addParameter("PARAM_AREA_ID", areaId);

    Response response = sendAndGetResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (Area) response.getReturnObject();
  }

  @Override
  public List<Long> getQuestionIds(Token userToken, long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Request request = new Request("GET_QUESTION_IDS");
    request.addParameter("PARAM_USER", userToken).addParameter("PARAM_AREA_ID", areaId);

    Response response = sendAndGetResponse(request);

    if (response.isException()) {
      rethrowStandardExceptions(response);
    }

    return (List<Long>) response.getReturnObject();
  }

  @Override
  public Question getQuestion(Token userToken, long areaId, long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    return null;
  }

  @Override
  public List<Long> getAnswerIds(Token userToken, long areaId, long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    return null;
  }

  @Override
  public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    return null;
  }

  @Override
  public void updateArea(Token userToken, Area area)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {}

  @Override
  public void updateQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {}

  @Override
  public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {}
}
