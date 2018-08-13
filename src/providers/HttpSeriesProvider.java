package providers;

import auth.CrunchyrollSessionId;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpSeriesProvider extends AbstractHttpSeriesProvider {
    @Override
    protected URL getUrl() throws MalformedURLException {

        //http://api.crunchyroll.com/list_series.0.json?&session_id=nrg4fp05inifs2dglx0rv5vkazta23hi&media_type=anime&filter=simulcast&limit=5000
        String address = "http://api.crunchyroll.com/list_series.0.json?&session_id=" + CrunchyrollSessionId.getInstance().getId() +
                "&media_type=anime&filter=simulcast&limit=5000";

        return new URL(address);
    }

    @Override
    public byte[] dataSourceToBytes() throws IOException {
        return new byte[0];
    }
}
