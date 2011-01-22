package models.results;

import java.util.HashMap;
import java.util.Map;
import models.PartyI;

/**
 *
 * @author inaki
 */
public class RepresentativeResult {

    public ResultI result;
    public Map<PartyI, Integer> filteredResultEntries;
    private double thresold = 0;

    public RepresentativeResult(ResultI result, int seatAmount, double thresold) {
    }




    Integer getResult(PartyI party) {
        return null;
    }
    
    private void filterResultEntries() {
        filteredResultEntries = new HashMap<PartyI, Integer>();
        for (PartyI partyI : result.getParties()) {
            if (result.getPercentageOverVotes(partyI) > thresold) {
                filteredResultEntries.put(partyI, result.getResult(partyI));
            }

        }
        
    }


}
