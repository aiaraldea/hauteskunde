package models.results.dhont;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import models.PartyI;
import models.results.ResultI;

/**
 *
 * @author inaki
 */
public class DHontResults implements Serializable {

    private Map<Integer, Map<Integer, List<DHontResultEntry>>> resultsMap =
            new TreeMap<Integer, Map<Integer, List<DHontResultEntry>>>(Collections.reverseOrder());
    private List<DHontResult> results = new ArrayList<DHontResult>();
    private Map<PartyI, Map<Integer, DHontResult>> finalResultsMap =
            new HashMap<PartyI, Map<Integer, DHontResult>>();
    private Map<PartyI, Integer> assignedSeats = new HashMap<PartyI, Integer>();
    private Map<PartyI, Integer> seatsInDoubt = new HashMap<PartyI, Integer>();
    private int seatAmount = 1;

    public DHontResults(ResultI result, int seatAmount) {
        this.seatAmount = seatAmount;
        for (PartyI partyI : result.getParties()) {
            assignedSeats.put(partyI, 0);
            seatsInDoubt.put(partyI, 0);
            for (int i = 1; i <= seatAmount; i++) {
                put(partyI, result.getResult(partyI), i);
            }
        }
        createResultList();
    }

    private void put(PartyI party, int votes, int divisor) {
        int quotient = votes / divisor;
        if (!resultsMap.containsKey(quotient)) {
            resultsMap.put(quotient, new TreeMap<Integer, List<DHontResultEntry>>(Collections.reverseOrder()));
        }
        if (!resultsMap.get(quotient).containsKey(votes)) {
            resultsMap.get(quotient).put(votes, new ArrayList<DHontResultEntry>());
        }
        resultsMap.get(quotient).get(votes).add(new DHontResultEntry(party, votes, divisor));
    }

    private void createResultList() {
        int assignedSeatAmount = 0;
        int order = 1;
        boolean tie = false;
        for (Map<Integer, List<DHontResultEntry>> mapForVotes : resultsMap.values()) {
            for (List<DHontResultEntry> votes : mapForVotes.values()) {
                if (votes.size() + assignedSeatAmount > seatAmount) {
                    tie = true;
                }
                for (DHontResultEntry dHontResultEntry : votes) {
                    results.add(new DHontResult(dHontResultEntry, order, tie));
                    if (tie == true) {
                        seatsInDoubt.put(dHontResultEntry.getParty(), seatsInDoubt.get(dHontResultEntry.getParty()) + 1);
                    } else {
                        assignedSeats.put(dHontResultEntry.getParty(), assignedSeats.get(dHontResultEntry.getParty()) + 1);
                    }
                    assignedSeatAmount++;
                }
                order += votes.size();
                if (assignedSeatAmount >= seatAmount) {
                    break;
                }
            }
            if (assignedSeatAmount >= seatAmount) {
                break;
            }
        }
        for (DHontResult dHontResult : results) {
            if (!finalResultsMap.containsKey(dHontResult.getParty())) {
                finalResultsMap.put(dHontResult.getParty(), new HashMap<Integer, DHontResult>());
            }
            finalResultsMap.get(dHontResult.getParty()).put(dHontResult.getDivisor(), dHontResult);
        }
    }

    public List<DHontResult> getResults() {
        return results;
    }

    public DHontResult getResult(PartyI party, int divisor) {
        if (!finalResultsMap.containsKey(party)) {
            return null;
        } else {
            return finalResultsMap.get(party).get(divisor);
        }
    }

    public int getAssignedSeats(PartyI party) {
        if (assignedSeats.containsKey(party)) {
            return assignedSeats.get(party);
        } else {
            return 0;
        }
    }

    public int getSeatsInDoubt(PartyI party) {
        if (seatsInDoubt.containsKey(party)) {
            return seatsInDoubt.get(party);
        } else {
            return 0;
        }
    }
}
