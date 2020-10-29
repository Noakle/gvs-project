package de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd;


import de.hhn.it.vs.common.core.usermanagement.*;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.helper.CheckingHelper;

import java.util.*;

/**
 * Created by wnck on 21/03/2017.
 */

public class WnckUserManagementService implements BDUserManagementService {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckUserManagementService.class);

  private Map<String, User> usersByEmail;
  private Map<String, String> passwordsByEmail;
  private Map<Token, String> emailByToken;
  protected TokenFactory factory;


  public WnckUserManagementService() {
    logger.debug("constructor - instance created.");
    usersByEmail = new HashMap<>();
    passwordsByEmail = new HashMap<>();
    emailByToken = new HashMap<>();
    factory = new UserManagementServiceTokenFactory();
  }

  private void assumeTokenValid(Token userToken) throws InvalidTokenException,
          IllegalParameterException {
    CheckingHelper.assertThatIsNotNull(userToken, "token");
    if (!emailByToken.containsKey(userToken)) {
      String message = "This token is not valid in this UserManagementService";
      logger.warn(message);
      throw new InvalidTokenException(message);
    }
  }

  @Override
  public Token register(final String email, final String password, final String name)
          throws IllegalParameterException, UserNameAlreadyAssignedException,
          ServiceNotAvailableException {
    logger.debug("register    - Email: " + email + ", password: " + password + ", name: " + name);
    CheckingHelper.assertThatIsReadableString(email, "email");
    CheckingHelper.assertThatIsReadableString(password, "password");
    CheckingHelper.assertThatIsReadableString(name, "name");

    // TODO: Check mail address

    if (usersByEmail.containsKey(email)) {
      throw new IllegalParameterException("Email already registered: " + email);
    }

    User user = new User(name, email);
    Token token = factory.createToken(user);
    user.setToken(token);
    emailByToken.put(token, email);
    usersByEmail.put(email, user);
    passwordsByEmail.put(email, password);

    return token;
  }

  @Override
  public Token login(final String email, final String password) throws IllegalParameterException,
          ServiceNotAvailableException {
    logger.debug("login       - Email: " + email + ", password: " + password);
    CheckingHelper.assertThatIsReadableString(email, "email");
    CheckingHelper.assertThatIsReadableString(password, "password");

    // check if the user is known
    if (!usersByEmail.containsKey(email)) {
      throw new IllegalParameterException("Email not known in UserManagementService.");
    }

    // check if the password matches
    String storedPassword = passwordsByEmail.get(email);

    // TODO: don't write passwords onto the console in production. This is only for debug purposes
    if (!password.equals(storedPassword)) {
      logger.warn("Given password " + password + " does not match stored password "
              + storedPassword);
      throw new IllegalParameterException("Credentials don't match.");
    }

    User user = usersByEmail.get(email);


    return user.getToken();
  }

  @Override
  public User resolveUser(final Token userToken)
          throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
    logger.debug("resolveUser - Token: " + userToken);
    assumeTokenValid(userToken);

    User user = usersByEmail.get(emailByToken.get(userToken));

    return user;
  }

  @Override
  public List<User> getUsers(final Token token)
          throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
    logger.debug("getUsers - Token: " + token);
    assumeTokenValid(token);

    Collection<User> users = usersByEmail.values();
    List<User> result = new ArrayList<>();
    result.addAll(users);
    return result;
  }

  @Override
  public void changeName(final Token token, final String s)
          throws IllegalParameterException, InvalidTokenException,
          UserNameAlreadyAssignedException, ServiceNotAvailableException {
    logger.debug("changeName  - Token: " + token + ", name: " + s);
    assumeTokenValid(token);
    CheckingHelper.assertThatIsNotNull(s, "name");
    User user = resolveUser(token);
    user.setName(s);
  }
}
