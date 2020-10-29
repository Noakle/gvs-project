package de.hhn.it.vs.common.qna.model;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public @Data class Answer implements Serializable {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(Answer.class);

  @PositiveOrZero
  private long id;
  @NotBlank
  private String content;

  public Answer() {
  }

  public Answer(Answer answer) {
    this(answer.getContent());
    this.id = answer.getId();
  }

  public Answer(final String content) {
    this.content = content;
  }
}
