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
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class BDpyleServiceViaRmi implements BDQnAService {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(BDpyleServiceViaRmi.class);
    private  String hostname;
    private  int portNummer;
    private  RmipyleService rmipyleService;
    public BDpyleServiceViaRmi(String hostname, int portNummer)
    {
        this.hostname = hostname;
        this.portNummer = portNummer;
    }
    public boolean serviceKommuication()
    {
        try {
            Registry registry = LocateRegistry.getRegistry(hostname,portNummer);
            RmipyleService rmipyleService = (RmipyleService)registry.lookup(RmipyleService.REGISTRY_KEY);
            if (rmipyleService == null)
            {
               //throw new Exception("cannot connect to service: "+hostname+"and"+portNummer);
                return false;
            }


        }catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
        return true;
    }

    @Override
    public long createArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        long result = 0;
        result = rmipyleService.createArea(userToken, area);
        return result;

    }

    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        long result = 0;
        result = rmipyleService.createQuestion(userToken, areaId, question);
        return result;
    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        long result = 0;
        result = rmipyleService.createAnswer(userToken, areaId, questionId, answer);
        return result;
    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        List<Long> result = null;
        result = rmipyleService.getAreaIds(userToken);
        return result;
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        Area result = new Area();
        result = rmipyleService.getArea(userToken, areaId);
        return  result;
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        List<Long> result = null;
        result = rmipyleService.getQuestionIds(userToken, areaId);
        return result;
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        Question result = new Question();
        result = rmipyleService.getQuestion(userToken, areaId, questionId);
        return result;
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        List<Long> result = null;
        result = rmipyleService.getAnswerIds(userToken, areaId, questionId);
        return result;
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        Answer result = new Answer();
        result = rmipyleService.getAnswer(userToken, areaId, questionId, answerId);
        return result;
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {

        rmipyleService.updateArea(userToken, area);
    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        rmipyleService.updateQuestion(userToken, areaId, question);
    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        rmipyleService.updateAnswer(userToken, areaId, questionId, answer);
    }
}
