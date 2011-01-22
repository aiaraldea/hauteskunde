package models.results;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.PartyI;

/**
 *
 * @author inaki
 */
public class GenericResult implements ResultI {

    public Integer census = 0;
    public Integer countedCensus = 0;
    public Integer nullVoteAmount = null;
    public Integer whiteVoteAmount = null;
    public Map<PartyI, Integer> resultEntries;
    public double status = 0;

    public Integer getCensus() {
        return census;
    }

    public Integer getWhiteVoteAmount() {
        return whiteVoteAmount;
    }

    public Integer getNullVoteAmount() {
        return nullVoteAmount;
    }

    public Integer getAbstentions() {
        if (nullVoteAmount == null || whiteVoteAmount == null) {
            return null;
        }
        int votes = 0;
        for (Integer amount : resultEntries.values()) {
            votes += amount;
        }
        votes += nullVoteAmount + whiteVoteAmount;
        return getCountedCensus() - votes;
    }

    public Double getStatus() {
        return Math.rint((double) countedCensus * 10000 / (double) census) / 100d;
    }

    public void addVote(PartyI party, int voteAmount) {
        if (resultEntries == null) {
            resultEntries = new HashMap<PartyI, Integer>();
        }
        if (!resultEntries.containsKey(party)) {
            resultEntries.put(party, voteAmount);
        } else {
            resultEntries.put(party, voteAmount + resultEntries.get(party));
        }
    }

    public void addWhiteVote(Integer amount) {
        if (amount != null) {
            if (whiteVoteAmount == null) {
                whiteVoteAmount = amount;
            } else {
                whiteVoteAmount += amount;
            }
        }
    }

    public void addNullVote(Integer amount) {
        if (amount != null) {
            if (nullVoteAmount == null) {
                nullVoteAmount = amount;
            } else {
                nullVoteAmount += amount;
            }
        }
    }

    public void addCensus(Integer amount) {
        if (amount != null) {
            if (census == null) {
                census = amount;
            } else {
                census += amount;
            }
        }
    }

    public List<PartyI> getParties() {
        if (resultEntries == null) {
            return Collections.EMPTY_LIST;
        }
        Map<Integer, List<PartyI>> m = new TreeMap<Integer, List<PartyI>>();

        for (Map.Entry<PartyI, Integer> entry : resultEntries.entrySet()) {
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
        return resultEntries.get(party);
    }

    public void addCountedCensus(Integer amount) {
        if (amount != null) {
            if (countedCensus == null) {
                countedCensus = amount;
            } else {
                countedCensus += amount;
            }
        }
    }

    public int getCountedCensus() {
        return countedCensus;
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
