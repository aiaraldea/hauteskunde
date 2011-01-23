package models.election;

import models.results.PollingStationResult;
import models.results.ResultI;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import models.PollingStation;
import models.ResultProvider;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class PollingStationBallot extends Model implements ResultProvider {

    @ManyToOne
    public DistrictBallot districtBallot;
    @ManyToOne
    public PollingStation pollingStation;
    public int census;
    @OneToOne(mappedBy = "pollingStationBallot")
    public PollingStationResult result;

    @Override
    public String toString() {
        return pollingStation + " - " + districtBallot.election.name;
    }

    public static PollingStationBallot findPollingStation(
            String municipality,
            String pollingStationDistrict,
            String pollingStationSection,
            String pollingStationTable) {
        return PollingStationBallot.find(
                "pollingStation.municipality.code = ? "
                + "and pollingStation.psDistrict = ? "
                + "and pollingStation.section = ? "
                + "and pollingStation.table = ?",
                municipality,
                pollingStationDistrict,
                pollingStationSection,
                pollingStationTable).first();
    }

    public ResultI getResults() {
        if (result == null) {
            result = new PollingStationResult(this);
        }
        return result;
    }
}
