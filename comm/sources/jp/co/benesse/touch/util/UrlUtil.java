package jp.co.benesse.touch.util;

import android.os.Build;
import android.text.TextUtils;

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
    private static final String GENERALINFO_URL1 = "https://czemi-t.benesse.ne.jp/generalinfo/v1/";
    private static final String GENERALINFO_URL2 = "https://czemi.benesse.ne.jp/generalinfo/v1/";
    public static final String LOGOUT_API = "logout";
    private static final String LOG_URL1 = "https://dsklg-t1.benesse.ne.jp/api";
    private static final String LOG_URL2 = "https://dsklg-p1.benesse.ne.jp/api";
    private static final String LOG_URL3 = "https://dsklg.benesse.ne.jp/api";
    private static final String MEMBER_URL1 = "https://membership-test.benesse.ne.jp/ap/m/contractInfQuery/latestShippedInfQuery/";
    private static final String MEMBER_URL2 = "https://membership.benesse.ne.jp/ap/m/contractInfQuery/latestShippedInfQuery/";
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
    private static final String TAG = UrlUtil.class.getSimpleName();
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
        String url = UrlCacheUtil.getInstance().getUrlBae();
        if (TextUtils.isEmpty(url)) {
            String result = BAE_URL6;
            String version = getBuildId();
            String uryType = getUrlType(version);
            if (uryType.equals(OS_TYPE_001)) {
                result = BAE_URL1;
            } else if (uryType.equals(OS_TYPE_002)) {
                result = BAE_URL2;
            } else if (uryType.equals(OS_TYPE_003)) {
                result = BAE_URL3;
            } else if (uryType.equals(OS_TYPE_004)) {
                result = BAE_URL4;
            } else if (uryType.equals(OS_TYPE_005)) {
                result = BAE_URL5;
            } else if (uryType.equals(OS_TYPE_006)) {
                result = BAE_URL6;
            }
            return result;
        }
        return url;
    }

    public static String getUrlLog() {
        String url = UrlCacheUtil.getInstance().getUrlDsklg();
        if (TextUtils.isEmpty(url)) {
            String result = LOG_URL3;
            String version = getBuildId();
            String uryType = getUrlType(version);
            if (uryType.equals(OS_TYPE_001) || uryType.equals(OS_TYPE_002) || uryType.equals(OS_TYPE_003) || uryType.equals(OS_TYPE_004)) {
                result = LOG_URL1;
            } else if (uryType.equals(OS_TYPE_005)) {
                result = LOG_URL2;
            } else if (uryType.equals(OS_TYPE_006)) {
                result = LOG_URL3;
            }
            return result;
        }
        return url;
    }

    public static String getUrlAkamai() {
        String url = UrlCacheUtil.getInstance().getUrlAkamai();
        if (!TextUtils.isEmpty(url)) {
            if (!url.endsWith("/")) {
                return url + "/";
            }
            return url;
        }
        String result = AKAMAI_URL6;
        String version = getBuildId();
        String uryType = getUrlType(version);
        if (uryType.equals(OS_TYPE_001)) {
            result = AKAMAI_URL1;
        } else if (uryType.equals(OS_TYPE_002)) {
            result = AKAMAI_URL2;
        } else if (uryType.equals(OS_TYPE_003)) {
            result = AKAMAI_URL3;
        } else if (uryType.equals(OS_TYPE_004)) {
            result = AKAMAI_URL4;
        } else if (uryType.equals(OS_TYPE_005)) {
            result = AKAMAI_URL5;
        } else if (uryType.equals(OS_TYPE_006)) {
            result = AKAMAI_URL6;
        }
        return result;
    }

    public static String getUrlBaeMember() {
        String url = UrlCacheUtil.getInstance().getUrlMemberIF053();
        if (TextUtils.isEmpty(url)) {
            String result = MEMBER_URL2;
            String version = getBuildId();
            String uryType = getUrlType(version);
            if (uryType.equals(OS_TYPE_001) || uryType.equals(OS_TYPE_002) || uryType.equals(OS_TYPE_003) || uryType.equals(OS_TYPE_004)) {
                result = MEMBER_URL1;
            } else if (uryType.equals(OS_TYPE_005) || uryType.equals(OS_TYPE_006)) {
                result = MEMBER_URL2;
            }
            return result;
        }
        return url;
    }

    public static String getUrlAuth() {
        String url = UrlCacheUtil.getInstance().getUrlLoginApi();
        if (!TextUtils.isEmpty(url)) {
            if (!url.endsWith("/")) {
                return url + "/";
            }
            return url;
        }
        String result = AUTH_URL2;
        String version = getBuildId();
        String uryType = getUrlType(version);
        if (uryType.equals(OS_TYPE_001) || uryType.equals(OS_TYPE_002) || uryType.equals(OS_TYPE_003) || uryType.equals(OS_TYPE_004)) {
            result = AUTH_URL1;
        } else if (uryType.equals(OS_TYPE_005) || uryType.equals(OS_TYPE_006)) {
            result = AUTH_URL2;
        }
        return result;
    }

    public static String getUrlSso() {
        String url = UrlCacheUtil.getInstance().getUrlLoginSso();
        if (TextUtils.isEmpty(url)) {
            String result = SSO_URL2;
            String version = getBuildId();
            String uryType = getUrlType(version);
            if (uryType.equals(OS_TYPE_001) || uryType.equals(OS_TYPE_002) || uryType.equals(OS_TYPE_003) || uryType.equals(OS_TYPE_004)) {
                result = SSO_URL1;
            } else if (uryType.equals(OS_TYPE_005) || uryType.equals(OS_TYPE_006)) {
                result = SSO_URL2;
            }
            return result;
        }
        return url;
    }

    public static String getUrlTime() {
        String url = UrlCacheUtil.getInstance().getUrlTime();
        if (TextUtils.isEmpty(url)) {
            String result = TIME_URL6;
            String version = getBuildId();
            String uryType = getUrlType(version);
            if (uryType.equals(OS_TYPE_001)) {
                result = TIME_URL1;
            } else if (uryType.equals(OS_TYPE_002)) {
                result = TIME_URL2;
            } else if (uryType.equals(OS_TYPE_003)) {
                result = TIME_URL3;
            } else if (uryType.equals(OS_TYPE_004)) {
                result = TIME_URL4;
            } else if (uryType.equals(OS_TYPE_005)) {
                result = TIME_URL5;
            } else if (uryType.equals(OS_TYPE_006)) {
                result = TIME_URL6;
            }
            return result;
        }
        return url;
    }

    protected static String getBuildId() {
        String version = Build.ID;
        return version;
    }

    protected static String getUrlType(String version) {
        if (TextUtils.isEmpty(version)) {
            return OS_TYPE_006;
        }
        String[] splitUrlType = version.split(VER_SPLIT);
        if (splitUrlType.length != 3) {
            return OS_TYPE_006;
        }
        String urlType = splitUrlType[2].replace('T', '0');
        return urlType;
    }

    public static String getUrlGeneralInfo() {
        String url = UrlCacheUtil.getInstance().getUrlGeneralInfo();
        if (TextUtils.isEmpty(url)) {
            String result = GENERALINFO_URL2;
            String version = getBuildId();
            String urlType = getUrlType(version);
            if (urlType.equals(OS_TYPE_001) || urlType.equals(OS_TYPE_002) || urlType.equals(OS_TYPE_003) || urlType.equals(OS_TYPE_004)) {
                result = GENERALINFO_URL1;
            } else if (urlType.equals(OS_TYPE_005) || urlType.equals(OS_TYPE_006)) {
                result = GENERALINFO_URL2;
            }
            return result;
        }
        return url;
    }
}
