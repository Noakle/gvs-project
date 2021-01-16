package de.hhn.it.vs.distribution.qna.provider.pyle.rest;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PyleServiceRestController {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(PyleServiceRestController.class);

    private BDQnAService bdQnAService;

    @RequestMapping(value =" Area", method = RequestMethod.POST)
    @ResponseBody
    public long createArea(@RequestHeader("token") String userToken, @RequestBody Area area ) throws InvalidTokenException, ServiceNotAvailableException, IllegalParameterException, UserNameAlreadyAssignedException {
        Token token = new Token(userToken);
        return bdQnAService.createArea(token, area);
    }

    @RequestMapping(value =" Question", method = RequestMethod.POST)
    @ResponseBody
    public long createQuestion(@RequestHeader("token") String usertoken, @PathVariable Long areaId, @RequestBody Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Token token = new Token(usertoken);
        return bdQnAService.createQuestion(token,areaId,question);
    }

    @RequestMapping(value =" Answer", method = RequestMethod.POST)
    @ResponseBody
    public long createAnswer(@RequestHeader("token") String usertoken, @PathVariable Long areaId, @PathVariable Long questionId, @RequestBody Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Token token = new Token (usertoken);
        return bdQnAService.createAnswer(token, areaId,questionId,answer);
    }

    @RequestMapping(value =" AreaIds", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> getAreaIds(@RequestHeader("token") String usertoken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Token token = new Token(usertoken);
        return bdQnAService.getAreaIds(token);
    }


    @RequestMapping(value =" AreaId", method = RequestMethod.GET)
    @ResponseBody
    public Area getArea(@RequestHeader("token") String usertoken, @PathVariable Long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException {
        Token token = new Token(usertoken);
        return bdQnAService.getArea(token, areaId);
    }

    @RequestMapping(value =" QuestionIds", method = RequestMethod.GET)
    @ResponseBody
    public List<Long> getQuestionIds(@RequestHeader("token") String usertoken, @PathVariable Long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException
    {
        Token token = new Token(usertoken);
        return bdQnAService.getQuestionIds(token,areaId);
    }
}
