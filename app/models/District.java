package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class District extends Model {

    @Required
    @Column(unique = true)
    public String name;

    public District(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
