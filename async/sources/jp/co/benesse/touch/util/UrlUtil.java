package jp.co.benesse.touch.util;

import android.os.Build;
import android.text.TextUtils;
import jp.co.benesse.touch.debug.Logger;

public class UrlUtil {
    private static final String AKAMAI_URL1 = "https://townak.benesse.ne.jp/test2/A/sp_84/";
    private static final String AKAMAI_URL2 = "https://townak.benesse.ne.jp/test2/B/sp_84/";
    private static final String AKAMAI_URL3 = "https://townak.benesse.ne.jp/test/A/sp_84/";
    private static final String AKAMAI_URL4 = "https://townak.benesse.ne.jp/test/B/sp_84/";
    private static final String AKAMAI_URL5 = "https://townak.benesse.ne.jp/rel/B/sp_84/";
    private static final String AKAMAI_URL6 = "https://townak.benesse.ne.jp/rel/A/sp_84/";
    public static final String AUTH_API = "authapi";
    private static final String AUTH_URL1 = "https://loginc-test.benesse.ne.jp/appapi/";
    private static final String AUTH_URL2 = "https://loginc.benesse.ne.jp/appapi/";
    private static final String BAE_URL1 = "https://d2-st.benesse.ne.jp/api";
    private static final String BAE_URL2 = "https://d2-st-staging.benesse.ne.jp/api";
    private static final String BAE_URL3 = "https://d2-ut.benesse.ne.jp/api";
    private static final String BAE_URL4 = "https://d2-ut-staging.benesse.ne.jp/api";
    private static final String BAE_URL5 = "https://d2-staging.benesse.ne.jp/api";
    private static final String BAE_URL6 = "https://d2.benesse.ne.jp/api";
    private static final String GA_TRACKING_ID1 = "UA-89253981-1";
    private static final String GA_TRACKING_ID2 = "UA-55772315-19";
    private static final String GENERALINFO_URL1 = "https://czemi-t.benesse.ne.jp/generalinfo/v1/";
    private static final String GENERALINFO_URL2 = "https://czemi.benesse.ne.jp/generalinfo/v1/";
    public static final String LOGOUT_API = "logout";
    private static final String LOG_URL1 = "https://dsklg-t1.benesse.ne.jp/api";
    private static final String LOG_URL2 = "https://dsklg-az-p1.benesse.ne.jp/api";
    private static final String LOG_URL3 = "https://dsklg-az.benesse.ne.jp/api";
    private static final String MEMBER_URL1 = "https://membership-test2.benesse.ne.jp/ap/m/contractInfQuery/latestShippedInfQuery/";
    private static final String MEMBER_URL2 = "https://membership2.benesse.ne.jp/ap/m/contractInfQuery/latestShippedInfQuery/";
    public static final String ONETIMEKEY_API = "onetimekey";
    private static final String OS_TYPE_001 = "001";
    private static final String OS_TYPE_002 = "092";
    private static final String OS_TYPE_003 = "005";
    private static final String OS_TYPE_004 = "096";
    private static final String OS_TYPE_005 = "099";
    private static final String OS_TYPE_006 = "000";
    public static final String SESSION_UPDATE_API = "update";
    private static final String SSO_URL1 = "https://login-test.benesse.ne.jp/appsso/sso";
    private static final String SSO_URL2 = "https://login.benesse.ne.jp/appsso/sso";
    private static final String TAG = "UrlUtil";
    private static final String TIME_URL1 = "http://ct2-st.benesse.ne.jp/cgi-bin/GetDate.pl";
    private static final String TIME_URL2 = "http://ct2-stag-st.benesse.ne.jp/cgi-bin/GetDate.pl";
    private static final String TIME_URL3 = "http://ct2-k.benesse.ne.jp/cgi-bin/GetDate.pl";
    private static final String TIME_URL4 = "http://ct2-stag-k.benesse.ne.jp/cgi-bin/GetDate.pl";
    private static final String TIME_URL5 = "http://ct2-stag.benesse.ne.jp/cgi-bin/GetDate.pl";
    private static final String TIME_URL6 = "http://ct2.benesse.ne.jp/cgi-bin/GetDate.pl";
    private static final String VER_SPLIT = "\\.";
    private static final int VER_SPLIT_NUM = 3;

    private UrlUtil() {
    }

