package providers;

import auth.CrunchyrollSessionId;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpMediaProvider extends AbstractHttpSeriesProvider {
    private String series_id;

    @Override
    protected URL getUrl() throws MalformedURLException {
        //http://api.crunchyroll.com/list_media.0.json?&session_id=[series_id here]&series_id=[series_id_here]&limit=1&sort=desc
        String webaddress = "http://api.crunchyroll.com/list_media.0.json?&session_id=" +
                CrunchyrollSessionId.getInstance().getId() +
                "&series_id=" + series_id + "&limit=1&sort=desc";
        return new URL(webaddress);
    }

    @Override
    public byte[] dataSourceToBytes() throws IOException {
        return new byte[0];
    }

    public void setSeriesId(String seriesId) {
        this.series_id = seriesId;
    }
}
