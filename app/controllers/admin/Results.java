package controllers.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.election.DistrictBallot;
import models.election.DistrictBallotParty;
import models.election.Election;
import models.PartyI;
import models.election.PollingStationBallot;
import models.results.PollingStationResult;
import models.results.ResultEntry;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.Scope.Flash;

/**
 *
 * @author inaki
 */
public class Results extends Controller {

    public static void listElections() {
        List<Election> elections = Election.findAll();
        render(elections);
    }

    public static void listDistricts(Long electionId) {
        List<DistrictBallot> districts = DistrictBallot.find("election.id", electionId).fetch();
        render(districts);
    }

    public static void listPollingStations(Long districId) {
        List<PollingStationBallot> pollingStations = PollingStationBallot.find("districtBallot.id", districId).fetch();
        render(pollingStations);
    }

    public static void resultsSheet(Long pollingStationId) {
        PollingStationBallot pollingStation = PollingStationBallot.findById(pollingStationId);
        Map<Long, ResultEntry> resultMap = createResultMap(pollingStation);
        List<ResultEntry> results = new ArrayList<ResultEntry>(resultMap.values());

        Flash.current().put("pollingStationId", pollingStationId);
        render(results, pollingStation);
    }

    private static Map<Long, ResultEntry> createResultMap(PollingStationBallot pollingStation) {
        PollingStationResult result = PollingStationResult.find("pollingStationBallot", pollingStation).first();
        List<DistrictBallotParty> parties = DistrictBallotParty.find("districtBallot", pollingStation.districtBallot).fetch();

        if (result == null) {
            result = new PollingStationResult(pollingStation);
            result.save();
        }

        Map<Long, ResultEntry> resultMap = new HashMap<Long, ResultEntry>();

        for (PartyI party : parties) {
            resultMap.put(
                    ((DistrictBallotParty) party).id,
                    new ResultEntry(
                    (DistrictBallotParty) party,
                    (PollingStationResult) pollingStation.getResults(),
                    result.getResult(party)));
        }
        return resultMap;
    }

    public static void save(Integer nullVoteAmount, Integer whiteVoteAmount, List<ResultDTO> result) {
        Long pollingStationId = Long.parseLong(Flash.current().get("pollingStationId"));
        PollingStationBallot pollingStation = PollingStationBallot.findById(pollingStationId);
        Map<Long, ResultEntry> resultMap = createResultMap(pollingStation);
        pollingStation.result.nullVoteAmount = nullVoteAmount;
        pollingStation.result.whiteVoteAmount = whiteVoteAmount;
        pollingStation.result.save();
        ResultEntry resultObject;
        if (result != null) {
            for (ResultDTO resultItem : result) {
                resultObject = resultMap.get(resultItem.partyId);
                if (resultObject != null) {
                    resultObject.votes = resultItem.votes;
                }
                saveOrCreate(resultObject);
            }
        }

        pollingStation.districtBallot.clearCache();
        resultsSheet(pollingStationId);
    }

    private static void saveOrCreate(ResultEntry entry) {
        if (entry.isPersistent()) {
            entry.save();
        } else {
            entry.create();
        }
    }
}
