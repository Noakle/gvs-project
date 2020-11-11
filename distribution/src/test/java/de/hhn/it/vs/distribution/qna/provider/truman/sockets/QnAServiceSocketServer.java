package de.hhn.it.vs.distribution.qna.provider.truman.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient;
import de.hhn.it.vs.distribution.sockets.SimpleDelegatingServer;

public class QnAServiceSocketServer {

    public static void main(String[] args) throws Exception {
        BDUserManagementService userManagementService = new WnckUserManagementService();
        BDQnAService qnAService = new WnckQnAService(userManagementService);

        SimpleDelegatingServer delegatingUserManagementServer = new SimpleDelegatingServer(1099,
                userManagementService, UserManagementServiceServeOneClient.class);
        SimpleDelegatingServer delegatingQnAServer = new SimpleDelegatingServer(1100,
                qnAService, QnAServiceServeOneClient.class);

        delegatingUserManagementServer.foreverAcceptAndDelegate();
        delegatingQnAServer.foreverAcceptAndDelegate();
    }
}