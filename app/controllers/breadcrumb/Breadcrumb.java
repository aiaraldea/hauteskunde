package controllers.breadcrumb;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author inaki
 */
public class Breadcrumb {

    protected List<BreadcrumbEntry> breadcrumbEntries = new ArrayList<BreadcrumbEntry>();

    public void addBreadcrumbEntry(String text, String url) {
        breadcrumbEntries.add(new BreadcrumbEntry(text, url));
    }

    public List<BreadcrumbEntry> getEntries() {
        return breadcrumbEntries;
    }
}
