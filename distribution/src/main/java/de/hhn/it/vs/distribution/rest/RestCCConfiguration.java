package de.hhn.it.vs.distribution.rest;

import de.hhn.it.vs.common.core.usermanagement.BDUserManagementService;
import de.hhn.it.vs.common.core.usermanagement.provider.wnck.bd.WnckUserManagementService;
import de.hhn.it.vs.common.qna.provider.wnck.WnckQnAService;
import de.hhn.it.vs.common.qna.service.BDQnAService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestCCConfiguration {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(RestCCConfiguration.class);

  private BDUserManagementService userManagementService;
  private BDQnAService qnAService;

  public RestCCConfiguration() {
    userManagementService = new WnckUserManagementService();
    qnAService = new WnckQnAService(userManagementService);
  }

  @Bean(autowire = Autowire.BY_TYPE)
  public BDUserManagementService getUserManagementService() {
    return userManagementService;
  }

  @Bean(autowire = Autowire.BY_TYPE)
  public BDQnAService getQnAService() {
    return qnAService;
  }
}
