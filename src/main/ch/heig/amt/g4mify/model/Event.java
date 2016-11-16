package ch.heig.amt.g4mify.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() {
        return id;
    }

}
