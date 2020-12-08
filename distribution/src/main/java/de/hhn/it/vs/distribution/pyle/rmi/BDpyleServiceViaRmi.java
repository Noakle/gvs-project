package de.hhn.it.vs.distribution.pyle.rmi;

import com.sun.media.jfxmedia.logging.Logger;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class BDpyleServiceViaRmi implements RmipyleService {
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

        try {

            result = rmipyleService.createArea(userToken, area);

        }catch (Exception ex)
        {
            System.out.println(ex.toString());
        }
        return  result;
    }

    @Override
    public long createQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return 0;
    }

    @Override
    public long createAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return 0;
    }

    @Override
    public List<Long> getAreaIds(Token userToken) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return null;
    }

    @Override
    public Area getArea(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return null;
    }

    @Override
    public List<Long> getQuestionIds(Token userToken, long areaId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return null;
    }

    @Override
    public Question getQuestion(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return null;
    }

    @Override
    public List<Long> getAnswerIds(Token userToken, long areaId, long questionId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return null;
    }

    @Override
    public Answer getAnswer(Token userToken, long areaId, long questionId, long answerId) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {
        return null;
    }

    @Override
    public void updateArea(Token userToken, Area area) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {

    }

    @Override
    public void updateQuestion(Token userToken, long areaId, Question question) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {

    }

    @Override
    public void updateAnswer(Token userToken, long areaId, long questionId, Answer answer) throws ServiceNotAvailableException, IllegalParameterException, InvalidTokenException, RemoteException {

    }
}
