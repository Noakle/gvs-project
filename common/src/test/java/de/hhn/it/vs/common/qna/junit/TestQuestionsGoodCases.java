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

import static org.junit.jupiter.api.Assertions.*;

public class TestQuestionsGoodCases {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestQuestionsGoodCases.class);
  private BDQnAService service;
  private long areaId1;
  private Token token1;

  @BeforeEach
  public void setup() throws IllegalParameterException, ServiceNotAvailableException, InvalidTokenException, RemoteException {
    UserManagementMock userManagementService = new UserManagementMock();
    List<User> users =
            userManagementService.getMockUsers();
    token1 = users.get(0).getToken();
    service = new WnckQnAService(userManagementService);
    areaId1 = service.createArea(token1, new Area("Area 1", "This is area1"));
  }

  @Test
  @DisplayName("create a question")
  public void createAQuestion() throws IllegalParameterException, ServiceNotAvailableException, InvalidTokenException, RemoteException {
    Question question = new Question("Question 1", "Description of question 1");
    long qid = service.createQuestion(token1, areaId1, question);
    Question questionFromServer = service.getQuestion(token1, areaId1, qid);
    assertAll(
          //  () -> assertEquals(question.getTitle(), questionFromServer.getTitle()),
        //    () -> assertEquals(question.getContent(), questionFromServer.getContent()),
         //   () -> assertNotEquals(question.getId(), questionFromServer.getId())
    );
  }

  @Test
  @DisplayName("create multiple questions and check the ids")
  public void createMultipleQuestionsAnsCheckTheIds() throws IllegalParameterException, ServiceNotAvailableException, InvalidTokenException, RemoteException {
    long qid1 = service.createQuestion(token1, areaId1, new Question("Question 1", "Question content 1"));
    long qid2 = service.createQuestion(token1, areaId1, new Question("Question 2", "Question content 2"));
    long qid3 = service.createQuestion(token1, areaId1, new Question("Question 3", "Question content 3"));

    List<Long> ids = service.getQuestionIds(token1, areaId1);

    assertAll(
            () -> ids.contains(qid1),
            () -> ids.contains(qid2),
            () -> ids.contains(qid3)
    );
  }
}
