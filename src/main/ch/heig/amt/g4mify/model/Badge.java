package ch.heig.amt.g4mify.model;

import ch.heig.amt.g4mify.json.JsonEntity;

import javax.persistence.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "badges")
public class Badge implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private BadgeType type;

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BadgeType getType() {
        return type;
    }

    public void setType(BadgeType type) {
        this.type = type;
    }
}
