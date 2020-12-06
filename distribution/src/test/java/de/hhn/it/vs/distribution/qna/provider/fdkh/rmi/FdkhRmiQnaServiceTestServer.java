package de.hhn.it.vs.distribution.qna.provider.fdkh.rmi;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FdkhRmiQnaServiceTestServer {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(FdkhRmiQnaServiceTestServer.class);

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);

        BDUserManagementService userManagementService = new WnckUserManagementService();
        BDQnAService service = new WnckQnAService(userManagementService);

 /*       RmiUserManagementServiceImpl rmiUserManagementWrapper = new RmiUserManagementServiceImpl(userManagementService);
        RmiUserManagementService proxyUserManagment = (RmiUserManagementService) UnicastRemoteObject.exportObject(rmiUserManagementWrapper, 0);
        registry.rebind(RmiUserManagementService.REGISTRY_KEY, proxyUserManagment);*/

        RmiFdkhServiceImpl rmiQnaWrapper = new RmiFdkhServiceImpl(service);
        RmiFdkhService proxyQna = (RmiFdkhService) UnicastRemoteObject.exportObject(rmiQnaWrapper, 0);
        registry.rebind(RmiFdkhService.REGISTRY_KEY, proxyQna);

//       logger.info("UserManagementService ready on proxy: " + proxyUserManagment);
        logger.info("QnaService ready on proxy: " + proxyQna);
        logger.info("waiting for call...");
    }
}
