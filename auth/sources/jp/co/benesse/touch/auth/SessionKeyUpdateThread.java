package jp.co.benesse.touch.auth;

import android.annotation.SuppressLint;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.comm.ICommService;

public class SessionKeyUpdateThread implements Runnable {
    private static final String TAG = "SessionKeyUpdateThread";
    IAuthCallback callback;
    boolean secure;
    AuthService service;
    String sessionkey;
    static final Lock otherThreadUpdateWaitLock = new ReentrantLock();
    static final Condition otherThreadUpdateWaitCond = otherThreadUpdateWaitLock.newCondition();
    static boolean otherThreadUpdateWaitUnlockFlag = false;
    static boolean otherThreadUpdateWaitLockingFlag = false;
    static Object getKeyMonitor = new Object();
    Lock waitUpdateLock = new ReentrantLock();
    Condition waitUpdateCond = this.waitUpdateLock.newCondition();
    UpdateWaintingCheckListener waitUpdateFlagListener = new UpdateWaintingCheckListener(false);

    public SessionKeyUpdateThread(AuthService authService, boolean secure, IAuthCallback callback) {
        this.service = authService;
        this.callback = callback;
        this.secure = secure;
    }

    @Override
    @SuppressLint({"NewApi"})
    public void run() {
        String authKey;
        SessionUpdateMonitor monitor = SessionUpdateMonitor.getInstance();
        boolean isUpdating = false;
        try {
            synchronized (getKeyMonitor) {
                isUpdating = monitor.isUpdating();
                this.waitUpdateLock.lock();
                this.service.getSessionkey(true, new IAuthCallback.Stub() {
                    @Override
                    public void setStringValue(String value) throws RemoteException {
                        SessionKeyUpdateThread.this.sessionkey = value;
                    }

                    @Override
                    public void onResult() throws RemoteException {
                        SessionKeyUpdateThread.this.waitUpdateLock.lock();
                        SessionKeyUpdateThread.this.waitUpdateFlagListener.setFlag(true);
                        SessionKeyUpdateThread.this.waitUpdateCond.signal();
                        SessionKeyUpdateThread.this.waitUpdateLock.unlock();
                    }
                });
                long timeout = 100000000000L;
                while (!this.waitUpdateFlagListener.getFlag()) {
                    long l = this.waitUpdateCond.awaitNanos(timeout);
                    if (l <= 0 || this.waitUpdateFlagListener.getFlag()) {
                        break;
                    } else {
                        timeout = l;
                    }
                }
                authKey = this.service.getAuthKey();
                this.waitUpdateFlagListener.setFlag(false);
                if (!isUpdating) {
                    monitor.setUpdatingStatus(true);
                    otherThreadUpdateWaitLockingFlag = true;
                }
            }
            if (!isUpdating) {
                Map<String, String> postParams = new HashMap<>();
                postParams.put(AuthService.SESSIONID, this.sessionkey);
                postParams.put(AuthService.AUTHID, authKey);
                postParams.put(AuthService.APP_ID, SboxAdapter.getAppIdentifier());
                SessionUpdateRequest sessionReq = new SessionUpdateRequest();
                sessionReq.setPostParams(postParams);
                SessionUpdateResponse sessionRes = new SessionUpdateResponse(this.service, this.secure, this.waitUpdateLock, this.waitUpdateCond, this.waitUpdateFlagListener);
                if (TextUtils.isEmpty(this.sessionkey) || TextUtils.isEmpty(authKey)) {
                    sessionRes.onResult(1, -1);
                    try {
                        otherThreadUpdateWaitLock.unlock();
                    } catch (Exception e) {
                    }
                    this.waitUpdateLock.unlock();
                    if (!isUpdating) {
                        monitor.setUpdatingStatus(false);
                        monitor.notifyUpdateAll();
                    }
                    try {
                        if (this.callback != null) {
                            this.callback.setStringValue(this.sessionkey);
                            this.callback.onResult();
                        }
                    } catch (RemoteException e2) {
                    }
                    otherThreadUpdateWaitUnlockFlag = false;
                    this.service.finishRequest();
                    return;
                }
                ICommService commservice = this.service.getCommService();
                if (commservice == null) {
                    throw new RemoteException("commService is null.");
                }
                commservice.requestComm2AuthServer(sessionReq, sessionRes);
                long timeout2 = 100000000000L;
                while (!this.waitUpdateFlagListener.getFlag()) {
                    long l2 = this.waitUpdateCond.awaitNanos(timeout2);
                    if (l2 <= 0 || this.waitUpdateFlagListener.getFlag()) {
                        break;
                    } else {
                        timeout2 = l2;
                    }
                }
                this.waitUpdateFlagListener.setFlag(false);
                this.sessionkey = sessionRes.sessionkey;
                otherThreadUpdateWaitLock.lock();
                otherThreadUpdateWaitUnlockFlag = true;
                otherThreadUpdateWaitCond.signalAll();
                otherThreadUpdateWaitLockingFlag = false;
            } else if (otherThreadUpdateWaitLockingFlag) {
                otherThreadUpdateWaitLock.lock();
                long timeout3 = 100000000000L;
                while (!otherThreadUpdateWaitUnlockFlag) {
                    long l3 = otherThreadUpdateWaitCond.awaitNanos(timeout3);
                    if (l3 <= 0 || otherThreadUpdateWaitUnlockFlag) {
                        break;
                    } else {
                        timeout3 = l3;
                    }
                }
                otherThreadUpdateWaitUnlockFlag = false;
            }
            try {
                otherThreadUpdateWaitLock.unlock();
            } catch (Exception e3) {
            }
            this.waitUpdateLock.unlock();
            if (!isUpdating) {
                monitor.setUpdatingStatus(false);
                monitor.notifyUpdateAll();
            }
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(this.sessionkey);
                    this.callback.onResult();
                }
            } catch (RemoteException e4) {
            }
            otherThreadUpdateWaitUnlockFlag = false;
            this.service.finishRequest();
        } catch (RemoteException e5) {
            try {
                otherThreadUpdateWaitLock.unlock();
            } catch (Exception e6) {
            }
            this.waitUpdateLock.unlock();
            if (!isUpdating) {
                monitor.setUpdatingStatus(false);
                monitor.notifyUpdateAll();
            }
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(this.sessionkey);
                    this.callback.onResult();
                }
            } catch (RemoteException e7) {
            }
            otherThreadUpdateWaitUnlockFlag = false;
            this.service.finishRequest();
        } catch (Exception e8) {
            try {
                otherThreadUpdateWaitLock.unlock();
            } catch (Exception e9) {
            }
            this.waitUpdateLock.unlock();
            if (!isUpdating) {
                monitor.setUpdatingStatus(false);
                monitor.notifyUpdateAll();
            }
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(this.sessionkey);
                    this.callback.onResult();
                }
            } catch (RemoteException e10) {
            }
            otherThreadUpdateWaitUnlockFlag = false;
            this.service.finishRequest();
        } catch (Throwable th) {
            try {
                otherThreadUpdateWaitLock.unlock();
            } catch (Exception e11) {
            }
            this.waitUpdateLock.unlock();
            if (!isUpdating) {
                monitor.setUpdatingStatus(false);
                monitor.notifyUpdateAll();
            }
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(this.sessionkey);
                    this.callback.onResult();
                }
            } catch (RemoteException e12) {
            }
            otherThreadUpdateWaitUnlockFlag = false;
            this.service.finishRequest();
            throw th;
        }
    }

    class UpdateWaintingCheckListener {
        boolean flag;

        public UpdateWaintingCheckListener(boolean b) {
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
