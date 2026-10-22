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
import java.net.URL;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.auth.IAuthCallback;
import jp.co.benesse.touch.comm.volley.BaeJsonRequest;
import org.json.JSONObject;

public class BaeRequestTask extends RequestTaskBase {
    public static final String TAG = "BaeRequestTask";
    private IHttpRequest req;
    private IHttpResponse res;
    private CommService service;
    private boolean sessionExpiredRetry;
    String sessionKey;
    final Lock responseWaitlock = new ReentrantLock();
    final Condition responseWaitCond = this.responseWaitlock.newCondition();
    final Lock updateSessionkeyWaitLock = new ReentrantLock();
    final Condition updateSessionkeyCond = this.updateSessionkeyWaitLock.newCondition();
    boolean updateSessionkeyFlag = false;
    final Condition getSessionkeyCond = this.responseWaitlock.newCondition();
    boolean getSessionkeyFlag = false;
    private String reqId = null;
    boolean secure = true;

    public BaeRequestTask(IHttpRequest req, IHttpResponse res, CommService commService, boolean retry) {
        this.sessionExpiredRetry = false;
        this.req = req;
        this.res = res;
        this.service = commService;
        this.sessionExpiredRetry = retry;
    }

    @Override
    public void run() {
        final String uriStr = null;
        try {
            uriStr = this.req.getUri();
            this.reqId = this.req.getRequestId();
            try {
                BaeJsonRequest baeReq = new BaeJsonRequest(1, uriStr, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String id = null;
                        try {
                            BaeRequestTask.this.res.setResponseString(response.toString());
                            BaeRequestTask.this.res.doProcess();
                            id = BaeRequestTask.this.req.getRequestId();
                            BaeRequestTask.this.service.onResult(id, uriStr, 0, "BaeRequest Result OK");
                        } catch (RemoteException e) {
                            BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, id, uriStr, -8, "BaeRequest Can't  remote process response.", 0);
                        }
                    }
                }, new BaeErrorListener(uriStr));
                this.secure = true;
                if (new URL(uriStr).getProtocol().equals("https")) {
                    this.secure = true;
                } else {
                    this.secure = false;
                }
                getSessionKey(this.secure);
                String sessionHeaderName = this.service.getAuthService().getSessionHeaderName(this.secure);
                if (this.sessionKey == null || sessionHeaderName == null) {
                    notifyResponseError(this.service, this.res, this.reqId, uriStr, -11, "Can't get Session Key.", 0);
                    return;
                }
                baeReq.setHeader(sessionHeaderName, this.sessionKey);
                baeReq.setHeader("X-API-Version", "1.0");
                baeReq.setHeader("X-Caller-Id", "TouchCommService");
                Map<String, String> postParams = this.req.getPostParams();
                if (this.req.getHttpRequestBody() != null) {
                    baeReq.setBody(this.req.getHttpRequestBody().getBytes("utf-8"));
                }
                if (postParams != null && postParams.entrySet() != null) {
                    for (Map.Entry<String, String> entry : postParams.entrySet()) {
                    }
                }
                RetryPolicy policy = new DefaultRetryPolicy(CommService.HTTP_CONNECTION_TIMEOUT_MS, 2, 0.0f);
                baeReq.setRetryPolicy(policy);
                this.service.httpReqQueue.add(baeReq);
            } catch (Exception e) {
                notifyResponseError(this.service, this.res, this.reqId, uriStr, -8, "BaeRequest RemoteException.", 0);
            }
        } catch (RemoteException e2) {
            notifyResponseError(this.service, this.res, this.reqId, uriStr, -8, "Can't get Request URI.", 0);
        }
    }

    private void getSessionKey(boolean secure) {
        IAuthCallback callback = new SessionGetCallbackImpl();
        this.responseWaitlock.lock();
        try {
            try {
                this.service.getAuthService().getSessionKey(secure, callback);
                long timeout = 100000000000L;
                while (!this.getSessionkeyFlag) {
                    long l = this.getSessionkeyCond.awaitNanos(timeout);
                    if (l <= 0 || this.getSessionkeyFlag) {
                        break;
                    } else {
                        timeout = l;
                    }
                }
                this.getSessionkeyFlag = false;
                this.responseWaitlock.unlock();
            } catch (RemoteException e) {
                this.sessionKey = null;
                this.responseWaitlock.unlock();
            } catch (InterruptedException e2) {
                this.sessionKey = null;
                this.responseWaitlock.unlock();
            }
        } catch (Throwable th) {
            this.responseWaitlock.unlock();
            throw th;
        }
    }

    class SessionGetCallbackImpl extends IAuthCallback.Stub {
        String callbackSessionkey;

        SessionGetCallbackImpl() {
        }

        @Override
        public void setStringValue(String value) throws RemoteException {
            this.callbackSessionkey = value;
        }

        @Override
        public void onResult() throws RemoteException {
            if (this.callbackSessionkey != null) {
                BaeRequestTask.this.sessionKey = this.callbackSessionkey;
            } else {
                BaeRequestTask.this.sessionKey = null;
            }
            BaeRequestTask.this.responseWaitlock.lock();
            BaeRequestTask.this.getSessionkeyFlag = true;
            BaeRequestTask.this.getSessionkeyCond.signal();
            BaeRequestTask.this.responseWaitlock.unlock();
        }
    }

    class SessionUpdateCallbackImpl extends IAuthCallback.Stub {
        String callbackSessionkey;

        SessionUpdateCallbackImpl() {
        }

        @Override
        public void setStringValue(String value) throws RemoteException {
            this.callbackSessionkey = value;
        }

        @Override
        public void onResult() throws RemoteException {
            if (this.callbackSessionkey != null) {
                BaeRequestTask.this.sessionKey = this.callbackSessionkey;
            } else {
                BaeRequestTask.this.sessionKey = null;
            }
            BaeRequestTask.this.updateSessionkeyWaitLock.lock();
            BaeRequestTask.this.updateSessionkeyFlag = true;
            BaeRequestTask.this.updateSessionkeyCond.signal();
            BaeRequestTask.this.updateSessionkeyWaitLock.unlock();
        }
    }

    protected void updateSessionkeyAndRetrySub() {
        this.updateSessionkeyWaitLock.lock();
        try {
            IAuthCallback callback = new SessionUpdateCallbackImpl();
            this.service.getAuthService().updateSessionKey(this.secure, callback);
            long timeout = 100000000000L;
            while (!this.updateSessionkeyFlag) {
                long l = this.updateSessionkeyCond.awaitNanos(timeout);
                if (l <= 0 || this.updateSessionkeyFlag) {
                    break;
                } else {
                    timeout = l;
                }
            }
            this.updateSessionkeyFlag = false;
        } catch (RemoteException e) {
        } catch (InterruptedException e2) {
        } finally {
            this.updateSessionkeyWaitLock.unlock();
        }
        this.service.requestBaeServer(this.req, this.res, false);
    }

    protected void updateSessionkeyAndRetry() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BaeRequestTask.this.updateSessionkeyAndRetrySub();
            }
        };
        Thread newThread = new Thread(runnable);
        newThread.start();
    }

    class BaeErrorListener implements Response.ErrorListener {
        String uri;

        public BaeErrorListener(String uri) {
            this.uri = uri;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            NetworkResponse networkResponse = error.networkResponse;
            if (networkResponse == null) {
                if (error instanceof NoConnectionError) {
                    BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -2, "BaeRequest no connection.", 0);
                    return;
                }
                if (error instanceof ServerError) {
                    BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -1, "BaeRequest Server Error.", 0);
                    return;
                } else if (error instanceof TimeoutError) {
                    BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -4, "BaeRequest Connection Timeout.", 0);
                    return;
                } else {
                    BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -8, "BaeRequest General Error.", 0);
                    return;
                }
            }
            if (networkResponse.statusCode > 400) {
                switch (networkResponse.statusCode) {
                    case Error.HttpStatusCode.SESSION_NOT_FOUND:
                        BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -9, "BaeRequest AuthKey is Expired.", networkResponse.statusCode);
                        break;
                    case Error.HttpStatusCode.EXPIRED_SESSION:
                        if (!BaeRequestTask.this.sessionExpiredRetry) {
                            BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -11, "Can't update sessionkey.", 0);
                        } else {
                            BaeRequestTask.this.updateSessionkeyAndRetry();
                        }
                        break;
                    default:
                        BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -3, "BaeRequest HTTP Response Error.", networkResponse.statusCode);
                        break;
                }
                return;
            }
            BaeRequestTask.this.notifyResponseError(BaeRequestTask.this.service, BaeRequestTask.this.res, BaeRequestTask.this.reqId, this.uri, -3, "BaeRequest HTTP Response Error.", networkResponse.statusCode);
        }
    }
}
