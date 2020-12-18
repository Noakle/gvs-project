package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rest;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.distribution.rest.RestCCConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Import(RestCCConfiguration.class)

/**
 * Created by wnck on 30.04.17.
 */

@RestController
@RequestMapping("usermanagementservice")
public class UserManagementServiceRestController {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(UserManagementServiceRestController.class);

  private BDUserManagementService userManagementService;

  @Autowired(required = true)
  public UserManagementServiceRestController(final BDUserManagementService userManagementService) {
    this.userManagementService = userManagementService;
  }

  @RequestMapping(value = "users", method = RequestMethod.POST)
  public Token createUser(@RequestBody Map<String, String> params) throws
          UserNameAlreadyAssignedException,
          IllegalParameterException, ServiceNotAvailableException {
    return userManagementService.register(
            params.get("email"),
            params.get("secret"),
            params.get("name"));
  }

  @RequestMapping(value = "login/{email}/", method = RequestMethod.POST)
  public Token getLogin(@PathVariable String email, @RequestBody String password) throws
          IllegalParameterException, ServiceNotAvailableException {
    return userManagementService.login(email, password);
  }

  @RequestMapping(value = "users", method = RequestMethod.GET)
    public List<User> getUsers(@RequestHeader("Token") String userTokenString) throws
          IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
    Token userToken = new Token(userTokenString);
    List<User> users = userManagementService.getUsers(userToken);
    return users;
  }


  @RequestMapping(value = "users/{requestedTokenString}", method = RequestMethod.GET)
  public User getUser(@PathVariable String requestedTokenString) throws
          IllegalParameterException, InvalidTokenException, ServiceNotAvailableException {
    Token requestedUserToken = new Token(requestedTokenString);
    return userManagementService.resolveUser(requestedUserToken);
  }

  @RequestMapping(value = "users/{userTokenString}", method = RequestMethod.PUT)
  public void updateUser(@PathVariable String userTokenString, @RequestBody String name) throws
          InvalidTokenException, IllegalParameterException, ServiceNotAvailableException,
          UserNameAlreadyAssignedException {
    Token userToken = new Token(userTokenString);
    userManagementService.changeName(userToken, name);
  }
}
