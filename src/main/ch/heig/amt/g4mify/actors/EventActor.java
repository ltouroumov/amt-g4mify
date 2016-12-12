package ch.heig.amt.g4mify.actors;

import akka.actor.UntypedActor;
import ch.heig.amt.g4mify.actors.messages.ReceivedEvent;
import ch.heig.amt.g4mify.model.Event;
import ch.heig.amt.g4mify.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.logging.Logger;

/**
 * @author ldavid
 * @created 11/21/16
 */
@Component
@Scope("prototype")
public class EventActor extends UntypedActor {

    private static Logger LOG = Logger.getLogger(EventActor.class.getSimpleName());

    @Autowired
    private EventsRepository eventsRepository;

    @Override
    public void onReceive(Object message) throws Throwable {

        if (message instanceof ReceivedEvent) {
            Event event = eventsRepository.findOne(((ReceivedEvent) message).getEventId());
            LOG.info("Processing Event " + event.getId());
            Thread.sleep(20000);
            event.setProcessed(Timestamp.from(Instant.now()));
            eventsRepository.save(event);
            LOG.info("Finished Processing Event " + event.getId());
        } else {
            throw new Exception("EventActor received unkown message type");
        }

    }

}
