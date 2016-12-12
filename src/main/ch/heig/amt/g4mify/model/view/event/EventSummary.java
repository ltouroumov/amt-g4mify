package ch.heig.amt.g4mify.model.view.event;

import ch.heig.amt.g4mify.model.CounterUpdate;
import ch.heig.amt.g4mify.model.view.user.UserSummary;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author ldavid
 * @created 12/12/16
 */
public class EventSummary {

    public long id;

    public Timestamp received;

    public Timestamp processed;

    public List<CounterUpdate> updates;

}
