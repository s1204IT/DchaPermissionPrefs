package jp.co.benesse.touch.common.touchconstant;

public final class AuthResponse {
    public static final String RESPONSE_XML_KEY_ARRAY_INDEX = "aryIdx";
    public static final String RESPONSE_XML_KEY_ERROR_CD = "errCd";
    public static final String RESPONSE_XML_KEY_ERROR_MSG = "errMsg";
    public static final String RESPONSE_XML_KEY_FNC_DEC_NO = "fncDecNo";
    public static final String RESPONSE_XML_KEY_GNN_CD = "gnnCd";
    public static final String RESPONSE_XML_KEY_LINE = "lin";
    public static final String RESPONSE_XML_KEY_PERIOD_PRD_CTT_BEAN = "periodPrdCttBean";
    public static final String RESPONSE_XML_KEY_STATUS_CD = "sttCd";
    public static final String RESPONSE_XML_KEY_YAR = "yar";
    public static final String RESPONSE_XML_KEY_ZEMI_KBN = "zemiKbn";
    public static final String RES_AUTH_KEY_RESULT = "result";
    public static final String RES_AUTH_KEY_RESULT_CODE = "resultcode";
    public static final int RES_AUTH_RESULT_ERROR = -1;
    public static final int RES_AUTH_RESULT_NG = 1;
    public static final int RES_AUTH_RESULT_OK = 0;
    public static final int RES_AUTH_USER_DB_ACCESS_ERROR = 1021;
    public static final int RES_AUTH_USER_DIFFERENT_USER = -2;
    public static final int RES_AUTH_USER_EXPIRED_SESSION = -1;
    public static final int RES_AUTH_USER_FORMAT_ERROR = 1011;
    public static final int RES_AUTH_USER_GENERAL_ERROR = -3;
    public static final int RES_AUTH_USER_ID_PASSWORD_ILLEGAL = 1;
    public static final int RES_AUTH_USER_INPUT_PARAM_FORMAT_ERROR = 1013;
    public static final int RES_AUTH_USER_LOCK_USER = 2;
    public static final int RES_AUTH_USER_NOTHING_APP_ID = 1012;
    public static final int RES_AUTH_USER_OTHER_ERROR = 1100;
    public static final int RES_AUTH_USER_SUCCESS = 0;
    public static final int RES_GET_AKAMAI_TOKEN_ERROR = 2;
    public static final int RES_GET_AKAMAI_TOKEN_MAINTENANCE = 3;
    public static final int RES_GET_AKAMAI_TOKEN_OTHER = 4;
    public static final int RES_GET_AKAMAI_TOKEN_SUCCESS = 1;
    public static final String RES_GET_ONE_TIME_KEY_PREFIX_COMM_ERR = "err:COMM:";
    public static final String RES_GET_ONE_TIME_KEY_PREFIX_ERR = "err:";
    public static final String RES_GET_ONE_TIME_KEY_PREFIX_OK = "otk:";

    private AuthResponse() {
    }
}
