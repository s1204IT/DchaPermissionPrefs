package jp.co.benesse.touch.common.touchconstant;

public final class IntentName {
    public static final String ACTION_ASYNCCOMM_SERVICE = "jp.co.benesse.touch.asynccomm.AsyncCommService";
    public static final String ACTION_AUTH_SERVICE = "jp.co.benesse.touch.auth.AuthService";
    public static final String ACTION_AUTH_USER_RESULT_RESPONSE = "jp.co.benesse.touch.auth.AuthKeyResponse.intent.action";
    public static final String ACTION_BOOT_ALARM = "jp.co.benesse.touch.alarm.StartAlarm";
    public static final String ACTION_BOOT_ALARM_NOTIFY = "jp.co.benesse.touch.alarm.BootAlarmNotify";
    public static final String ACTION_COMM_SERVICE = "jp.co.benesse.touch.comm.CommService";
    public static final String ACTION_COMM_SERVICE_RESPONSE = "jp.co.benesse.touch.comm.intent.action.COMM";
    public static final String ACTION_DCHA_SERVICE = "jp.co.benesse.dcha.dchaservice.DchaService";
    public static final String ACTION_UPDATE_LOG_SERVICE = "jp.co.benesse.touch.asynccomm.UpdateLogPostService";
    public static final String ACTION_UPDATE_SERVICE_REQ_UPDATE_CHECK = "jp.co.benesse.touch.update.check";
    public static final String ACTION_UPDATE_SERVICE_RES_UPDATE_CHECK = "jp.co.benesse.touch.update.check.response";
    public static final String ACTION_UPDATE_SESSION_RESULT_RESPONSE = "jp.co.benesse.touch.auth.SessionUpdateResponse.intent.action";
    public static final String CATEGORY_AUTH_RESULT_RESPONSE = "jp.co.benesse.touch.auth.intent.category.RESULT";
    public static final String CATEGORY_COMM_SERVICE_RESULT_RESPONSE = "jp.co.benesse.touch.comm.intent.category.RESULT";
    public static final String CHU_BROWSER_CLASS_BASE = "jp.co.benesse.touch.chubrowser.BaseActivity";
    public static final String CLASS_ALARM = "jp.co.benesse.touch.alarm.activity.AlarmActivity";
    public static final String CLASS_ALARM_REGISTER_SERVICE = "jp.co.benesse.touch.alarm.service.RegisterService";
    public static final String CLASS_BASE = "jp.co.benesse.touch.browser.BaseActivity";
    public static final String CLASS_HEALTH_CHECK = "jp.co.benesse.dcha.allgrade.usersetting.activity.HealthCheckActivity";
    public static final String CLASS_ODEKAKE = "jp.co.benesse.touch.odekake.BrowserActivity";
    public static final String CLASS_USER_SETTING = "jp.co.benesse.dcha.allgrade.usersetting.activity.SettingMenuActivity";
    public static final String EXTRA_ASYNCCOMM_API = "api";
    public static final String EXTRA_ASYNCCOMM_REPLACE_FLAG = "replaceFlag";
    public static final String EXTRA_ASYNCCOMM_REQUEST = "request";
    public static final String EXTRA_ASYNCCOMM_UPDATE_LOG = "updateLog";
    public static final String EXTRA_ASYNCCOMM_URL = "url";
    public static final String EXTRA_URL = "EXTRA_URL";
    public static final int FLG_RCVR_START_HOME = 270532608;
    public static final int FLG_RESTART_CHUZEMI_BROWSER = 872415232;
    public static final int FLG_START_ALARM = 268468224;
    public static final int FLG_START_BROWSER = 268468224;
    public static final int FLG_START_CHUZEMI_BROWSER = 335544320;
    public static final int FLG_START_ERR = 268468224;
    public static final int FLG_START_HOME = 270565376;
    public static final int FLG_START_UPD = 270565376;
    public static final String PKG_ALARM = "jp.co.benesse.touch.alarm";
    public static final String PKG_ASYNCCOMM_SERVICE = "jp.co.benesse.touch.asynccomm";
    public static final String PKG_AUTH_SERVICE = "jp.co.benesse.touch.auth";
    public static final String PKG_BROWSER = "jp.co.benesse.touch.browser";
    public static final String PKG_CALIBRATION = "jp.co.benesse.touch.calibration";
    public static final String PKG_CHU_BROWSER = "jp.co.benesse.touch.chubrowser";
    public static final String PKG_COMMON = "jp.co.benesse.touch";
    public static final String PKG_COMMON_DCHA = "jp.co.benesse.dcha";
    public static final String PKG_COMMON_WARNING = "jp.co.benesse.touch.commonwarning";
    public static final String PKG_COMM_SERVICE = "jp.co.benesse.touch.comm";
    public static final String PKG_DCHA_SERVICE = "jp.co.benesse.dcha.dchaservice";
    public static final String PKG_HOME = "jp.co.benesse.touch.home";
    public static final String PKG_ODEKAKE = "jp.co.benesse.touch.odekake";
    public static final String PKG_UPDATE_SERVICE = "jp.co.benesse.touch.update";
    public static final String PKG_UPDATE_SERVICE_INSTALL = "jp.co.benesse.touch.update.install";
    public static final String PKG_USER_SETTING = "jp.co.benesse.dcha.allgrade.usersetting";
    public static final String PKG_UTIL_SERVICE = "jp.co.benesse.dcha.dchautilservice";

    private IntentName() {
    }
}
