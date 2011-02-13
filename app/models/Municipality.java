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

    public String state;
    public String code;
    public String name;
    @OneToMany(mappedBy = "municipality")
    public List<PollingStation> pollingStations;

    public Municipality(String state, String code) {
        this(state, code, null);
    }

    public Municipality(String state, String code, String name) {
        this.state = state;
        this.code = code;
        this.name = name;
    }

    public static Municipality loadOrCreateMunicipality(String stateCode, String code, String name) {
        Municipality municipality = Municipality.find("byStateAndCode", stateCode, code).first();
        if (municipality == null) {
            municipality = new Municipality(stateCode, code, name);
            municipality.save();
        }
        return municipality;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}
