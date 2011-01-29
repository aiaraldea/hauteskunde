package models.results;

import java.io.Serializable;
import java.util.List;
import models.PartyI;

/**
 *
 * @author inaki
 */
public interface ResultI extends Serializable {

    List<PartyI> getParties();

    Integer getCensus();

    Integer getWhiteVoteAmount();

    Double getWhiteVotePercentageOverVotes();

    Double getWhiteVotePercentageOverCensus();

    Integer getNullVoteAmount();

    Double getNullVotePercentageOverCensus();

    Integer getAbstentions();

    Double getAbstentionsPercentage();

    Double getStatus();

    int getCountedCensus();

    Integer getResult(PartyI party);

    Double getPercentageOverVotes(PartyI party);

    Double getPercentageOverCensus(PartyI party);
}
