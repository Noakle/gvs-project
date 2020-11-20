package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by wnck on 09.04.17.
 */

public class RmiUserManagementServiceImpl implements RmiUserManagementService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(RmiUserManagementServiceImpl.class);

  BDUserManagementService service;

  public RmiUserManagementServiceImpl(final BDUserManagementService service) {
    this.service = service;
  }

  @Override
  public Token register(final String email, final String password, final String name) throws
          IllegalParameterException, UserNameAlreadyAssignedException,
          ServiceNotAvailableException, RemoteException {
    return service.register(email, password, name);
  }

  @Override
  public Token login(final String email, final String password) throws IllegalParameterException,
          ServiceNotAvailableException, RemoteException {
    return service.login(email, password);
  }

  @Override
  public User resolveUser(final Token userToken) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException, RemoteException {
    return service.resolveUser(userToken);
  }

  @Override
  public List<User> getUsers(final Token userToken) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException, RemoteException {
    return service.getUsers(userToken);
  }

  @Override
  public void changeName(final Token userToken, final String newName) throws
          IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException,
          ServiceNotAvailableException, RemoteException {
    service.changeName(userToken, newName);
  }
}
