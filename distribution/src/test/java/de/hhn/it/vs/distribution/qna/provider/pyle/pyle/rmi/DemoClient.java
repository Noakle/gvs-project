package de.hhn.it.vs.distribution.pyle.provider.pyle.rmi;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaRmi;
import de.hhn.it.vs.distribution.pyle.rmi.BDpyleServiceViaRmi;
import de.hhn.it.vs.distribution.pyle.rmi.RmipyleService;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.testsupport.TestMode;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DemoClient {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(DemoClient.class);
    private RmipyleService rmipyleService;
    private BDUserManagementService bdUserManagementService;


    public DemoClient(final TestMode mode)
    {
        instantiateBDClient(mode);
    }

    private void instantiateBDClient(TestMode mode) {
        switch(mode)
        {
            case MOCK:
                bdUserManagementService = new WnckUserManagementService();
               // rmipyleService = new WnckQnAService(bdUserManagementService);
                break;
            case RMI:
                bdUserManagementService = new BDUserManagementServiceViaRmi("localhost", 1099);
                rmipyleService = new BDpyleServiceViaRmi("localhost", 1099);
                break;
            case REST:
            default:
                throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);

        }
    }

    public static void main(String[] args) throws IllegalParameterException,
    ServiceNotAvailableException, UserNameAlreadyAssignedException, InvalidTokenException {
    /*Token token = new Token();
    Area area = new Area();
    String host = (args.length < 1) ? null : args[0];
    try
    {
        long response = 0;
        //Registry registry = LocateRegistry.getRegistry(host);
        BDpyleServiceViaRmi bDpyleServiceViaRmi = new BDpyleServiceViaRmi("127.0.0.1", 1099);
       if (bDpyleServiceViaRmi.serviceKommuication()) {
            response = bDpyleServiceViaRmi.createArea(token, area);
           System.out.println("Response: "+response);
       }
       else
       {
           System.out.println("Client is not connect to the service");
       }

    }catch (RemoteException re)
    {
        System.out.println(re.toString());
    } catch (ServiceNotAvailableException e) {
        e.toString();
    } catch (InvalidTokenException e) {
        e.toString();
    } catch (IllegalParameterException e) {
        e.toString();
    }*/
        DemoClient demoClient = new DemoClient(TestMode.RMI);
        demoClient.runDemo();
    }
    private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
        QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
        commonDemo.runDemo(bdUserManagementService, (BDQnAService) rmipyleService);
    }


}
