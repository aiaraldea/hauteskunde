package models.results;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import models.election.DistrictBallotParty;
import play.db.jpa.Model;

@Entity
public class ResultEntry extends Model {

    @ManyToOne
    public DistrictBallotParty party;
    @ManyToOne
    public PollingStationResult result;
    public Integer votes = null;

    public ResultEntry(DistrictBallotParty party, PollingStationResult result) {
        this.party = party;
        this.result = result;
    }

    public ResultEntry(DistrictBallotParty party, PollingStationResult result, Integer votes) {
        this.party = party;
        this.result = result;
        this.votes = votes;
    }
}
