package ch.heig.amt.g4mify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ldavid
 * @created 11/9/16
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
    "ch.heig.amt.g4mify.config",
    "ch.heig.amt.g4mify.api",
    "ch.heig.amt.g4mify.util"
})
@EnableSwagger2
public class Application {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

}
