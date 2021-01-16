package de.hhn.it.vs.distribution.qna.provider.wnck.rmi;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class WnckQnAServiceViaRmi implements BDQnAService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckQnAServiceViaRmi.class);

  private String hostname;
  private int portNumber;
  private RmiQnAService service;

  public WnckQnAServiceViaRmi(final String hostname, final int portNumber) {
    this.hostname = hostname;
    this.portNumber = portNumber;
  }

  private void connectToService() throws ServiceNotAvailableException {
    logger.debug("get access to registry on host {} with port {}", hostname, portNumber);
    try {
      Registry registry = LocateRegistry.getRegistry(hostname, portNumber);
      service = (RmiQnAService) registry.lookup(RmiQnAService.REGISTRY_KEY);
    } catch (RemoteException | NotBoundException ex) {
      String errorMessage = "cannot connect to service on host / port "
              + hostname + " / " + portNumber;
      throw new ServiceNotAvailableException(errorMessage, ex);
    }
  }

  private void checkRemoteReference() throws ServiceNotAvailableException {
    if (service == null) {
      connectToService();
    }
  }


  @Override
  public long createArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.createArea(userToken, area);
    } catch (RemoteException | UserNameAlreadyAssignedException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public long createQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.createQuestion(userToken, areaId, question);
    } catch (RemoteException | UserNameAlreadyAssignedException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public long createAnswer(final Token userToken, final long areaId, final long questionId,
                           final Answer answer) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.createAnswer(userToken, areaId, questionId, answer);
    } catch (RemoteException | UserNameAlreadyAssignedException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public List<Long> getAreaIds(final Token userToken) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.getAreaIds(userToken);
    } catch (RemoteException | UserNameAlreadyAssignedException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public Area getArea(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.getArea(userToken, areaId);
    } catch (RemoteException | UserNameAlreadyAssignedException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public List<Long> getQuestionIds(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.getQuestionIds(userToken, areaId);
    } catch (RemoteException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public Question getQuestion(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.getQuestion(userToken, areaId, questionId);
    } catch (RemoteException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public List<Long> getAnswerIds(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.getAnswerIds(userToken, areaId, questionId);
    } catch (RemoteException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public Answer getAnswer(final Token userToken, final long areaId, final long questionId,
                          final long answerId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      return service.getAnswer(userToken, areaId, questionId, answerId);
    } catch (RemoteException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public void updateArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      service.updateArea(userToken, area);
    } catch (RemoteException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }

  }

  @Override
  public void updateQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      service.updateQuestion(userToken, areaId, question);
    } catch (RemoteException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }

  @Override
  public void updateAnswer(final Token userToken, final long areaId, final long questionId,
                           final Answer answer) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    checkRemoteReference();
    try {
      service.updateAnswer(userToken, areaId, questionId, answer);
    } catch (RemoteException e) {
      throw new ServiceNotAvailableException("Rmi problem: ", e);
    }
  }
}
