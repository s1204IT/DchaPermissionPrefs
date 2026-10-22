package jp.co.benesse.touch.auth;

import android.os.RemoteException;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.comm.ICommService;

public class AkamaiTokenGetThread implements Runnable {
    public static final String TAG = "AkamaiTokenGetThread";
    private static final Object syncMonitor = new Object();
    List<String> accessList;
    private AuthService authService;
    private IAuthCallback callback;
    String url;
    Lock waitUpdateLock = new ReentrantLock();
    Condition waitUpdateCond = this.waitUpdateLock.newCondition();
    Lock responseWaitLock = new ReentrantLock();
    Condition responseWaitCond = this.responseWaitLock.newCondition();
    AkamaiTokenWaitingCheckListener waitResponseFlagListener = new AkamaiTokenWaitingCheckListener(false);

    public AkamaiTokenGetThread(String url, List<String> accessList, AuthService authService, IAuthCallback callback) {
        this.url = null;
        this.accessList = null;
        this.url = url;
        this.accessList = accessList;
        this.authService = authService;
        this.callback = callback;
    }

    @Override
    public void run() {
        String retStr = null;
        try {
            synchronized (syncMonitor) {
                try {
                    AkamaiTokenRequest req = new AkamaiTokenRequest(this.url, this.accessList);
                    AkamaiTokenResponse res = new AkamaiTokenResponse(this.responseWaitLock, this.responseWaitCond, this.waitResponseFlagListener);
                    this.responseWaitLock.lock();
                    try {
                        try {
                            ICommService commService = this.authService.getCommService();
                            commService.requestComm2BaeServer(req, res);
                            long timeout = 100000000000L;
                            while (!this.waitResponseFlagListener.getFlag()) {
                                long l = this.responseWaitCond.awaitNanos(timeout);
                                if (l <= 0 || this.waitResponseFlagListener.getFlag()) {
                                    break;
                                } else {
                                    timeout = l;
                                }
                            }
                            this.waitResponseFlagListener.setFlag(false);
                            retStr = res.result;
                        } catch (InterruptedException e) {
                            retStr = null;
                            this.responseWaitLock.unlock();
                        }
                    } catch (Exception e2) {
                        retStr = null;
                    } finally {
                        this.responseWaitLock.unlock();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(retStr);
                    this.callback.onResult();
                }
            } catch (RemoteException e3) {
            }
            this.authService.finishRequest();
        } catch (Exception e4) {
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(retStr);
                    this.callback.onResult();
                }
            } catch (RemoteException e5) {
            }
            this.authService.finishRequest();
        } catch (Throwable th2) {
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(retStr);
                    this.callback.onResult();
                }
            } catch (RemoteException e6) {
            }
            this.authService.finishRequest();
            throw th2;
        }
    }

    public class AkamaiTokenWaitingCheckListener {
        boolean flag;

        public AkamaiTokenWaitingCheckListener(boolean b) {
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
