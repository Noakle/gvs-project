package de.hhn.it.vs.common.core.usermanagement.junit;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by wnck on 21/03/2017.
 */

public class TestCorrectRegisterAndLogin {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestCorrectRegisterAndLogin.class);
  private static final String EMAIL1 = "hugo1@hugo.de";
  private static final String PASSWORD1 = "secret";
  private static final String NAME1 = "hugo1";

  private BDUserManagementService service;

  @BeforeEach
  public void setup() {
    service = new WnckUserManagementService();
  }

  @Test
  public void testRegisterResolveAndLogin() throws UserNameAlreadyAssignedException,
          IllegalParameterException,
          ServiceNotAvailableException, InvalidTokenException {
    Token token = service.register(EMAIL1, PASSWORD1, NAME1);
    assertNotNull(token);
    User user = service.resolveUser(token);
    assertNotNull(user);
    assertEquals(user.getEmail(), EMAIL1);
    assertEquals(user.getName(), NAME1);
    Token token2 = service.login(EMAIL1, PASSWORD1);
    assertNotNull(token2);
    assertEquals(token, token2);
  }


}
