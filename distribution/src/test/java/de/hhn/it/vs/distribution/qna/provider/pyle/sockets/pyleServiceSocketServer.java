package de.hhn.it.vs.distribution.qna.provider.pyle.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rest.BDUserManagementServiceViaRest;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi.BDUserManagementServiceViaRmi;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.qna.provider.wnck.rest.BDQnAServiceViaRest;
import de.hhn.it.vs.distribution.qna.provider.wnck.rmi.WnckQnAServiceViaRmi;
import de.hhn.it.vs.distribution.qna.provider.wnck.testclient.WnckQnAServiceDemoClient;
import de.hhn.it.vs.distribution.testsupport.TestMode;

import java.rmi.RemoteException;

public class pyleServiceSocketServer  {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(pyleServiceSocketServer.class);

    BDQnAService qnAService;
    private BDUserManagementService userManagementService;

    public pyleServiceSocketServer(final TestMode mode) {
        instantiateBDClient(mode);
    }

    private void instantiateBDClient(TestMode mode) {
        switch (mode) {
            case MOCK:
                userManagementService = new WnckUserManagementService();
                qnAService = new WnckQnAService(userManagementService);
                break;
            case SOCKET:
                userManagementService = new BDUserManagementServiceViaSockets("localhost", 1099);
                qnAService =  new BDPyleServiceViaSockets("localhost", 1099);
                break;
           case RMI:
                userManagementService = new BDUserManagementServiceViaRmi("localhost", 1099);
                qnAService = new WnckQnAServiceViaRmi("localhost", 1099);
                break;
            case REST:
                userManagementService = new BDUserManagementServiceViaRest("http://localhost:8080/usermanagementservice/");
                qnAService = new BDQnAServiceViaRest("http://localhost:8080/wnckqnaservice/");
                break;
            default:
                throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
        }
    }


    public static void main(String[] args) throws IllegalParameterException,
            ServiceNotAvailableException, UserNameAlreadyAssignedException, InvalidTokenException, RemoteException {

        WnckQnAServiceDemoClient qnAServiceDemo = new WnckQnAServiceDemoClient(TestMode.REST);
        qnAServiceDemo.runDemo();
    }

    public void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException, RemoteException {
        QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
        commonDemo.runDemo(userManagementService, qnAService);
    }

}
