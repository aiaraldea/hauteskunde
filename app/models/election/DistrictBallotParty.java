package models.election;

import models.results.ResultEntry;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import models.Party;
import models.PartyI;
import play.db.jpa.Model;

@Entity
public class DistrictBallotParty extends Model implements PartyI {

    public String name;
    @ManyToOne
    public Party mainParty;
    @ManyToOne
    public DistrictBallot districtBallot;
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    public List<ResultEntry> results;

    public void setName(String name) {
        if (mainParty.name.equals(name)) {
            name = null;
        }
        this.name = name;
    }

    public String getName() {
        if (name == null || name.length() == 0) {
            return mainParty.name;
        } else {
            return name;
        }
    }

    @Override
    public String toString() {
        return getName() + "(" + districtBallot + ")";
    }
}
