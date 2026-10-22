package jp.co.benesse.touch.common.anrsendservice;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import jp.co.benesse.touch.common.anrsendservice.util.AnalyticsChuzemiUtil;
import jp.co.benesse.touch.common.anrsendservice.util.AnalyticsShozemiUtil;
import jp.co.benesse.touch.common.anrsendservice.util.AnalyticsUtilBase;
import jp.co.benesse.touch.common.anrsendservice.util.Logger;

public class AnalyticsAdapter {
    public static final String GA_ACTION_CHUZEMI_ANR = "ANR_Chu";
    public static final String GA_ACTION_SHOZEMI_ANR = "ANR_Sho";
    public static final String GA_CATEGORY_CHUZEMI_ERROR = "Error";
    public static final String GA_CATEGORY_SHOZEMI_ERROR = "Error";
    private static final String TAG = AnalyticsAdapter.class.getSimpleName();
    private String mAction;
    private AnalyticsUtilBase mAnalyticsUtil;
    private String mCategory;

    public AnalyticsAdapter(Context context) {
        if (checkPackageName(context, AnalyticsShozemiUtil.PACKAGE_NAME)) {
            this.mAnalyticsUtil = new AnalyticsShozemiUtil();
            this.mCategory = "Error";
            this.mAction = GA_ACTION_SHOZEMI_ANR;
        }
        if (checkPackageName(context, AnalyticsChuzemiUtil.PACKAGE_NAME)) {
            this.mAnalyticsUtil = new AnalyticsChuzemiUtil();
            this.mCategory = "Error";
            this.mAction = GA_ACTION_CHUZEMI_ANR;
        }
    }

    protected boolean checkPackageName(Context context, String str) {
        Logger.d(TAG, "checkPackageName 001", "packageName:", str);
        PackageManager packageManager = context.getPackageManager();
        try {
            Logger.d(TAG, "checkPackageName 002");
            packageManager.getPackageInfo(str, 4);
            Logger.d(TAG, "checkPackageName 003");
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            Logger.d(TAG, "checkPackageName 004");
            return false;
        }
    }

    public void sendAnrLogFile(Context context, File file) {
        Logger.d(TAG, "sendLog 001", "path:", file);
        if (file.exists()) {
            AnrLogAccessor anrLogAccessor = new AnrLogAccessor();
            try {
                try {
                    anrLogAccessor.open(file);
                    boolean zSendAnrEvent = true;
                    while (zSendAnrEvent) {
                        String line = anrLogAccessor.readLine();
                        if (line == null) {
                            break;
                        } else {
                            zSendAnrEvent = sendAnrEvent(context, line);
                        }
                    }
                    if (zSendAnrEvent) {
                        Logger.d(TAG, "sendLog 003");
                        anrLogAccessor.clear();
                    }
                } catch (IOException e) {
                    Logger.d(TAG, "sendLog 004", e);
                }
                anrLogAccessor.close();
            } catch (Throwable th) {
                anrLogAccessor.close();
                throw th;
            }
        }
        Logger.d(TAG, "sendLog 005");
    }

    protected boolean sendAnrEvent(Context context, String str) {
        boolean zSendEvent;
        Logger.d(TAG, "sendEvent 001", "label:", str);
        if (TextUtils.isEmpty(str) || this.mAnalyticsUtil == null) {
            zSendEvent = false;
        } else {
            Logger.d(TAG, "sendEvent 002");
            zSendEvent = this.mAnalyticsUtil.sendEvent(context, this.mCategory, this.mAction, str);
        }
        Logger.d(TAG, "sendEvent 003 result", Boolean.valueOf(zSendEvent));
        return zSendEvent;
    }
}
