package de.hhn.it.vs.distribution.qna.provider.fdkh.rest;

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
        FdkhServiceRestController.class,
        UserManagementServiceRestController.class
})

public class FdkhServiceRestServer {
    private static final org.slf4j.Logger logger =
            org.slf4j.LoggerFactory.getLogger(FdkhServiceRestServer.class);

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        ApplicationContext ctx = new AnnotationConfigApplicationContext(RestCCConfiguration.class);
        SpringApplication.run(FdkhServiceRestServer.class, args);
    }
}
