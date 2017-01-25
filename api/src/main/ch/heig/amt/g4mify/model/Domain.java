package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "domains")
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String key;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Counter> counters;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BadgeRule> badgeRules;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRule> eventRules;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BadgeType> badgeTypes;

    public Domain() {
        this.users = new ArrayList<>();
        this.counters = new ArrayList<>();
        this.badgeRules = new ArrayList<>();
        this.eventRules = new ArrayList<>();
        this.badgeTypes = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Counter> getCounters() {
        return counters;
    }

    public List<BadgeRule> getBadgeRules() {
        return badgeRules;
    }

    public List<EventRule> getEventRules() {
        return eventRules;
    }

    public List<BadgeType> getBadgeTypes() {
        return badgeTypes;
    }
}
