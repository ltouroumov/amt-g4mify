package ch.heig.amt.g4mify.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author ldavid
 * @created 11/14/16
 */
@Entity
public class User {

    @Id
    private String username;

    private String displayname;

    @ManyToOne
    private Domain domain;

    @OneToMany(mappedBy = "user")
    private List<Badge> badges;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }
}
