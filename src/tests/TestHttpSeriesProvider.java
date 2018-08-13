package tests;

import auth.CrunchyrollSessionId;
import model.SeriesManager;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.SeriesParser;
import parser.exceptions.AllMissingDataException;
import providers.HttpSeriesProvider;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static main.Main.getSessId;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHttpSeriesProvider {
    @BeforeEach
    void setup() throws IOException {
        SeriesManager.getInstance().clearSeries();
        URLConnection connection = new URL("http://crunchyroll.com").openConnection();
        Map<String, List<String>> headers = connection.getHeaderFields();
        List<String> values = headers.get("Set-Cookie");

        String sessionID;

        // for each cookie, if the key is "sess_id", store its value into sessionID
        for(Iterator iterator = values.iterator(); iterator.hasNext();) {
            sessionID = getSessId(iterator.next().toString());
            if (!sessionID.equals("")) {
                CrunchyrollSessionId.getInstance().setID(sessionID);
                break;
            }
        }
    }

    @Test
    void testWebsiteData() throws IOException, JSONException, AllMissingDataException {
        HttpSeriesProvider website = new HttpSeriesProvider();
        String data = website.dataSourceToString();

        SeriesParser.parseAllSeries(data);

        assertEquals(69, SeriesManager.getInstance().getNumSeries());
    }

}
