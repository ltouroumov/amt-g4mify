package ch.heig.amt.g4mify.actors;

import akka.actor.UntypedActor;
import ch.heig.amt.g4mify.actors.messages.ReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final EventProcessor eventProcessor;

    @Autowired
    public EventActor(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof ReceivedEvent) {
            eventProcessor.process((ReceivedEvent) message);
        } else {
            throw new Exception("EventActor received unkown message type");
        }

    }

}
