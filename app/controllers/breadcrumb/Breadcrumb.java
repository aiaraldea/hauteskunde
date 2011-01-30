package controllers.breadcrumb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class Breadcrumb {

    private List<BreadcrumbEntry> breadcrumbEntries = new ArrayList<BreadcrumbEntry>();

    public void addBreadcrumbEntry(String text, String url) {
        breadcrumbEntries.add(new BreadcrumbEntry(text, url));
    }

    public static Breadcrumb createBreadcrumb(Election election) {
        Breadcrumb eb = new Breadcrumb();
        eb = Breadcrumb.addRoot(eb);
        eb = Breadcrumb.addElection(eb, election);
        return eb;
    }

    public static Breadcrumb createBreadcrumb(DistrictBallot district) {
        Breadcrumb eb = new Breadcrumb();
        eb = Breadcrumb.addRoot(eb);
        eb = Breadcrumb.addElection(eb, district.election);
        eb = Breadcrumb.addDistrict(eb, district);
        return eb;
    }

    public static Breadcrumb createBreadcrumb(PollingStationBallot pollingStation) {
        Breadcrumb eb = new Breadcrumb();
        eb = Breadcrumb.addRoot(eb);
        eb = Breadcrumb.addElection(eb, pollingStation.districtBallot.election);
        eb = Breadcrumb.addDistrict(eb, pollingStation.districtBallot);
        eb = Breadcrumb.addPollingStation(eb, pollingStation);
        return eb;
    }

    private static Breadcrumb addRoot(Breadcrumb breadcrumb) {
        Map map = new HashMap();
        String url = Router.reverse("results.Results.index", map).url;
        breadcrumb.addBreadcrumbEntry(Messages.get("Elections"), url);
        return breadcrumb;
    }

    private static Breadcrumb addElection(Breadcrumb breadcrumb, Election election) {
        Map map = new HashMap();
        map.put("electionId", election.id);
        String url = Router.reverse("results.Results.listDistricts", map).url;
        breadcrumb.addBreadcrumbEntry(election.name, url);
        return breadcrumb;
    }

    private static Breadcrumb addDistrict(Breadcrumb breadcrumb, DistrictBallot districtBallot) {
        Map map = new HashMap();
        map.put("districtId", districtBallot.id);
        String url = Router.reverse("results.Results.listPollingStations", map).url;
        breadcrumb.addBreadcrumbEntry(districtBallot.district.name, url);
        return breadcrumb;
    }

    private static Breadcrumb addPollingStation(Breadcrumb breadcrumb, PollingStationBallot pollingStation) {
        Map map = new HashMap();
        map.put("pollingStationId", pollingStation.id);
        String url = Router.reverse("results.Results.resultsSheet", map).url;
        breadcrumb.addBreadcrumbEntry(pollingStation.toString(), url);
        return breadcrumb;
    }

    public List<BreadcrumbEntry> getEntries() {
        return breadcrumbEntries;
    }
}
