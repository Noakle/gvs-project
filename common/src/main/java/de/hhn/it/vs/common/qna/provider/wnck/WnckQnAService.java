package de.hhn.it.vs.common.qna.provider.wnck;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;

import java.util.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class WnckQnAService implements BDQnAService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckQnAService.class);

  private final Map<Long, WnckArea> wnckAreaMap;
  private final Validator validator;
  private final BDUserManagementService userManagementService;


  public WnckQnAService(BDUserManagementService userManagementService) {
    logger.info("Service created.");
    this.userManagementService = userManagementService;
    wnckAreaMap = new HashMap<>();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

  }

  private WnckArea getWnckAreaById(final long areaId)
          throws IllegalParameterException {
    if (!wnckAreaMap.containsKey(areaId)) {
      throw new IllegalParameterException("Area does not exist. Id: " + areaId);
    }
    return wnckAreaMap.get(areaId);
  }

  private void assertThatUserExists(Token userToken) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    userManagementService.resolveUser(userToken);
  }

  private void validate(final Object object) throws IllegalParameterException {
    Set<ConstraintViolation<Object>> results = validator.validate(object);
    String problem = "Problem in validation of " + object.getClass().getSimpleName() + ": ";
    if (results.size() == 0) {
      return;
    }

    for (ConstraintViolation<Object> result : results) {
      problem += result.getPropertyPath() + " " + result.getMessage() + " | ";
    }
    throw new IllegalParameterException(problem);

  }

  @Override
  public long createArea(final Token userToken, final Area area)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("createArea: {}", area);
    assertThatUserExists(userToken);
    validate(area);
    WnckArea wnckArea = new WnckArea(area);
    wnckAreaMap.put(wnckArea.getId(), wnckArea);
    return wnckArea.getId();
  }

  @Override
  public long createQuestion(final Token userToken, final long areaId, final Question question)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("createQuestion: in areaId {} with question {}", areaId, question);
    assertThatUserExists(userToken);
    validate(question);
    WnckArea wnckArea = getWnckAreaById(areaId);
    WnckQuestion wnckQuestion = new WnckQuestion(question);
    wnckArea.addQuestion(wnckQuestion);
    return wnckQuestion.getId();
  }

  @Override
  public long createAnswer(final Token userToken, final long areaId, final long questionId,
                           final Answer answer)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("createAnswer: In areaId {} for questionId {} with anser {}",
            areaId, questionId, answer);
    assertThatUserExists(userToken);
    validate(answer);
    WnckQuestion wnckQuestion = getWnckAreaById(areaId).getWnckQuestionById(questionId);
    WnckAnswer wnckAnswer = new WnckAnswer(answer);
    wnckQuestion.addAnswer(wnckAnswer);
    return wnckAnswer.getId();
  }

  @Override
  public List<Long> getAreaIds(final Token userToken)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("getAreaIds:");
    assertThatUserExists(userToken);
    List<Long> results = new ArrayList<>();
    Collection<WnckArea> wnckAreas = wnckAreaMap.values();
    for (WnckArea wnckArea : wnckAreas) {
      results.add(wnckArea.getId());
    }
    return results;
  }

  @Override
  public Area getArea(final Token userToken, final long areaId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("getArea: for areaId {}", areaId);
    assertThatUserExists(userToken);
    WnckArea wnckArea = getWnckAreaById(areaId);
    return wnckArea.getArea();
  }

  @Override
  public List<Long> getQuestionIds(final Token userToken, final long areaId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("getQuestionIds: for areaId {}", areaId);
    assertThatUserExists(userToken);
    return getWnckAreaById(areaId).getQuestionIds();
  }

  @Override
  public Question getQuestion(final Token userToken, final long areaId, final long questionId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("getQuestion: for areaId {} and questionId", areaId, questionId);
    assertThatUserExists(userToken);
    return getWnckAreaById(areaId).getWnckQuestionById(questionId).getQuestion();
  }

  @Override
  public List<Long> getAnswerIds(final Token userToken, final long areaId, final long questionId)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("getAnswerIds: for areaId {} and questionId {}", areaId, questionId);
    assertThatUserExists(userToken);
    return getWnckAreaById(areaId).getWnckQuestionById(questionId).getAnswerIds();
  }

  @Override
  public Answer getAnswer(final Token userToken, final long areaId, final long questionId,
                          final long answerId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    logger.debug("getAnswer: for areaId {} and questionId {} and answerId {}", areaId, questionId
            , answerId);
    assertThatUserExists(userToken);
    return getWnckAreaById(areaId).getWnckQuestionById(questionId).getWnckAnswerById(answerId).getAnswer();
  }

  @Override
  public void updateArea(final Token userToken, final Area area)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("updateArea:  with area {}", area);
    assertThatUserExists(userToken);
    validate(area);
    getWnckAreaById(area.getId()).update(area);
  }

  @Override
  public void updateQuestion(final Token userToken, final long areaId, final Question question)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("updateQuestion:  with areaId {} and question {}", areaId, question);
    assertThatUserExists(userToken);
    validate(question);
    //getWnckAreaById(areaId).getWnckQuestionById(question.getId()).update(question);
  }

  @Override
  public void updateAnswer(final Token userToken, final long areaId, final long questionId,
                           final Answer answer)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    logger.debug("updateAnswer: with areId {} and questionId {} and answer {}", areaId,
            questionId, answer);
    assertThatUserExists(userToken);
    validate(answer);
    //getWnckAreaById(areaId).getWnckQuestionById(questionId).getWnckAnswerById(answer.getId()).update(answer);
  }
}
