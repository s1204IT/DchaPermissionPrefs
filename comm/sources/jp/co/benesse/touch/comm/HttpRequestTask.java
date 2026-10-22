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
import jp.co.benesse.touch.comm.volley.HttpRequest;

public class HttpRequestTask extends RequestTaskBase {
    public static final String TAG = AuthRequestTask.class.getName();
    private IHttpRequest req;
    private String reqId;
    private IHttpResponse res;
    private CommService service;

    public HttpRequestTask(IHttpRequest req, IHttpResponse res, CommService commService) {
        this.reqId = null;
        this.req = req;
        this.res = res;
        this.service = commService;
        try {
            this.reqId = req.getRequestId();
        } catch (RemoteException e) {
        }
    }

    @Override
    public void run() {
        try {
            final String uriStr = this.req.getUri();
            try {
                HttpRequest httpReq = new HttpRequest(0, uriStr, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HttpRequestTask.this.res.setResponseString(response);
                            HttpRequestTask.this.res.doProcess();
                            HttpRequestTask.this.service.onResult(HttpRequestTask.this.reqId, uriStr, 0, "HttpRequest Result OK");
                        } catch (RemoteException e) {
                            HttpRequestTask.this.notifyResponseError(HttpRequestTask.this.service, HttpRequestTask.this.res, HttpRequestTask.this.reqId, uriStr, -8, "HttpRequest Can't  remote process response.", 0);
                        } catch (Exception e2) {
                            HttpRequestTask.this.notifyResponseError(HttpRequestTask.this.service, HttpRequestTask.this.res, HttpRequestTask.this.reqId, uriStr, -8, "HttpRequest Can't process response.", 0);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            HttpRequestTask.this.notifyResponseError(HttpRequestTask.this.service, HttpRequestTask.this.res, HttpRequestTask.this.reqId, uriStr, -3, "HttpRequest HTTP Response Error.", networkResponse.statusCode);
                            return;
                        }
                        if (error instanceof NoConnectionError) {
                            HttpRequestTask.this.notifyResponseError(HttpRequestTask.this.service, HttpRequestTask.this.res, HttpRequestTask.this.reqId, uriStr, -2, "HttpRequest no connection.", 0);
                        } else if (error instanceof ServerError) {
                            HttpRequestTask.this.notifyResponseError(HttpRequestTask.this.service, HttpRequestTask.this.res, HttpRequestTask.this.reqId, uriStr, -1, "HttpRequest Server Error.", 0);
                        } else if (error instanceof TimeoutError) {
                            HttpRequestTask.this.notifyResponseError(HttpRequestTask.this.service, HttpRequestTask.this.res, HttpRequestTask.this.reqId, uriStr, -4, "HttpRequest Connection Timeout.", 0);
                        }
                    }
                });
                Map postParams = this.req.getPostParams();
                httpReq.setParams(postParams);
                RetryPolicy policy = new DefaultRetryPolicy(CommService.HTTP_CONNECTION_TIMEOUT_MS, 2, 0.0f);
                httpReq.setRetryPolicy(policy);
                this.service.httpReqQueue.add(httpReq);
            } catch (RemoteException e) {
                notifyResponseError(this.service, this.res, this.reqId, uriStr, -8, "HttpRequest RemoteException.", 0);
            }
        } catch (RemoteException e2) {
            notifyResponseError(this.service, this.res, this.reqId, null, -8, "Can't get Request URI.", 0);
        }
    }
}
