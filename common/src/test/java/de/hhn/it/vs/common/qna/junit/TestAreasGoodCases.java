package de.hhn.it.vs.common.qna.junit;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.helper.UserManagementMock;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestAreasGoodCases {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestAreasGoodCases.class);
  public static final String A_NEW_DESCRIPTION = "A new description";
  private BDQnAService service;
  private Area area1;
  private Area area2;
  private Area area3;
  private Token token1;

  @BeforeEach
  void setup() {

    UserManagementMock userManagementService = new UserManagementMock();
    List<User> users = userManagementService.getMockUsers();
    token1 = users.get(0).getToken();
    service = new WnckQnAService(userManagementService);
    area1 = new Area("title area 1", "description area 1");
    area2 = new Area("title area 2", "description area 2");
    area3 = new Area("title area 3", "description area 3");
  }

  @Test
  @DisplayName("create multiple areas")
  public void createMultipleAreas() throws IllegalParameterException,
          ServiceNotAvailableException, InvalidTokenException, RemoteException {
    long areaId1 = service.createArea(token1, area1);
    long areaId2 = service.createArea(token1, area2);
    long areaId3 = service.createArea(token1, area3);
    assertAll(
            () -> assertNotEquals(areaId1, areaId2),
            () -> assertNotEquals(areaId1, areaId3),
            () -> assertNotEquals(areaId3, areaId2)

    );
  }

  @Test
  @DisplayName("get ids of multiple areas")
  public void getIdsOfMultipleAreas() throws IllegalParameterException,
          ServiceNotAvailableException, InvalidTokenException, RemoteException {
    long areaId1 = service.createArea(token1, area1);
    long areaId2 = service.createArea(token1, area2);
    long areaId3 = service.createArea(token1, area3);
    List<Long> ids = service.getAreaIds(token1);
    assertAll(
            () -> assertTrue(ids.contains(areaId1)),
            () -> assertTrue(ids.contains(areaId2)),
            () -> assertTrue(ids.contains(areaId3))
    );
  }

  @Test
  @DisplayName("create and get area")
  public void createAndGetArea() throws IllegalParameterException, ServiceNotAvailableException,
          InvalidTokenException, RemoteException {
    long areaId1 = service.createArea(token1, area1);
    Area areafromService = service.getArea(token1, areaId1);
    assertEquals(area1.getDescription(), areafromService.getDescription());
    assertEquals(area1.getTitle(), areafromService.getTitle());
    assertNotEquals(area1.getId(), areafromService.getId());
  }

  @Test
  @DisplayName("update an area")
  public void updateAnArea() throws IllegalParameterException, ServiceNotAvailableException,
          InvalidTokenException, RemoteException {
    long areaId1 = service.createArea(token1, area1);
    Area areafromService = service.getArea(token1, areaId1);
    Area updatedArea = new Area(areafromService);
    updatedArea.setDescription(A_NEW_DESCRIPTION);
    service.updateArea(token1, updatedArea);
    Area areafromServiceAgain = service.getArea(token1, areaId1);
    assertAll(
            () -> assertEquals(areafromService.getTitle(), areafromServiceAgain.getTitle()),
            () -> assertNotEquals(areafromService.getDescription(),
                    areafromServiceAgain.getDescription()),
            () -> assertEquals(A_NEW_DESCRIPTION, areafromServiceAgain.getDescription())
    );
  }
}
