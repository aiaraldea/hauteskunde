package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class Municipality extends Model {

    public String code;
    public String name;
    @OneToMany(mappedBy = "municipality")
    public List<PollingStation> pollingStations;

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
