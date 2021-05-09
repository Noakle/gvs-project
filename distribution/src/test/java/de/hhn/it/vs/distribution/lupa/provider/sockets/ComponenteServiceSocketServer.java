package de.hhn.it.vs.distribution.lupa.provider.sockets;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.sockets.UserManagementServiceServeOneClient;
import de.hhn.it.vs.distribution.sockets.SimpleDelegatingServer;

/**
 * Created by wnck on 27.04.17.
 */

public class ComponenteServiceSocketServer {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(ComponenteServiceSocketServer.class);


    public static void main(String[] args) throws Exception {
        BDUserManagementService userManagementService = new WnckUserManagementService();
        SimpleDelegatingServer delegatingServer = new SimpleDelegatingServer(1099,
                userManagementService, ComponentServiceServeOneClient.class);
        delegatingServer.foreverAcceptAndDelegate();

    }
}
