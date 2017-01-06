package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 1/6/17
 */
@Entity
@Table(name = "event_rules")
public class EventRule {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private Domain domain;

    @Column(nullable = false)
    @ElementCollection
    private List<String> types;

    @Column(nullable = false)
    private String script;

    public EventRule() {
        this.types = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
