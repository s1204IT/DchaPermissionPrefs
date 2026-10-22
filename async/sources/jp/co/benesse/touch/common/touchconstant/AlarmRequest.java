package jp.co.benesse.touch.common.touchconstant;

public final class AlarmRequest {
    public static final int IDX_ENABLED_EVENT = 1;
    public static final int IDX_ENABLED_EXAM = 2;
    public static final int IDX_ENABLED_LEARNING = 0;
    public static final String KEY_ALARM_URL = "ALARM_URL";
    public static final String KEY_REQ_DATA = "REQ_DATA";
    public static final String KEY_REQ_SBT = "REQ_SBT";
    public static final int REQ_SBT_REGISTER_EVENT_ALARM = 1;
    public static final int REQ_SBT_REGISTER_EXAM_ALARM = 6;
    public static final int REQ_SBT_REGISTER_NEXT_WEEK_ALARM = 5;
    public static final int REQ_SBT_REGISTER_STUDY_ALARM = 0;
    public static final int REQ_SBT_REREGISTER_ALARM = 4;
    public static final int REQ_SBT_SET_ENABLED = 2;
    public static final int REQ_SBT_SET_NICKNAME = 3;

    private AlarmRequest() {
    }
}
