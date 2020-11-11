package de.hhn.it.vs.distribution.qna.provider.truman.sockets;

import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.sockets.SimpleDelegatingServer;

public class QnAServiceSocketServer {

    public static void main(String[] args) throws Exception {
        BDQnAService qnAService = new WnckQnAService(new WnckUserManagementService());

        SimpleDelegatingServer delegatingQnAServer = new SimpleDelegatingServer(1099,
                qnAService, QnAServiceServeOneClient.class);
        delegatingQnAServer.foreverAcceptAndDelegate();
    }
}