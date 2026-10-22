package jp.co.benesse.touch.asynccomm;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import jp.co.benesse.touch.auth.IAuthService;
import jp.co.benesse.touch.common.touchconstant.IntentName;

public class AsyncCommService extends IntentService {
    private static final int SERVICE_BINDED_CHECK_COUNT = 50;
    private static final int SERVICE_BINDED_CHECK_MS = 100;
    private static final String TAG = "AsyncCommService";
    private IAuthService authService;
    private ServiceConnection authServiceConnection;

    private class AuthServiceConnection implements ServiceConnection {
        private static final String TAG_AUTH_SERVICE_CONNECTION = "AuthServiceConnection";

        private AuthServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AsyncCommService.this.authService = IAuthService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            AsyncCommService.this.authService = null;
        }
    }

    public AsyncCommService() {
        super(TAG);
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
    protected void onHandleIntent(Intent intent) {
        AsyncCommLogic asyncCommLogic = new AsyncCommLogic(this);
        asyncCommLogic.createSendRequest(intent);
        asyncCommLogic.executeSendRequest();
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
}
