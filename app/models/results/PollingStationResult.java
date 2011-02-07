package models.results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import models.PartyI;
import models.election.DistrictBallotParty;
import models.election.PollingStationBallot;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class PollingStationResult extends Model implements ResultI {

    @OneToOne
    private PollingStationBallot pollingStationBallot;
    public Integer nullVoteAmount = null;
    public Integer whiteVoteAmount = null;
    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    private List<ResultEntry> resultEntries;
    @Transient
    private Map<PartyI, ResultEntry> resultEntriesMap;

    private PollingStationResult(Builder b) {
        this.pollingStationBallot = b.pollingStationBallot;
        this.nullVoteAmount = b.nullVoteAmount;
        this.whiteVoteAmount = b.whiteVoteAmount;
        resultEntries = new ArrayList<ResultEntry>();
        for (Builder.ResultPair resultPair : b.resultEntries) {
            this.resultEntries.add(new ResultEntry(resultPair.party, this, resultPair.votes));
        }
        initResultEntriesMap();
    }

    public PollingStationResult(PollingStationBallot pollingStationBallot) {
        this.pollingStationBallot = pollingStationBallot;
    }

    public static class Builder {

        private PollingStationBallot pollingStationBallot;
        private Integer nullVoteAmount = null;
        private Integer whiteVoteAmount = null;
        private List<ResultPair> resultEntries = new ArrayList<ResultPair>();

        public Builder pollingStationBallot(PollingStationBallot pollingStationBallot) {
            this.pollingStationBallot = pollingStationBallot;
            return this;
        }

        public Builder nullVoteAmount(Integer nullVoteAmount) {
            this.nullVoteAmount = nullVoteAmount;
            return this;
        }

        public Builder whiteVoteAmount(Integer whiteVoteAmount) {
            this.whiteVoteAmount = whiteVoteAmount;
            return this;
        }

        public Builder addResult(DistrictBallotParty party, int voteAmount) {
            resultEntries.add(new ResultPair(party, voteAmount));
            return this;
        }

        public PollingStationResult build() {
            return new PollingStationResult(this);
        }

        private static class ResultPair {

            DistrictBallotParty party;
            Integer votes;

            public ResultPair(DistrictBallotParty party, Integer votes) {
                this.party = party;
                this.votes = votes;
            }
        }
    }

    public Integer getCensus() {
        return pollingStationBallot.census;
    }

    public Integer getWhiteVoteAmount() {
        return whiteVoteAmount;
    }

    public Integer getNullVoteAmount() {
        return nullVoteAmount;
    }

    public Integer getAbstentions() {
        initResultEntriesMap();
        if (nullVoteAmount == null || whiteVoteAmount == null) {
            return null;
        }
        int votes = 0;
        for (ResultEntry entry : resultEntriesMap.values()) {
            if (entry.votes != null) {
                votes += entry.votes;
            }
        }
        votes += nullVoteAmount + whiteVoteAmount;
        return getCensus() - votes;
    }

    public Double getStatus() {
        float status = 100;
        initResultEntriesMap();
        for (ResultEntry entry : resultEntriesMap.values()) {
            if (entry.votes == null) {
                status = 0;
            }
        }
        if (whiteVoteAmount == null || nullVoteAmount == null) {
            status = 0;
        }
        return Double.valueOf(status);
    }

    public List<PartyI> getParties() {
        initResultEntriesMap();
        if (resultEntriesMap == null) {
            return Collections.EMPTY_LIST;
        }
        Map<Integer, List<PartyI>> m = new TreeMap<Integer, List<PartyI>>();

        for (Map.Entry<PartyI, ResultEntry> entry : resultEntriesMap.entrySet()) {
            if (entry.getValue() != null) {
                if (!m.containsKey(entry.getValue().votes)) {
                    m.put(entry.getValue().votes, new ArrayList<PartyI>());
                }
                m.get(entry.getValue().votes).add(entry.getKey());
            }
        }

        List l = new ArrayList();
        for (List<PartyI> list : m.values()) {
            for (PartyI partyI : list) {
                l.add(partyI);
            }
        }
        Collections.reverse(l);
        return l;
    }

    public Integer getResult(PartyI party) {
        initResultEntriesMap();
        if(resultEntriesMap.get(party) == null) {
            return null;
        }
        return resultEntriesMap.get(party).votes;
    }

    private void initResultEntriesMap() {
        if (resultEntriesMap == null) {
            resultEntriesMap = new HashMap<PartyI, ResultEntry>();
            if (resultEntries != null) {
                for (ResultEntry resultEntry : resultEntries) {
                    resultEntriesMap.put(resultEntry.party, resultEntry);
                }
            }
        }
    }

    public void addResult(DistrictBallotParty party, int voteAmount) {
        initResultEntriesMap();
        if (resultEntriesMap.containsKey(party)) {
            resultEntriesMap.get(party).votes = voteAmount;
        } else {
            ResultEntry re = new ResultEntry(party, this, voteAmount);
            resultEntries.add(re);
            resultEntriesMap.put(party, re);
        }
    }

    public int getCountedCensus() {
        if (getStatus().equals(Double.valueOf(100))) {
            return getCensus();
        } else {
            return 0;
        }
    }

    public Double getPercentageOverVotes(PartyI party) {
        return getPercentage(getResult(party), getCountedCensus(), getNonVoters());
    }

    public Double getPercentageOverCensus(PartyI party) {
        return getPercentage(getResult(party), getCountedCensus());
    }

    public Double getWhiteVotePercentageOverVotes() {
        return getPercentage(getWhiteVoteAmount(), getCountedCensus(), getNonVoters());
    }

    public Double getWhiteVotePercentageOverCensus() {
        return getPercentage(getWhiteVoteAmount(), getCountedCensus());
    }

    public Double getNullVotePercentageOverCensus() {
        return getPercentage(getNullVoteAmount(), getCountedCensus());
    }

    public Double getAbstentionsPercentage() {
        return getPercentage(getAbstentions(), getCountedCensus());
    }

    private static Double getPercentage(Integer votes, Integer total) {
        return getPercentage(votes, total, 0);
    }

    private static Double getPercentage(Integer votes, Integer total, Integer substractToTotal) {
        if (votes == null || total == null || substractToTotal == null) {
            return null;
        } else {
            return Math.rint((double) votes * 10000 / (double) (total - substractToTotal)) / 100d;
        }
    }

    private Integer getNonVoters() {
        if (getNullVoteAmount() == null || getAbstentions() == null) {
            return null;
        } else {
            return getAbstentions() + getNullVoteAmount();
        }
    }
}
