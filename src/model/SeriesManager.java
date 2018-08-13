package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Manager for the list of series
 */
public class SeriesManager implements Iterable<Series> {
    private static SeriesManager instance;
    private Map<String, Series> simulcasts;

    private SeriesManager() {
        simulcasts = new HashMap<>();
    }

    /**
     * Gets one and only instance of this class
     *
     * @return  the instance of class
     */
    public static SeriesManager getInstance() {
        if (instance == null) {
            instance = new SeriesManager();
        }

        return instance;
    }

    /**
     * Get series with given id, creating it and adding it to the manager if necessary
     *
     * @param id   id of this series
     * @return     series with the given id
     */
    public Series getSeriesWithId(String id) {
        if (simulcasts.containsKey(id)) {
            return simulcasts.get(id);
        } else {
            Series s = new Series(id);
            simulcasts.put(id, s);
            return simulcasts.get(id);
        }
    }

    /**
     * Get series with given id, creating it and adding it to the manager if necessary,
     *      using the given information
     *
     * @param id           id of this series
     * @param name         name of this series
     * @param imgLargeURL  imgURL of this series for general view
     * @param imgFullURL   imgURL of this series for single view
     * @return        series with the given id, name, and imgURL
     */
    public Series getSeriesWithId(String id, String name, String imgLargeURL, String imgFullURL) {
        if (simulcasts.containsKey(id)) {
            return simulcasts.get(id);
        } else {
            Series s = new Series(id);
            s.setName(name);
            s.setImgLargeURL(imgLargeURL);
            s.setImgFullURL(imgFullURL);
            simulcasts.put(id, s);
            return simulcasts.get(id);
        }
    }

    public int getNumSeries() {
        return simulcasts.size();
    }

    public void clearSeries() {
        simulcasts.clear();
    }

    @Override
    public Iterator<Series> iterator() {
        return simulcasts.values().iterator();
    }
}
