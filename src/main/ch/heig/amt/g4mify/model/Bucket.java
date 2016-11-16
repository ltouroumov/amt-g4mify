package ch.heig.amt.g4mify.model;

import javax.persistence.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
public class Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long time;

    private long value;

    @ManyToOne
    private Metric metric;

    public long getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }
}
