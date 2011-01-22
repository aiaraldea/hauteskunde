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
    @OneToOne(mappedBy="pollingStationBallot")
    public PollingStationResult result;

    @Override
    public String toString() {
        return pollingStation + " - " + districtBallot.election.name;
    }
    
    public ResultI getResults() {
        if (result == null) {
            result = new PollingStationResult();
            result.pollingStationBallot = this;
        }
        return result;
    }
}
