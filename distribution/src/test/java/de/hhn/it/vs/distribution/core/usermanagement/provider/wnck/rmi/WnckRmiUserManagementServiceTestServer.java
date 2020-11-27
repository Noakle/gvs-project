package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by wnck on 09.04.17.
 */

public class WnckRmiUserManagementServiceTestServer {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckRmiUserManagementServiceTestServer.class);

  public static void main(String[] args) throws RemoteException {
    Registry registry = LocateRegistry.createRegistry(1099);
    BDUserManagementService service = new WnckUserManagementService();

    RmiUserManagementServiceImpl rmiWrapper = new RmiUserManagementServiceImpl(service);
    RmiUserManagementService proxy = (RmiUserManagementService) UnicastRemoteObject.exportObject(rmiWrapper, 0);
    registry.rebind(RmiUserManagementService.REGISTRY_KEY, proxy);
    logger.info("Service ready on proxy " + proxy);
  }
}
