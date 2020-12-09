package de.hhn.it.vs.distribution.qna.provider.nkaz.rmi;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import java.rmi.RemoteException;
import java.util.List;

public class RmiUserManagementServiceImpl implements RmiUserManagementService {

  private final BDUserManagementService service;

  public RmiUserManagementServiceImpl(
      BDUserManagementService service) {
    this.service = service;
  }

  @Override
  public Token register(String email, String password, String name)
      throws IllegalParameterException, UserNameAlreadyAssignedException, ServiceNotAvailableException {
    return service.register(email, password, name);
  }

  @Override
  public Token login(String email, String password)
      throws IllegalParameterException, ServiceNotAvailableException, RemoteException {
    return service.login(email, password);
  }

  @Override
  public User resolveUser(Token userToken)
      throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException, RemoteException {
    return service.resolveUser(userToken);
  }

  @Override
  public List<User> getUsers(Token userToken)
      throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException, RemoteException {
    return service.getUsers(userToken);
  }

  @Override
  public void changeName(Token userToken, String newName)
      throws IllegalParameterException, InvalidTokenException, UserNameAlreadyAssignedException, ServiceNotAvailableException, RemoteException {
    service.changeName(userToken, newName);
  }
}
