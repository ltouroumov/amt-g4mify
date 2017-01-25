package ch.heig.amt.g4mify.extension;

import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author ldavid
 * @created 11/21/16
 */
@Component
public class SpringExtension implements Extension {

    private ApplicationContext applicationContext;

    public void initialize(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorName) {
        return Props.create(SpringActorProducer.class, applicationContext, actorName);
    }

}
