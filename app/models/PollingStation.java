package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.StringUtils;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class PollingStation extends Model {

    @Required
    @ManyToOne
    public Municipality municipality;
    @Required
    public String psDistrict;
    @Required
    public String section;
    @Required
    public String table;
    @Required
    public String name;

    public PollingStation(Municipality municipality, String psDistrict, String section, String table) {
        this(municipality, psDistrict, section, table, StringUtils.EMPTY);
    }

    public PollingStation(Municipality municipality, String psDistrict, String section, String table, String name) {
        this.municipality = municipality;
        this.psDistrict = psDistrict;
        this.section = section;
        this.table = table;
        this.name = name;
    }

    @Override
    public String toString() {
        if (StringUtils.isEmpty(name)) {
            return getCode();
        } else {
            return getCode() + " / " + name;
        }
    }

    public String getCode() {
        return psDistrict + "-" + section + "-" + table;
    }
}
