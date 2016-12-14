package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"profileId", "domain_id"})
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String profileId;

    @Column(nullable = false)
    private String profileUrl;

    @ManyToOne
    private Domain domain;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Badge> badges;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> events;

    public User() {
        this.badges = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
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
