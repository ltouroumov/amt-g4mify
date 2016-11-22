package ch.heig.amt.g4mify.model;

import javax.persistence.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "domains")
public class Domain implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
