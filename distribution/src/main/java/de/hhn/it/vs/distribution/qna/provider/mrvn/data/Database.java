package de.hhn.it.vs.distribution.qna.provider.mrvn.data;

import java.util.List;

/**
 * Database interface used in this component.
 */
public interface Database {

  /**
   * Returns a list of all the users registered by the service.
   *
   * @return String representation of a User
   */
  public List<User> getUserList();

  /**
   * Returns the User entry of this name.
   *
   * @param name the name of the user
   * @return the User representation of the user
   */
  public User getUser(String name);
}
