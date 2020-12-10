package de.hhn.it.vs.distribution.qna.provider.nkaz.testclient;

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
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.qna.provider.nkaz.rmi.BDQnAServiceViaRmi;
import de.hhn.it.vs.distribution.qna.provider.nkaz.rmi.RmiQnAService;
import de.hhn.it.vs.distribution.qna.provider.nkaz.rmi.RmiQnAServiceImpl;
import de.hhn.it.vs.distribution.qna.provider.nkaz.sockets.BDQnAServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class NKAZQnADemoClient {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(NKAZQnADemoClient.class);

  private BDQnAService qnAService;
  private BDUserManagementService userManagementService;


  public NKAZQnADemoClient(TestMode testMode) {
    instantiateBDClient(testMode);
  }

  private void instantiateBDClient(TestMode mode) {
    switch (mode) {
      case MOCK:
        qnAService = new WnckQnAService(new WnckUserManagementService());
        break;
      case SOCKET:
        qnAService = new BDQnAServiceViaSockets("localhost", 1101);
        userManagementService = new BDUserManagementServiceViaSockets("localhost", 1099);
        break;
      case RMI:
        userManagementService = new BDUserManagementServiceViaRmi("localhost", 1099);
        qnAService = new BDQnAServiceViaRmi("localhost",1099);
        break;
      case REST:
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }

  public static void main(String[] args) throws Exception {
    NKAZQnADemoClient client = new NKAZQnADemoClient(TestMode.RMI);
    client.runDemo();
  }

  private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    QnAServiceDemoClient qnAServiceDemoClient = new QnAServiceDemoClient();
    qnAServiceDemoClient.runDemo(userManagementService, qnAService);
  }
}
