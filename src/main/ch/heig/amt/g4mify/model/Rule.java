package ch.heig.amt.g4mify.model;

import javax.persistence.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String condition;

    @ManyToOne(optional = false)
    private BadgeType grants;

    @ManyToOne(optional = false)
    private Domain domain;

    public long getId() {
        return id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public BadgeType getGrants() {
        return grants;
    }

    public void setGrants(BadgeType grants) {
        this.grants = grants;
    }
}
