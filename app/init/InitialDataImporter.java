package init;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.District;
import models.Municipality;
import models.PollingStation;
import models.election.DistrictBallot;
import models.election.DistrictBallotParty;
import models.election.Election;
import models.election.PollingStationBallot;
import models.results.PollingStationResult;
import org.apache.commons.lang.StringUtils;
import org.supercsv.io.CsvMapReader;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author inaki
 */
public class InitialDataImporter {

    public static void importData() {
        String dataBizkaia = ""
                + "\"Provincia\",\"Cod.Municipio\",\"AMBITO\",\"Distrito\",\"Sección\",\"Mesa\",\"Censo\",\"Votantes\",\"Nulos\",\"Válidos\",\"Blancos\",\"Votos Cand.\",\"Abstenciones\",\"EAJ-PNV\",\"PP\",\"EA\",\"EAE / ANV\",\"PSE-EE/PSOE\",\"EB / ARALAR\",\"URE-EEB\"\n"
                + "48,005,\"ARAKALDO\",1,1,\"U\",94,64,0,64,0,64,30,45,0,,19,,,\n"
                + "48,009,\"ARRANKUDIAGA\",1,1,\"U\",734,540,204,336,14,322,194,306,7,,,9,,\n"
                + "48,074,\"URDUÑA-ORDUÑA\",1,1,\"U\",926,586,129,457,13,444,340,186,42,22,,51,70,73\n"
                + "48,074,\"URDUÑA-ORDUÑA\",1,2,\"A\",413,288,75,213,6,207,125,78,16,11,,18,41,43\n"
                + "48,074,\"URDUÑA-ORDUÑA\",1,2,\"B\",540,407,85,322,6,316,133,130,29,18,,32,50,57\n"
                + "48,074,\"URDUÑA-ORDUÑA\",1,2,\"C\",37,23,8,15,1,14,14,13,0,0,,0,0,1\n"
                + "48,074,\"URDUÑA-ORDUÑA\",1,2,\"D\",38,26,0,26,0,26,12,6,0,0,,2,0,18\n"
                + "48,074,\"URDUÑA-ORDUÑA\",1,2,\"E\",15,5,1,4,0,4,10,3,0,0,,0,0,1\n"
                + "48,074,\"URDUÑA-ORDUÑA\",1,2,\"F\",15,11,4,7,0,7,4,4,1,0,,0,0,2\n"
                + "48,074,\"URDUÑA-ORDUÑA\",2,1,\"A\",640,447,116,331,8,323,193,120,37,23,,28,52,63\n"
                + "48,074,\"URDUÑA-ORDUÑA\",2,1,\"B\",868,617,143,474,6,468,251,192,24,38,,40,89,85\n"
                + "48,075,\"OROZKO\",1,1,\"A\",749,525,92,433,10,423,224,302,38,49,,10,24,\n"
                + "48,075,\"OROZKO\",1,1,\"B\",816,616,94,522,15,507,200,347,46,58,,18,38,\n"
                + "48,075,\"OROZKO\",1,1,\"C\",338,222,44,178,0,178,116,138,7,24,,2,7,";
//        readData(dataBizkaia);
        String dataAraba = ""
                + "\"Provincia\",\"Cod.Municipio\",\"AMBITO\",\"Distrito\",\"Sección\",\"Mesa\",\"Censo\",\"Votantes\",\"Nulos\",\"Válidos\",\"Blancos\",\"Votos Cand.\",\"Abstenciones\",\"EAJ-PNV\",\"PP\",\"EA\",\"EB\",\"EAE / ANV\",\"PSE-EE/PSOE\",\"ARALAR\",\"EB / ARALAR\"\n"
                + "01,002,\"AMURRIO\",1,1,\"A\",755,480,2,478,7,471,275,101,83,112,14,85,51,25,\n"
                + "01,002,\"AMURRIO\",1,1,\"B\",638,455,12,443,10,433,183,87,72,146,7,63,37,21,\n"
                + "01,002,\"AMURRIO\",1,1,\"C\",300,226,0,226,7,219,74,84,22,41,4,47,9,12,\n"
                + "01,002,\"AMURRIO\",1,2,\"A\",656,449,7,442,3,439,207,110,45,130,17,68,50,19,\n"
                + "01,002,\"AMURRIO\",1,2,\"B\",634,430,0,430,4,426,204,114,44,128,6,67,53,14,\n"
                + "01,002,\"AMURRIO\",1,2,\"C\",236,155,3,152,1,151,81,52,14,32,1,39,0,13,\n"
                + "01,002,\"AMURRIO\",1,3,\"U\",787,560,9,551,10,541,227,129,63,138,13,95,69,34,\n"
                + "01,002,\"AMURRIO\",1,4,\"A\",615,418,2,416,5,411,197,110,39,112,7,66,51,26,\n"
                + "01,002,\"AMURRIO\",1,4,\"B\",729,481,8,473,7,466,248,129,46,132,12,66,52,29,\n"
                + "01,002,\"AMURRIO\",1,5,\"A\",536,366,1,365,8,357,170,74,42,106,9,49,70,7,\n"
                + "01,002,\"AMURRIO\",1,5,\"B\",483,312,3,309,3,306,171,69,36,85,11,36,60,9,\n"
                + "01,002,\"AMURRIO\",1,6,\"A\",530,371,5,366,9,357,159,103,33,98,6,61,44,12,\n"
                + "01,002,\"AMURRIO\",1,6,\"B\",538,372,4,368,10,358,166,89,27,103,12,63,52,12,\n"
                + "01,002,\"AMURRIO\",1,7,\"U\",831,572,3,569,15,554,259,122,62,152,21,85,90,22,\n"
                + "01,004,\"ARTZINIEGA\",1,1,\"A\",698,496,48,448,11,437,202,151,92,62,13,,37,82,\n"
                + "01,004,\"ARTZINIEGA\",1,1,\"B\",719,502,61,441,9,432,217,150,63,55,9,,44,111,\n"
                + "01,010,\"AYALA / AIARA\",1,1,\"A\",958,659,105,554,12,542,299,308,101,83,32,,18,,\n"
                + "01,010,\"AYALA / AIARA\",1,1,\"B\",456,364,71,293,9,284,92,172,41,48,13,,10,,\n"
                + "01,010,\"AYALA / AIARA\",1,2,\"U\",787,547,63,484,9,475,240,199,43,149,23,,61,,\n"
                + "01,036,\"LAUDIO / LLODIO\",1,1,\"A\",804,473,5,468,10,458,331,201,77,28,,80,61,,11\n"
                + "01,036,\"LAUDIO / LLODIO\",1,1,\"B\",851,563,5,558,5,553,288,224,92,27,,103,76,,31\n"
                + "01,036,\"LAUDIO / LLODIO\",1,2,\"A\",829,519,3,516,12,504,310,202,53,19,,116,82,,32\n"
                + "01,036,\"LAUDIO / LLODIO\",1,2,\"B\",913,561,9,552,12,540,352,200,74,15,,123,100,,28\n"
                + "01,036,\"LAUDIO / LLODIO\",1,3,\"A\",524,312,1,311,5,306,212,80,33,7,,83,74,,29\n"
                + "01,036,\"LAUDIO / LLODIO\",1,3,\"B\",569,367,2,365,7,358,202,99,40,9,,91,98,,21\n"
                + "01,036,\"LAUDIO / LLODIO\",1,4,\"U\",654,392,4,388,8,380,262,145,49,7,,74,85,,20\n"
                + "01,036,\"LAUDIO / LLODIO\",1,5,\"A\",742,440,4,436,5,431,302,136,30,8,,181,54,,22\n"
                + "01,036,\"LAUDIO / LLODIO\",1,5,\"B\",808,479,2,477,9,468,329,151,42,24,,154,73,,24\n"
                + "01,036,\"LAUDIO / LLODIO\",1,6,\"A\",895,563,6,557,7,550,332,216,64,18,,161,78,,13\n"
                + "01,036,\"LAUDIO / LLODIO\",1,6,\"B\",878,611,7,604,11,593,267,249,82,27,,116,86,,33\n"
                + "01,036,\"LAUDIO / LLODIO\",1,7,\"A\",589,366,4,362,12,350,223,114,56,16,,71,79,,14\n"
                + "01,036,\"LAUDIO / LLODIO\",1,7,\"B\",541,343,5,338,3,335,198,101,54,25,,65,81,,9\n"
                + "01,036,\"LAUDIO / LLODIO\",1,8,\"A\",549,319,5,314,3,311,230,100,45,4,,78,67,,17\n"
                + "01,036,\"LAUDIO / LLODIO\",1,8,\"B\",569,337,4,333,9,324,232,101,57,10,,79,65,,12\n"
                + "01,036,\"LAUDIO / LLODIO\",1,9,\"A\",581,397,5,392,7,385,184,137,49,21,,100,58,,20\n"
                + "01,036,\"LAUDIO / LLODIO\",1,9,\"B\",576,350,3,347,4,343,226,117,36,12,,102,59,,17\n"
                + "01,036,\"LAUDIO / LLODIO\",1,10,\"A\",744,464,6,458,6,452,280,167,59,8,,126,76,,16\n"
                + "01,036,\"LAUDIO / LLODIO\",1,10,\"B\",767,499,12,487,12,475,268,164,52,16,,121,90,,32\n"
                + "01,036,\"LAUDIO / LLODIO\",1,11,\"U\",676,467,5,462,9,453,209,200,37,25,,116,56,,19\n"
                + "01,036,\"LAUDIO / LLODIO\",1,12,\"U\",586,362,6,356,6,350,224,100,60,8,,70,92,,20\n"
                + "01,036,\"LAUDIO / LLODIO\",1,13,\"A\",542,328,2,326,6,320,214,132,30,12,,77,48,,21\n"
                + "01,036,\"LAUDIO / LLODIO\",1,13,\"B\",519,291,5,286,5,281,228,126,29,10,,57,44,,15\n"
                + "01,042,\"OKONDO\",1,1,\"U\",893,773,101,672,11,661,120,274,15,365,,,7,,";
//        readData(dataAraba);
    }

