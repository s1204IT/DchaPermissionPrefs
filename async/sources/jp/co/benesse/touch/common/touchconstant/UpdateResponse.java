package jp.co.benesse.touch.common.touchconstant;

public final class UpdateResponse {
    public static final String EXTRA_ERROR_CODE = "EXTRA_ERROR_CODE";
    public static final String EXTRA_MODE = "EXTRA_MODE";
    public static final String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
    public static final String EXTRA_TYPE = "EXTRA_UPDATE_TYPE";
    public static final String EXTRA_UPDATABLE = "EXTRA_UPDATABLE";
    public static final int MODE_ERROR = -1;
    public static final int MODE_SD = 1;
    public static final int MODE_WIFI = 0;
    public static final int RESULT_CERTIFICATION_ERR = -2;
    public static final int RESULT_INTERNET_ERR = -3;
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_SYSTEM_ERR = -1;
    public static final int RESULT_WIFI_ERR = -4;
    public static final int TYPE_APP = 2;
    public static final int TYPE_NON = 4;
    public static final int TYPE_OS_MW = 1;
    public static final int TYPE_UPDATESERVICE = 0;
    public static final int UPDATABLE_EXIST = 1;
    public static final int UPDATABLE_NOT_EXIST = 0;

    private UpdateResponse() {
    }
}
