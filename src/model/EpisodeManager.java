package model;

import java.util.HashMap;
import java.util.Map;

public class EpisodeManager {
    private Map<String, LatestEpisode> simulcastEpisodes;
    private static EpisodeManager instance;

    private EpisodeManager() {
        simulcastEpisodes = new HashMap<>();
    }

    public static EpisodeManager getInstance() {
        if (instance == null) {
            instance = new EpisodeManager();
        }

        return instance;
    }

    public LatestEpisode getLatestEpisode(String seriesID) {
        if (simulcastEpisodes.containsKey(seriesID)) {
            return simulcastEpisodes.get(seriesID);
        } else {
            LatestEpisode le = new LatestEpisode(0, "", "", seriesID);
            simulcastEpisodes.put(seriesID, le);
            return simulcastEpisodes.get(seriesID);
        }
    }

    public LatestEpisode getLatestEpisode(String seriesID, int epNum, String name, String premTime) {
        if (simulcastEpisodes.containsKey(seriesID)) {
            return simulcastEpisodes.get(seriesID);
        } else {
            LatestEpisode le = new LatestEpisode(epNum, name, premTime, seriesID);
            simulcastEpisodes.put(seriesID, le);
            return simulcastEpisodes.get(seriesID);
        }
    }

    public void clearEpisodes() {
        simulcastEpisodes.clear();
    }

    public int getNumEpisodes() {
        return simulcastEpisodes.size();
    }
}
