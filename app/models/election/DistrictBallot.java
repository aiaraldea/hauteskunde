package models.election;

import models.results.ResultI;
import models.results.DistrictResult;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import models.District;
import models.PartyI;
import models.ResultProvider;
import models.results.dhont.DHontResults;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class DistrictBallot extends Model implements ResultProvider {

    @ManyToOne
    public Election election;
    @ManyToOne
    public District district;
    @OneToMany(mappedBy = "districtBallot")
    public List<PollingStationBallot> pollingStations;
    @OneToMany(mappedBy = "districtBallot")
    public List<DistrictBallotParty> parties;
    public int seats = 1;
    public double thresold = 0;

    @Override
    public String toString() {
        return election + " - " + district;
    }

    public ResultI getResults() {
        DistrictResult result = new DistrictResult();
        for (PollingStationBallot pollingStationBallot : pollingStations) {
            for (PartyI party : pollingStationBallot.getResults().getParties()) {
                result.addVote(party, pollingStationBallot.getResults().getResult(party));
            }
            result.addWhiteVote(pollingStationBallot.getResults().getWhiteVoteAmount());
            result.addNullVote(pollingStationBallot.getResults().getNullVoteAmount());
            result.addCensus(pollingStationBallot.census);
            if (pollingStationBallot.getResults().getStatus().equals(Double.valueOf(100))) {
                result.addCountedCensus(pollingStationBallot.census);
            }
        }
        return result;
    }

    public DHontResults getDHontResults() {
        return new DHontResults(getResults(), seats);
    }
}
