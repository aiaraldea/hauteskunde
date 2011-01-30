package models.election;

import models.results.ResultI;
import models.results.DistrictResult;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import models.District;
import models.PartyI;
import models.ResultProvider;
import models.results.dhont.DHontResults;
import play.cache.Cache;
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
    @Transient
    private Boolean allPartiesLegal;

    @Override
    public String toString() {
        return election + " - " + district;
    }

    public ResultI getResults() {
        Object cached = Cache.get(getResultCacheKey());
        if (cached == null) {
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
            Cache.set(getResultCacheKey(), result);
            return result;
        } else {
            return (ResultI) cached;
        }
    }

    private String getResultCacheKey() {
        return "results_" + toString();
    }

    public DHontResults getDHontResults() {
        Object cached = Cache.get(getDHontResultCacheKey());
        if (cached == null) {
            DHontResults r = new DHontResults(getResults(), seats);
            Cache.set(getDHontResultCacheKey(), r);
            return r;
        } else {
            return (DHontResults) cached;
        }
    }

    public DHontResults getDHontResultsOnlyLegal() {
        Object cached = Cache.get(getDHontResultCacheKey() + "_legal");
        if (cached == null) {
            DHontResults r = new DHontResults(getResults(), seats, true);
            Cache.set(getDHontResultCacheKey() + "_legal", r);
            return r;
        } else {
            return (DHontResults) cached;
        }
    }

    private String getDHontResultCacheKey() {
        return "dHontResults_" + toString();
    }

    public void clearCache() {
        Cache.delete(getResultCacheKey());
        Cache.delete(getDHontResultCacheKey());
        Cache.delete(getDHontResultCacheKey() + "_legal");
    }

    public boolean isAllPartiesLegal() {
        if (allPartiesLegal == null) {
            allPartiesLegal = true;
            for (DistrictBallotParty districtBallotParty : parties) {
                if (!districtBallotParty.isLegal()) {
                    allPartiesLegal = false;
                    break;
                }
            }
        }
        return allPartiesLegal;
    }

    @Override
    public void _save() {
        clearCache();
        super._save();
    }
}
