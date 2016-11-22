package ch.heig.amt.g4mify.model;

import ch.heig.amt.g4mify.json.JsonEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "counters")
public class Counter implements BaseModel {

    @Id
    private String name;

    @ManyToOne
    private Domain domain;

    @OneToMany(mappedBy = "counter")
    private List<Metric> metrics;

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
