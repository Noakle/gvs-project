package de.hhn.it.vs.distribution.qna.provider.nkaz.rmi;

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

public class RmiQnAServiceImpl implements RmiQnAService {

  private final BDQnAService service;

  public RmiQnAServiceImpl(final BDQnAService service) {
    this.service = service;
  }

  @Override
  public long createArea(Token userToken, Area area)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.createArea(userToken, area);
  }

  @Override
  public long createQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.createQuestion(userToken, areaId, question);
  }

  @Override
  public long createAnswer(Token userToken, long areaId, long questionId, Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.createAnswer(userToken, areaId, questionId, answer);
  }

  @Override
  public List<Long> getAreaIds(Token userToken)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.getAreaIds(userToken);
  }

  @Override
  public Area getArea(Token userToken, long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.getArea(userToken, areaId);
  }

  @Override
  public List<Long> getQuestionIds(Token userToken, long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.getQuestionIds(userToken, areaId);
  }

  @Override
  public Question getQuestion(Token userToken, long areaId, long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.getQuestion(userToken, areaId, questionId);
  }

  @Override
  public List<Long> getAnswerIds(Token userToken, long areaId, long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.getAnswerIds(userToken, areaId, questionId);
  }

  @Override
  public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    return service.getAnswer(userToken, areaId, questionId, answerId);
  }

  @Override
  public void updateArea(Token userToken, Area area)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    service.updateArea(userToken, area);
  }

  @Override
  public void updateQuestion(Token userToken, long areaId, Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    service.updateQuestion(userToken, areaId, question);
  }

  @Override
  public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException,
          RemoteException {
    service.updateAnswer(userToken, areaId, questionId, answer);
  }
}
