package de.hhn.it.vs.distribution.qna.provider.team_rocket.testclient;

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
import de.hhn.it.vs.distribution.qna.provider.team_rocket.sockets.BDQnAServiceViaSockets;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class Team_RocketQnAServiceDemoClient {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(Team_RocketQnAServiceDemoClient.class);

  private BDQnAService qnAService;
  private BDUserManagementService userManagementService;

  public Team_RocketQnAServiceDemoClient(TestMode testMode) {
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
      case REST:
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }

  public static void main(String[] args) throws Exception {
    Team_RocketQnAServiceDemoClient client = new Team_RocketQnAServiceDemoClient(TestMode.SOCKET);
    client.runDemo();
  }

  private void runDemo()
      throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException,
          UserNameAlreadyAssignedException {
    QnAServiceDemoClient qnAServiceDemoClient = new QnAServiceDemoClient();
    qnAServiceDemoClient.runDemo(userManagementService, qnAService);
  }
}
