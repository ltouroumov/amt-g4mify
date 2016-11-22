package ch.heig.amt.g4mify.model;

import ch.heig.amt.g4mify.json.JsonEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "users")
public class User implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true)
    private String username;

    private String displayname;

    @ManyToOne
    private Domain domain;

    @OneToMany(mappedBy = "user")
    private List<Badge> badges;

    @OneToMany(mappedBy = "user")
    private List<Event> events;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public List<Event> getEvents() {
        return events;
    }
}
