package tests;

import model.Series;
import model.SeriesManager;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.SeriesParser;
import parser.exceptions.AllMissingDataException;
import providers.FileDataProvider;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestSeriesParser {
    @BeforeEach
    void setup() {
        SeriesManager.getInstance().clearSeries();
    }

    @Test
    void testNumSeries() {
        String data = "";
        try {
            data = new FileDataProvider("testSimulcastSeries.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SeriesParser.parseAllSeries(data);
        } catch (JSONException e) {
            fail("Shouldn't have caught exception");
        } catch (AllMissingDataException e) {
            fail("Shouldn't have caught this other exception");
        }

        assertEquals(69, SeriesManager.getInstance().getNumSeries());
    }

    @Test
    void testSeriesDataCorrect() {
        String data = "";
        try {
            data = new FileDataProvider("testSimulcastSeries.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            SeriesParser.parseAllSeries(data);
        } catch (JSONException e) {
            fail("Shouldn't have caught exception");
        } catch (AllMissingDataException e) {
            fail("Shouldn't have caught this other exception");
        }

        Series testSeries = new Series("273203");
        testSeries.setImgLargeURL("https://img1.ak.crunchyroll.com/i/spire2/deb8a1ab3a394773f36f125c9c229b611531935323_large.jpg");
        testSeries.setImgFullURL("https://img1.ak.crunchyroll.com/i/spire2/deb8a1ab3a394773f36f125c9c229b611531935323_full.jpg");
        testSeries.setName("Overlord");

        String id = testSeries.getId();

        assertEquals(id, SeriesManager.getInstance().getSeriesWithId(id).getId());
        assertEquals(testSeries.getName(), SeriesManager.getInstance().getSeriesWithId(id).getName());
        assertEquals(testSeries.getImgLargeURL(), SeriesManager.getInstance().getSeriesWithId(id).getImgLargeURL());
        assertEquals(testSeries.getImgFullURL(), SeriesManager.getInstance().getSeriesWithId(id).getImgFullURL());
    }
}
