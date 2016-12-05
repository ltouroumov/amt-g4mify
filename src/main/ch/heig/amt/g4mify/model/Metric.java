package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "metrics")
public class Metric {

    @Id
    private String name;

    @ManyToOne
    private Counter counter;

    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL)
    private List<Bucket> buckets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public List<Bucket> getBuckets() {
        return buckets;
    }
}
