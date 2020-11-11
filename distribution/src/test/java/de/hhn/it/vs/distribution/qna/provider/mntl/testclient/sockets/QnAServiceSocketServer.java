package de.hhn.it.vs.distribution.qna.provider.mntl.testclient.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient;
import de.hhn.it.vs.distribution.qna.provider.mntl.sockets.QnAServiceServeOneClient;
import de.hhn.it.vs.distribution.sockets.SimpleDelegatingServer;

/**
 * Testserver
 * @author Leibl, Nauendorf
 * @version 2020-11-11
 */
public class QnAServiceSocketServer {

  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(QnAServiceSocketServer.class);


  public static void main(String[] args) throws Exception {
    logger.debug("build..");
    BDUserManagementService userManagementService = new WnckUserManagementService();
    BDQnAService qnAService = new WnckQnAService(userManagementService);

    SimpleDelegatingServer delegatingServer = new SimpleDelegatingServer(1099,
        userManagementService, UserManagementServiceServeOneClient.class);

    SimpleDelegatingServer qnaDelegatingServer = new SimpleDelegatingServer(1100,
        qnAService, QnAServiceServeOneClient.class);

    logger.debug("server accepts and delegates..");
    delegatingServer.foreverAcceptAndDelegate();
    qnaDelegatingServer.foreverAcceptAndDelegate();

  }


}
