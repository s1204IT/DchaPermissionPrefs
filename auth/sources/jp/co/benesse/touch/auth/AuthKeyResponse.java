package jp.co.benesse.touch.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.RemoteException;
import android.text.TextUtils;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"NewApi"})
public class AuthKeyResponse extends AuthServerResponse {
    public static final String TAG = "AuthKeyResponse";
    AuthService service;

    public AuthKeyResponse(AuthService service) {
        this.service = service;
    }

    @Override
    public void doProcess() throws RemoteException {
        if (this.jsonResponse == null) {
            throw new RemoteException("Response JSON is null.");
        }
        long now = System.currentTimeMillis();
        try {
            JSONObject rootJson = new JSONObject(this.jsonResponse);
            String result = rootJson.getString("result");
            if (result != null && result.equals(AuthService.RESULT_OK)) {
                SboxAdapter sbox = SboxAdapter.getInstance();
                String userId = rootJson.getString(AuthService.USERID);
                String localUserId = sbox.getValue(AuthRequest.SBOX_USER_ID);
                if (TextUtils.isEmpty(localUserId) || userId.equals(localUserId)) {
                    String authId = rootJson.getString(AuthService.AUTHID);
                    sbox.setValue(AuthRequest.SBOX_AUTH_KEY, authId);
                    if (TextUtils.isEmpty(localUserId)) {
                        sbox.setValue(AuthRequest.SBOX_USER_ID, userId);
                    }
                    JSONArray sessionArray = rootJson.getJSONArray(AuthService.SESSION);
                    for (int i = 0; i < sessionArray.length(); i++) {
                        JSONObject data = sessionArray.getJSONObject(i);
                        if (!data.has(AuthService.SECURE) || !data.has(AuthService.NAME) || !data.has("id")) {
                            onResult(-1, -1);
                        } else {
                            data.put(AuthService.UPDATETIME, now);
                        }
                    }
                    sbox.setArrayValues(AuthRequest.SBOX_SESSION, sessionArray);
                    onResult(0, 0);
                    return;
                }
                onResult(-1, -2);
                return;
            }
            if (result != null && result.equals(AuthService.RESULT_NG)) {
                String resultCode = rootJson.getString("resultcode");
                onResult(1, toIntCode(resultCode));
            } else if (result != null && result.equals(AuthService.RESULT_ERROR)) {
                String resultCode2 = rootJson.getString("resultcode");
                onResult(-1, toIntCode(resultCode2));
            } else {
                onResult(-1, -3);
            }
        } catch (JSONException e) {
            onResult(-1, -3);
        } catch (Exception e2) {
            onResult(-1, -3);
        }
    }

    void onResult(int result, int resultCode) {
        Intent resultIntent = new Intent();
        resultIntent.setAction(IntentName.ACTION_AUTH_USER_RESULT_RESPONSE);
        resultIntent.addCategory(IntentName.CATEGORY_AUTH_RESULT_RESPONSE);
        resultIntent.putExtra("result", result);
        resultIntent.putExtra("resultcode", resultCode);
        this.service.sendBroadcast(resultIntent);
    }

    @Override
    public void onError(String reqID, String uri, int errCode, String errMsg, int statusCode) throws RemoteException {
    }

    @Override
    public void onProgress(long currentSize, long fullSize) throws RemoteException {
    }
}
