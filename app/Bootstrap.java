
import init.InitialDataImporter;
import models.election.Election;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {

    @Override
    public void doJob() {
        // Check if the database is empty
        if (Election.count() == 0) {
            Fixtures.load("initial-data.yml");
            Fixtures.load("initial-data-parties.yml");
//            Fixtures.load("initial-data-ps-araba.yml");
            Fixtures.load("initial-data-ps-amurrio.yml");
//            Fixtures.load("initial-data-ps-laudio.yml");
//            Fixtures.load("initial-data-ps-bizkaia.yml");
            Fixtures.load("initial-data2011.yml");
            Fixtures.load("initial-data2007.yml");
            Fixtures.load("initial-data2003.yml");
            InitialDataImporter.importData();
        }
    }
}
