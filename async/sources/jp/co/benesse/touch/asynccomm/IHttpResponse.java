package jp.co.benesse.touch.asynccomm;

public interface IHttpResponse {
    public static final int RESULT_ERROR_CONNECT_SERVER = 2004;
    public static final int RESULT_ERROR_CONNECT_SESSION = 2002;
    public static final int RESULT_ERROR_CONNECT_SYSTEM = 2001;
    public static final int RESULT_ERROR_INPUT_CHECK_SYSTEM = 1002;
    public static final int RESULT_ERROR_INPUT_CHECK_VALIDATE = 1003;
    public static final int RESULT_ERROR_RESPONSE_UNEXPECTED = 3003;
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_TIME_OUT = 1001;
    public static final int RESULT_ERROR_CONNECT_CLIENT = 2003;
    public static final int RESULT_ERROR_RESPONSE_VALIDATE = 3001;
    public static final int RESULT_ERROR_RESPONSE_USER_KEY = 3002;
    public static final Integer[] NO_RETRY_LIST = {0, 1003, Integer.valueOf(RESULT_ERROR_CONNECT_CLIENT), Integer.valueOf(RESULT_ERROR_RESPONSE_VALIDATE), Integer.valueOf(RESULT_ERROR_RESPONSE_USER_KEY)};

    String getResponse();

    int getStatusCode();

    void onRespose(int i);

    void setResponse(String str);

    void setStatusCode(int i);
}
