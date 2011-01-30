package controllers.admin;

import controllers.breadcrumb.ResultFormBreadcrumb;
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
import play.mvc.Controller;

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
        Election election = Election.findById(electionId);
        List<DistrictBallot> districts = election.getDistrictBallots();
        ResultFormBreadcrumb breadcrumb = ResultFormBreadcrumb.createBreadcrumb(election);
        render(districts, breadcrumb);
    }

    public static void listPollingStations(Long districtId) {
        DistrictBallot district = DistrictBallot.findById(districtId);
        List<PollingStationBallot> pollingStations = district.pollingStations;
        ResultFormBreadcrumb breadcrumb = ResultFormBreadcrumb.createBreadcrumb(district);
        render(pollingStations, breadcrumb);
    }

    public static void resultsSheet(Long pollingStationId) {
        PollingStationBallot pollingStation = PollingStationBallot.findById(pollingStationId);
        Map<Long, ResultEntry> resultMap = createResultMap(pollingStation);
        List<ResultEntry> results = new ArrayList<ResultEntry>(resultMap.values());
        ResultFormBreadcrumb breadcrumb = ResultFormBreadcrumb.createBreadcrumb(pollingStation);

        render(results, pollingStation, breadcrumb);
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

    public static void save(Long pollingStationId, Integer nullVoteAmount, Integer whiteVoteAmount, List<ResultDTO> result) {
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
        flash.success("Data saved");

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
