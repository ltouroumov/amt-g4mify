package ch.heig.amt.g4mify.model;

import ch.heig.amt.g4mify.json.JsonEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "events")
public class Event implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<CounterUpdate> updates;

    public Event() {
    }

    public long getId() {
        return id;
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
