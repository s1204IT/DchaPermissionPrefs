package jp.co.benesse.touch.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public class UrlCacheUtil {
    private static final String COLUMN_KVS_KEY = "key";
    private static final String COLUMN_KVS_SELECTION = "key IN( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String COLUMN_KVS_VALUE = "value";
    private static final String CONNECT_ID_AKAMAI = "townak";
    private static final String CONNECT_ID_BAE = "bae";
    private static final String CONNECT_ID_DCHADATE = "dchaDate";
    private static final String CONNECT_ID_DSKLG = "dsklg";
    private static final String CONNECT_ID_GA = "googleAnalyticsTracker";
    private static final String CONNECT_ID_GENERALINFO = "generalinfochuzemi";
    private static final String CONNECT_ID_IF053 = "membershipIf053";
    private static final String CONNECT_ID_LOGINAPI = "loginApi";
    private static final String CONNECT_ID_LOGINSSO = "loginSso";
    private static final String ENVIRONMENT_NAME = "environment";
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
    private String mGaTrackingID = null;
    private String mEnvironmentName = null;

    private UrlCacheUtil() {
    }

    public static synchronized UrlCacheUtil getInstance() {
        return mUrlCacheUtil;
    }

    public synchronized void loadUrl(Context context) {
        Map<String, String> kvsUrl = getKvsUrl(context);
        this.mBaeUrl = kvsUrl.get(CONNECT_ID_BAE);
        this.mDsklg = kvsUrl.get(CONNECT_ID_DSKLG);
        this.mAkamaiUrl = kvsUrl.get(CONNECT_ID_AKAMAI);
        this.mLoginApiUrl = kvsUrl.get(CONNECT_ID_LOGINAPI);
        this.mLoginSsoUrl = kvsUrl.get(CONNECT_ID_LOGINSSO);
        this.mTimeUrl = kvsUrl.get(CONNECT_ID_DCHADATE);
        this.mMemberIF053Url = kvsUrl.get(CONNECT_ID_IF053);
        this.mGeneralInfoUrl = kvsUrl.get(CONNECT_ID_GENERALINFO);
        this.mGaTrackingID = kvsUrl.get(CONNECT_ID_GA);
        this.mEnvironmentName = kvsUrl.get(ENVIRONMENT_NAME);
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
        this.mGaTrackingID = null;
        this.mEnvironmentName = null;
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

    public synchronized String getGaTrackingID() {
        return this.mGaTrackingID;
    }

    public synchronized String getEnvironmentName() {
        return this.mEnvironmentName;
    }

    protected static Map<String, String> getKvsUrl(Context context) {
        HashMap map = new HashMap();
        if (context != null) {
            Cursor cursorQuery = null;
            try {
                cursorQuery = context.getContentResolver().query(URI_TEST_ENVIRONMENT_INFO, new String[]{"key", "value"}, COLUMN_KVS_SELECTION, new String[]{CONNECT_ID_BAE, CONNECT_ID_DSKLG, CONNECT_ID_AKAMAI, CONNECT_ID_LOGINAPI, CONNECT_ID_LOGINSSO, CONNECT_ID_DCHADATE, CONNECT_ID_IF053, CONNECT_ID_GENERALINFO, CONNECT_ID_GA, ENVIRONMENT_NAME}, null);
                if (cursorQuery != null) {
                    for (boolean zMoveToFirst = cursorQuery.moveToFirst(); zMoveToFirst; zMoveToFirst = cursorQuery.moveToNext()) {
                        map.put(cursorQuery.getString(cursorQuery.getColumnIndex("key")), cursorQuery.getString(cursorQuery.getColumnIndex("value")));
                    }
                }
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            } catch (Exception unused) {
                if (cursorQuery != null) {
                }
            } catch (Throwable th) {
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
                throw th;
            }
        }
        return map;
    }
}
