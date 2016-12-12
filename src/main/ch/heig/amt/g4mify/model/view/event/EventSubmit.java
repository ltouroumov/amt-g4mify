package ch.heig.amt.g4mify.model.view.event;

import ch.heig.amt.g4mify.model.CounterUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 12/12/16
 */
public class EventSubmit {

    public long user;

    public List<CounterUpdate> updates;

    public EventSubmit() {
        this.updates = new ArrayList<>();
    }
}
