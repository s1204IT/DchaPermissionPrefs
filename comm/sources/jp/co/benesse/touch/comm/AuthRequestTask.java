package jp.co.benesse.touch.comm;

import android.os.RemoteException;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import java.util.Map;
import jp.co.benesse.touch.comm.volley.AuthJsonRequest;
import org.json.JSONObject;

public class AuthRequestTask extends RequestTaskBase {
    public static final String TAG = "AuthRequestTask";
    private IHttpRequest req;
    private IHttpResponse res;
    private CommService service;

    public AuthRequestTask(IHttpRequest req, IHttpResponse res, CommService commService) {
        this.req = req;
        this.res = res;
        this.service = commService;
    }

    @Override
    public void run() {
        final String uriStr = null;
        try {
            uriStr = this.req.getUri();
            String id = this.req.getRequestId();
            try {
                AuthJsonRequest authReq = new AuthJsonRequest(1, uriStr, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            AuthRequestTask.this.res.setResponseString(response.toString());
                            AuthRequestTask.this.res.doProcess();
                            AuthRequestTask.this.req.getRequestId();
                        } catch (RemoteException e) {
                            AuthRequestTask.this.notifyResponseError(AuthRequestTask.this.service, AuthRequestTask.this.res, null, uriStr, -8, "AuthRequest Can't  remote process response.", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        String id2 = null;
                        try {
                            id2 = AuthRequestTask.this.req.getRequestId();
                        } catch (RemoteException e) {
                        }
                        if (networkResponse != null) {
                            AuthRequestTask.this.notifyResponseError(AuthRequestTask.this.service, AuthRequestTask.this.res, id2, uriStr, -3, "AuthRequest HTTP Response Error.", networkResponse.statusCode);
                            return;
                        }
                        if (error instanceof NoConnectionError) {
                            AuthRequestTask.this.notifyResponseError(AuthRequestTask.this.service, AuthRequestTask.this.res, id2, uriStr, -2, "AuthRequest no connection.", 0);
                            return;
                        }
                        if (error instanceof ServerError) {
                            AuthRequestTask.this.notifyResponseError(AuthRequestTask.this.service, AuthRequestTask.this.res, id2, uriStr, -1, "AuthRequest Server Error.", 0);
                        } else if (error instanceof TimeoutError) {
                            AuthRequestTask.this.notifyResponseError(AuthRequestTask.this.service, AuthRequestTask.this.res, id2, uriStr, -4, "AuthRequest Connection Timeout.", 0);
                        } else {
                            AuthRequestTask.this.notifyResponseError(AuthRequestTask.this.service, AuthRequestTask.this.res, id2, uriStr, -8, "AuthRequest General Error.", 0);
                        }
                    }
                });
                Map postParams = this.req.getPostParams();
                authReq.setParams(postParams);
                if (postParams != null && postParams.entrySet() != null) {
                    for (Map.Entry<String, String> entry : postParams.entrySet()) {
                    }
                }
                RetryPolicy policy = new DefaultRetryPolicy(CommService.HTTP_CONNECTION_TIMEOUT_MS, 2, 0.0f);
                authReq.setRetryPolicy(policy);
                this.service.httpReqQueue.add(authReq);
            } catch (RemoteException e) {
                notifyResponseError(this.service, this.res, id, uriStr, -8, "AuthRequest RemoteException.", 0);
            }
        } catch (RemoteException e2) {
            notifyResponseError(this.service, this.res, null, uriStr, -8, "Can't get Request URI.", 0);
        }
    }
}
