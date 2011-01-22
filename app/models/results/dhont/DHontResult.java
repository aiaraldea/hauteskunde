package models.results.dhont;

import models.PartyI;

/**
 *
 * @author inaki
 */
public class DHontResult {

    private int order;
    private DHontResultEntry entry;
    private boolean tie;

    public DHontResult(DHontResultEntry entry, int order, boolean tie) {
        this.order = order;
        this.entry = entry;
        this.tie = tie;
    }

    public int getOrder() {
        return order;
    }

    public PartyI getParty() {
        return entry.getParty();
    }

    public int getQuotient() {
        return entry.getQuotient();
    }

    public int getDivisor() {
        return entry.getDivisor();
    }

    public int getVoteAmount() {
        return entry.getVoteAmount();
    }

    public boolean isTie() {
        return tie;
    }
}
