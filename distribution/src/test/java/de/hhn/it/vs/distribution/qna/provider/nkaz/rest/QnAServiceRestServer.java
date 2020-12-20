package de.hhn.it.vs.distribution.qna.provider.nkaz.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rest.UserManagementServiceRestController;
import de.hhn.it.vs.distribution.rest.RestCCConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackageClasses = {
        UserManagementServiceRestController.class,
        QnAServiceRestController.class
})
public class QnAServiceRestServer {
  public static void main(String[] args) {
      // java.time.Instant makes problems with serialization / deserialization,
      // so include the new jsr310 data type mappers.
      ObjectMapper mapper = new ObjectMapper();
      mapper.findAndRegisterModules();

      ApplicationContext ctx = new AnnotationConfigApplicationContext(RestCCConfiguration.class);
      SpringApplication.run(QnAServiceRestServer.class, args);
  }

  /*
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build();
    }
   */
}
