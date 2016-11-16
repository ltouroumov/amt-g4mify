package ch.heig.amt.g4mify.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

}
