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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.auth.IAuthCallback;
import jp.co.benesse.touch.auth.IAuthService;
import jp.co.benesse.touch.comm.volley.MemberRequest;
import jp.co.benesse.touch.comm.volley.MemberXMLStringRequest;

public class MemberRequestTask extends RequestTaskBase {
    public static final String TAG = "MemberRequestTask";
    String path;
    private IHttpRequest req;
    private IHttpResponse res;
    boolean secure;
    private CommService service;
    private boolean sessionExpiredRetry;
    String sessionKey;
    private String reqId = null;
    final Lock responseWaitlock = new ReentrantLock();
    final Condition responseWaitCond = this.responseWaitlock.newCondition();
    final Lock updateSessionkeyWaitLock = new ReentrantLock();
    final Condition updateSessionkeyCond = this.updateSessionkeyWaitLock.newCondition();
    boolean updateSessionkeyFlag = false;
    final Condition getSessionkeyCond = this.responseWaitlock.newCondition();
    boolean getSessionkeyFlag = false;

    public MemberRequestTask(IHttpRequest req, IHttpResponse res, CommService service, boolean retry) {
        this.sessionExpiredRetry = false;
        this.req = req;
        this.res = res;
        this.service = service;
        this.sessionExpiredRetry = retry;
    }

    @Override
    public void run() {
        try {
            final String uriStr = this.req.getUri();
            MemberRequest memberReq = new MemberXMLStringRequest(1, uriStr, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        MemberRequestTask.this.res.setResponseString(response);
                        MemberRequestTask.this.res.doProcess();
                        MemberRequestTask.this.service.onResult(MemberRequestTask.this.reqId, uriStr, 0, "MemberRequest xml Result OK");
                    } catch (RemoteException e) {
                        MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, uriStr, -8, "MemberRequest Can't  remote process response.", 0);
                    }
                }
            }, new MemberErrorListener(uriStr));
            try {
                this.secure = true;
                if (new URL(uriStr).getProtocol().equals("https")) {
                    this.secure = true;
                } else {
                    this.secure = false;
                }
                getSessionKey(this.secure);
                IAuthService authService = this.service.getAuthService();
                String sessionHeaderName = authService.getSessionHeaderName(this.secure);
                if (this.sessionKey == null || sessionHeaderName == null) {
                    notifyResponseError(this.service, this.res, this.reqId, uriStr, -11, "Can't get Session Key.", 0);
                    return;
                }
                memberReq.setHeader(sessionHeaderName, this.sessionKey);
                if (this.req.getHttpRequestBody() != null) {
                    memberReq.setBody(this.req.getHttpRequestBody().getBytes("utf-8"));
                }
                RetryPolicy policy = new DefaultRetryPolicy(CommService.HTTP_CONNECTION_TIMEOUT_MS, 2, 0.0f);
                memberReq.setRetryPolicy(policy);
                this.service.httpReqQueue.add(memberReq);
            } catch (RemoteException e) {
                notifyResponseError(this.service, this.res, this.reqId, uriStr, -8, "Can't get Request Body.", 0);
            } catch (Exception e2) {
                notifyResponseError(this.service, this.res, this.reqId, uriStr, -8, "Exception.", 0);
            }
        } catch (RemoteException e3) {
            notifyResponseError(this.service, this.res, this.reqId, null, -8, "Can't get Request URI.", 0);
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
                MemberRequestTask.this.sessionKey = this.callbackSessionkey;
            } else {
                MemberRequestTask.this.sessionKey = null;
            }
            MemberRequestTask.this.responseWaitlock.lock();
            MemberRequestTask.this.getSessionkeyFlag = true;
            MemberRequestTask.this.getSessionkeyCond.signal();
            MemberRequestTask.this.responseWaitlock.unlock();
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
                MemberRequestTask.this.sessionKey = this.callbackSessionkey;
            } else {
                MemberRequestTask.this.sessionKey = null;
            }
            MemberRequestTask.this.updateSessionkeyWaitLock.lock();
            MemberRequestTask.this.updateSessionkeyFlag = true;
            MemberRequestTask.this.updateSessionkeyCond.signal();
            MemberRequestTask.this.updateSessionkeyWaitLock.unlock();
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
        this.service.requestMemberServer(this.req, this.res, false);
    }

    protected void updateSessionkeyAndRetry() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MemberRequestTask.this.updateSessionkeyAndRetrySub();
            }
        };
        Thread newThread = new Thread(runnable);
        newThread.start();
    }

    class MemberErrorListener implements Response.ErrorListener {
        String uri;

        public MemberErrorListener(String uri) {
            this.uri = uri;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            NetworkResponse networkResponse = error.networkResponse;
            if (networkResponse == null) {
                if (error instanceof NoConnectionError) {
                    MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -2, "MemberRequest no connection.", 0);
                    return;
                }
                if (error instanceof ServerError) {
                    MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -1, "MemberRequest Server Error.", 0);
                    return;
                } else if (error instanceof TimeoutError) {
                    MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -4, "MemberRequest Connection Timeout.", 0);
                    return;
                } else {
                    MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -8, "MemberRequest error response.", 0);
                    return;
                }
            }
            if (networkResponse.statusCode > 400) {
                switch (networkResponse.statusCode) {
                    case Error.HttpStatusCode.SESSION_NOT_FOUND:
                        MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -9, "MemberRequest AuthKey is Expired.", networkResponse.statusCode);
                        break;
                    case Error.HttpStatusCode.EXPIRED_SESSION:
                        if (!MemberRequestTask.this.sessionExpiredRetry) {
                            MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -11, "Can't update sessionkey.", 0);
                        } else {
                            MemberRequestTask.this.updateSessionkeyAndRetry();
                        }
                        break;
                    default:
                        MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -3, "MemberRequest HTTP Response Error.", networkResponse.statusCode);
                        break;
                }
                return;
            }
            MemberRequestTask.this.notifyResponseError(MemberRequestTask.this.service, MemberRequestTask.this.res, MemberRequestTask.this.reqId, this.uri, -3, "MemberRequest HTTP Response Error.", networkResponse.statusCode);
        }
    }
}
