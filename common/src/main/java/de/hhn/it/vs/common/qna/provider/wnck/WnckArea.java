package de.hhn.it.vs.common.qna.provider.wnck;

import de.hhn.it.vs.common.exceptions.IllegalParameterException;
import de.hhn.it.vs.common.qna.model.Area;

import java.util.*;

public class WnckArea {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(WnckArea.class);

  private static long areaCounter;

  private Area area;
  private Map<Long, WnckQuestion> questionMap;

  public WnckArea(final Area area) {
    questionMap = new HashMap<>();
    this.area = new Area(area);
    this.area.setId(++areaCounter);
  }

  public Area getArea() {
    return new Area(area);
  }


  public void addQuestion(WnckQuestion question) {
    questionMap.put(question.getId(), question);
  }

  public long getId() {
    return area.getId();
  }

  public WnckQuestion getWnckQuestionById(final long questionId) throws IllegalParameterException {
    if (!questionMap.containsKey(questionId)) {
      throw new IllegalParameterException("Question does not exist in area. id: " + questionId);
    }
    return questionMap.get(questionId);
  }

  public List<Long> getQuestionIds() {
    List<Long> results = new ArrayList<>();
    Collection<WnckQuestion> wnckQuestions = questionMap.values();
    for (WnckQuestion wnckQuestion : wnckQuestions) {
      results.add(wnckQuestion.getId());
    }
    return results;
  }

  public void update(final Area area) {
    this.area.setTitle(area.getTitle());
    this.area.setDescription(area.getDescription());
  }
}
