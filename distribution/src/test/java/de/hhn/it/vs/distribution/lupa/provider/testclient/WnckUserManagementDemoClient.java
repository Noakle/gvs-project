package de.hhn.it.vs.distribution.lupa.provider.testclient;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.distribution.core.usermanagement.UserManagementDemoClient;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.BDUserManagementServiceViaSockets;
import de.hhn.it.vs.distribution.lupa.provider.ComponentDemoClient;
import de.hhn.it.vs.distribution.lupa.provider.sockets.BDComponentServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

import java.io.IOException;

public class WnckUserManagementDemoClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckUserManagementDemoClient.class);

  BDUserManagementService userManagementService;
  BDComponentServiceViaSockets userManegementServiceLupa;


  public WnckUserManagementDemoClient(TestMode mode) {
    instantiateBDClient(mode);
  }

  private void instantiateBDClient(TestMode mode) {
    switch (mode) {
      case MOCK:
        userManagementService = new WnckUserManagementService();
        break;
      case SOCKET:
        userManegementServiceLupa = new BDComponentServiceViaSockets("localhost", 1099);
        break;
      case RMI:
      case REST:
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }

  private void runDemo() throws IOException {
    ComponentDemoClient componentDemoClient = new ComponentDemoClient();
    componentDemoClient.runDemo(userManegementServiceLupa);
  }

  public static void main(String[] args) {
    //BDComponentServiceViaSockets userManegementServiceLupa = new BDComponentServiceViaSockets("www.javaworld.com", 80);
    WnckUserManagementDemoClient client = new WnckUserManagementDemoClient(TestMode.SOCKET);
    try {
      client.runDemo();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
