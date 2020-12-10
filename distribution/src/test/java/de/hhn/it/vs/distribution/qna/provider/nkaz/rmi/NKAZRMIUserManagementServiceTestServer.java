package de.hhn.it.vs.distribution.qna.provider.nkaz.rmi;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi.RmiUserManagementService;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi.RmiUserManagementServiceImpl;
import de.hhn.it.vs.distribution.qna.provider.fdkh.rmi.RmiFdkhService;
import de.hhn.it.vs.distribution.qna.provider.fdkh.rmi.RmiFdkhServiceImpl;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class NKAZRMIUserManagementServiceTestServer {

  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(NKAZRMIUserManagementServiceTestServer.class);

  public static void main(String[] args) throws RemoteException {
    Registry registry = LocateRegistry.createRegistry(1099);

    BDUserManagementService bdService = new WnckUserManagementService();
    BDQnAService qnaService = new WnckQnAService(bdService);

    RmiUserManagementServiceImpl rmiUserManagementWrapper = new RmiUserManagementServiceImpl(
        bdService);
    RmiUserManagementService proxyUserManagement = (RmiUserManagementService) UnicastRemoteObject
        .exportObject(rmiUserManagementWrapper, 0);
    registry.rebind(RmiUserManagementService.REGISTRY_KEY, proxyUserManagement);

    RmiFdkhServiceImpl rmiQnaWrapper = new RmiFdkhServiceImpl(qnaService);
    RmiFdkhService proxyQna = (RmiFdkhService) UnicastRemoteObject.exportObject(rmiQnaWrapper, 0);
    registry.rebind(RmiQnAService.REGISTRY_KEY, proxyQna);

    logger.info("Ready to operate!");
  }

}
