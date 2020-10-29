package de.hhn.it.vs.common.qna.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public @Data class Area implements Serializable {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(Area.class);

  @PositiveOrZero
  private long id;
  @NotBlank
  private String title;
  @NotBlank
  private String description;

  public Area() {
  }

  public Area(Area area) {
    this(area.getTitle(), area.getDescription());
    this.id = area.getId();
  }

  public Area(final String title, final String description) {
    this.title = title;
    this.description = description;
  }

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }
}
