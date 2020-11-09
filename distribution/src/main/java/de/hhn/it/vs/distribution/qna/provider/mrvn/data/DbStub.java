package de.hhn.it.vs.distribution.qna.provider.mrvn.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A very simple database stub to model the behaviour of an external database component.
 */
public class DbStub implements Database {
  /**
   * Contains the string representation of all registered users.
   */
  private ArrayList<User> userlist;

  /**
   * Creates an empty database.
   */
  public DbStub() {
    userlist = new ArrayList<>();
  }

  /**
   * Getter for userList.
   *
   * @return list of users
   */
  public List<User> getUserList() {
    return new ArrayList<User>(userlist);
  }

  /**
   * Adds the passed user to the list of registered user.
   */
  public void addUser(User user) {
    userlist.add(user);
  }
}
