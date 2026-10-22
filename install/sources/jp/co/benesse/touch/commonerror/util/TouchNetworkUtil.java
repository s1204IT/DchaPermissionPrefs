package jp.co.benesse.touch.commonerror.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TouchNetworkUtil {
    private static final String TAG = "TouchNetworkUtil";

    private TouchNetworkUtil() {
    }

    public static final boolean isConnective(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService("connectivity");
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }
}
