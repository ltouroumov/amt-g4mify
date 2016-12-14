package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private Timestamp awarded;

    @Column(nullable = false)
    private long level = 0;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private BadgeType type;

    public long getId() {
        return id;
    }

    public Timestamp getAwarded() {
        return awarded;
    }

    public void setAwarded(Timestamp awarded) {
        this.awarded = awarded;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
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
