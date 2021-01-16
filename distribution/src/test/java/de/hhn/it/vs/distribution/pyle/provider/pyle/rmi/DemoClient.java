package de.hhn.it.vs.distribution.pyle.provider.pyle.rmi;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaRmi;
import de.hhn.it.vs.distribution.pyle.rmi.BDpyleServiceViaRmi;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.testsupport.TestMode;

import java.rmi.RemoteException;

public class DemoClient {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(DemoClient.class);
    private BDpyleServiceViaRmi rmipyleService;
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
            case SOCKET:
               // userManagementService = new BDUserManagementServiceViaSockets("localhost", 1099);

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
            ServiceNotAvailableException, UserNameAlreadyAssignedException, InvalidTokenException, RemoteException {
        DemoClient demoClient = new DemoClient(TestMode.RMI);
        demoClient.runDemo();
    }
    private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException, RemoteException {
        QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
        commonDemo.runDemo(bdUserManagementService, (BDQnAService) rmipyleService);
    }


}
