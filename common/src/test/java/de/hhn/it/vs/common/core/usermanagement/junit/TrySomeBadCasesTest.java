package de.hhn.it.vs.common.core.usermanagement.junit;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Created by wnck on 16.04.17.
 */

public class TrySomeBadCasesTest {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TrySomeBadCasesTest.class);
  private BDUserManagementService service;

  @BeforeEach
  public void setup() {
    service = new WnckUserManagementService();
  }

  @Test
  public void registerTwiceWithSameEmail() {
    try {
      service.register("hugo@hugo.de", "secret", "hugo");
    } catch (Exception e) {
      fail("this should work.");
    }

    try {
      service.register("hugo@hugo.de", "othersecret", "egon");
      fail("This should throw an exception.");
    } catch (IllegalParameterException e) {
      // success
    } catch (UserNameAlreadyAssignedException e) {
      fail("Wrong exception.");
    } catch (ServiceNotAvailableException e) {
      fail("Wrong exception.");
    }


  }


}
