package de.hhn.it.vs.distribution.qna.provider.nkaz.rmi;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;



public class BDUserManagementServiceViaRmi implements BDUserManagementService {
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(BDUserManagementServiceViaRmi.class);


  private String hostname = "localhost";
  private int portNumber = 1099;
  private RmiUserManagementService service;

  public BDUserManagementServiceViaRmi(final String hostname, final int portNumber) {
    this.hostname = hostname;
    this.portNumber = portNumber;
  }

  private void connectToService() throws ServiceNotAvailableException {
    logger.debug("get access to registry on host {} with port {}", hostname, portNumber);
    try {
      Registry registry = LocateRegistry.getRegistry(hostname, portNumber);
      service = (RmiUserManagementService) registry.lookup(RmiUserManagementService.REGISTRY_KEY);
    } catch (RemoteException | NotBoundException ex) {
      String errorMessage = "cannot connect to service on host / port "
          + hostname + " / " + portNumber;
      throw new ServiceNotAvailableException(errorMessage, ex);
    }
  }

  private void checkRemoteReference() throws ServiceNotAvailableException {
    if (service == null ) {
      connectToService();
    }
  }

  @Override
  public Token register(final String email, final String password, final String name) throws
      IllegalParameterException, UserNameAlreadyAssignedException,
      ServiceNotAvailableException {
    checkRemoteReference();
    return service.register(email, password, name);
  }

  @Override
  public Token login(final String email, final String password) throws IllegalParameterException,
      ServiceNotAvailableException {
    try {
      checkRemoteReference();
      return service.login(email, password);
    } catch (RemoteException e) {
      logger.warn("Problems with RMI: " + e.getMessage());
      throw new ServiceNotAvailableException(e);
    }
  }

  @Override
  public User resolveUser(final Token userToken) throws IllegalParameterException,
      InvalidTokenException, ServiceNotAvailableException {
    try {
      checkRemoteReference();
      return service.resolveUser(userToken);
    } catch (RemoteException e) {
      logger.warn("Problems with RMI: " + e.getMessage());
      throw new ServiceNotAvailableException(e);
    }
  }

  @Override
  public List<User> getUsers(final Token userToken) throws IllegalParameterException,
      InvalidTokenException, ServiceNotAvailableException {
    try {
      checkRemoteReference();
      return service.getUsers(userToken);
    } catch (RemoteException e) {
      logger.warn("Problems with RMI: " + e.getMessage());
      throw new ServiceNotAvailableException(e);
    }
  }

  @Override
  public void changeName(final Token userToken, final String newName) throws
      IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException,
      ServiceNotAvailableException {

    try {
      checkRemoteReference();
      service.changeName(userToken, newName);
    } catch (RemoteException e) {
      logger.warn("Problems with RMI: " + e.getMessage());
      throw new ServiceNotAvailableException(e);
    }

  }
}
