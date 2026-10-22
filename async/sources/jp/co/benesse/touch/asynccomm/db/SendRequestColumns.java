package jp.co.benesse.touch.asynccomm.db;

import android.provider.BaseColumns;

public class SendRequestColumns implements BaseColumns {
    public static final String REQUEST = "Request";
    public static final String TABLE = "T_SendReq";
    public static final String ID = "Id";
    public static final String URL = "Url";
    public static final String API = "Api";
    public static final String REPLACE_FLAG = "Replace_flag";
    public static final String[] PROJECTION_SELECT_SEND_REQUEST = {ID, URL, API, "Request", REPLACE_FLAG};

    private SendRequestColumns() {
    }
}
