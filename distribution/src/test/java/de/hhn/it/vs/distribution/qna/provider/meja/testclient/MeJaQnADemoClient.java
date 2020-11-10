package de.hhn.it.vs.distribution.qna.provider.meja.testclient;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.UserManagementDemoClient;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.meja.provider.sockets.BDQnAServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

/**
 * Created by meja on 10.11.2020.
 */

public class MeJaQnADemoClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(MeJaQnADemoClient.class);

  BDUserManagementService userManagementService;
  BDQnAService qnAService;


  public MeJaQnADemoClient(TestMode mode) {
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
        qnAService = new BDQnAServiceViaSockets("localhost", 1050);
        break;
      case RMI:
      case REST:
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }

  private void runDemo() {
    UserManagementDemoClient userManagementDemoClient = new UserManagementDemoClient();
    userManagementDemoClient.runDemo(userManagementService);
  }

  public static void main(String[] args) {
    MeJaQnADemoClient client = new MeJaQnADemoClient(TestMode.SOCKET);
    client.runDemo();
  }

}
