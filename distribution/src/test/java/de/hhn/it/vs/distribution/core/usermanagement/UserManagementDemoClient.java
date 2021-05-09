package de.hhn.it.vs.distribution.core.usermanagement;


import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.helper.UserManagementMock;

import java.io.IOException;

public class UserManagementDemoClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(UserManagementDemoClient.class);
  public static final String HUGO_HUGO_DE = "hugo@hugo.de";
  public static final String SECRET = "secret";
  public static final String CLIENT = "CLIENT";
  public static final String USER1 = UserManagementMock.USER_1;
  public static final String USER1_EMAIL = UserManagementMock.USER1_USER_DE;
  public static final String USER2 = UserManagementMock.USER_2;
  public static final String USER2_EMAIL = UserManagementMock.USER2_USER_DE;


  public void runDemo(BDUserManagementService userManagementService) throws IOException {
    try {
      logger.debug("register first user ...");
      Token token1 = userManagementService.register(USER1_EMAIL, SECRET, USER1);
      logger.debug("register second user ...");
      Token token2 = userManagementService.register(USER2_EMAIL, SECRET, USER2);
      logger.debug("Token 1 = " + token1);
      logger.debug("Token 2 = " + token2);

      User user1 = userManagementService.resolveUser(token1);
      logger.debug("User1 = " + user1);


      Token tokenAgain1 = userManagementService.login(USER1_EMAIL, SECRET);
      logger.debug("First Token was: " + token1);
      logger.debug("Token again was: " + tokenAgain1);
    } catch (IllegalParameterException
            | UserNameAlreadyAssignedException
            | ServiceNotAvailableException
            | InvalidTokenException e) {
      e.printStackTrace();
    }

    // try something with an exception
    try {
      User userWhichCannotExist = userManagementService.resolveUser(new Token("NonononoToken"));
    } catch (IllegalParameterException | ServiceNotAvailableException e) {
      e.printStackTrace();
    } catch (InvalidTokenException e) {
      logger.debug("Correct exception thrown.");
    }
  }

}
