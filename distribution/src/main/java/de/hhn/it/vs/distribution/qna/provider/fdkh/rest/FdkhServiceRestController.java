package de.hhn.it.vs.distribution.qna.provider.fdkh.rest;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
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
public class FdkhServiceRestController {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(FdkhServiceRestController.class);

    private BDQnAService qnAService;

    @Autowired(required = true)
    public FdkhServiceRestController(final BDQnAService qnAService){
        this.qnAService = qnAService;
    }

    @RequestMapping(value = "area", method = RequestMethod.POST)
    public
    long createArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area )
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException, UserNameAlreadyAssignedException {
        logger.info("createArea");
        Token userToken = new Token(userTokenString);
        return qnAService.createArea(userToken, area);
    }

    @RequestMapping(value = "areas/{areaId}/question", method = RequestMethod.POST)
    @ResponseBody
    public
    long createQuestion(@RequestHeader("Token") String userTokenString,@PathVariable long areaId, @RequestBody Question question)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException, UserNameAlreadyAssignedException {
        logger.info("createQuestion");
        Token userToken = new Token(userTokenString);
        return qnAService.createQuestion(userToken, areaId, question);
    }

    @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answer", method = RequestMethod.POST)
    @ResponseBody
    public
    long createAnswer(@RequestHeader("Token") String userTokenString,@PathVariable long areaId, @PathVariable long questionId, @RequestBody Answer answer)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException, UserNameAlreadyAssignedException {
        logger.info("createAnswer");
        Token userToken = new Token(userTokenString);
        return qnAService.createAnswer(userToken, areaId, questionId, answer);
    }

    @RequestMapping(value = "areas", method = RequestMethod.GET)
    @ResponseBody
    public
    List<Long> getAreaIds(@RequestHeader("Token") String userTokenString)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException, UserNameAlreadyAssignedException {
        logger.info("getAreaIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getAreaIds(userToken);
    }

    @RequestMapping(value = "areas/{areaId}", method = RequestMethod.GET)
    @ResponseBody
    public
    Area getArea(@RequestHeader("Token") String userTokenString, @PathVariable long areaId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException, UserNameAlreadyAssignedException {
        logger.info("getArea");
        Token userToken = new Token(userTokenString);
        return qnAService.getArea(userToken, areaId);
    }

    @RequestMapping(value = "areas/{areaId}/questions", method = RequestMethod.GET)
    @ResponseBody
    public
    List<Long> getQuestionIds(@RequestHeader("Token") String userTokenString, @PathVariable long areaId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getQuestionIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getQuestionIds(userToken, areaId);
    }

    @RequestMapping(value = "areas/{areaId}/questions/{questionId}", method = RequestMethod.GET)
    @ResponseBody
    public
    Question getQuestion(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getQuestion");
        Token userToken = new Token(userTokenString);
        return qnAService.getQuestion(userToken, areaId, questionId);
    }

    @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers", method = RequestMethod.GET)
    @ResponseBody
    public
    List<Long> getAnswerIds(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAnswerIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getAnswerIds(userToken, areaId, questionId);
    }

    @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers/{answerId}", method = RequestMethod.GET)
    @ResponseBody
    public
    Answer getAnswer(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId, @PathVariable long answerId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAnswer");
        Token userToken = new Token(userTokenString);
        return qnAService.getAnswer(userToken, areaId, questionId, answerId);
    }

    @RequestMapping(value = "areas/status", method = RequestMethod.PUT)
    @ResponseBody
    public
    void updateArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateArea");
        Token userToken = new Token(userTokenString);
        qnAService.updateArea(userToken, area);
    }

    @RequestMapping(value = "areas/{areaId}/questions/status", method = RequestMethod.PUT)
    @ResponseBody
    public
    void updateQuestion(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @RequestBody Question question)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateQuestion");
        Token userToken = new Token(userTokenString);
        qnAService.updateQuestion(userToken, areaId, question);
    }

    @RequestMapping(value = "areas/{areaId}/questions/{questionId}/answers/status", method = RequestMethod.PUT)
    @ResponseBody
    public
    void updateAnswer(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId, @RequestBody Answer answer)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateAnswer");
        Token userToken = new Token(userTokenString);
        qnAService.updateAnswer(userToken, areaId, questionId, answer);
    }
}
