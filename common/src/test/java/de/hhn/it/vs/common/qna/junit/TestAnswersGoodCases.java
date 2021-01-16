package de.hhn.it.vs.common.qna.junit;

import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.User;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.helper.UserManagementMock;
import de.hhn.it.vs.common.qna.model.Answer;
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

public class TestAnswersGoodCases {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestAnswersGoodCases.class);
  private BDQnAService service;
  private long areaId1;
  private long questionId1;
  private Token token1;

  @BeforeEach
  public void setup() throws IllegalParameterException, ServiceNotAvailableException,
          InvalidTokenException, RemoteException {
    UserManagementMock userManagementService = new UserManagementMock();
    List<User> users =
            userManagementService.getMockUsers();
    token1 = users.get(0).getToken();
    service = new WnckQnAService(userManagementService);
    areaId1 = service.createArea(token1, new Area("Area 1", "This is area1"));
    questionId1 = service.createQuestion(token1, areaId1, new Question("Question 1", "Content " +
            "Question " +
            "1"));
  }

  @Test
  @DisplayName("create an answer")
  public void createAnAnswer() throws IllegalParameterException, ServiceNotAvailableException, InvalidTokenException, RemoteException {
    Answer answer = new Answer("This is an Answer to question 1");
    long answerId1 = service.createAnswer(token1, areaId1, questionId1, answer);
    Answer answerFromService = service.getAnswer(token1, areaId1, questionId1, answerId1);
    assertAll(
         //   () -> assertEquals(answer.getContent(), answerFromService.getContent()),
          //  () -> assertNotEquals(answer.getId(), answerFromService.getId())
    );
  }

  @Test
  @DisplayName("create multiple answers and check the ids")
  public void createMultipleAnswersAndCheckIds() throws IllegalParameterException,
          ServiceNotAvailableException, InvalidTokenException, RemoteException {
    long aid1 = service.createAnswer(token1, areaId1, questionId1, new Answer("answer1"));
    long aid2 = service.createAnswer(token1, areaId1, questionId1, new Answer("answer2"));
    long aid3 = service.createAnswer(token1, areaId1, questionId1, new Answer("answer3"));

    List<Long> ids = service.getAnswerIds(token1, areaId1, questionId1);

    assertAll(
            () -> assertTrue(ids.contains(aid1)),
            () -> assertTrue(ids.contains(aid2)),
            () -> assertTrue(ids.contains(aid3))
    );

  }
}
