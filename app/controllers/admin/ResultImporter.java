package controllers.admin;

import controllers.breadcrumb.Breadcrumb;
import init.Fixtures;
import init.InitialDataImporter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import play.mvc.Controller;
import play.mvc.Router;

/**
 *
 * @author inaki
 */
public class ResultImporter extends Controller {

    public static void index() {
        Breadcrumb breadcrumb = new Breadcrumb();
        Map map = new HashMap();
        String url = Router.reverse("CRUD.index", map).url;
        breadcrumb.addBreadcrumbEntry("Admin", url);
        url = Router.reverse("admin.ResultImporter.index", map).url;
        breadcrumb.addBreadcrumbEntry("Result Importer", url);
        render(breadcrumb);
    }

    public static void uploadYaml(File file) throws IOException {
        if (file == null || !file.canRead()) {
            flash.error("Please, provide a valid file");
            index();
        }
        FileInputStream fis = new FileInputStream(file);
        Fixtures.loadModels(fis);
        flash.success("Yaml file imported");
        index();
    }

    public static void uploadFile(File file) throws IOException {
        FileReader r;
        StringBuilder b = new StringBuilder();
        if (file == null || !file.canRead()) {
            flash.error("Please, provide a valid file");
            index();
        }
        try {
            r = new FileReader(file);
            BufferedReader br = new BufferedReader(r);
            String s;
            s = br.readLine();
            while (StringUtils.isNotEmpty(s)) {
                b.append("\n");
                b.append(s);
                s = br.readLine();
            }
            br.close();
            r.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResultImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        InitialDataImporter.readData(b.toString());
        flash.success("File imported");
        index();
    }
}
