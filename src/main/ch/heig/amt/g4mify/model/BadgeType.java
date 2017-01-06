package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "badge_types", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"domain_id", "key"})
})
public class BadgeType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = true)
    private String image;

    @Column(nullable = false)
    private boolean isSingleton = true;

    @ManyToOne(optional = true)
    private BadgeType previous;

    @ManyToOne(optional = false)
    private Domain domain;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Badge> badges;

    @ManyToMany(mappedBy = "badgeTypes")
    private List<BadgeRule> badgeRules;

    public BadgeType() {
        this.badges = new ArrayList<>();
        this.badgeRules = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSingleton() {
        return isSingleton;
    }

    public void setSingleton(boolean singleton) {
        this.isSingleton = singleton;
    }

    public BadgeType getPrevious() {
        return previous;
    }

    public void setPrevious(BadgeType previous) {
        this.previous = previous;
    }

    public Domain getDomain() { return domain; }

    public void setDomain(Domain domain) { this.domain = domain; }

    public List<Badge> getBadges() {
        return badges;
    }

    public List<BadgeRule> getBadgeRules() {
        return badgeRules;
    }
}
