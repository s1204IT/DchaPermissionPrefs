package jp.co.benesse.touch.auth;

import android.os.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SessionKeyGetThread implements Runnable {
    private static final String TAG = "SessionKeyGetThread";
    IAuthCallback callback;
    boolean secure;
    AuthService service;
    Lock waitUpdateLock = new ReentrantLock();
    Condition waitUpdateCond = this.waitUpdateLock.newCondition();
    boolean waitUpdateFlag = false;

    public SessionKeyGetThread(AuthService authService, boolean secure, IAuthCallback callback) {
        this.service = authService;
        this.secure = secure;
        this.callback = callback;
    }

    @Override
    public void run() {
        SessionUpdateMonitor updateMonitor = SessionUpdateMonitor.getInstance();
        if (updateMonitor.isUpdating()) {
            updateMonitor.registerUpdateListener(new SessionUpdateListener() {
                @Override
                public void notifyUpdate() {
                    SessionKeyGetThread.this.waitUpdateLock.lock();
                    SessionKeyGetThread.this.waitUpdateFlag = true;
                    SessionKeyGetThread.this.waitUpdateCond.signal();
                    SessionKeyGetThread.this.waitUpdateLock.unlock();
                }
            });
            this.waitUpdateLock.lock();
            long timeout = 100000000000L;
            while (!this.waitUpdateFlag) {
                try {
                    long l = this.waitUpdateCond.awaitNanos(timeout);
                    if (l <= 0 || this.waitUpdateFlag) {
                        break;
                    } else {
                        timeout = l;
                    }
                } catch (InterruptedException e) {
                    this.waitUpdateLock.unlock();
                } catch (Throwable th) {
                    this.waitUpdateLock.unlock();
                    throw th;
                }
            }
            this.waitUpdateFlag = false;
            this.waitUpdateLock.unlock();
        }
        String sessionKey = getSessionKey(this.secure);
        try {
            if (this.callback != null) {
                this.callback.setStringValue(sessionKey);
                this.callback.onResult();
            }
        } catch (RemoteException e2) {
        }
    }

    protected String getSessionKey(boolean secure) {
        String sessionKey = null;
        try {
            JSONArray sessions = SboxAdapter.getInstance().getArrayValues(AuthRequest.SBOX_SESSION);
            if (sessions == null) {
                break;
            }
            for (int i = 0; i < sessions.length(); i++) {
                JSONObject data = sessions.getJSONObject(i);
                if (!data.has(AuthService.SECURE) || !data.has(AuthService.NAME) || !data.has("id")) {
                    break;
                    break;
                    break;
                }
                if (data.getString(AuthService.SECURE).equals(AuthService.SECURE_TRUE) && secure) {
                    sessionKey = data.getString("id");
                    break;
                }
                if (data.getString(AuthService.SECURE).equals(AuthService.SECURE_FALSE) && !secure) {
                    sessionKey = data.getString("id");
                    break;
                }
            }
            return sessionKey;
        } catch (JSONException e) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
