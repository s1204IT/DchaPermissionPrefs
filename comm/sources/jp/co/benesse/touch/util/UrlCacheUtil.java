package jp.co.benesse.touch.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public class UrlCacheUtil {
    private static final String COLUMN_KVS_KEY = "key";
    private static final String COLUMN_KVS_SELECTION = "key IN( ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String COLUMN_KVS_VALUE = "value";
    private static final String CONNECT_ID_AKAMAI = "townak";
    private static final String CONNECT_ID_BAE = "bae";
    private static final String CONNECT_ID_DCHADATE = "dchaDate";
    private static final String CONNECT_ID_DSKLG = "dsklg";
    private static final String CONNECT_ID_GENERALINFO = "generalinfochuzemi";
    private static final String CONNECT_ID_IF053 = "membershipIf053";
    private static final String CONNECT_ID_LOGINAPI = "loginApi";
    private static final String CONNECT_ID_LOGINSSO = "loginSso";
    private static final UrlCacheUtil mUrlCacheUtil = new UrlCacheUtil();
    private static final Uri URI_TEST_ENVIRONMENT_INFO = Uri.parse("content://jp.co.benesse.dcha.databox.db.KvsProvider/kvs/test.environment.info");
    private String mBaeUrl = null;
    private String mDsklg = null;
    private String mAkamaiUrl = null;
    private String mLoginApiUrl = null;
    private String mLoginSsoUrl = null;
    private String mTimeUrl = null;
    private String mMemberIF053Url = null;
    private String mGeneralInfoUrl = null;

    private UrlCacheUtil() {
    }

    public static synchronized UrlCacheUtil getInstance() {
        return mUrlCacheUtil;
    }

    public synchronized void loadUrl(Context context) {
        Map<String, String> urlMap = getKvsUrl(context);
        this.mBaeUrl = urlMap.get(CONNECT_ID_BAE);
        this.mDsklg = urlMap.get(CONNECT_ID_DSKLG);
        this.mAkamaiUrl = urlMap.get(CONNECT_ID_AKAMAI);
        this.mLoginApiUrl = urlMap.get(CONNECT_ID_LOGINAPI);
        this.mLoginSsoUrl = urlMap.get(CONNECT_ID_LOGINSSO);
        this.mTimeUrl = urlMap.get(CONNECT_ID_DCHADATE);
        this.mMemberIF053Url = urlMap.get(CONNECT_ID_IF053);
        this.mGeneralInfoUrl = urlMap.get(CONNECT_ID_GENERALINFO);
    }

    public synchronized void clearUrl() {
        this.mBaeUrl = null;
        this.mDsklg = null;
        this.mAkamaiUrl = null;
        this.mLoginApiUrl = null;
        this.mLoginSsoUrl = null;
        this.mTimeUrl = null;
        this.mMemberIF053Url = null;
        this.mGeneralInfoUrl = null;
    }

    public synchronized String getUrlBae() {
        return this.mBaeUrl;
    }

    public synchronized String getUrlDsklg() {
        return this.mDsklg;
    }

    public synchronized String getUrlAkamai() {
        return this.mAkamaiUrl;
    }

    public synchronized String getUrlLoginApi() {
        return this.mLoginApiUrl;
    }

    public synchronized String getUrlLoginSso() {
        return this.mLoginSsoUrl;
    }

    public synchronized String getUrlTime() {
        return this.mTimeUrl;
    }

    public synchronized String getUrlMemberIF053() {
        return this.mMemberIF053Url;
    }

    public synchronized String getUrlGeneralInfo() {
        return this.mGeneralInfoUrl;
    }

    protected static Map<String, String> getKvsUrl(Context context) {
        Map<String, String> urlMap = new HashMap<>();
        if (context != null) {
            String[] projection = {"key", "value"};
            String[] selectionArgs = {CONNECT_ID_BAE, CONNECT_ID_DSKLG, CONNECT_ID_AKAMAI, CONNECT_ID_LOGINAPI, CONNECT_ID_LOGINSSO, CONNECT_ID_DCHADATE, CONNECT_ID_IF053, CONNECT_ID_GENERALINFO};
            Cursor cursor = null;
            try {
                ContentResolver cr = context.getContentResolver();
                cursor = cr.query(URI_TEST_ENVIRONMENT_INFO, projection, COLUMN_KVS_SELECTION, selectionArgs, null);
                if (cursor != null) {
                    for (boolean next = cursor.moveToFirst(); next; next = cursor.moveToNext()) {
                        urlMap.put(cursor.getString(cursor.getColumnIndex("key")), cursor.getString(cursor.getColumnIndex("value")));
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        }
        return urlMap;
    }
}
