package de.hhn.it.vs.distribution.qna.provider.nkaz.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient;
import de.hhn.it.vs.distribution.sockets.SimpleDelegatingServer;

public class QnAServiceSocketServer {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(QnAServiceSocketServer.class);

  public static void main(String[] args) throws Exception {
    BDUserManagementService userManagementService = new WnckUserManagementService();
    BDQnAService qnAService = new WnckQnAService(userManagementService);

    SimpleDelegatingServer delegatingServerUserManagement = new SimpleDelegatingServer(1099,
            userManagementService, UserManagementServiceServeOneClient.class);
    delegatingServerUserManagement.foreverAcceptAndDelegate();

    SimpleDelegatingServer delegatingServerQnA =
        new SimpleDelegatingServer(1101, qnAService, QnAServiceServeOneClient.class);
    delegatingServerQnA.foreverAcceptAndDelegate();
  }
}
