package ch.heig.amt.g4mify.model;

import javax.persistence.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
public class Badge {

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
