package jp.co.benesse.touch.asynccomm;

import android.app.IntentService;
import android.content.Intent;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import jp.co.benesse.touch.util.UrlUtil;

public class UpdateLogPostService extends IntentService {
    private static final String CHARSET_NAME_UTF8 = "UTF-8";
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String KEY_API_UPDATELOG = "x-api-key";
    private static final String KEY_CONNECTION = "Connection";
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String METHOD_POST = "POST";
    private static final int READ_TIMEOUT = 10000;
    private static final String VALUE_API_UPDATELOG = "DchaMonthlyDL";
    private static final String VALUE_CONNECTION = "close";
    private static final String VALUE_CONTENT_TYPE = "application/JSON; charset=UTF-8";

    public UpdateLogPostService() {
        super("UpdateLogPostService");
    }

    @Override
    public void onHandleIntent(Intent intent) throws Throwable {
        Closeable closeable;
        Closeable closeable2;
        HttpsURLConnection httpsURLConnection = null;
        outputStream = null;
        OutputStream outputStream = null;
        HttpsURLConnection httpsURLConnection2 = null;
        try {
            String stringExtra = intent.getStringExtra(IntentName.EXTRA_ASYNCCOMM_UPDATE_LOG);
            HttpsURLConnection httpsURLConnection3 = (HttpsURLConnection) new URL(UrlUtil.getUrlGeneralInfo()).openConnection();
            try {
                httpsURLConnection3.setDoOutput(true);
                httpsURLConnection3.setRequestMethod(METHOD_POST);
                httpsURLConnection3.setRequestProperty(KEY_API_UPDATELOG, VALUE_API_UPDATELOG);
                httpsURLConnection3.setRequestProperty(KEY_CONTENT_TYPE, VALUE_CONTENT_TYPE);
                httpsURLConnection3.setRequestProperty(KEY_CONNECTION, VALUE_CONNECTION);
                httpsURLConnection3.setConnectTimeout(10000);
                httpsURLConnection3.setReadTimeout(10000);
                httpsURLConnection3.connect();
                byte[] bytes = stringExtra.getBytes(CHARSET_NAME_UTF8);
                outputStream = httpsURLConnection3.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
                httpsURLConnection3.getResponseCode();
                if (httpsURLConnection3 != null) {
                    httpsURLConnection3.disconnect();
                }
                close(outputStream);
            } catch (IOException unused) {
                closeable2 = outputStream;
                httpsURLConnection2 = httpsURLConnection3;
                if (httpsURLConnection2 != null) {
                    httpsURLConnection2.disconnect();
                }
                close(closeable2);
            } catch (Throwable th) {
                th = th;
                closeable = outputStream;
                httpsURLConnection = httpsURLConnection3;
                if (httpsURLConnection != null) {
                    httpsURLConnection.disconnect();
                }
                close(closeable);
                throw th;
            }
        } catch (IOException unused2) {
            closeable2 = null;
        } catch (Throwable th2) {
            th = th2;
            closeable = null;
        }
    }

    private void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException unused) {
        }
    }
}
