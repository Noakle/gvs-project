package de.hhn.it.vs.distribution.qna.provider.mabo.testclient;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.testclient.WnckUserManagementDemoClient;
import de.hhn.it.vs.distribution.fdkh.provider.fdkhDemoClient;
import de.hhn.it.vs.distribution.fdkh.provider.socket.BDfdkhServiceViaSockets;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class MaboQnAServiceDemoClient {
  private static final org.slf4j.Logger logger =
    org.slf4j.LoggerFactory.getLogger(WnckUserManagementDemoClient.class);

  BDQnAService qnAService;
  BDUserManagementService userManagementService;


  public MaboQnAServiceDemoClient(TestMode mode) {
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

  private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
    commonDemo.runDemo(userManagementService, qnAService);
  }

  public static void main(String[] args) throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    MaboQnAServiceDemoClient maboQnAServiceDemoClient = new MaboQnAServiceDemoClient(TestMode.SOCKET);
    maboQnAServiceDemoClient.runDemo();
  }
}
