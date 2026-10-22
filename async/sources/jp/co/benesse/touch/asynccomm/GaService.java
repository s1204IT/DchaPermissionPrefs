package jp.co.benesse.touch.asynccomm;

import android.app.Application;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import jp.co.benesse.touch.auth.IAuthService;
import jp.co.benesse.touch.common.touchconstant.IntentName;

public class GaService extends IntentService {
    private static final String GA_EVENT_ACTION = "GA_EVENT_ACTION";
    private static final String GA_EVENT_CATEGORY = "GA_EVENT_CATEGORY";
    private static final String GA_EVENT_LABEL = "GA_EVENT_LABEL";
    private static final String GA_EVENT_VALUE = "GA_EVENT_VALUE";
    private static final String GA_EXCEPTIO_DESCRIPTION = "GA_EXCEPTIO_DESCRIPTION";
    private static final String GA_EXCEPTIO_FATAL = "GA_EXCEPTIO_FATAL";
    private static final String GA_SCREEN_NAME = "GA_SCREEN_NAME";
    private static final String GA_TRACK_TYPE = "GA_TRACK_TYPE";
    private static final int GA_TRACK_TYPE_EVENT = 1;
    private static final int GA_TRACK_TYPE_EXCEPTION = 2;
    private static final int GA_TRACK_TYPE_SCREEN = 0;
    private static final String REPLACE_STRING_LINE = "{line}";
    private static final int SERVICE_BINDED_CHECK_COUNT = 50;
    private static final int SERVICE_BINDED_CHECK_MS = 100;
    private static final String TAG = "GaService";
    private IAuthService authService;
    private ServiceConnection authServiceConnection;

    private class AuthServiceConnection implements ServiceConnection {
        private static final String TAG_AUTH_SERVICE_CONNECTION = "AuthServiceConnection";

        private AuthServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            GaService.this.authService = IAuthService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            GaService.this.authService = null;
        }
    }

    public GaService() {
        super(GaService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.authServiceConnection = new AuthServiceConnection();
        Intent intent = new Intent(IntentName.ACTION_AUTH_SERVICE);
        intent.setPackage(IntentName.PKG_AUTH_SERVICE);
        bindService(intent, this.authServiceConnection, 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ServiceConnection serviceConnection = this.authServiceConnection;
        if (serviceConnection != null) {
            if (this.authService != null) {
                unbindService(serviceConnection);
                this.authService = null;
            }
            this.authServiceConnection = null;
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Application application = getApplication();
        int intExtra = intent.getIntExtra(GA_TRACK_TYPE, -1);
        try {
            if (intExtra == 0) {
                String stringExtra = intent.getStringExtra(GA_SCREEN_NAME);
                if (!TextUtils.isEmpty(stringExtra) && stringExtra.contains(REPLACE_STRING_LINE)) {
                    stringExtra = stringExtra.replace(REPLACE_STRING_LINE, getAuthService().getStringValue("line"));
                }
                if (application instanceof TouchApplication) {
                    ((TouchApplication) application).sendAnalyticsScreenView(stringExtra);
                    return;
                }
                return;
            }
            if (intExtra == 1) {
                String stringExtra2 = intent.getStringExtra(GA_EVENT_CATEGORY);
                String stringExtra3 = intent.getStringExtra(GA_EVENT_ACTION);
                String stringExtra4 = intent.getStringExtra(GA_EVENT_LABEL);
                if (!TextUtils.isEmpty(stringExtra4) && stringExtra4.contains(REPLACE_STRING_LINE)) {
                    stringExtra4 = stringExtra4.replace(REPLACE_STRING_LINE, getAuthService().getStringValue("line"));
                }
                Long lValueOf = intent.hasExtra(GA_EVENT_VALUE) ? Long.valueOf(intent.getIntExtra(GA_EVENT_VALUE, -1)) : null;
                if (application instanceof TouchApplication) {
                    ((TouchApplication) application).sendAnalyticsEvent(stringExtra2, stringExtra3, stringExtra4, lValueOf);
                    return;
                }
                return;
            }
            if (intExtra == 2) {
                String stringExtra5 = intent.getStringExtra(GA_EXCEPTIO_DESCRIPTION);
                boolean booleanExtra = intent.getBooleanExtra(GA_EXCEPTIO_FATAL, false);
                if (TextUtils.isEmpty(stringExtra5)) {
                    return;
                }
                int length = stringExtra5.length();
                if (length >= SERVICE_BINDED_CHECK_MS) {
                    length = SERVICE_BINDED_CHECK_MS;
                }
                String strSubstring = TextUtils.substring(stringExtra5, 0, length);
                if (application instanceof TouchApplication) {
                    ((TouchApplication) application).sendAnalyticsException(strSubstring, booleanExtra);
                }
            }
        } catch (Exception unused) {
        }
    }

    protected IAuthService getAuthService() {
        if (this.authService == null) {
            this.authServiceConnection = new AuthServiceConnection();
            Intent intent = new Intent(IntentName.ACTION_AUTH_SERVICE);
            intent.setPackage(IntentName.PKG_AUTH_SERVICE);
            bindService(intent, this.authServiceConnection, 1);
            for (int i = 0; i < SERVICE_BINDED_CHECK_COUNT && this.authService == null; i++) {
                try {
                    new CountDownLatch(1).await(100L, TimeUnit.MILLISECONDS);
                } catch (InterruptedException unused) {
                }
            }
        }
        return this.authService;
    }
}
