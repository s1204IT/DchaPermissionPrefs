package jp.co.benesse.touch.comm;

import android.os.RemoteException;

public abstract class RequestTaskBase implements Runnable {
    @Override
    public abstract void run();

    void notifyResponseError(CommService service, IHttpResponse res, String reqID, String uri, int errCode, String errMsg, int statusCode) {
        try {
            res.onError(reqID, uri, errCode, errMsg, statusCode);
        } catch (RemoteException e) {
        }
        service.onError(reqID, uri, errCode, errMsg, statusCode);
    }
}
