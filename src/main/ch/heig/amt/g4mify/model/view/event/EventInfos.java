package ch.heig.amt.g4mify.model.view.event;

import ch.heig.amt.g4mify.model.view.user.UserSummary;

import java.util.List;

/**
 * @author ldavid
 * @created 12/12/16
 */
public class EventInfos {

    public UserSummary user;

    public List<EventSummary> events;

    public EventInfos(UserSummary user, List<EventSummary> events) {
        this.user = user;
        this.events = events;
    }
}
