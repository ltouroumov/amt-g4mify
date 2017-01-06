package ch.heig.amt.g4mify.model.view.event;

import ch.heig.amt.g4mify.model.EventData;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ldavid
 * @created 12/12/16
 */
public class EventSubmit {

    public long user;

    public String type;

    public Map<String, EventData> data;

    public EventSubmit() {
        this.data = new HashMap<>();
    }

}
