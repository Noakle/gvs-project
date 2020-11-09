package de.hhn.it.vs.distribution.qna.provider.mrvn.data;

import java.util.List;

public interface Database {

  /**
   * Returns a list of all the users registered by the service.
   *
   * @return String representation of a User
   */
  public List<User> getUserList();
}
