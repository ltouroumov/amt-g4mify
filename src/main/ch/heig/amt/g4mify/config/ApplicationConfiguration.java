package ch.heig.amt.g4mify.config;

import akka.actor.ActorSystem;
import ch.heig.amt.g4mify.extension.SpringExtension;
import ch.heig.amt.g4mify.json.EntityJsonDeserializer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author ldavid
 * @created 11/21/16
 */
@Configuration
@ComponentScan(basePackages = {
    "ch.heig.amt.g4mify.extension",
    "ch.heig.amt.g4mify.actors",
    "ch.heig.amt.g4mify.json"
})
public class ApplicationConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private SpringExtension springExtension;

    @Bean
    @Autowired
    public Jackson2ObjectMapperBuilder objectMapperBuilder(EntityJsonDeserializer entityDeserializer) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.deserializers(entityDeserializer);
        return builder;
    }

    @Bean
    public ActorSystem actorSystem() {

        ActorSystem system = ActorSystem
                .create("AkkaTaskProcessing", akkaConfiguration());
        springExtension.initialize(applicationContext);
        return system;
    }

    @Bean
    public Config akkaConfiguration() {
        return ConfigFactory.load();
    }

}
