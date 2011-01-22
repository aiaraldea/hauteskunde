package models.results;

import java.util.HashMap;
import java.util.Map;
import models.election.DistrictBallotParty;
import models.Party;
import models.PartyI;

/**
 *
 * @author inaki
 */
public class DistrictResult extends GenericResult implements ResultI {

    public Map<Party, Integer> resultEntriesbyParty;

    @Override
    public void addVote(PartyI party, int voteAmount) {
        initResultEntriesMap();
        if (!resultEntries.containsKey(party)) {
            resultEntries.put((DistrictBallotParty) party, voteAmount);
            resultEntriesbyParty.put(((DistrictBallotParty) party).mainParty, voteAmount);
        } else {
            resultEntries.put((DistrictBallotParty) party, voteAmount + resultEntries.get((DistrictBallotParty) party));
            resultEntriesbyParty.put(((DistrictBallotParty) party).mainParty, voteAmount + resultEntriesbyParty.get(((DistrictBallotParty) party).mainParty));
        }
    }

    @Override
    public Integer getResult(PartyI party) {
        initResultEntriesMap();
        Integer result = null;
        if (party instanceof DistrictBallotParty) {
            result = resultEntries.get((DistrictBallotParty) party);
        } else {
            result = resultEntriesbyParty.get((Party) party);
        }
        return result;
    }

    private void initResultEntriesMap() {
        if (resultEntries == null || resultEntriesbyParty == null) {
            resultEntries = new HashMap<PartyI, Integer>();
            resultEntriesbyParty = new HashMap<Party, Integer>();
        }
    }
}
