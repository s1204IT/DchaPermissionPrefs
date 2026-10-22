package jp.co.benesse.touch.asynccomm;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.lang.reflect.Method;
import jp.co.benesse.touch.util.UrlCacheUtil;
import jp.co.benesse.touch.util.UrlUtil;

public class TouchApplication extends Application {
    private static final String TAG = "TouchApplication";
    private FirebaseAnalytics mFirebaseAnalytics;

    public void sendAnalyticsException(String str, boolean z) {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UrlCacheUtil.getInstance().loadUrl(this);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        boolean zIsEmpty = TextUtils.isEmpty(UrlUtil.getEnvironmentName());
        boolean zEquals = TextUtils.equals(getApplicationInfo().packageName, getDebugAnalyticsProperty());
        if (zIsEmpty || zEquals) {
            this.mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        }
    }

    public void sendAnalyticsScreenView(String str) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, str);
            this.mFirebaseAnalytics.logEvent("screen", bundle);
        } catch (Exception unused) {
        }
    }

    public void sendAnalyticsEvent(String str, String str2, String str3, Long l) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("category", str);
            bundle.putString("action", str2);
            bundle.putString("label", str3);
            if (l != null) {
                bundle.putLong("value", l.longValue());
            }
            this.mFirebaseAnalytics.logEvent(NotificationCompat.CATEGORY_EVENT, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDebugAnalyticsProperty() {
        try {
            Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class);
            declaredMethod.setAccessible(true);
            return declaredMethod.invoke(null, "debug.firebase.analytics.app").toString();
        } catch (ReflectiveOperationException | RuntimeException unused) {
            return null;
        }
    }
}
