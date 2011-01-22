package models.results.dhont;

import models.PartyI;

/**
 *
 * @author inaki
 */
public class DHontResultEntry {

    private PartyI party;
    private int quotient;
    private int voteAmount;
    private int divisor;

    public DHontResultEntry(PartyI party, int voteAmount, int divisor) {
        this.party = party;
        this.divisor = divisor;
        this.voteAmount = voteAmount;
        this.quotient = voteAmount / divisor;
    }

    public PartyI getParty() {
        return party;
    }

    public int getQuotient() {
        return quotient;
    }

    public int getDivisor() {
        return divisor;
    }

    public int getVoteAmount() {
        return voteAmount;
    }
}
