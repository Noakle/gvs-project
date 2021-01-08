package de.hhn.it.vs.distribution.pyle.rmi;

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

public class RmipyleServiceImpl implements RmipyleService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(RmipyleServiceImpl.class);

    BDQnAService bdQnAService ;

    public RmipyleServiceImpl(BDQnAService bdQnAService)
    {
        this.bdQnAService = bdQnAService;
    }
    @Override
    public long createArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.createArea(userToken, area);
    }

    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.createQuestion(userToken,areaId,question);
    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.createAnswer(userToken, areaId, questionId, answer);
    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.getAreaIds(userToken);
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.getArea(userToken, areaId);
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.getQuestionIds(userToken, areaId);
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.getQuestion(userToken, areaId, questionId);
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return null;
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return bdQnAService.getAnswer(userToken, areaId, questionId, answerId);
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        bdQnAService.updateArea(userToken, area);

    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        bdQnAService.updateQuestion(userToken, areaId, question);

    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        bdQnAService.updateAnswer(userToken, areaId, questionId, answer);

    }
}
