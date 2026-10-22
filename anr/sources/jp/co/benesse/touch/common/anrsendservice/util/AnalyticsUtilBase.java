package jp.co.benesse.touch.common.anrsendservice.util;

import android.content.Context;
import android.content.Intent;

public abstract class AnalyticsUtilBase {
    private static final String GA_EXTRA_EVENT_ACTION = "GA_EVENT_ACTION";
    private static final String GA_EXTRA_EVENT_CATEGORY = "GA_EVENT_CATEGORY";
    private static final String GA_EXTRA_EVENT_LABEL = "GA_EVENT_LABEL";
    private static final String GA_EXTRA_TRACKING_TYPE = "GA_TRACK_TYPE";
    private static final int GA_TRACKING_TYPE_EVENT = 1;
    private static final String TAG = AnalyticsUtilBase.class.getSimpleName();

    protected abstract String getClassName();

    protected abstract String getPackageName();

    public boolean sendEvent(Context context, String str, String str2, String str3) {
        try {
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), getClassName());
            intent.putExtra(GA_EXTRA_TRACKING_TYPE, 1);
            intent.putExtra(GA_EXTRA_EVENT_CATEGORY, str);
            intent.putExtra(GA_EXTRA_EVENT_ACTION, str2);
            intent.putExtra(GA_EXTRA_EVENT_LABEL, str3);
            return context.startService(intent) != null;
        } catch (Exception e) {
            Logger.e(TAG, e);
            return false;
        }
    }
}
