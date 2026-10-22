package jp.co.benesse.touch.auth.application;

import android.app.Application;
import jp.co.benesse.touch.auth.SboxAdapter;
import jp.co.benesse.touch.util.UrlCacheUtil;

public class TouchApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UrlCacheUtil.getInstance().loadUrl(this);
        SboxAdapter.createInstance(getApplicationContext());
    }
}
