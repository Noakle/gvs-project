package de.hhn.it.vs.distribution.qna.provider.nkaz.rmi;

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

public class NKAZRMIQnaServiceTestServer {

  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(NKAZRMIQnaServiceTestServer.class);

  public static void main(String[] args) throws RemoteException {
    Registry registry = LocateRegistry.createRegistry(1099);

    BDUserManagementService bdUserManagementService = new WnckUserManagementService();
    BDQnAService qnAService = new WnckQnAService(bdUserManagementService);

    RmiUserManagementServiceImpl rmiUserManagementWrapper =
        new RmiUserManagementServiceImpl(bdUserManagementService);
    RmiUserManagementService proxyUserManagement =
        (RmiUserManagementService) UnicastRemoteObject.exportObject(rmiUserManagementWrapper, 0);
    registry.rebind(RmiUserManagementService.REGISTRY_KEY, proxyUserManagement);

    RmiQnAServiceImpl rmiQnaWrapper = new RmiQnAServiceImpl(qnAService);
    RmiQnAService proxyQna = (RmiQnAService) UnicastRemoteObject.exportObject(rmiQnaWrapper, 0);
    registry.rebind(RmiQnAService.REGISTRY_KEY, proxyQna);

    logger.info("Ready to operate!");
  }
}
