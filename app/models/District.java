package models;

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
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
