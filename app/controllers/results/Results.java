package controllers.results;

import controllers.breadcrumb.ResultsBreadcrumb;
import java.util.List;
import models.election.DistrictBallot;
import models.election.Election;
import models.election.PollingStationBallot;
import models.results.ResultI;
import play.mvc.Controller;

/**
 *
 * @author inaki
 */
public class Results extends Controller {

    public static void index() {
        List<Election> elections = Election.findAll();
        render(elections);
    }

    public static void listDistricts(Long electionId) {
        Election election = Election.findById(electionId);
        ResultsBreadcrumb breadcrumb = ResultsBreadcrumb.createBreadcrumb(election);
        render(election, breadcrumb);
    }

    public static void listPollingStations(Long districtId) {
        DistrictBallot district = DistrictBallot.findById(districtId);
        ResultsBreadcrumb breadcrumb = ResultsBreadcrumb.createBreadcrumb(district);
        render(district, breadcrumb);
    }

    public static void resultsSheet(Long pollingStationId) {
        PollingStationBallot pollingStation = PollingStationBallot.findById(pollingStationId);
        ResultI result = pollingStation.getResults();
        ResultsBreadcrumb breadcrumb = ResultsBreadcrumb.createBreadcrumb(pollingStation);
        render(result, breadcrumb);
    }
}
