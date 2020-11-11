package de.hhn.it.vs.distribution.qna.provider.raat.testclient.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.qna.provider.raat.sockets.BDqnaServiceViaSockets;
import de.hhn.it.vs.distribution.qna.provider.raat.testclient.sockets.RaatQnADemoClient;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class RaatQnADemoClient {

    private static final org.slf4j.Logger logger =
        org.slf4j.LoggerFactory.getLogger(
            de.hhn.it.vs.distribution.qna.provider.raat.testclient.sockets.RaatQnADemoClient.class);

    private BDQnAService qnAService;
    private BDUserManagementService userManagementService;

    public RaatQnADemoClient(TestMode testMode) {
      instantiateBDClient(testMode);
    }

    private void instantiateBDClient(TestMode mode) {
      switch (mode) {
        case MOCK:
          qnAService = new WnckQnAService(new WnckUserManagementService());
          break;
        case SOCKET:
          qnAService = new BDqnaServiceViaSockets("localhost", 1101);
          userManagementService = new BDUserManagementServiceViaSockets("localhost", 1099);
          break;
        case RMI:
        case REST:
        default:
          throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
      }
    }

    public static void main(String[] args) throws Exception {
      de.hhn.it.vs.distribution.qna.provider.raat.testclient.sockets.RaatQnADemoClient
          client = new de.hhn.it.vs.distribution.qna.provider.raat.testclient.sockets.RaatQnADemoClient(TestMode.SOCKET);
      client.runDemo();
    }

    private void runDemo() throws InvalidTokenException, IllegalParameterException,
        ServiceNotAvailableException, UserNameAlreadyAssignedException {
      QnAServiceDemoClient qnAServiceDemoClient = new QnAServiceDemoClient();
      qnAServiceDemoClient.runDemo(userManagementService, qnAService);
    }
  }


