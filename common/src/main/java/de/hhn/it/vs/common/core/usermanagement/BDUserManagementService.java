package de.hhn.it.vs.common.core.usermanagement;


import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.util.List;

/**
 * Created by wnck on 11/03/2017.
 */
public interface BDUserManagementService {
  /**
   * Registers a user in the system.
   *
   * @param email    identifies the user
   * @param password authenticates the user to get access to his token. The password has to contain
   *                 at minimum four characters.
   * @param name     nick name of the user to be used in UIs. This nick name has to contain at
   *                 minimum
   *                 two visible characters.
   * @return Token identifying the user
   * @throws IllegalParameterException        if one of the parameters doesn't match the
   *                                          specification
   * @throws UserNameAlreadyAssignedException if the nick name is already assigned to another user
   * @throws ServiceNotAvailableException     if the service cannot be provided
   */
  Token register(String email, String password, String name) throws IllegalParameterException,
          UserNameAlreadyAssignedException, ServiceNotAvailableException, IOException;

  /**
   * Login for a registered user, e.g. from another device or after restart of a client
   *
   * @param email    identifies the user
   * @param password authenticates the user
   * @return Token identifying the user
   * @throws IllegalParameterException    if email or password do not match the values in the user
   *                                      management service
   * @throws ServiceNotAvailableException if the service cannot be provided
   */
  Token login(String email, String password)
          throws IllegalParameterException, ServiceNotAvailableException;

  /**
   * returns user info related to the given token.
   *
   * @param userToken token identifying the user
   * @return User object related to the given token
   * @throws IllegalParameterException    if a null reference is given
   * @throws InvalidTokenException        if the token is no valid token from this user
   *                                      management service
   * @throws ServiceNotAvailableException if the service cannot be provided
   */
  User resolveUser(Token userToken)
          throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException;

  /**
   * Returns a list of registered users. This method is for admin or debug purposes.
   *
   * @param userToken token identifying the user
   * @return List of User objects
   * @throws IllegalParameterException    if a null reference is given
   * @throws InvalidTokenException        if the token is no valid token from this user
   *                                      management service
   * @throws ServiceNotAvailableException if the service cannot be provided
   */
  List<User> getUsers(Token userToken)
          throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException;

  /**
   * Allows the user to change his nick name.
   *
   * @param userToken Token identifying the user
   * @param newName   new nick name. Must the same specification as in the register method.
   * @throws IllegalParameterException        if either the token is a null reference or the name
   *                                          does not match the specification.
   * @throws InvalidTokenException            if the token is no valid token from this user
   *                                          management service
   * @throws UserNameAlreadyAssignedException if the name is already used by another user
   * @throws ServiceNotAvailableException     if the service cannot be provided
   */
  void changeName(Token userToken, String newName) throws IllegalParameterException,
          InvalidTokenException, UserNameAlreadyAssignedException, ServiceNotAvailableException;
}
