package de.hhn.it.vs.distribution.qna;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.Token;
import de.hhn.it.vs.common.core.usermanagement.UserNameAlreadyAssignedException;
import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.exceptions.InvalidTokenException;
import de.hhn.it.vs.common.exceptions.ServiceNotAvailableException;
import de.hhn.it.vs.common.qna.model.Answer;
import de.hhn.it.vs.common.qna.model.Area;
import de.hhn.it.vs.common.qna.model.Question;
import de.hhn.it.vs.common.qna.service.BDQnAService;

import java.rmi.RemoteException;
import java.util.List;

public class QnAServiceDemoClient {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(QnAServiceDemoClient.class);




  public void runDemo(BDUserManagementService userManagementService, BDQnAService service) throws IllegalParameterException,
          UserNameAlreadyAssignedException,
          ServiceNotAvailableException, InvalidTokenException, RemoteException {
    Token aliceToken = userManagementService.register("alice@alice.de", "secret", "alice");
    Token bobToken = userManagementService.register("bob@bob.de", "secret", "bob");

    // Alice creates a new area
    Area area = new Area("Household challenges", "questions around the household.");
    logger.info("Alice tries creating a new area with " + area);
    long areaIdForAlice = service.createArea(aliceToken, area);
    logger.info("Alice new area has id " + areaIdForAlice);

    // Alice create a new question
    Question question = new Question("How to clean the inner washing machine?", "How can I clean " +
            "the inner washing machine when I do not have a special washing machine cleaner?");
    logger.info("Alice tries creating a new question with " + question);
    long questionIdForAlice = service.createQuestion(aliceToken, areaIdForAlice, question);
    logger.info("Alice new question has id " + questionIdForAlice);

    // Bob looks for questions
    List<Long> areaIds = service.getAreaIds(bobToken);
    logger.info("Bobs number of areas is " + areaIds.size());
    long areaIdForBob = areaIds.get(0);
    Area areaForBob = service.getArea(bobToken, areaIdForBob);
    logger.info(("Bobs area received is " + areaForBob));
    List<Long> questionIds = service.getQuestionIds(bobToken, areaIdForBob);
    logger.info("Bobs number of questions is " + questionIds.size());
    long questionIdForBob = questionIds.get(0);
    Question questionForBob = service.getQuestion(bobToken, areaIdForBob, questionIdForBob);
    logger.info("Bob question received is " + questionForBob);
    Answer answerFromBob = new Answer("Use four dish washer tabs. This works great!");
    long answerIdForBob = service.createAnswer(bobToken, areaIdForBob, questionIdForBob,
            answerFromBob);
    logger.info("Bobs new answer has id " + answerIdForBob);

    // Alice looks for answers regarding her question
    List<Long> answerIdsForAlice = service.getAnswerIds(aliceToken, areaIdForAlice,
            questionIdForAlice);
    logger.info("Alice number of answers is " + answerIdsForAlice.size());
    long answerIdForAlice = answerIdsForAlice.get(0);
    Answer answerForAlice = service.getAnswer(aliceToken, areaIdForAlice, questionIdForAlice,
            answerIdForAlice);
    logger.info("Alice got the answer " + answerForAlice);

    logger.info("This is how the story ends.");
  }

}
