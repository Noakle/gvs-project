package de.hhn.it.vs.distribution.pyle.provider.pyle.rmi;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import de.hhn.it.vs.distribution.pyle.rmi.RmipyleService;
import de.hhn.it.vs.distribution.pyle.rmi.RmipyleServiceImpl;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PyleRmiBDQNAServiceServer {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(PyleRmiBDQNAServiceServer.class);


    public static void main(String[] agrs) {
        try {
            BDUserManagementService wnckUserManagementService = new WnckUserManagementService();
            BDQnAService wnckQnAService = new WnckQnAService(wnckUserManagementService);
        RmipyleServiceImpl rmipyleServiceImpl = new RmipyleServiceImpl(wnckQnAService);

            RmipyleService rmipyleService1 = (RmipyleService)UnicastRemoteObject.exportObject(rmipyleServiceImpl, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind(RmipyleService.REGISTRY_KEY, rmipyleService1);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

    }
}
