package de.hhn.it.vs.common.qna.provider.wnck;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.qna.model.Question;

import java.util.*;

public class WnckQuestion {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckQuestion.class);

  private static long questionCounter;

  private Question question;
  private Map<Long, WnckAnswer> wnckAnswerMap;

  public WnckQuestion(final Question question) {
    wnckAnswerMap = new HashMap<>();
    this.question = new Question(question);
    this.question.setId(++questionCounter);
  }

  public Question getQuestion() {
    return new Question(question);
  }

  public WnckAnswer getWnckAnswerById(long answerId) throws IllegalParameterException {
    if (!wnckAnswerMap.containsKey(answerId)) {
      throw new IllegalParameterException("Question does not contain answer. Id: " + answerId);
    }
    return wnckAnswerMap.get(answerId);
  }

  public long getId() {
    return question.getId();
  }

  public void addAnswer(final WnckAnswer wnckAnswer) {
    wnckAnswerMap.put(wnckAnswer.getId(), wnckAnswer);
  }

  public List<Long> getAnswerIds() {
    List<Long> results = new ArrayList<>();
    Collection<WnckAnswer> wnckAnswers = wnckAnswerMap.values();
    for (WnckAnswer wnckAnswer : wnckAnswers) {
      results.add(wnckAnswer.getId());
    }
    return results;
  }

  public void update(final Question question) {
    this.question.setTitle(question.getTitle());
    this.question.setContent(question.getContent());
  }
}
