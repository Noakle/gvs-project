package de.hhn.it.vs.distribution.qna.provider.mrvn;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.sockets.Request;
import de.hhn.it.vs.distribution.sockets.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BDQnAServiceViaSockets implements BDQnAService {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(BDUserManagementServiceViaSockets.class);

  private String hostname;
  private int portNumber;
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;

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
      throw new ServiceNotAvailableException("Cannot connect to host " + hostname + " with port "
        + portNumber + ".", ex);
    }
  }

  public void disconnectFromService() {
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

  private void rethrowStandardExceptions(final Response response) throws
    IllegalParameterException, ServiceNotAvailableException, InvalidTokenException {
    Exception exceptionFromRemote = response.getExceptionObject();

    // check all acceptable exception types and rethrow
    if (exceptionFromRemote instanceof IllegalParameterException) {
      throw (IllegalParameterException) exceptionFromRemote;
    }

    if (exceptionFromRemote instanceof InvalidTokenException) {
      throw (InvalidTokenException) exceptionFromRemote;
    }

    if (exceptionFromRemote instanceof ServiceNotAvailableException) {
      throw (ServiceNotAvailableException) exceptionFromRemote;
    }

    rethrowUnexpectedException(exceptionFromRemote);
  }

  private void rethrowUnexpectedException(final Exception exceptionFromRemote) throws
    ServiceNotAvailableException {
    // if we reach this part, than the exception is an exception we do not expect!
    logger.error("WTF - Unknown exception object in response: " + exceptionFromRemote);
    exceptionFromRemote.printStackTrace();
    throw new ServiceNotAvailableException("Unknown exception received in response object.",
      exceptionFromRemote);
  }

  @Override
  public long createArea(Token userToken, Area area) throws ServiceNotAvailableException,
      IllegalParameterException, InvalidTokenException {
    Request request = new Request("createArea")
        .addParameter("userToken", userToken)
        .addParameter("area", area);
    Response response = sendAndGetResponse(request);


    return 0;
  }

  @Override
  public long createQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    return 0;
  }

  @Override
  public long createAnswer(Token userToken, long areaId, long questionId, Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    return 0;
  }

  @Override
  public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException,
      IllegalParameterException, InvalidTokenException {
    return null;
  }

  @Override
  public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException,
      IllegalParameterException, InvalidTokenException {
    return null;
  }

  @Override
  public List<Long> getQuestionIds(Token userToken, long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    return null;
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
  public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException,
      IllegalParameterException, InvalidTokenException {

  }

  @Override
  public void updateQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

  }

  @Override
  public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws
      ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {

  }
}
