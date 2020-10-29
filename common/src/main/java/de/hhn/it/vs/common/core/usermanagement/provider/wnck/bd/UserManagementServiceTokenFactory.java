package de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd;


import de.hhn.it.vs.common.core.usermanagement.User;

/**
 * Created by wnck on 18/12/2016.
 */

public class UserManagementServiceTokenFactory extends AbstractTokenFactory {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(UserManagementServiceTokenFactory.class);


  /**
   * Must generate a unique string which cannot reproduce the original data, e.g. by using a hash
   * function.
   * Same object data should produces same tokens.
   *
   * @param object input to the mechanism to generate always the same token based on the same object
   * @return resulting string
   */
  @Override
  protected String generateTokenString(final Object object) {
    // For test reasons we want that users with the same email get always the same token
    if (object instanceof User) {
      User user = (User) object;
      return "email-" + user.getEmail();
    }

    return object.toString();
  }
}
