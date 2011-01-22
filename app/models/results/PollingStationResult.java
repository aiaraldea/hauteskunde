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
import models.election.PollingStationBallot;
import play.db.jpa.Model;

/**
 *
 * @author inaki
 */
@Entity
public class PollingStationResult extends Model implements ResultI {

    @OneToOne
    public PollingStationBallot pollingStationBallot;
    public Integer nullVoteAmount = null;
    public Integer whiteVoteAmount = null;
    @OneToMany(mappedBy = "result", cascade = CascadeType.ALL)
    public List<ResultEntry> resultEntries;
    @Transient
    private Map<PartyI, Integer> resultEntriesMap;

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
        for (Integer amount : resultEntriesMap.values()) {
            if (amount != null) {
                votes += amount;
            }
        }
        votes += nullVoteAmount + whiteVoteAmount;
        return getCensus() - votes;
    }

    public Double getStatus() {
        float status = 100;
        initResultEntriesMap();
        for (Integer voteAmount : resultEntriesMap.values()) {
            if (voteAmount == null) {
                status = 0;
            }
        }
        if (whiteVoteAmount == null || nullVoteAmount == null) {
            status = 0;
        }
        return Double.valueOf(status);
    }

    public boolean isCounted() {
        return false;
    }

    public List<PartyI> getParties() {
        initResultEntriesMap();
        if (resultEntriesMap == null) {
            return Collections.EMPTY_LIST;
        }
        Map<Integer, List<PartyI>> m = new TreeMap<Integer, List<PartyI>>();

        for (Map.Entry<PartyI, Integer> entry : resultEntriesMap.entrySet()) {
            if (!m.containsKey(entry.getValue())) {
                m.put(entry.getValue(), new ArrayList<PartyI>());
            }
            m.get(entry.getValue()).add(entry.getKey());
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
        return resultEntriesMap.get(party);
    }

    private void initResultEntriesMap() {
        if (resultEntriesMap == null) {
            resultEntriesMap = new HashMap<PartyI, Integer>();
            if (resultEntries != null) {
                for (ResultEntry resultEntry : resultEntries) {
                    resultEntriesMap.put(resultEntry.party, resultEntry.votes);
                }
            }
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
