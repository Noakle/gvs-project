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

@RestController
@RequestMapping
public class FdkhServiceRestController {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(FdkhServiceRestController.class);

    private BDQnAService qnAService;

    @Autowired(required = true)
    public FdkhServiceRestController(final BDQnAService qnAService){
        this.qnAService = qnAService;
    }

    @RequestMapping(value = "/area", method = RequestMethod.POST)
    public @ResponseBody
    long createArea(@RequestHeader("Token") String userTokenString, @RequestBody Area area )
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createArea");
        Token userToken = new Token(userTokenString);
        return qnAService.createArea(userToken, area);
    }

    @RequestMapping(value = "/question/{areaID}", method = RequestMethod.POST)
    public @ResponseBody
    long createQuestion(@RequestHeader("Token") String userTokenString,@PathVariable long areaID, @RequestBody Question question)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createQuestion");
        Token userToken = new Token(userTokenString);
        return qnAService.createQuestion(userToken, areaID, question);
    }

    @RequestMapping(value = "/answere/{areaID}/{questionID}", method = RequestMethod.POST)
    public @ResponseBody
    long createAnswere(@RequestHeader("Token") String userTokenString,@PathVariable long areaID, @PathVariable long questionID, @RequestBody Answer answer)
            throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException{
        logger.info("createAnswere");
        Token userToken = new Token(userTokenString);
        return qnAService.createAnswer(userToken, areaID, questionID, answer);
    }
}
