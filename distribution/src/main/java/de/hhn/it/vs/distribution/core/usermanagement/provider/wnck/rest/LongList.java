package de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rest;

import java.util.ArrayList;
import java.util.List;

public class LongList {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(LongList.class);

  private List<Long> idList;

  public LongList() {
    idList = new ArrayList<>();
  }

  public List<Long> getIdList() {
    return idList;
  }

  public void setIdList(final List<Long> idList) {
    this.idList = idList;
  }
}
