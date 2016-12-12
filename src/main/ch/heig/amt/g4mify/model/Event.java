package ch.heig.amt.g4mify.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private Timestamp received;

    @Column(nullable = true)
    private Timestamp processed = null;

    @ManyToOne(optional = false)
    private User user;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<CounterUpdate> updates;

    public Event() {
        this.updates = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public Timestamp getReceived() {
        return received;
    }

    public void setReceived(Timestamp received) {
        this.received = received;
    }

    public Timestamp getProcessed() {
        return processed;
    }

    public void setProcessed(Timestamp processed) {
        this.processed = processed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CounterUpdate> getUpdates() {
        return updates;
    }
}
