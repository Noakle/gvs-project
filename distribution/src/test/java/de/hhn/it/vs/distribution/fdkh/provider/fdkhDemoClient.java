package de.hhn.it.vs.distribution.fdkh.provider;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.fdkh.provider.socket.BDfdkhServiceViaSockets;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.qna.provider.wnck.testclient.WnckQnAServiceDemoClient;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class fdkhDemoClient {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(fdkhDemoClient.class);

    private BDQnAService qnAService;
    private BDUserManagementService userManagementService;

    public fdkhDemoClient(final TestMode mode) {
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
                qnAService = new BDfdkhServiceViaSockets("localhost", 1098);
                break;
            case RMI:
            case REST:
            default:
                throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
        }
    }


    public static void main(String[] args) throws IllegalParameterException,
            ServiceNotAvailableException, UserNameAlreadyAssignedException, InvalidTokenException {

        fdkhDemoClient qnAServiceDemo = new fdkhDemoClient(TestMode.SOCKET);
        qnAServiceDemo.runDemo();
    }

    private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
        QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
        commonDemo.runDemo(userManagementService, qnAService);
    }
}
