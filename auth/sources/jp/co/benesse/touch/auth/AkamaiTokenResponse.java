package jp.co.benesse.touch.auth;

import android.annotation.SuppressLint;
import android.os.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import jp.co.benesse.touch.common.touchconstant.CommResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class AkamaiTokenResponse extends BaeServerResponse {
    public static final String TAG = "AkamaiTokenResponse";
    public static final String TOKEN = "token";
    public static final String TOKEN_NAME = "tokenName";
    private int errStatusCode;
    Condition responseWaitCond;
    Lock responseWaitlock;
    AkamaiTokenGetThread.AkamaiTokenWaitingCheckListener waitResponseFlagListener;
    public String result = null;
    private String message = "response timeout.";

    public AkamaiTokenResponse(Lock responseWaitlock, Condition responseWaitCond, AkamaiTokenGetThread.AkamaiTokenWaitingCheckListener waitResponseFlagListener) {
        this.responseWaitlock = responseWaitlock;
        this.responseWaitCond = responseWaitCond;
        this.waitResponseFlagListener = waitResponseFlagListener;
    }

    @Override
    @SuppressLint({"NewApi"})
    public void doProcess() throws RemoteException {
        if (this.jsonResponse == null) {
            throw new RemoteException("Response JSON is null.");
        }
        JSONObject resultJson = new JSONObject();
        try {
            try {
                JSONObject rootJson = new JSONObject(this.jsonResponse);
                if (rootJson.has("result")) {
                    JSONObject responseJson = rootJson.getJSONObject("result");
                    if (responseJson == null || !responseJson.has(TOKEN_NAME) || !responseJson.has(TOKEN)) {
                        throw new RemoteException("Response JSON is not expected value.");
                    }
                    resultJson.put("code", 1);
                    resultJson.put("result", responseJson.get(TOKEN_NAME) + "=" + responseJson.get(TOKEN));
                } else if (rootJson.has(CommResponse.RES_COMM_KEY_ERROR)) {
                    JSONObject responseJson2 = rootJson.getJSONObject(CommResponse.RES_COMM_KEY_ERROR);
                    String errorCode = responseJson2.getString("code");
                    responseJson2.getString(CommResponse.RES_COMM_KEY_RESULT_MESSAGE);
                    responseJson2.getString(CommResponse.RES_COMM_KEY_DATA);
                    resultJson.put("code", 2);
                    resultJson.put(CommResponse.RES_COMM_KEY_ERROR, errorCode);
                } else if (rootJson.has(CommResponse.RES_COMM_KEY_RESULT_MESSAGE)) {
                    rootJson.getString(CommResponse.RES_COMM_KEY_RESULT_MESSAGE);
                    resultJson.put("code", 3);
                } else {
                    resultJson.put("code", 4);
                }
                this.result = resultJson.toString();
                this.responseWaitlock.lock();
                this.waitResponseFlagListener.setFlag(true);
                this.responseWaitCond.signal();
                this.responseWaitlock.unlock();
            } catch (JSONException e) {
                throw new RemoteException(e.toString());
            } catch (Exception e2) {
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
        try {
            try {
                JSONObject resultJson = new JSONObject();
                resultJson.put("code", errCode);
                resultJson.put(CommResponse.RES_COMM_KEY_RESULT_MESSAGE, errMsg);
                resultJson.put(CommResponse.RES_COMM_KEY_STATUS_CODE, statusCode);
                this.result = resultJson.toString();
                this.responseWaitlock.lock();
                this.waitResponseFlagListener.setFlag(true);
                this.responseWaitCond.signal();
                this.responseWaitlock.unlock();
            } catch (Exception e) {
                throw new RemoteException();
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
    public void onProgress(long currentSize, long fullSize) throws RemoteException {
    }
}
