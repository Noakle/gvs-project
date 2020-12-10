package de.hhn.it.vs.distribution.qna.provider.wnck.testclient;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi.BDUserManagementServiceViaRmi;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.qna.provider.wnck.rmi.WnckQnAServiceViaRmi;
import de.hhn.it.vs.distribution.qna.provider.wnck.sockets.BDQnAServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class WnckQnAServiceDemoClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckQnAServiceDemoClient.class);

  BDQnAService qnAService;
  private BDUserManagementService userManagementService;

  public WnckQnAServiceDemoClient(final TestMode mode) {
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
        qnAService = new BDQnAServiceViaSockets("localhost", 1100);
        break;
      case RMI:
        userManagementService = new BDUserManagementServiceViaRmi("localhost", 1099);
        qnAService = new WnckQnAServiceViaRmi("localhost", 1099);
        break;
      case REST:
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }


  public static void main(String[] args) throws IllegalParameterException,
          ServiceNotAvailableException, UserNameAlreadyAssignedException, InvalidTokenException {

    WnckQnAServiceDemoClient qnAServiceDemo = new WnckQnAServiceDemoClient(TestMode.RMI);
    qnAServiceDemo.runDemo();
  }

  private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
    commonDemo.runDemo(userManagementService, qnAService);
  }

}
