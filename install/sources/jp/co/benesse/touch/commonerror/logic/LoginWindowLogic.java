package jp.co.benesse.touch.commonerror.logic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import jp.co.benesse.touch.auth.IAuthService;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import jp.co.benesse.touch.commonerror.R;
import jp.co.benesse.touch.commonerror.activity.LoginWindowActivity;

public class LoginWindowLogic {
    private static final String TAG = "LoginWindowLogic";
    private ServiceConnection authServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LoginWindowLogic.this.isServiceBind = true;
            LoginWindowLogic.this.touchAuthService = IAuthService.Stub.asInterface(service);
            String userId = null;
            try {
                userId = LoginWindowLogic.this.touchAuthService.getStringValue(AuthRequest.SBOX_USER_ID);
            } catch (RemoteException e) {
            }
            if (TextUtils.isEmpty(userId)) {
                LoginWindowLogic.this.loginWindowActivity.getmEditTextPassword().setBackgroundResource(R.drawable.common_flame);
                LoginWindowLogic.this.loginWindowActivity.getmEditTextId().setBackgroundResource(R.drawable.js002_04010_flame_current);
                LoginWindowLogic.this.loginWindowActivity.getmEditTextId().setTextColor(-16777216);
                LoginWindowLogic.this.loginWindowActivity.getmEditTextId().setFocusable(true);
                LoginWindowLogic.this.loginWindowActivity.getmEditTextId().setFocusableInTouchMode(true);
                LoginWindowLogic.this.loginWindowActivity.getmEditTextId().requestFocus();
                return;
            }
            LoginWindowLogic.this.loginWindowActivity.getmEditTextId().setText(userId);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LoginWindowLogic.this.isServiceBind = false;
            LoginWindowLogic.this.touchAuthService = null;
        }
    };
    private boolean isServiceBind;
    private LoginWindowActivity loginWindowActivity;
    private IAuthService touchAuthService;

    public LoginWindowLogic(LoginWindowActivity loginWindowActivity) {
        this.loginWindowActivity = loginWindowActivity;
    }

    public void authbindService() {
        Intent authIntent = new Intent();
        authIntent.setAction(IntentName.ACTION_AUTH_SERVICE);
        authIntent.setPackage(IntentName.PKG_AUTH_SERVICE);
        this.loginWindowActivity.bindService(authIntent, this.authServiceConnection, 1);
    }

    public void loginCertification(String usrId, String password) throws RemoteException {
        this.touchAuthService.authUser(usrId, password);
    }

    public boolean hasChuBrowser() {
        PackageManager pm = this.loginWindowActivity.getPackageManager();
        try {
            pm.getPackageInfo(IntentName.PKG_CHU_BROWSER, 128);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void onDestroy() {
        if (this.isServiceBind) {
            this.loginWindowActivity.unbindService(this.authServiceConnection);
        }
        this.touchAuthService = null;
        this.authServiceConnection = null;
        this.loginWindowActivity = null;
    }
}
