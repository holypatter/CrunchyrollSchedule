package tests;

import model.EpisodeManager;
import model.Series;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.MediaParser;
import parser.exceptions.AllMissingDataException;
import providers.FileDataProvider;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMediaParser {
    @BeforeEach
    void setup() {
        EpisodeManager.getInstance().clearEpisodes();
    }

    @Test
    void testMediaData() {
        String data = "";
        String id = "273203";
        Series series = new Series(id);
        try {
            data = new FileDataProvider("testMediaLatest.json").dataSourceToString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            MediaParser.parseAllMedia(data, series);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (AllMissingDataException e) {
            e.printStackTrace();
        }

        assertEquals(1, EpisodeManager.getInstance().getNumEpisodes());
        assertEquals("Two Leaders", EpisodeManager.getInstance().getLatestEpisode(id).getName());
        assertEquals(5, EpisodeManager.getInstance().getLatestEpisode(id).getEpNum());
        assertEquals("2018-08-07T07:30:00-07:00", EpisodeManager.getInstance().getLatestEpisode(id).getPremiumAvailableTime());
    }
}
