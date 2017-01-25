package ch.heig.amt.g4mify.actors.messages;

/**
 * @author ldavid
 * @created 12/12/16
 */
public class ReceivedEvent {

    private final long eventId;

    public ReceivedEvent(long eventId) {
        this.eventId = eventId;
    }

    public long getEventId() {
        return eventId;
    }

}
