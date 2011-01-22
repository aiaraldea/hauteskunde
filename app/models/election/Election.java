package models.election;

import models.results.ResultI;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import models.PartyI;
import models.ResultProvider;
import models.results.ElectionResult;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class Election extends Model implements ResultProvider {

    @Required
    public String name;
    @Required
    public Date date;
    @OneToMany(mappedBy = "election")
    private List<DistrictBallot> districtBallots;

    @Override
    public String toString() {
        return name;
    }

    public ResultI getResults() {
        ElectionResult result = new ElectionResult();
        for (DistrictBallot districtBallot : districtBallots) {
            for (PartyI party : districtBallot.getResults().getParties()) {
                result.addVote(((DistrictBallotParty) party).mainParty, districtBallot.getResults().getResult(party));
            }
            result.addWhiteVote(districtBallot.getResults().getWhiteVoteAmount());
            result.addNullVote(districtBallot.getResults().getNullVoteAmount());
            result.addCensus(districtBallot.getResults().getCensus());
            result.addCountedCensus(districtBallot.getResults().getCountedCensus());
        }

        return result;
    }
}
