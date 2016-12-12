package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.time.Duration;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "metrics")
public class Metric {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    private Duration duration;

    @ManyToOne
    private Counter counter;

    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL)
    private List<Bucket> buckets;

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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
