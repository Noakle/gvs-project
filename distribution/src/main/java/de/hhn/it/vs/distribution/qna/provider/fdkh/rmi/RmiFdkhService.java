package de.hhn.it.vs.distribution.qna.provider.fdkh.rmi;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RmiFdkhService extends Remote {

    public static final String REGISTRY_KEY = "cc.qna";

    public long createArea(Token userToken, Area area)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public long createQuestion(Token userToken, long areaId, Question question)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public List<Long> getAreaIds(Token userToken)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public Area getArea(Token userToken, long areaId)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public List<Long> getQuestionIds(Token userToken, long areaId)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public Question getQuestion(Token userToken, long areaId, long questionId)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public void updateArea(Token userToken, Area area)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public void updateQuestion(Token userToken, long areaId, Question question)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer)
            throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException;

}
