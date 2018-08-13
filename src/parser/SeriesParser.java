package parser;

import model.Series;
import model.SeriesManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import parser.exceptions.AllMissingDataException;

/**
 * parses the current season series from
 * http://api.crunchyroll.com/list_series.0.json?&session_id=nrg4fp05inifs2dglx0rv5vkazta23hi&media_type=anime&filter=alpha
 */
public class SeriesParser {

    public static void parseAllSeries(String jsonResponse) throws JSONException, AllMissingDataException {
        JSONObject allArrays = new JSONObject(jsonResponse);
        JSONArray allSeries = allArrays.getJSONArray("data");

        int numOfSeries = 0;

        for (int i = 0; i < allSeries.length(); i++) {
            JSONObject series = allSeries.getJSONObject(i);
            numOfSeries += parseOneSeries(series);
        }

        if (numOfSeries == 0) {
            throw new AllMissingDataException();
        }
    }

    private static int parseOneSeries(JSONObject series) {
        if (series.has("series_id") && series.has("name") && series.has("landscape_image") && series.has("description")) {
            boolean imgHasAllInfo;
            try {
                Series s = SeriesManager.getInstance().getSeriesWithId(series.getString("series_id"));
                s.setName(series.getString("name"));
                s.setDescription(series.getString("description"));
                JSONObject landscapeIMG = series.getJSONObject("landscape_image");
                imgHasAllInfo = parseIMG(landscapeIMG, s);
                if (imgHasAllInfo) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                // missing some information, so ignore this series
            }
        }
        return 0;
    }

    private static boolean parseIMG(JSONObject landscapeIMG, Series series) throws JSONException {
        if (landscapeIMG.has("large_url") && landscapeIMG.has("full_url")) {
            series.setImgLargeURL(landscapeIMG.getString("large_url"));
            series.setImgFullURL(landscapeIMG.getString("full_url"));
            return true;
        }
        return false;
    }


}
