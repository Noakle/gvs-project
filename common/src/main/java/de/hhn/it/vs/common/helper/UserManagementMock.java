package de.hhn.it.vs.common.helper;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Simulates a user management.
 * The user management knows some registered users.
 *
 * <p>It is not possible to create new data (users) in this mock
 * implementation. But it is possible to use
 * the mock data for interactions.
 *
 * <p>With getMockUsers you  get all mock users.
 */
public class UserManagementMock implements BDUserManagementService
         {
  public static final String USER1_USER_DE = "user1@user.de";
  public static final String USER2_USER_DE = "user2@user.de";
  public static final String USER3_USER_DE = "user3@user.de";
  public static final String USER_1 = "user1";
  public static final String USER_2 = "user2";
  public static final String USER_3 = "user3";
  public static final String USER_1_TOKEN = "user1-token";
  public static final String USER_2_TOKEN = "user2-token";
  public static final String USER_3_TOKEN = "user3-token";


  private HashMap<String, Token> email2token;
  private HashMap<Token, User> token2users;
  private User user1, user2, user3;


  /**
   * Simple constructor to allocate supporting data structures.
   */
  public UserManagementMock() {
    // create data structures
    email2token = new LinkedHashMap<>();
    token2users = new LinkedHashMap<>();

    // configure mock users
    user1 = new User(USER_1, USER1_USER_DE);
    user1.setToken(new Token(USER_1_TOKEN));
    token2users.put(user1.getToken(), user1);
    email2token.put(USER1_USER_DE, user1.getToken());
    user2 = new User(USER_2, USER2_USER_DE);
    user2.setToken(new Token(USER_2_TOKEN));
    token2users.put(user2.getToken(), user2);
    email2token.put(USER2_USER_DE, user2.getToken());
    user3 = new User(USER_3, USER3_USER_DE);
    user3.setToken(new Token(USER_3_TOKEN));
    token2users.put(user3.getToken(), user3);
    email2token.put(USER3_USER_DE, user3.getToken());

  }

  /**
   * Returns a list of available mock users.
   * @return list of users in the mock implementation
   */
  public List<User> getMockUsers() {
    List<User> result = new ArrayList<>();
    result.addAll(token2users.values());
    return result;
  }



  @Override
  public Token register(String email, String password, String name) throws
          IllegalParameterException, UserNameAlreadyAssignedException,
          ServiceNotAvailableException {
    // if the data matches a mock user, send back the token.
    if (email2token.containsKey(email)) {
      User user = token2users.get(email2token.get(email));
      if (user.getName().equals(name)) {
        return user.getToken();
      }
    }

    // OK, no match: So kill this misuse of the mock implementation
    throw new IllegalArgumentException("This is a mock version which cannot register new users");
  }

  @Override
  public Token login(String email, String password) throws IllegalParameterException,
          ServiceNotAvailableException {
    if (!email2token.containsKey(email)) {
      throw new IllegalParameterException("Email not known: " + email);
    }
    return email2token.get(email);
  }

  @Override
  public User resolveUser(Token userToken) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    CheckingHelper.assertThatIsNotNull(userToken, "UserToken");
    if (!token2users.containsKey(userToken)) {
      throw new InvalidTokenException("No valid user token: " + userToken);
    }
    return token2users.get(userToken);
  }

  @Override
  public List<User> getUsers(Token userToken) throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    List<User> result = new ArrayList<>();
    result.addAll(token2users.values());
    return result;
  }

  @Override
  public void changeName(Token userToken, String newName) throws IllegalParameterException,
          InvalidTokenException, UserNameAlreadyAssignedException, ServiceNotAvailableException {
    throw new IllegalArgumentException("This is a mock version which cannot rename users");

  }
}
