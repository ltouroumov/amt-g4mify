package ch.heig.amt.g4mify.model;

import javax.persistence.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "buckets")
public class Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private long time;

    @Column(nullable = false)
    private long value;

    @Column(nullable = false)
    private long version = 0;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

}
