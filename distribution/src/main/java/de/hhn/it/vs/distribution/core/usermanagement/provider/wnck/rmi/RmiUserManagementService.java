package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rmi;


import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by wnck on 11/03/2017.
 */
public interface RmiUserManagementService extends Remote {
  public static final String REGISTRY_KEY = "cc.usermanagement";

  Token register(String email, String password, String name) throws IllegalParameterException,
          UserNameAlreadyAssignedException, ServiceNotAvailableException, RemoteException;

  Token login(String email, String password)
          throws IllegalParameterException, ServiceNotAvailableException, RemoteException;

  User resolveUser(Token userToken)
          throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException,
          RemoteException;

  List<User> getUsers(Token userToken)
          throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException,
          RemoteException;

  void changeName(Token userToken, String newName) throws IllegalParameterException,
          InvalidTokenException, UserNameAlreadyAssignedException,
          ServiceNotAvailableException, RemoteException;
}
