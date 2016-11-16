package ch.heig.amt.g4mify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ldavid
 * @created 11/9/16
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

}
