package controllers.admin;

/**
 *
 * @author inaki
 */
public class ResultDTO {

    public Long partyId;
    public Integer votes;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
}
