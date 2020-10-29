package de.hhn.it.vs.common.core.usermanagement.junit;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.TokenFactory;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by wnck on 16.04.17.
 */

public class UseBadParametersTest {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(UseBadParametersTest.class);
  private BDUserManagementService service;
  private Token goodToken;
  private TokenFactory objectTokenFactory;
  private Token badToken;


  @BeforeEach
  public void setup() throws UserNameAlreadyAssignedException, IllegalParameterException, ServiceNotAvailableException {
    service = new WnckUserManagementService();
    goodToken = service.register("sdsdf@gaga.de", "secret", "sdsdf");
    badToken = new Token("This is not a user token");
  }

  @Test
  @DisplayName("Try to register a user with bad parameters")
  public void registerWithBadParameters() {
    Token token = null;

    // try with email as null
    IllegalParameterException exception = assertThrows(IllegalParameterException.class,
            () ->service.register(null, "secret", "hugo") );

    // try with password as null
    exception = assertThrows(IllegalParameterException.class,
            () ->service.register("hugo@hugo.de", null, "hugo"));

    // try with name as null
    exception = assertThrows(IllegalParameterException.class,
            () ->service.register("hugo@hugo.de", "secret", null));

  }

  @Test
  @DisplayName("Try to resolve a user with null Token")
  public void resolveWithNullToken() {
    IllegalParameterException exception = assertThrows(IllegalParameterException.class,
            () -> service.resolveUser(null));
  }

  @Test
  @DisplayName("Try to resolve a user with a bad Token")
  public void resolveWithForeignToken() {
    InvalidTokenException exception = assertThrows(InvalidTokenException.class,
            () -> service.resolveUser(badToken));
  }

  @Test
  @DisplayName("Try to change user name with a bad Token")
  public void changeNameWithForeignToken() {
    InvalidTokenException exception = assertThrows(InvalidTokenException.class,
            () -> service.changeName(badToken, "egon"));
  }

  @Test
  public void changeNameWithNullReferenceAsToken() throws InvalidTokenException, IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    IllegalParameterException exception = assertThrows(IllegalParameterException.class, () ->
            service.changeName(null, "egon"));
  }

  @Test
  public void changeNameWithNullReferenceAsName() throws InvalidTokenException,
          IllegalParameterException, ServiceNotAvailableException, UserNameAlreadyAssignedException {
    IllegalParameterException exception = assertThrows(IllegalParameterException.class, () ->
            service.changeName(goodToken, null));
  }

  @Test
  public void getUsersWithNullReferenceAsToken() throws IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
    IllegalParameterException exception = assertThrows(IllegalParameterException.class, () ->
            service.getUsers(null));
  }

  @Test
  public void getUsersWithBadTokenAsToken() throws IllegalParameterException,
          InvalidTokenException, ServiceNotAvailableException {
    InvalidTokenException exception = assertThrows(InvalidTokenException.class, () ->
            service.getUsers(badToken));
  }

}
