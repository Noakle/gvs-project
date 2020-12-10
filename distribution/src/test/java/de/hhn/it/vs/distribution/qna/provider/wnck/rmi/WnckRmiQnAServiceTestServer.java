package de.hhn.it.vs.distribution.qna.provider.wnck.rmi;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi.RmiUserManagementService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi.RmiUserManagementServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by wnck on 09.04.17.
 */

public class WnckRmiQnAServiceTestServer {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckRmiQnAServiceTestServer.class);

  public static void main(String[] args) throws RemoteException {
    // Create registry
    Registry registry = LocateRegistry.createRegistry(1099);

    // Local service objects
    BDUserManagementService userManagementService = new WnckUserManagementService();
    BDQnAService qnAService = new WnckQnAService(userManagementService);

    // Rmi wrapper for UserManagementService
    RmiUserManagementServiceImpl rmiWrapper = new RmiUserManagementServiceImpl(userManagementService);
    RmiUserManagementService userManagementProxy = (RmiUserManagementService) UnicastRemoteObject.exportObject(rmiWrapper, 0);
    registry.rebind(RmiUserManagementService.REGISTRY_KEY, userManagementProxy);

    // Rmi wrapper for QnAService
    WnckRmiQnAServiceImpl rmiQnAServiceWrapper = new WnckRmiQnAServiceImpl(qnAService);
    RmiQnAService qnaProxy = (RmiQnAService) UnicastRemoteObject.exportObject(rmiQnAServiceWrapper, 0);
    registry.rebind(RmiQnAService.REGISTRY_KEY, qnaProxy);

    logger.info("Service ready.");
  }
}
