package ch.heig.amt.g4mify.actors;

import akka.actor.UntypedActor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @author ldavid
 * @created 11/21/16
 */
@Component
@Scope("prototype")
public class EventActor extends UntypedActor {

    private static Logger LOG = Logger.getLogger(EventActor.class.getSimpleName());

    @Override
    public void onReceive(Object message) throws Throwable {
        LOG.info("Received Message");
    }

}
