package providers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpMediaProvider extends AbstractHttpSeriesProvider {
    @Override
    protected URL getUrl() throws MalformedURLException {
        return null;
    }

    @Override
    public byte[] dataSourceToBytes() throws IOException {
        return new byte[0];
    }
}
