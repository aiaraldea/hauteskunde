package controllers.breadcrumb;

import java.util.HashMap;
import java.util.Map;
import models.election.DistrictBallot;
import models.election.Election;
import models.election.PollingStationBallot;
import play.mvc.Router;

/**
 *
 * @author inaki
 */
public class ResultFormBreadcrumb extends Breadcrumb {

    public static ResultFormBreadcrumb createBreadcrumb(Election election) {
        ResultFormBreadcrumb eb = new ResultFormBreadcrumb();
        eb = ResultFormBreadcrumb.addRoot(eb);
        eb = ResultFormBreadcrumb.addElection(eb, election);
        return eb;
    }

    public static ResultFormBreadcrumb createBreadcrumb(DistrictBallot district) {
        ResultFormBreadcrumb eb = new ResultFormBreadcrumb();
        eb = ResultFormBreadcrumb.addRoot(eb);
        eb = ResultFormBreadcrumb.addElection(eb, district.election);
        eb = ResultFormBreadcrumb.addDistrict(eb, district);
        return eb;
    }

    public static ResultFormBreadcrumb createBreadcrumb(PollingStationBallot pollingStation) {
        ResultFormBreadcrumb eb = new ResultFormBreadcrumb();
        eb = ResultFormBreadcrumb.addRoot(eb);
        eb = ResultFormBreadcrumb.addElection(eb, pollingStation.districtBallot.election);
        eb = ResultFormBreadcrumb.addDistrict(eb, pollingStation.districtBallot);
        eb = ResultFormBreadcrumb.addPollingStation(eb, pollingStation);
        return eb;
    }

    private static ResultFormBreadcrumb addRoot(ResultFormBreadcrumb breadcrumb) {
        Map map = new HashMap();
        String url = Router.reverse("admin.Results.listElections", map).url;
        breadcrumb.addBreadcrumbEntry("Elections", url);
        return breadcrumb;
    }

    private static ResultFormBreadcrumb addElection(ResultFormBreadcrumb breadcrumb, Election election) {
        Map map = new HashMap();
        map.put("electionId", election.id);
        String url = Router.reverse("admin.Results.listDistricts", map).url;
        breadcrumb.addBreadcrumbEntry(election.name, url);
        return breadcrumb;
    }

    private static ResultFormBreadcrumb addDistrict(ResultFormBreadcrumb breadcrumb, DistrictBallot districtBallot) {
        Map map = new HashMap();
        map.put("districtId", districtBallot.id);
        String url = Router.reverse("admin.Results.listPollingStations", map).url;
        breadcrumb.addBreadcrumbEntry(districtBallot.district.name, url);
        return breadcrumb;
    }

    private static ResultFormBreadcrumb addPollingStation(ResultFormBreadcrumb breadcrumb, PollingStationBallot pollingStation) {
        Map map = new HashMap();
        map.put("pollingStationId", pollingStation.id);
        String url = Router.reverse("admin.Results.resultsSheet", map).url;
        breadcrumb.addBreadcrumbEntry(pollingStation.toString(), url);
        return breadcrumb;
    }
}
