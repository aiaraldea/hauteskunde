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

    public PollingStationBallot(DistrictBallot districtBallot, PollingStation pollingStation, int census) {
        this.districtBallot = districtBallot;
        this.pollingStation = pollingStation;
        this.census = census;
    }

    @Override
    public String toString() {
        return pollingStation + " - " + districtBallot.election.name;
    }

    public static PollingStationBallot findPollingStation(
            String state,
            String municipality,
            String pollingStationDistrict,
            String pollingStationSection,
            String pollingStationTable) {
        return PollingStationBallot.find(
                "pollingStation.municipality.state = ? "
                + "and pollingStation.municipality.code = ? "
                + "and pollingStation.psDistrict = ? "
                + "and pollingStation.section = ? "
                + "and pollingStation.table = ?",
                state,
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

    @Override
    public void _save() {
        districtBallot.clearCache();
        super._save();
    }

    public static PollingStationBallot loadOrCreatePollingStationBallot(
            DistrictBallot districtBallot,
            PollingStation pollingStation,
            int census) {
        PollingStationBallot pollingStationBallot =
                PollingStationBallot.find("districtBallot.id = ? and pollingStation.id = ?", districtBallot.id, pollingStation.id).first();
        if (pollingStationBallot == null) {
            pollingStationBallot = new PollingStationBallot(districtBallot, pollingStation, census);
            pollingStationBallot.save();
        }
        return pollingStationBallot;
    }
}
