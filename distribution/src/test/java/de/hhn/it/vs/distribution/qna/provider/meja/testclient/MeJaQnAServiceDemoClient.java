package de.hhn.it.vs.distribution.qna.provider.meja.testclient;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.qna.QnAServiceDemoClient;
import de.hhn.it.vs.distribution.testsupport.TestMode;

public class MeJaQnAServiceDemoClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(MeJaQnAServiceDemoClient.class);

  BDQnAService qnAService;
  private BDUserManagementService userManagementService;

  public MeJaQnAServiceDemoClient(final TestMode mode) {
    instantiateBDClient(mode);
  }

  private void instantiateBDClient(TestMode mode) {
    switch (mode) {
      case MOCK:
        userManagementService = new WnckUserManagementService();
        qnAService = new WnckQnAService(userManagementService);
        break;
      case RMI:
      case SOCKET:
      case REST:
      default:
        throw new IllegalArgumentException("Unknown or unimplemented distribution mode: " + mode);
    }
  }


  public static void main(String[] args) throws IllegalParameterException,
          ServiceNotAvailableException, UserNameAlreadyAssignedException, InvalidTokenException {

    MeJaQnAServiceDemoClient qnAServiceDemo = new MeJaQnAServiceDemoClient(TestMode.MOCK);
    qnAServiceDemo.runDemo();
  }

  private void runDemo() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    QnAServiceDemoClient commonDemo = new QnAServiceDemoClient();
    commonDemo.runDemo(userManagementService, qnAService);
  }

}
