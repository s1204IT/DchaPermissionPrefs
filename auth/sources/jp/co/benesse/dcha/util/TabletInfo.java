package jp.co.benesse.dcha.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public class TabletInfo {
    private static final String PROVIDER_KEY_SERIAL_NO = "bc:serial_no";

    private TabletInfo() {
    }

    public static String getSerial(Context context, String def) {
        ContentResolver cr = context.getContentResolver();
        String serial = Settings.System.getString(cr, PROVIDER_KEY_SERIAL_NO);
        if (TextUtils.isEmpty(serial)) {
            return def;
        }
        return serial;
    }
}
