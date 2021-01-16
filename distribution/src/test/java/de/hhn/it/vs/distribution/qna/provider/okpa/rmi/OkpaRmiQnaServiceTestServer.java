package de.hhn.it.vs.distribution.qna.provider.okpa.rmi;

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
 * Created by okpa on 09/12/2020.
 */
public class OkpaRmiQnaServiceTestServer {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(OkpaRmiQnaServiceTestServer.class);
  
  public static void main(String[] args) throws RemoteException {
    // implement services
    BDUserManagementService userManagementService = new WnckUserManagementService();
    BDQnAService qnAService = new WnckQnAService(userManagementService);
    // implement wrapper
    RmiUserManagementServiceImpl userManagementWrapper =
        new RmiUserManagementServiceImpl(userManagementService);
    RmiQnAServiceImpl qnAWrapper = new RmiQnAServiceImpl(qnAService);
    // export wrapper implementations
    RmiUserManagementService userManagementProxy =
        (RmiUserManagementService) UnicastRemoteObject.exportObject(userManagementWrapper, 0);
    RmiQnAService qnAProxy = (RmiQnAService) UnicastRemoteObject.exportObject(qnAWrapper, 0);
    // init registry object
    Registry registry = LocateRegistry.createRegistry(1099);
    // bind resulted remote references
    registry.rebind(RmiUserManagementService.REGISTRY_KEY, userManagementProxy);
    logger.info("UserManagementService ready on proxy " + userManagementProxy);
    registry.rebind(RmiQnAService.REGISTRY_KEY, qnAProxy);
    logger.info("QnAService ready on proxy " + qnAProxy);
  }
}