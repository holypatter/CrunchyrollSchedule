package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import parser.exceptions.AllMissingDataException;

/**
 * Parser for the latest episode of the series
 * http://api.crunchyroll.com/list_media.0.json?&session_id=nrg4fp05inifs2dglx0rv5vkazta23hi&series_id=[series_id_here]&limit=1&sort=desc
 */
public class MediaParser {

    public static void parseAllMedia(String jsonResponse) throws JSONException, AllMissingDataException {
        JSONObject allMedia = new JSONObject(jsonResponse);
        JSONArray medias = allMedia.getJSONArray("data");

        int numMedia = 0;

        for (int i = 0; i < medias.length(); i++) {
            JSONObject media = medias.getJSONObject(i);
            numMedia += parseMedia(media);
        }

        if (numMedia == 0) {
            throw new AllMissingDataException();
        }
    }

    private static int parseMedia(JSONObject media) {
        if (media.has("name") && media.has("premium_available_time") && media.has("episode_number")) {


            return 1;
        }

        return 0;
    }
}
