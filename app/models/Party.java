package models;

import javax.persistence.Entity;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class Party extends Model implements PartyI {

    @Required
    public String name;
    public String longName;

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
