package de.hhn.it.vs.distribution.qna.provider.okpa.testclient;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi.BDUserManagementServiceViaRmi;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.testclient.WnckUserManagementDemoClient;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.qna.provider.okpa.rmi.BDQnAServiceViaRmi;
import de.hhn.it.vs.distribution.qna.provider.okpa.sockets.BDQnAServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class OkpaQnADemoClient {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(WnckUserManagementDemoClient.class);
  
  BDQnAService qnAService;
  private BDUserManagementService userManagementService;
  
  public OkpaQnADemoClient(TestMode mode) {
    instantiateBDClient(mode);
  }
  
  private void instantiateBDClient(TestMode mode) {
    switch (mode) {
      case MOCK:
        userManagementService = new WnckUserManagementService();
        qnAService = new WnckQnAService(userManagementService);
        break;
      case SOCKET:
        userManagementService =
            new BDUserManagementServiceViaSockets("localhost", 1099);
        qnAService = new BDQnAServiceViaSockets("localhost", 1100);
        break;
      case RMI:
        userManagementService =
            new BDUserManagementServiceViaRmi("localhost", 1099);
        qnAService = new BDQnAServiceViaRmi("localhost", 1099);
        break;
      case REST:
        break;
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }
  
  private void runDemo() throws InvalidTokenException, IllegalParameterException,
                                    ServiceNotAvailableException, UserNameAlreadyAssignedException {
    QnAServiceDemoClient qnAServiceDemoClient = new QnAServiceDemoClient();
    qnAServiceDemoClient.runDemo(userManagementService, qnAService);
  }
  
  public static void main(String[] args)
      throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException,
                 UserNameAlreadyAssignedException {
    OkpaQnADemoClient client = new OkpaQnADemoClient(TestMode.RMI);
    client.runDemo();
  }
}