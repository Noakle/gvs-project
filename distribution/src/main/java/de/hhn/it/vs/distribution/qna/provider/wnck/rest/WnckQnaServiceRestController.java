package de.hhn.it.vs.distribution.qna.provider.wnck.rest;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Import(RestCCConfiguration.class)

@RestController
@RequestMapping("wnckqnaservice")
public class WnckQnaServiceRestController {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckQnaServiceRestController.class);

  private BDQnAService qnAService;

  @Autowired(required = true)
  public WnckQnaServiceRestController(final BDQnAService qnAService) {
    this.qnAService = qnAService;
  }


  @RequestMapping(value = "areas", method = RequestMethod.POST)
  public long createArea(@RequestHeader("Token") final String userTokenString,
                         @RequestBody final Area area) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.createArea(userToken, area);
  }

  @RequestMapping(value = "areas/{areaId}/questions", method = RequestMethod.POST)
  public long createQuestion(@RequestHeader("Token") final String userTokenString,
                             @PathVariable final long areaId, @RequestBody final Question question)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.createQuestion(userToken, areaId, question);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers",
          method = RequestMethod.POST)
  public long createAnswer(@RequestHeader("Token") final String userTokenString,
                           @PathVariable final long areaId, @PathVariable final long questionId,
                           @RequestBody final Answer answer) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.createAnswer(userToken, areaId, questionId, answer);
  }

  @RequestMapping(value = "areas", method = RequestMethod.GET)
  public List<Long> getAreaIds(@RequestHeader("Token") final String userTokenString) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getAreaIds(userToken);
  }

  @RequestMapping(value = "areas/{areaId}", method = RequestMethod.GET)
  public Area getArea(@RequestHeader("Token") final String userTokenString,
                      @PathVariable final long areaId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getArea(userToken, areaId);
  }

  @RequestMapping(value = "areas/{areaId}/questions", method = RequestMethod.GET)
  public List<Long> getQuestionIds(@RequestHeader("Token") final String userTokenString,
                                   @PathVariable final long areaId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getQuestionIds(userToken, areaId);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}", method = RequestMethod.GET)
  public Question getQuestion(@RequestHeader("Token") final String userTokenString,
                              @PathVariable final long areaId,
                              @PathVariable final long questionId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getQuestion(userToken, areaId, questionId);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers", method =
          RequestMethod.GET)
  public List<Long> getAnswerIds(@RequestHeader("Token") final String userTokenString,
                                 @PathVariable final long areaId,
                                 @PathVariable final long questionId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getAnswerIds(userToken, areaId, questionId);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers/{answerId}", method =
          RequestMethod.GET)
  public Answer getAnswer(@RequestHeader("Token") final String userTokenString,
                          @PathVariable final long areaId, @PathVariable final long questionId,
                          @PathVariable final long answerId) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    return qnAService.getAnswer(userToken, areaId, questionId, answerId);
  }

  @RequestMapping(value = "areas/{areaId}", method = RequestMethod.PUT)
  public void updateArea(@RequestHeader("Token") final String userTokenString,
                         @PathVariable @RequestBody final Area area)
          throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    qnAService.updateArea(userToken, area);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}", method = RequestMethod.PUT)
  public void updateQuestion(@RequestHeader("Token") final String userTokenString,
                             @PathVariable final long areaId,
                             @RequestBody final Question question) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    qnAService.updateQuestion(userToken, areaId, question);
  }

  @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers/{answerId}", method =
          RequestMethod.PUT)
  public void updateAnswer(@RequestHeader("Token") final String userTokenString,
                           @PathVariable final long areaId, @PathVariable final long questionId,
                           @RequestBody final Answer answer) throws ServiceNotAvailableException,
          IllegalParameterException, InvalidTokenException {
    Token userToken = new Token(userTokenString);
    qnAService.updateAnswer(userToken, areaId, questionId, answer);
  }
}
