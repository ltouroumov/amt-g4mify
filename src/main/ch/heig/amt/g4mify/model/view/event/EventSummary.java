package ch.heig.amt.g4mify.model.view.event;

import ch.heig.amt.g4mify.model.EventData;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author ldavid
 * @created 12/12/16
 */
public class EventSummary {

    public long id;

    public Timestamp received;

    public Timestamp processed;

    public String type;

    public Map<String, EventData> data;

}
