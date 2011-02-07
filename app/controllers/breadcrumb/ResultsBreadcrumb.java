package controllers.breadcrumb;

import java.util.HashMap;
import java.util.Map;
import models.election.DistrictBallot;
import models.election.Election;
import models.election.PollingStationBallot;
import play.i18n.Messages;
import play.mvc.Router;

/**
 *
 * @author inaki
 */
public class ResultsBreadcrumb extends Breadcrumb {

    public static ResultsBreadcrumb createBreadcrumb(Election election) {
        ResultsBreadcrumb eb = new ResultsBreadcrumb();
        eb = ResultsBreadcrumb.addRoot(eb);
        eb = ResultsBreadcrumb.addElection(eb, election);
        return eb;
    }

    public static ResultsBreadcrumb createBreadcrumb(DistrictBallot district) {
        ResultsBreadcrumb eb = new ResultsBreadcrumb();
        eb = ResultsBreadcrumb.addRoot(eb);
        eb = ResultsBreadcrumb.addElection(eb, district.election);
        eb = ResultsBreadcrumb.addDistrict(eb, district);
        return eb;
    }

    public static ResultsBreadcrumb createBreadcrumb(PollingStationBallot pollingStation) {
        ResultsBreadcrumb eb = new ResultsBreadcrumb();
        eb = ResultsBreadcrumb.addRoot(eb);
        eb = ResultsBreadcrumb.addElection(eb, pollingStation.districtBallot.election);
        eb = ResultsBreadcrumb.addDistrict(eb, pollingStation.districtBallot);
        eb = ResultsBreadcrumb.addPollingStation(eb, pollingStation);
        return eb;
    }

    private static ResultsBreadcrumb addRoot(ResultsBreadcrumb breadcrumb) {
        Map map = new HashMap();
        String url = Router.reverse("results.Results.index", map).url;
        breadcrumb.addBreadcrumbEntry(Messages.get("Elections"), url);
        return breadcrumb;
    }

    private static ResultsBreadcrumb addElection(ResultsBreadcrumb breadcrumb, Election election) {
        Map map = new HashMap();
        map.put("electionId", election.id);
        String url = Router.reverse("results.Results.listDistricts", map).url;
        breadcrumb.addBreadcrumbEntry(election.name, url);
        return breadcrumb;
    }

    private static ResultsBreadcrumb addDistrict(ResultsBreadcrumb breadcrumb, DistrictBallot districtBallot) {
        Map map = new HashMap();
        map.put("districtId", districtBallot.id);
        String url = Router.reverse("results.Results.listPollingStations", map).url;
        breadcrumb.addBreadcrumbEntry(districtBallot.district.name, url);
        return breadcrumb;
    }

    private static ResultsBreadcrumb addPollingStation(ResultsBreadcrumb breadcrumb, PollingStationBallot pollingStation) {
        Map map = new HashMap();
        map.put("pollingStationId", pollingStation.id);
        String url = Router.reverse("results.Results.resultsSheet", map).url;
        breadcrumb.addBreadcrumbEntry(pollingStation.toString(), url);
        return breadcrumb;
    }
}
