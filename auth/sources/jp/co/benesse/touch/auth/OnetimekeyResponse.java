package jp.co.benesse.touch.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.json.JSONException;
import org.json.JSONObject;

public class OnetimekeyResponse extends AuthServerResponse {
    public static final String TAG = "OnetimekeyGetResponse";
    Context context;
    int errStatusCode;
    Condition responseWaitCond;
    Lock responseWaitlock;
    OnetimekeyGetThread.OnetimekeyWaitingCheckListener waitResponseFlagListener;
    public String oneTimeKey = null;
    public String result = null;
    public String resultCode = null;
    int errCode = -4;
    String message = "response timeout.";

    public OnetimekeyResponse(Context ctx, Lock responseWaitlock, Condition responseWaitCond, OnetimekeyGetThread.OnetimekeyWaitingCheckListener waitResponseFlagListener) {
        this.context = ctx;
        this.responseWaitlock = responseWaitlock;
        this.responseWaitCond = responseWaitCond;
        this.waitResponseFlagListener = waitResponseFlagListener;
    }

    @Override
    @SuppressLint({"NewApi"})
    public void doProcess() throws RemoteException {
        try {
            if (this.jsonResponse == null) {
                throw new RemoteException("Response JSON is null.");
            }
            try {
                try {
                    JSONObject rootJson = new JSONObject(this.jsonResponse);
                    this.result = rootJson.getString("result");
                    if (this.result != null && this.result.equals(AuthService.RESULT_OK)) {
                        this.oneTimeKey = rootJson.getString(AuthService.ONETIMEID);
                    } else if (this.result != null && this.result.equals(AuthService.RESULT_NG)) {
                        this.resultCode = rootJson.getString("resultcode");
                    } else if (this.result != null && this.result.equals(AuthService.RESULT_ERROR)) {
                        this.resultCode = rootJson.getString("resultcode");
                    }
                    this.responseWaitlock.lock();
                    this.waitResponseFlagListener.setFlag(true);
                    this.responseWaitCond.signal();
                    this.responseWaitlock.unlock();
                } catch (Exception e) {
                    throw new RemoteException(e.toString());
                }
            } catch (JSONException e2) {
                throw new RemoteException(e2.toString());
            }
        } catch (Throwable th) {
            this.responseWaitlock.lock();
            this.waitResponseFlagListener.setFlag(true);
            this.responseWaitCond.signal();
            this.responseWaitlock.unlock();
            throw th;
        }
    }

    @Override
    public void onError(String reqID, String uri, int errCode, String errMsg, int statusCode) throws RemoteException {
        this.oneTimeKey = null;
        this.errCode = errCode;
        this.message = errMsg;
        this.errStatusCode = statusCode;
        this.responseWaitlock.lock();
        this.waitResponseFlagListener.setFlag(true);
        this.responseWaitCond.signal();
        this.responseWaitlock.unlock();
    }

    @Override
    public void onProgress(long currentSize, long fullSize) throws RemoteException {
    }
}
