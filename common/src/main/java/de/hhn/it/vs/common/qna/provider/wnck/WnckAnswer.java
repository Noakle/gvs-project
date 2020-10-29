package de.hhn.it.vs.common.qna.provider.wnck;

import de.hhn.it.vs.common.qna.model.Answer;

public class WnckAnswer {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckAnswer.class);

  private static long answerCounter;

  private Answer answer;

  public WnckAnswer(final Answer answer) {
    this.answer = new Answer(answer);
    this.answer.setId(++answerCounter);
  }

  public Answer getAnswer() {
    return new Answer(answer);
  }

  public Long getId() {
    return answer.getId();
  }

  public void update(final Answer answer) {
    this.answer.setContent(answer.getContent());
  }
}
