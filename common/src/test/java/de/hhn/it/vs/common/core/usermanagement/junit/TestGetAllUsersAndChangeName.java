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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by wnck on 21/03/2017.
 */

public class TestGetAllUsersAndChangeName {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestGetAllUsersAndChangeName.class);
  private static final String EMAIL1 = "hugo1@hugo.de";
  private static final String PASSWORD1 = "secret";
  private static final String NAME1 = "hugo1";

  private static final String EMAIL2 = "hugo2@hugo.de";
  private static final String PASSWORD2 = "secret";
  private static final String NAME2 = "hugo2";
  private static final String BERTA = "berta";

  private BDUserManagementService service;

  @BeforeEach
  public void setup() throws UserNameAlreadyAssignedException, IllegalParameterException,
          ServiceNotAvailableException {
    service = new WnckUserManagementService();
    service.register(EMAIL1, PASSWORD1, NAME1);
  }
  @Test
  public void testGetAllUsers() throws UserNameAlreadyAssignedException,
          IllegalParameterException,
          ServiceNotAvailableException, InvalidTokenException {
    Token token = service.register(EMAIL2, PASSWORD1, NAME2);
    assertNotNull(token);
    List<User> users = service.getUsers(token);
    assertEquals(users.size(), 2);
    User user1 = users.get(0);
    User user2 = users.get(1);
    assertNotEquals(user1.getEmail(), user2.getEmail());
  }


  @Test
  public void testRenamingOfUser() throws IllegalParameterException, ServiceNotAvailableException, InvalidTokenException, UserNameAlreadyAssignedException {
    Token token = service.login(EMAIL1, PASSWORD1);
    User user1 = service.resolveUser(token);
    service.changeName(token, BERTA);
    User user2 = service.resolveUser(token);
    assertNotNull(user2);
    assertNotEquals(user2.getName(), NAME1);
    assertEquals(user2.getName(), BERTA);
  }
}
