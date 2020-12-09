package de.hhn.it.vs.distribution.qna.provider.nkaz.rmi;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class NKAZRMIUserManagementServiceTestServer {


  public static void main(String[] args) throws RemoteException {
    Registry registry = LocateRegistry.createRegistry(1099);

    BDUserManagementService bdService = new WnckUserManagementService();
    BDQnAService qnaService = new WnckQnAService(bdService);

    RmiUserManagementServiceImpl rmiServiceImpl = new RmiUserManagementServiceImpl(bdService);
    RmiUserManagementService proxy = (RmiUserManagementService) UnicastRemoteObject
        .exportObject(rmiServiceImpl, 0);
    registry.rebind(RmiUserManagementService.REGISTRY_KEY, proxy);
  }

}
