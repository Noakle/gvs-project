package de.hhn.it.vs.distribution.qna.provider.nkaz.rest;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.rest.RestCCConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Import(RestCCConfiguration.class)
@RestController
@RequestMapping("qnaservice")
public class QnAServiceRestController {
  private BDQnAService qnAService;

  @Autowired(required = true)
  public QnAServiceRestController(final BDQnAService qnAService) {
    this.qnAService = qnAService;
  }

  @RequestMapping(value = "areas", method = RequestMethod.POST)
  public long createArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.createArea(userToken, area);
  }

  @RequestMapping(value = "areas/{areaId}/questions", method = RequestMethod.POST)
  public long createQuestion(
      @RequestHeader("Token") String userTokenString,
      @PathVariable long areaId,
      @RequestBody Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.createQuestion(userToken, areaId, question);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers", method = RequestMethod.POST)
  public long createAnswer(
      @RequestHeader("Token") String userTokenString,
      @PathVariable long areaId,
      @PathVariable long questionId,
      @RequestBody Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.createAnswer(userToken, areaId, questionId, answer);
  }

  @RequestMapping(value = "areas", method = RequestMethod.GET)
  public List<Long> getAreaIds(@RequestHeader("Token") String userTokenString)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getAreaIds(userToken);
  }

  @RequestMapping(value = "areas/{areaId}", method = RequestMethod.GET)
  public Area getArea(@RequestHeader("Token") String userTokenString, @PathVariable long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getArea(userToken, areaId);
  }

  @RequestMapping(value = "areas/{areaId}/questions", method = RequestMethod.GET)
  public List<Long> getQuestionIds(
      @RequestHeader("Token") String userTokenString, @PathVariable long areaId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getQuestionIds(userToken, areaId);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}", method = RequestMethod.GET)
  public Question getQuestion(
      @RequestHeader("Token") String userTokenString,
      @PathVariable long areaId,
      @PathVariable long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getQuestion(userToken, areaId, questionId);
  }

  @RequestMapping(
      value = "areas/{areaId}/questions/{questionId}/answers",
      method = RequestMethod.GET)
  public List<Long> getAnswerIds(
      @RequestHeader("Token") String userTokenString,
      @PathVariable long areaId,
      @PathVariable long questionId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getAnswerIds(userToken, areaId, questionId);
  }

  @RequestMapping(
      value = "areas/{areaId}/questions/{questionId}/answers/{answerId}",
      method = RequestMethod.GET)
  public Answer getAnswer(
      @RequestHeader("Token") String userTokenString,
      @PathVariable long areaId,
      @PathVariable long questionId,
      @PathVariable long answerId)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getAnswer(userToken, areaId, questionId, answerId);
  }

  @RequestMapping(value = "areas", method = RequestMethod.PUT)
  public void updateArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    qnAService.updateArea(userToken, area);
  }

  @RequestMapping(value = "areas/{areaId}/questions", method = RequestMethod.PUT)
  public void updateQuestion(
      @RequestHeader("Token") String userTokenString,
      @PathVariable long areaId,
      @RequestBody Question question)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    qnAService.updateQuestion(userToken, areaId, question);
  }

  @RequestMapping(
      value = "areas/{areaId}/questions/{questionId}/answers",
      method = RequestMethod.PUT)
  public void updateAnswer(
      @RequestHeader("Token") String userTokenString,
      @PathVariable long areaId,
      @PathVariable long questionId,
      @RequestBody Answer answer)
      throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    qnAService.updateAnswer(userToken, areaId, questionId, answer);
  }
}
