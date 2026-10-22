package jp.co.benesse.dcha.util;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static final URL[] getUrls(String[] urls) {
        if (urls == null) {
            return null;
        }
        URL[] results = new URL[urls.length];
        for (int i = 0; i < results.length; i++) {
            try {
                URL url = new URL(urls[i]);
                results[i] = url;
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return results;
    }

    public static final String getHttpRequestRangeValue(long startSize, long endSize) {
        if (startSize < 0 || endSize < 0 || startSize > endSize) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("bytes=");
        buffer.append(startSize);
        buffer.append("-");
        buffer.append(endSize);
        return buffer.toString();
    }

    public static final void disconnect(HttpURLConnection connection) {
        if (connection != null) {
            connection.disconnect();
        }
    }
}
