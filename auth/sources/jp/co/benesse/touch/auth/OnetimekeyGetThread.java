package jp.co.benesse.touch.auth;

import android.os.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.common.touchconstant.AuthResponse;

public class OnetimekeyGetThread implements Runnable {
    public static final String TAG = "OnetimekeyThread";
    private AuthService authService;
    private IAuthCallback callback;
    String sessionkey;
    final Lock responseWaitlock = new ReentrantLock();
    final Condition getSessionKeyResponseWaitCond = this.responseWaitlock.newCondition();
    final Condition getOnetimeKeyResponseWaitCond = this.responseWaitlock.newCondition();
    OnetimekeyWaitingCheckListener waitResponseFlagListener = new OnetimekeyWaitingCheckListener(false);

    public OnetimekeyGetThread(AuthService dchaAuthService, IAuthCallback callback) {
        this.authService = dchaAuthService;
        this.callback = callback;
    }

    @Override
    public void run() {
        String result;
        try {
            this.responseWaitlock.lock();
            this.authService.getSessionkey(true, new IAuthCallback.Stub() {
                @Override
                public void setStringValue(String value) throws RemoteException {
                    OnetimekeyGetThread.this.sessionkey = value;
                }

                @Override
                public void onResult() throws RemoteException {
                    OnetimekeyGetThread.this.responseWaitlock.lock();
                    OnetimekeyGetThread.this.waitResponseFlagListener.setFlag(true);
                    OnetimekeyGetThread.this.getSessionKeyResponseWaitCond.signal();
                    OnetimekeyGetThread.this.responseWaitlock.unlock();
                }
            });
            long timeout = 100000000000L;
            while (!this.waitResponseFlagListener.getFlag()) {
                long l = this.getSessionKeyResponseWaitCond.awaitNanos(timeout);
                if (l <= 0 || this.waitResponseFlagListener.getFlag()) {
                    break;
                } else {
                    timeout = l;
                }
            }
            String authKey = this.authService.getAuthKey();
            this.waitResponseFlagListener.setFlag(false);
            String appId = SboxAdapter.getAppIdentifier();
            Map<String, String> postParams = new HashMap<>();
            postParams.put(AuthService.SESSIONID, this.sessionkey);
            postParams.put(AuthService.AUTHID, authKey);
            postParams.put(AuthService.APP_ID, appId);
            OnetimekeyRequest ssoReq = new OnetimekeyRequest();
            ssoReq.setPostParams(postParams);
            OnetimekeyResponse ssoRes = new OnetimekeyResponse(this.authService, this.responseWaitlock, this.getOnetimeKeyResponseWaitCond, this.waitResponseFlagListener);
            this.authService.getCommService().requestComm2AuthServer(ssoReq, ssoRes);
            long timeout2 = 100000000000L;
            while (!this.waitResponseFlagListener.getFlag()) {
                long l2 = this.getOnetimeKeyResponseWaitCond.awaitNanos(timeout2);
                if (l2 <= 0 || this.waitResponseFlagListener.getFlag()) {
                    break;
                } else {
                    timeout2 = l2;
                }
            }
            this.waitResponseFlagListener.setFlag(false);
            String onetimekey = ssoRes.oneTimeKey;
            if (onetimekey == null) {
                if (ssoRes.result == null) {
                    result = AuthResponse.RES_GET_ONE_TIME_KEY_PREFIX_COMM_ERR;
                } else if (ssoRes.result.equals(AuthService.RESULT_NG) || ssoRes.result.equals(AuthService.RESULT_ERROR)) {
                    result = "err:JSON:" + ssoRes.result + ":" + ssoRes.resultCode;
                } else {
                    result = "err:SYS:";
                }
            } else {
                result = AuthResponse.RES_GET_ONE_TIME_KEY_PREFIX_OK + onetimekey;
            }
            this.responseWaitlock.unlock();
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(result);
                    this.callback.onResult();
                }
            } catch (RemoteException e) {
            }
            this.authService.finishRequest();
        } catch (Exception e2) {
            this.responseWaitlock.unlock();
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(null);
                    this.callback.onResult();
                }
            } catch (RemoteException e3) {
            }
            this.authService.finishRequest();
        } catch (Throwable th) {
            this.responseWaitlock.unlock();
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(null);
                    this.callback.onResult();
                }
            } catch (RemoteException e4) {
            }
            this.authService.finishRequest();
            throw th;
        }
    }

    class OnetimekeyWaitingCheckListener {
        boolean flag;

        public OnetimekeyWaitingCheckListener(boolean b) {
            this.flag = false;
            this.flag = b;
        }

        public boolean getFlag() {
            return this.flag;
        }

        public void setFlag(boolean b) {
            this.flag = b;
        }
    }
}
