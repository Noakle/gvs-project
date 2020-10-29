package de.hhn.it.vs.common.qna.junit;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.helper.UserManagementMock;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestAreasBadCases {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestAreasBadCases.class);
  private BDQnAService service;
  private Token token1;


  @BeforeEach
  public void setup() {
    UserManagementMock userManagementService = new UserManagementMock();
    List<User> users =
            userManagementService.getMockUsers();
    token1 = users.get(0).getToken();
    service = new WnckQnAService(userManagementService);
  }

  @Test
  @DisplayName("try creating an area with null title")
  public void createAreaWithNullTitle() {
    Area area = new Area(null, "Area without title");
    IllegalParameterException illegalParameterException =
            assertThrows(IllegalParameterException.class,
                    () -> service.createArea(token1, area));
  }

  @Test
  @DisplayName("try creating an area with blank title")
  public void createAreaWithBlankTitle() {
    Area area = new Area("", "");
    IllegalParameterException illegalParameterException =
            assertThrows(IllegalParameterException.class,
                    () -> service.createArea(token1, area));
    logger.info(illegalParameterException.getMessage());
  }
}
