package de.hhn.it.vs.distribution.qna.provider.wnck.rmi;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;

import java.rmi.RemoteException;
import java.util.List;

public class WnckRmiQnAServiceImpl implements RmiQnAService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckRmiQnAServiceImpl.class);

  BDQnAService qnAService;

  public WnckRmiQnAServiceImpl(final BDQnAService qnAService) {
    this.qnAService = qnAService;
  }

  @Override
  public long createArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.createArea(userToken, area);
  }

  @Override
  public long createQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.createQuestion(userToken, areaId, question);
  }

  @Override
  public long createAnswer(final Token userToken, final long areaId, final long questionId,
                           final Answer answer) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.createAnswer(userToken, areaId, questionId, answer);
  }

  @Override
  public List<Long> getAreaIds(final Token userToken) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.getAreaIds(userToken);
  }

  @Override
  public Area getArea(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.getArea(userToken, areaId);
  }

  @Override
  public List<Long> getQuestionIds(final Token userToken, final long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.getQuestionIds(userToken, areaId);
  }

  @Override
  public Question getQuestion(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.getQuestion(userToken, areaId, questionId);
  }

  @Override
  public List<Long> getAnswerIds(final Token userToken, final long areaId, final long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.getAnswerIds(userToken, areaId, questionId);
  }

  @Override
  public Answer getAnswer(final Token userToken, final long areaId, final long questionId,
                          final long answerId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException, RemoteException {
    return qnAService.getAnswer(userToken, areaId, questionId, answerId);
  }

  @Override
  public void updateArea(final Token userToken, final Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    qnAService.updateArea(userToken, area);
  }

  @Override
  public void updateQuestion(final Token userToken, final long areaId, final Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
    qnAService.updateQuestion(userToken, areaId, question);
  }

  @Override
  public void updateAnswer(final Token userToken, final long areaId, final long questionId,
                           final Answer answer) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException, RemoteException {
    qnAService.updateAnswer(userToken, areaId, questionId, answer);
  }
}
