package de.hhn.it.vs.distribution.qna.provider.fdkh.rest;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.*;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.qna.provider.fdkh.rmi.BDFdkhServiceViaRmi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public @ResponseBody
    long createArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area )
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createArea");
        Token userToken = new Token(userTokenString);
        return qnAService.createArea(userToken, area);
    }

    @RequestMapping(value = "question/{areaID}", method = RequestMethod.POST)
    public @ResponseBody
    long createQuestion(@RequestHeader("Token") String userTokenString,@PathVariable long areaID, @RequestBody Question question)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createQuestion");
        Token userToken = new Token(userTokenString);
        return qnAService.createQuestion(userToken, areaID, question);
    }

    @RequestMapping(value = "answer/{areaID}/{questionID}", method = RequestMethod.POST)
    public @ResponseBody
    long createAnswer(@RequestHeader("Token") String userTokenString,@PathVariable long areaID, @PathVariable long questionID, @RequestBody Answer answer)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createAnswere");
        Token userToken = new Token(userTokenString);
        return qnAService.createAnswer(userToken, areaID, questionID, answer);
    }

    @RequestMapping(value = "areas", method = RequestMethod.GET)
    public @ResponseBody
    List<Long> getAreaIds(@RequestHeader("Token") String userTokenString)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAreaIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getAreaIds(userToken);
    }

    @RequestMapping(value = "area/{areaID}", method = RequestMethod.GET)
    public @ResponseBody
    Area getArea(@RequestHeader("Token") String userTokenString, @PathVariable long areaID)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getArea");
        Token userToken = new Token(userTokenString);
        return qnAService.getArea(userToken, areaID);
    }

    @RequestMapping(value = "question/{areaID}", method = RequestMethod.GET)
    public @ResponseBody
    List<Long> getQuestionIds(@RequestHeader("Token") String userTokenString, @PathVariable long areaID)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getQuestionIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getQuestionIds(userToken, areaID);
    }

    @RequestMapping(value = "question/{areaID}/{questionID}", method = RequestMethod.GET)
    public @ResponseBody
    Question getQuestion(@RequestHeader("Token") String userTokenString, @PathVariable long areaID, @PathVariable long questionID)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getQuestion");
        Token userToken = new Token(userTokenString);
        return qnAService.getQuestion(userToken, areaID, questionID);
    }

    @RequestMapping(value = "answer/{areaID}/{questionID}", method = RequestMethod.GET)
    public @ResponseBody
    List<Long> getAnswerIds(@RequestHeader("Token") String userTokenString, @PathVariable long areaID, @PathVariable long questionID)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAnswerIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getAnswerIds(userToken, areaID, questionID);
    }

    @RequestMapping(value = "answer/{areaID}/{questionID}/{answerID}", method = RequestMethod.GET)
    public @ResponseBody
    Answer getAnswer(@RequestHeader("Token") String userTokenString, @PathVariable long areaID, @PathVariable long questionID, @PathVariable long answerID)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAnswer");
        Token userToken = new Token(userTokenString);
        return qnAService.getAnswer(userToken, areaID, questionID, answerID);
    }

    @RequestMapping(value = "area/status", method = RequestMethod.PUT)
    public @ResponseBody
    void updateArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateArea");
        Token userToken = new Token(userTokenString);
        qnAService.updateArea(userToken, area);
    }

    @RequestMapping(value = "question/{areaID}/status", method = RequestMethod.PUT)
    public @ResponseBody
    void updateQuestion(@RequestHeader("Token") String userTokenString, @PathVariable long areaID, @RequestBody Question question)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateQuestion");
        Token userToken = new Token(userTokenString);
        qnAService.updateQuestion(userToken, areaID, question);
    }

    @RequestMapping(value = "answer/{areaID}/{questionID}/status", method = RequestMethod.PUT)
    public @ResponseBody
    void updateAnswer(@RequestHeader("Token") String userTokenString, @PathVariable long areaID, @PathVariable long questionID, @RequestBody Answer answer)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateAnswer");
        Token userToken = new Token(userTokenString);
        qnAService.updateAnswer(userToken, areaID, questionID, answer);
    }
}
