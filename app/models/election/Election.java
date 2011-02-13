package models.election;

import models.results.ResultI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import models.PartyI;
import models.ResultProvider;
import models.results.ElectionResult;
import models.results.dhont.DHontResults;
import play.data.validation.Required;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class Election extends Model implements ResultProvider {

    @Required
    @Column(unique = true)
    public String name;
    @Required
    public Date date;
    @OneToMany(mappedBy = "election")
    private List<DistrictBallot> districtBallots;
    @Transient
    Map<DistrictBallot, Map<PartyI, Integer>> seatings;
    @Transient
    Map<PartyI, Integer> seatingsTotal;

    public Election(String name) {
        this(name, null);
    }

    public Election(String name, Date date) {
        this.name = name;
        this.date = date;
    }

    @Override
    public String toString() {
        return name;
    }

    public List<DistrictBallot> getDistrictBallots() {
        return districtBallots;
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

    private void createSeatings() {
        seatings = new HashMap<DistrictBallot, Map<PartyI, Integer>>();
        seatingsTotal = new HashMap<PartyI, Integer>();
        for (DistrictBallot districtBallot : districtBallots) {
            DHontResults r = districtBallot.getDHontResults();
            for (DistrictBallotParty districtBallotParty : districtBallot.getParties()) {
                int assignedSeats = r.getAssignedSeats(districtBallotParty);
                if (!seatings.containsKey(districtBallot)) {
                    seatings.put(districtBallot, new HashMap<PartyI, Integer>());
                }

                seatings.get(districtBallot).put(districtBallotParty.mainParty, assignedSeats);
                if (!seatingsTotal.containsKey(districtBallotParty.mainParty)) {
                    seatingsTotal.put(districtBallotParty.mainParty, assignedSeats);
                } else {
                    seatingsTotal.put(
                            districtBallotParty.mainParty,
                            assignedSeats + seatingsTotal.get(districtBallotParty.mainParty));
                }
            }
        }
    }

    public Integer getSeating(DistrictBallot districtBallot, PartyI party) {
        if (seatings == null || seatingsTotal == null) {
            createSeatings();
        }
        if (seatings.get(districtBallot) != null) {
            return seatings.get(districtBallot).get(party);
        } else {
            return null;
        }
    }

    public Integer getSeating(PartyI party) {
        if (seatings == null || seatingsTotal == null) {
            createSeatings();
        }
        return seatingsTotal.get(party);
    }

    public static Election loadOrCreateElection(String eleccion) {
        Election election = Election.find("name", eleccion).first();
        if (election == null) {
            election = new Election(eleccion);
            election.save();
        }
        return election;
    }
}
