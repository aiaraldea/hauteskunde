package init;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.election.DistrictBallotParty;
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
        String data = ""
                + "\"Cod.Municipio\",\"AMBITO\",\"Distrito\",\"Sección\",\"Mesa\",\"Censo\",\"Votantes\",\"Nulos\",\"Válidos\",\"Blancos\",\"Votos Cand.\",\"Abstenciones\",\"EAJ-PNV\",\"PP\",\"EA\",\"EAE / ANV\",\"PSE-EE/PSOE\",\"EB / ARALAR\",\"URE-EEB\"\n"
                + "005,\"ARAKALDO\",1,1,\"U\",94,64,0,64,0,64,30,45,0,,19,,,\n"
                + "009,\"ARRANKUDIAGA\",1,1,\"U\",734,540,204,336,14,322,194,306,7,,,9,,\n"
                + "074,\"URDUÑA-ORDUÑA\",1,1,\"U\",926,586,129,457,13,444,340,186,42,22,,51,70,73\n"
                + "074,\"URDUÑA-ORDUÑA\",1,2,\"A\",413,288,75,213,6,207,125,78,16,11,,18,41,43\n"
                + "074,\"URDUÑA-ORDUÑA\",1,2,\"B\",540,407,85,322,6,316,133,130,29,18,,32,50,57\n"
                + "074,\"URDUÑA-ORDUÑA\",1,2,\"C\",37,23,8,15,1,14,14,13,0,0,,0,0,1\n"
                + "074,\"URDUÑA-ORDUÑA\",1,2,\"D\",38,26,0,26,0,26,12,6,0,0,,2,0,18\n"
                + "074,\"URDUÑA-ORDUÑA\",1,2,\"E\",15,5,1,4,0,4,10,3,0,0,,0,0,1\n"
                + "074,\"URDUÑA-ORDUÑA\",1,2,\"F\",15,11,4,7,0,7,4,4,1,0,,0,0,2\n"
                + "074,\"URDUÑA-ORDUÑA\",2,1,\"A\",640,447,116,331,8,323,193,120,37,23,,28,52,63\n"
                + "074,\"URDUÑA-ORDUÑA\",2,1,\"B\",868,617,143,474,6,468,251,192,24,38,,40,89,85\n"
                + "075,\"OROZKO\",1,1,\"A\",749,525,92,433,10,423,224,302,38,49,,10,24,\n"
                + "075,\"OROZKO\",1,1,\"B\",816,616,94,522,15,507,200,347,46,58,,18,38,\n"
                + "075,\"OROZKO\",1,1,\"C\",338,222,44,178,0,178,116,138,7,24,,2,7,\n";

        Reader r = new StringReader(data);

        CsvMapReader mapReader = new CsvMapReader(r, CsvPreference.EXCEL_PREFERENCE);



        String[] headers;
        try {
            headers = mapReader.getCSVHeader(true);
            Map<String, String> result;
            String codMunicipio;
            String distrito;
            String seccion;
            String mesa;
            int censo;
            int nulos;
            int blancos;
            PollingStationBallot pollingStationBallot;
            PollingStationResult.Builder b;
            int amount;
            String amountString;
            result = mapReader.read(headers);
            do {
                codMunicipio = result.get("Cod.Municipio");
                distrito = result.get("Distrito");
                seccion = result.get("Sección");
                mesa = result.get("Mesa");
                censo = Integer.valueOf(result.get("Censo"));
                nulos = Integer.valueOf(result.get("Nulos"));
                blancos = Integer.valueOf(result.get("Blancos"));
                pollingStationBallot = PollingStationBallot.findPollingStation(codMunicipio, distrito, seccion, mesa);
                pollingStationBallot.census = censo;
                pollingStationBallot.save();
                b =
                        new PollingStationResult.Builder().pollingStationBallot(pollingStationBallot).
                        nullVoteAmount(nulos).
                        whiteVoteAmount(blancos);
                for (DistrictBallotParty districtBallotParty : pollingStationBallot.districtBallot.parties) {
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

                b.build().save();
                result = mapReader.read(headers);
            } while (result != null);

        } catch (IOException ex) {
            Logger.getLogger(InitialDataImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
