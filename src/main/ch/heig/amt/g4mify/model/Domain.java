package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "domains")
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String key;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<Counter> counters;

    @OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
    private List<Rule> rules;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
