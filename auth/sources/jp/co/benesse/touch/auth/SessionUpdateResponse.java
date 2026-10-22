package jp.co.benesse.touch.auth;

import android.content.Intent;
import android.os.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import org.json.JSONArray;
import org.json.JSONObject;

public class SessionUpdateResponse extends AuthServerResponse {
    public static final String TAG = "SessionUpdateResponse";
    boolean secure;
    AuthService service;
    String sessionkey = null;
    Condition waitUpdateCond;
    SessionKeyUpdateThread.UpdateWaintingCheckListener waitUpdateFlagListener;
    Lock waitUpdateLock;

    public SessionUpdateResponse(AuthService service, boolean secure, Lock waitUpdateLock, Condition waitUpdateCond, SessionKeyUpdateThread.UpdateWaintingCheckListener waitUpdateFlagListener) {
        this.service = service;
        this.waitUpdateLock = waitUpdateLock;
        this.waitUpdateCond = waitUpdateCond;
        this.waitUpdateFlagListener = waitUpdateFlagListener;
        this.secure = secure;
    }

    @Override
    public void doProcess() throws RemoteException {
        try {
            if (this.jsonResponse == null) {
                onResult(-1, -3);
                return;
            }
            JSONObject rootJson = new JSONObject(this.jsonResponse);
            String result = rootJson.getString("result");
            if (result != null && result.equals(AuthService.RESULT_OK)) {
                SboxAdapter sbox = SboxAdapter.getInstance();
                String userId = rootJson.getString(AuthService.USERID);
                String localUserId = sbox.getValue(AuthRequest.SBOX_USER_ID);
                if (userId.equals(localUserId)) {
                    JSONArray sessionArray = rootJson.getJSONArray(AuthService.SESSION);
                    for (int i = 0; i < sessionArray.length(); i++) {
                        JSONObject data = sessionArray.getJSONObject(i);
                        if (!data.has(AuthService.SECURE) || !data.has(AuthService.NAME) || !data.has("id")) {
                            onResult(-1, -1);
                            return;
                        }
                        if (data.getString(AuthService.SECURE).equals(AuthService.SECURE_TRUE) && this.secure) {
                            this.sessionkey = data.getString("id");
                        } else if (data.getString(AuthService.SECURE).equals(AuthService.SECURE_FALSE) && !this.secure) {
                            this.sessionkey = data.getString("id");
                        }
                    }
                    String authId = rootJson.getString(AuthService.AUTHID);
                    sbox.setValue(AuthRequest.SBOX_AUTH_KEY, authId);
                    sbox.setArrayValues(AuthRequest.SBOX_SESSION, sessionArray);
                    onResult(0, 0);
                }
            } else if (result != null && result.equals(AuthService.RESULT_NG)) {
                String resultCode = rootJson.getString("resultcode");
                onResult(-1, toIntCode(resultCode));
            } else if (result == null || !result.equals(AuthService.RESULT_ERROR)) {
                onResult(-1, -3);
            } else {
                String resultCode2 = rootJson.getString("resultcode");
                onResult(-1, toIntCode(resultCode2));
            }
        } catch (Exception e) {
            onResult(-1, -3);
        } finally {
            this.waitUpdateLock.lock();
            this.waitUpdateFlagListener.setFlag(true);
            this.waitUpdateCond.signal();
            this.waitUpdateLock.unlock();
        }
    }

    void onResult(int result, int resultCode) {
        Intent resultIntent = new Intent();
        resultIntent.setAction(IntentName.ACTION_UPDATE_SESSION_RESULT_RESPONSE);
        resultIntent.addCategory(IntentName.CATEGORY_AUTH_RESULT_RESPONSE);
        resultIntent.putExtra("result", result);
        resultIntent.putExtra("resultcode", resultCode);
        this.service.sendBroadcast(resultIntent);
    }

    @Override
    public void onError(String reqID, String uri, int errCode, String errMsg, int statusCode) throws RemoteException {
        this.sessionkey = null;
        this.waitUpdateLock.lock();
        this.waitUpdateFlagListener.setFlag(true);
        this.waitUpdateCond.signal();
        this.waitUpdateLock.unlock();
    }

    @Override
    public void onProgress(long currentSize, long fullSize) throws RemoteException {
    }
}
