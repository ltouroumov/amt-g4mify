package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "counters", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "domain_id"})
})
public class Counter {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    private Domain domain;

    @OneToMany(mappedBy = "counter")
    private List<Metric> metrics = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }
}
