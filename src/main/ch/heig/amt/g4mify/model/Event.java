package ch.heig.amt.g4mify.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Column(nullable = false)
    private String type;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, EventData> data;

    public Event() {
        this.data = new HashMap<>();
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, EventData> getData() {
        return data;
    }

    public EventData getData(String key) {
        return data.get(key);
    }

}
