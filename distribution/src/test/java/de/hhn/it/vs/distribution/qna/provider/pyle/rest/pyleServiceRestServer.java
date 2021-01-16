package de.hhn.it.vs.distribution.qna.provider.pyle.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rest.UserManagementServiceRestController;
import de.hhn.it.vs.distribution.core.usermanagement.provider.wnck.rest.UserManagementServiceRestServer;
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
        UserManagementServiceRestServer.class
})

public class pyleServiceRestServer  {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(pyleServiceRestServer.class);

    public static void main(String[] args) {
        // java.time.Instant makes problems with serialization / deserialization,
        // so include the new jsr310 data type mappers.
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        ApplicationContext ctx = new AnnotationConfigApplicationContext(RestCCConfiguration.class);
        SpringApplication.run(pyleServiceRestServer.class, args);
    }
}
