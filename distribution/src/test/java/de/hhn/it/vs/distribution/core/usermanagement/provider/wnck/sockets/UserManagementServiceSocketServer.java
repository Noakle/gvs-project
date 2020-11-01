package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.distribution.sockets.SimpleDelegatingServer;

/**
 * Created by wnck on 27.04.17.
 */

public class UserManagementServiceSocketServer {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(UserManagementServiceSocketServer.class);


    public static void main(String[] args) throws Exception {
        BDUserManagementService userManagementService = new WnckUserManagementService();
        SimpleDelegatingServer delegatingServer = new SimpleDelegatingServer(1099,
                userManagementService, UserManagementServiceServeOneClient.class);
        delegatingServer.foreverAcceptAndDelegate();

    }
}
