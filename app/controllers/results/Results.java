package controllers.results;

import controllers.breadcrumb.Breadcrumb;
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
        Breadcrumb breadcrumb = Breadcrumb.createBreadcrumb(election);
        render(election, breadcrumb);
    }

    public static void listPollingStations(Long districtId) {
        DistrictBallot district = DistrictBallot.findById(districtId);
        Breadcrumb breadcrumb = Breadcrumb.createBreadcrumb(district);
        render(district, breadcrumb);
    }

    public static void resultsSheet(Long pollingStationId) {
        PollingStationBallot pollingStation = PollingStationBallot.findById(pollingStationId);
        ResultI result = pollingStation.getResults();
        Breadcrumb breadcrumb = Breadcrumb.createBreadcrumb(pollingStation);
        render(result, breadcrumb);
    }
}
