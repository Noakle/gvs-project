package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.testclient;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.distribution.core.usermanagement.UserManagementDemoClient;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

import java.io.IOException;

public class WnckUserManagementDemoClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckUserManagementDemoClient.class);

  BDUserManagementService userManagementService;


  public WnckUserManagementDemoClient(TestMode mode) {
    instantiateBDClient(mode);
  }

  private void instantiateBDClient(TestMode mode) {
    switch (mode) {
      case MOCK:
        userManagementService = new WnckUserManagementService();
        break;
      case SOCKET:
        userManagementService = new BDUserManagementServiceViaSockets("localhost", 1099);
        break;
      case RMI:
      case REST:
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }

  private void runDemo() throws IOException {
    UserManagementDemoClient userManagementDemoClient = new UserManagementDemoClient();
    userManagementDemoClient.runDemo(userManagementService);
  }

  public static void main(String[] args) {
    WnckUserManagementDemoClient client = new WnckUserManagementDemoClient(TestMode.SOCKET);
    try {
      client.runDemo();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
