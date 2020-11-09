package de.hhn.it.vs.distribution.qna.provider.mrvn.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A very simple database stub to model the behaviour of an external database component.
 */
public class DbStub implements Database {
  /**
   * Contains the name mapped on all registered users.
   */
  private HashMap<String, User> users;

  /**
   * Creates an empty database.
   */
  public DbStub() {
    users = new HashMap<>();
  }

  /**
   * Getter for userList.
   *
   * @return list of users
   */
  public List<User> getUserList() {
    return new ArrayList<User>(users.values());
  }

  /**
   * Adds the passed user to the list of registered user.
   */
  public void addUser(User user) {
    users.put(user.getName(), user);
  }

  /**
   * Returns the user with specified name
   *
   * @param name name of the user
   * @return User object for the user
   */
  public User getUser(String name) {
    return users.get(name);
  }
}
