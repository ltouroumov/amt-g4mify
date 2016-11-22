package ch.heig.amt.g4mify.model;

import ch.heig.amt.g4mify.json.JsonEntity;

import javax.persistence.*;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
@Table(name = "rules")
public class Rule implements BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

}
