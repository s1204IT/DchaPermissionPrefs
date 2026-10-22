package jp.co.benesse.touch.asynccomm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public final class NetStateUtil {
    private static final int NO_SIGNAL = 0;
    private static final int SIGNAL_LEVEL_NUM = 5;
    private static final String TAG = "NetStateUtil";

    private NetStateUtil() {
    }

    private static boolean isNetworkConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1;
    }

    private static int getWifiRssiLevel(Context context) {
        WifiInfo connectionInfo;
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        if (!wifiManager.isWifiEnabled() || (connectionInfo = wifiManager.getConnectionInfo()) == null) {
            return 0;
        }
        return WifiManager.calculateSignalLevel(connectionInfo.getRssi(), 5);
    }

    public static boolean checkNetState(Context context) {
        return isNetworkConnected(context) && getWifiRssiLevel(context) > 0;
    }
}
