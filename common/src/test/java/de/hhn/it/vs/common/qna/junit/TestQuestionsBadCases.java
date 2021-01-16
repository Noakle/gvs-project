package de.hhn.it.vs.common.qna.junit;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.helper.UserManagementMock;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestQuestionsBadCases {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestQuestionsBadCases.class);
  private BDQnAService service;
  private long areaId1;
  private Token token1;

  @BeforeEach
  public void setup() throws IllegalParameterException, ServiceNotAvailableException,
          InvalidTokenException, RemoteException {
    UserManagementMock userManagementService = new UserManagementMock();
    List<User> users =
            userManagementService.getMockUsers();
    token1 = users.get(0).getToken();
    service = new WnckQnAService(userManagementService);
    areaId1 = service.createArea(token1, new Area("Area 1", "This is area 1"));
  }

  @Test
  @DisplayName("try creating a question with null title")
  public void createAreaWithNullTitle() {
    Question question = new Question(null, "What is a question?");
    IllegalParameterException illegalParameterException =
            assertThrows(IllegalParameterException.class,
                    () -> service.createQuestion(token1, areaId1, question));
  }

  @Test
  @DisplayName("try creating a question with wrong area id")
  public void createAreaWithWrongAreaId() {
    Question question = new Question("Question?!?", "What is a question?");
    IllegalParameterException illegalParameterException =
            assertThrows(IllegalParameterException.class,
                    () -> service.createQuestion(token1, 4711, question));
  }


}
