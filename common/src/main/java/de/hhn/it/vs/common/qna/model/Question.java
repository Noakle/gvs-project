package de.hhn.it.vs.common.qna.model;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public  @Data class Question  implements Serializable {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(Question.class);

  @PositiveOrZero
  private long id;
  @NotBlank
  private String title;
  @NotBlank
  private String content;

  public Question() {
  }

  public Question(Question question) {
  this(question.getTitle(), question.getContent());
  this.id = question.getId();
  }

  public Question(final String title, final String content) {
    this.title = title;
    this.content = content;
  }
}
