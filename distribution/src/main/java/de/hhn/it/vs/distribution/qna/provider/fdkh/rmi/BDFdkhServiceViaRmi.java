package de.hhn.it.vs.distribution.qna.provider.fdkh.rmi;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class BDFdkhServiceViaRmi implements BDQnAService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDFdkhServiceViaRmi.class);

    private String hostname;
    private int portnumber;
    private RmiFdkhService service;

    public BDFdkhServiceViaRmi(final String hostname, final int portnumber) {
        this.hostname = hostname;
        this.portnumber = portnumber;
    }

    private void connectToService() throws ServiceNotAvailableException {
        logger.debug("get access to registry on host{] with port{]", hostname, portnumber);
        try {
            Registry registry = LocateRegistry.getRegistry(hostname, portnumber);
            service = (RmiFdkhService) registry.lookup(RmiFdkhService.REGISTRY_KEY);
        } catch (RemoteException | NotBoundException e) {
            String errorMessage = "cannot connect to service on host / port" + hostname + " / " + portnumber;
            throw new ServiceNotAvailableException(errorMessage, e);
        }
    }

    private void checkRemoteReference() throws ServiceNotAvailableException {
        if (service == null) {
            connectToService();
        }
    }

    @Override
    public long createArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.createArea(userToken, area);
        } catch (RemoteException | UserNameAlreadyAssignedException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.createQuestion(userToken, areaId, question);
        } catch (RemoteException | UserNameAlreadyAssignedException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.createAnswer(userToken, areaId, questionId, answer);
        } catch (RemoteException | UserNameAlreadyAssignedException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.getAreaIds(userToken);
        } catch (RemoteException | UserNameAlreadyAssignedException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.getArea(userToken, areaId);
        } catch (RemoteException | UserNameAlreadyAssignedException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.getQuestionIds(userToken, areaId);
        } catch (RemoteException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.getQuestion(userToken, areaId, questionId);
        } catch (RemoteException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.getAnswerIds(userToken, areaId, questionId);
        } catch (RemoteException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            return service.getAnswer(userToken, areaId, questionId, answerId);
        } catch (RemoteException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            service.updateArea(userToken, area);
        } catch (RemoteException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            service.updateQuestion(userToken, areaId, question);
        } catch (RemoteException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException {
        checkRemoteReference();
        try {
            service.updateAnswer(userToken, areaId, questionId, answer);
        } catch (RemoteException e) {
            logger.warn("Problems with RMI: {}", e.getMessage());
            throw new ServiceNotAvailableException(e);
        }
    }
}
