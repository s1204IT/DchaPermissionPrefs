package jp.co.benesse.touch.comm;

import android.os.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SettingServerTimeResponse extends IHttpResponse.Stub {
    public static final String TAG = "HttpGetServerTimeResponse";
    CommService.ServerTimeListener listener;
    String resString;
    public boolean result = false;
    Condition waitCond;
    Lock waitLock;

    public SettingServerTimeResponse(CommService.ServerTimeListener serverTimeListener, Lock waitLock, Condition waitCond) {
        this.listener = serverTimeListener;
        this.waitLock = waitLock;
        this.waitCond = waitCond;
    }

    @Override
    public void setResponseString(String resString) throws RemoteException {
        this.resString = resString;
    }

    @Override
    public void doProcess() throws RemoteException {
        if (this.listener != null) {
            this.listener.setServerTime(this.resString.trim());
            this.result = true;
        }
        this.waitLock.lock();
        this.waitCond.signal();
        this.waitLock.unlock();
    }

    @Override
    public void onError(String reqID, String uri, int errCode, String errMsg, int statusCode) throws RemoteException {
        this.waitLock.lock();
        this.waitCond.signal();
        this.waitLock.unlock();
    }

    @Override
    public void onProgress(long currentSize, long fullSize) throws RemoteException {
    }
}
