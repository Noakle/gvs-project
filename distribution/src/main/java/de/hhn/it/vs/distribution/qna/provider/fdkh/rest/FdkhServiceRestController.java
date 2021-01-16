package de.hhn.it.vs.distribution.qna.provider.fdkh.rest;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.*;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.qna.provider.fdkh.rmi.BDFdkhServiceViaRmi;
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
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createArea");
        Token userToken = new Token(userTokenString);
        return qnAService.createArea(userToken, area);
    }

    @RequestMapping(value = "question/{areaId}", method = RequestMethod.POST)
    @ResponseBody
    public
    long createQuestion(@RequestHeader("Token") String userTokenString,@PathVariable long areaId, @RequestBody Question question)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createQuestion");
        Token userToken = new Token(userTokenString);
        return qnAService.createQuestion(userToken, areaId, question);
    }

    @RequestMapping(value = "answer/{areaId}/{questionId}", method = RequestMethod.POST)
    @ResponseBody
    public
    long createAnswer(@RequestHeader("Token") String userTokenString,@PathVariable long areaId, @PathVariable long questionId, @RequestBody Answer answer)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createAnswere");
        Token userToken = new Token(userTokenString);
        return qnAService.createAnswer(userToken, areaId, questionId, answer);
    }

    @RequestMapping(value = "areas", method = RequestMethod.GET)
    @ResponseBody
    public
    List<Long> getAreaIds(@RequestHeader("Token") String userTokenString)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAreaIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getAreaIds(userToken);
    }

    @RequestMapping(value = "area/{areaId}", method = RequestMethod.GET)
    @ResponseBody
    public
    Area getArea(@RequestHeader("Token") String userTokenString, @PathVariable long areaId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getArea");
        Token userToken = new Token(userTokenString);
        return qnAService.getArea(userToken, areaId);
    }

    @RequestMapping(value = "question/{areaId}", method = RequestMethod.GET)
    @ResponseBody
    public
    List<Long> getQuestionIds(@RequestHeader("Token") String userTokenString, @PathVariable long areaId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getQuestionIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getQuestionIds(userToken, areaId);
    }

    @RequestMapping(value = "question/{areaId}/{questionId}", method = RequestMethod.GET)
    @ResponseBody
    public
    Question getQuestion(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getQuestion");
        Token userToken = new Token(userTokenString);
        return qnAService.getQuestion(userToken, areaId, questionId);
    }

    @RequestMapping(value = "answer/{areaId}/{questionId}", method = RequestMethod.GET)
    @ResponseBody
    public
    List<Long> getAnswerIds(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAnswerIds");
        Token userToken = new Token(userTokenString);
        return qnAService.getAnswerIds(userToken, areaId, questionId);
    }

    @RequestMapping(value = "answer/{areaId}/{questionId}/{answerId}", method = RequestMethod.GET)
    @ResponseBody
    public
    Answer getAnswer(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId, @PathVariable long answerId)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("getAnswer");
        Token userToken = new Token(userTokenString);
        return qnAService.getAnswer(userToken, areaId, questionId, answerId);
    }

    @RequestMapping(value = "area/status", method = RequestMethod.PUT)
    @ResponseBody
    public
    void updateArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateArea");
        Token userToken = new Token(userTokenString);
        qnAService.updateArea(userToken, area);
    }

    @RequestMapping(value = "question/{areaId}/status", method = RequestMethod.PUT)
    @ResponseBody
    public
    void updateQuestion(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @RequestBody Question question)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateQuestion");
        Token userToken = new Token(userTokenString);
        qnAService.updateQuestion(userToken, areaId, question);
    }

    @RequestMapping(value = "answer/{areaId}/{questionId}/status", method = RequestMethod.PUT)
    @ResponseBody
    public
    void updateAnswer(@RequestHeader("Token") String userTokenString, @PathVariable long areaId, @PathVariable long questionId, @RequestBody Answer answer)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("updateAnswer");
        Token userToken = new Token(userTokenString);
        qnAService.updateAnswer(userToken, areaId, questionId, answer);
    }
}