    public static void readData(String data) {
        Reader r = new StringReader(data);

        CsvMapReader mapReader = new CsvMapReader(r, CsvPreference.STANDARD_PREFERENCE);

        String[] headers;
        try {
            headers = mapReader.getCSVHeader(true);
            Map<String, String> result;
            String eleccion;
            String institucion;
            String codMunicipio;
            String nomMunicipio;
            String provincia;
            String distrito;
            String seccion;
            String mesa;
            int censo;
            int nulos;
            int blancos;
            PollingStationBallot pollingStationBallot;
            PollingStationResult psr;
            PollingStationResult.Builder b;
            int amount;
            String amountString;
            result = mapReader.read(headers);
            do {
                eleccion = result.get("Elección").trim();
                institucion = result.get("Institucion").trim();
                provincia = result.get("Provincia").trim();
                codMunicipio = result.get("Cod.Municipio").trim();
                nomMunicipio = result.get("AMBITO").trim();
                distrito = result.get("Distrito").trim();
                seccion = result.get("Sección").trim();
                mesa = result.get("Mesa").trim();
                censo = Integer.valueOf(result.get("Censo").trim());
                nulos = Integer.valueOf(result.get("Nulos").trim());
                blancos = Integer.valueOf(result.get("Blancos").trim());
                
                Election election = loadOrCreateElection(eleccion);
                District district = loadOrCreateDistrict(institucion);
                DistrictBallot districtBallot = loadOrCreateDistrictBallot(election, district);
                Municipality municipality = loadOrCreateMunicipality(provincia, codMunicipio, nomMunicipio);
                PollingStation pollingStation = loadOrCreatePollingStation(provincia, municipality, distrito, seccion, mesa);
                pollingStationBallot = loadOrCreatePollingStationBallot(districtBallot, pollingStation, censo);

                pollingStationBallot.census = censo;
                pollingStationBallot.save();
                psr = pollingStationBallot.result;
                if (psr == null) {
                    b =
                            new PollingStationResult.Builder().pollingStationBallot(pollingStationBallot).
                            nullVoteAmount(nulos).
                            whiteVoteAmount(blancos);
                    for (DistrictBallotParty districtBallotParty : pollingStationBallot.districtBallot.getParties()) {
                        if (StringUtils.isNotEmpty(districtBallotParty.importName)) {
                            amountString = result.get(districtBallotParty.importName);
                            if (StringUtils.isNotEmpty(amountString)) {
                                amount = Integer.valueOf(amountString);
                            } else {
                                amount = 0;
                            }
                            b.addResult(districtBallotParty, amount);
                        }
                    }

                    psr = b.build();
                    psr.save();
                } else {
                    psr.nullVoteAmount = nulos;
                    psr.whiteVoteAmount = blancos;
                    for (DistrictBallotParty districtBallotParty : pollingStationBallot.districtBallot.getParties()) {
                        if (StringUtils.isNotEmpty(districtBallotParty.importName)) {
                            amountString = result.get(districtBallotParty.importName);
                            if (StringUtils.isNotEmpty(amountString)) {
                                amount = Integer.valueOf(amountString);
                            } else {
                                amount = 0;
                            }
                            psr.addResult(districtBallotParty, amount);
                        }
                    }
                }
                result = mapReader.read(headers);
            } while (result != null);

        } catch (IOException ex) {
            Logger.getLogger(InitialDataImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Election loadOrCreateElection(String eleccion) {
        Election election = Election.find("name", eleccion).first();
        if (election == null) {
            election = new Election(eleccion);
            election.save();
        }
        return election;
    }

    private static District loadOrCreateDistrict(String name) {
        District district = District.find("name", name).first();
        if (district == null) {
            district = new District(name);
            district.save();
        }
        return district;
    }

    private static DistrictBallot loadOrCreateDistrictBallot(Election election, District district) {
        DistrictBallot districtBallot = DistrictBallot.find(
                "election.id = ? and district.id = ?",
                election.id, district.id).first();
        if (districtBallot == null) {
            districtBallot = new DistrictBallot(election, district);
            districtBallot.save();
        }
        return districtBallot;
    }

    private static Municipality loadOrCreateMunicipality(String stateCode, String code, String name) {
        Municipality municipality = Municipality.find("byStateAndCode", stateCode, code).first();
        if (municipality == null) {
            municipality = new Municipality(stateCode, code, name);
            municipality.save();
        }
        return municipality;
    }

    private static PollingStation loadOrCreatePollingStation(String provincia, Municipality municipality, String distrito, String seccion, String mesa) {
        PollingStation pollingStation = PollingStation.find(
                "municipality.state = ? "
                + "and municipality = ? "
                + "and psDistrict = ? "
                + "and section = ? "
                + "and table = ?",
                provincia,
                municipality,
                distrito,
                seccion,
                mesa).first();
        if (pollingStation == null) {
            pollingStation = new PollingStation(municipality, distrito, seccion, mesa);
            pollingStation.save();
        }
        return pollingStation;
    }

    private static PollingStationBallot loadOrCreatePollingStationBallot(DistrictBallot districtBallot, PollingStation pollingStation, int census) {
        PollingStationBallot pollingStationBallot =
                PollingStationBallot.find("districtBallot.id = ? and pollingStation.id = ?", districtBallot.id, pollingStation.id).first();
        if (pollingStationBallot == null) {
            pollingStationBallot = new PollingStationBallot(districtBallot, pollingStation, census);
            pollingStationBallot.save();
        }
        return pollingStationBallot;
    }
}