    public static String getUrlBae() {
        String urlBae = UrlCacheUtil.getInstance().getUrlBae();
        if (!TextUtils.isEmpty(urlBae)) {
            Logger.d(TAG, "result(cache):", urlBae);
            return urlBae;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = BAE_URL6;
        if (zEquals) {
            str = BAE_URL1;
        } else if (urlType.equals(OS_TYPE_002)) {
            str = BAE_URL2;
        } else if (urlType.equals(OS_TYPE_003)) {
            str = BAE_URL3;
        } else if (urlType.equals(OS_TYPE_004)) {
            str = BAE_URL4;
        } else if (urlType.equals(OS_TYPE_005)) {
            str = BAE_URL5;
        } else {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getUrlLog() {
        String urlDsklg = UrlCacheUtil.getInstance().getUrlDsklg();
        if (!TextUtils.isEmpty(urlDsklg)) {
            Logger.d(TAG, "result(cache):", urlDsklg);
            return urlDsklg;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = LOG_URL3;
        if (zEquals || urlType.equals(OS_TYPE_002) || urlType.equals(OS_TYPE_003) || urlType.equals(OS_TYPE_004)) {
            str = LOG_URL1;
        } else if (urlType.equals(OS_TYPE_005)) {
            str = LOG_URL2;
        } else {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getUrlAkamai() {
        String urlAkamai = UrlCacheUtil.getInstance().getUrlAkamai();
        if (!TextUtils.isEmpty(urlAkamai)) {
            if (!urlAkamai.endsWith("/")) {
                urlAkamai = urlAkamai + "/";
            }
            Logger.d(TAG, "result(cache):", urlAkamai);
            return urlAkamai;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = AKAMAI_URL6;
        if (zEquals) {
            str = AKAMAI_URL1;
        } else if (urlType.equals(OS_TYPE_002)) {
            str = AKAMAI_URL2;
        } else if (urlType.equals(OS_TYPE_003)) {
            str = AKAMAI_URL3;
        } else if (urlType.equals(OS_TYPE_004)) {
            str = AKAMAI_URL4;
        } else if (urlType.equals(OS_TYPE_005)) {
            str = AKAMAI_URL5;
        } else {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getUrlBaeMember() {
        String urlMemberIF053 = UrlCacheUtil.getInstance().getUrlMemberIF053();
        if (!TextUtils.isEmpty(urlMemberIF053)) {
            Logger.d(TAG, "result(cache):", urlMemberIF053);
            return urlMemberIF053;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = MEMBER_URL2;
        if (zEquals || urlType.equals(OS_TYPE_002) || urlType.equals(OS_TYPE_003) || urlType.equals(OS_TYPE_004)) {
            str = MEMBER_URL1;
        } else if (!urlType.equals(OS_TYPE_005)) {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getUrlAuth() {
        String urlLoginApi = UrlCacheUtil.getInstance().getUrlLoginApi();
        if (!TextUtils.isEmpty(urlLoginApi)) {
            if (!urlLoginApi.endsWith("/")) {
                urlLoginApi = urlLoginApi + "/";
            }
            Logger.d(TAG, "result(cache):", urlLoginApi);
            return urlLoginApi;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = AUTH_URL2;
        if (zEquals || urlType.equals(OS_TYPE_002) || urlType.equals(OS_TYPE_003) || urlType.equals(OS_TYPE_004)) {
            str = AUTH_URL1;
        } else if (!urlType.equals(OS_TYPE_005)) {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getUrlSso() {
        String urlLoginSso = UrlCacheUtil.getInstance().getUrlLoginSso();
        if (!TextUtils.isEmpty(urlLoginSso)) {
            Logger.d(TAG, "result(cache):", urlLoginSso);
            return urlLoginSso;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = SSO_URL2;
        if (zEquals || urlType.equals(OS_TYPE_002) || urlType.equals(OS_TYPE_003) || urlType.equals(OS_TYPE_004)) {
            str = SSO_URL1;
        } else if (!urlType.equals(OS_TYPE_005)) {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getUrlTime() {
        String urlTime = UrlCacheUtil.getInstance().getUrlTime();
        if (!TextUtils.isEmpty(urlTime)) {
            Logger.d(TAG, "result(cache):", urlTime);
            return urlTime;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = TIME_URL6;
        if (zEquals) {
            str = TIME_URL1;
        } else if (urlType.equals(OS_TYPE_002)) {
            str = TIME_URL2;
        } else if (urlType.equals(OS_TYPE_003)) {
            str = TIME_URL3;
        } else if (urlType.equals(OS_TYPE_004)) {
            str = TIME_URL4;
        } else if (urlType.equals(OS_TYPE_005)) {
            str = TIME_URL5;
        } else {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getUrlGeneralInfo() {
        String urlGeneralInfo = UrlCacheUtil.getInstance().getUrlGeneralInfo();
        if (!TextUtils.isEmpty(urlGeneralInfo)) {
            Logger.d(TAG, "result(cache):", urlGeneralInfo);
            return urlGeneralInfo;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = GENERALINFO_URL2;
        if (zEquals || urlType.equals(OS_TYPE_002) || urlType.equals(OS_TYPE_003) || urlType.equals(OS_TYPE_004)) {
            str = GENERALINFO_URL1;
        } else if (!urlType.equals(OS_TYPE_005)) {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getGaTrackingID() {
        String gaTrackingID = UrlCacheUtil.getInstance().getGaTrackingID();
        if (!TextUtils.isEmpty(gaTrackingID)) {
            Logger.d(TAG, "result(cache):", gaTrackingID);
            return gaTrackingID;
        }
        String urlType = getUrlType(getBuildId());
        boolean zEquals = urlType.equals(OS_TYPE_001);
        String str = GA_TRACKING_ID2;
        if (zEquals || urlType.equals(OS_TYPE_002) || urlType.equals(OS_TYPE_003) || urlType.equals(OS_TYPE_004)) {
            str = GA_TRACKING_ID1;
        } else if (!urlType.equals(OS_TYPE_005)) {
            urlType.equals(OS_TYPE_006);
        }
        Logger.d(TAG, "result:", str);
        return str;
    }

    public static String getEnvironmentName() {
        String environmentName = UrlCacheUtil.getInstance().getEnvironmentName();
        Logger.d(TAG, "result:", environmentName);
        return environmentName;
    }

    protected static String getBuildId() {
        return Build.ID;
    }

    protected static String getUrlType(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] strArrSplit = str.split(VER_SPLIT);
            if (strArrSplit.length == 3) {
                return strArrSplit[2].replace('T', '0');
            }
        }
        return OS_TYPE_006;
    }
}
