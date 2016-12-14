package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "metrics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "counter_id"})
})
public class Metric {

    @Id
    @GeneratedValue
    private long id;

    private String name;

    /**
     * Duration in minutes (will be rounded to the nearest 15 minutes, -1 means infinite)
     */
    private int duration;

    @ManyToOne(optional = false)
    private Counter counter;

    @OneToMany(mappedBy = "metric", cascade = CascadeType.ALL)
    private List<Bucket> buckets;

    public Metric() {
        this.buckets = new ArrayList<>();
    }

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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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
